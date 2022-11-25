package rlu.sets;

import java.util.concurrent.atomic.AtomicInteger;

public class RluCoarseSet<T> implements RluSetInterface<T> {

    AtomicInteger gClock; // Global clock shared by all threads
    RluThread<T>[] globalThreads; // To identify Active threads
    // In the global setup of JMH state, make sure we add all the threads to this
    // global threads array

    private RluNode<T> head;

    public RluCoarseSet(int threads) {
        gClock = new AtomicInteger(0);
        this.globalThreads = new RluThread[threads];
        head = new RluNode<>(Integer.MIN_VALUE);
        head.next = new RluNode<>(Integer.MAX_VALUE);
    }

    @Override
    public boolean add(T item, RluThread<T> ctx) {
        int key = item.hashCode();
        synchronized (this) {
            // Read the global clock
            ctx.lClock = gClock.get();
            ctx.isWriter = true;
            // Add the thread to the global threads array
            globalThreads[(int) Thread.currentThread().getId()] = ctx;

            RluNode<T> pred = head;
            RluNode<T> curr = pred.next;

            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }

            if (key == curr.key) {
                return false;
            }
            // this is where we add the rLU Logic, We need to add a Node
            // lock the nodes pred and curr

            // TODO rlu_commit_write_log will call synchronize, writebackand unlock and in
            // end the rlu swap write.

            RluNode<T> node = new RluNode<>(item, curr);
            // store this node in your own log
            ctx.node = node;
            // dont update the next pointer of pred yet apply quiescent state of RLU
            curr.header = new Header<T>(Thread.currentThread().getId(), curr);
            // so now it will show as the curr being locked by readers
            // now we need to update the gclock with a fetchandAdd so that readers steal
            // this new copy instead of the object copy

            // and then go apply the quiescent state of RLU
            ctx.runCounter++;
            ctx.wClock = gClock.get() + 1;
            gClock.getAndIncrement();

            // now implement the quiescent state of RLU
            commit_write();
            // now safe to writeback and unlock
            pred.next = node;
            ctx.wClock = Integer.MAX_VALUE;
            ctx.isWriter = false;
            // now we need to remove the thread from the global threads array
            globalThreads[(int) Thread.currentThread().getId()] = null;
            // do we need to implement the rlu swap write here?
            return true;

        }

    }

    @Override
    public boolean remove(T item, RluThread<T> ctx) {
        int key = item.hashCode();
        head.lock();
        RluNode<T> pred = head;
        RluNode<T> curr = pred.next;

        while (curr.key < key) {
            pred = curr;
            curr = curr.next;
        }
        // this is where we start the rLU logic, and similar to the add method
        if (key == curr.key) {
            pred.next = curr.next;
            return true;
        }

        return false;
    }

    @Override
    public boolean contains(T item, RluThread<T> ctx) {
        int key = item.hashCode();
        ctx.lClock = gClock.get();
        ctx.runCounter++;
        // register yourself as an active thread
        long threadId = Thread.currentThread().getId();
        globalThreads[(int) threadId] = ctx;
        RluNode<T> pred = head;
        RluNode<T> curr = pred.next;
        while (curr.key < key) {
            pred = curr;
            curr = curr.next;
        }
        // this is where we start the rLU logic, and we only go until dereference for
        // contains in a lock free traversal
        // check if the object is locked
        if (curr.isLocked()) {
            // find the id of the thread that locked the object
            // then compare your lclock wth that threads wclock
            // if your lclock is greater than or equal to the wclock then you can steal copy
            // from the thread's log
            if (ctx.lClock >= globalThreads[(int) curr.header.threadId].wClock) {
                // steal the copy from the thread's log
                // then unlock the object
                // then return true or false
                // we need to pick the curr from the log of the thread that locked the object

                RluNode<T> stolenCurrNode = globalThreads[(int) curr.header.threadId].node;
                ctx.runCounter++;
                // put null in the globalArrays for your Id
                globalThreads[(int) threadId] = null;
                return stolenCurrNode.key == key;
            } else {
                // if your lclock is less than the wclock then you need to wait
                // wait until the wclock is greater than or equal to your lclock
                // then steal the copy from the thread's log
                // then unlock the object
                // then return true or false
                return key == curr.key;
            }

        }
        return key == curr.key;
    }

    private void commit_write() {
        boolean priorReader = false;
        do {
            priorReader = false;
            for (int i = 0; i < globalThreads.length; i++) {
                if (globalThreads[i] != null) {
                    if (!globalThreads[i].isWriter) {
                        if (globalThreads[i].lClock <= gClock.get()) {
                            priorReader = true;
                            break;
                        }
                    }
                }
            }
        } while (priorReader);
    }
}

// if(globalThreads[(int)pred.header.threadId].getState() ==
// Thread.State.TERMINATED){
// return pred.header.actualNode;
// }

// add and remove will have same logic. and same order of methods for rLU
// the contains will not call the rlu_try_lock after its done with dereference
// method in a lock free contains

// ! the RLU paper implementation allows multiple updates in a single write
// operation, which i am nt sure if i can do that in first attempt.
// My first attempt is a single object update holding fine grained locks on just
// pred and curr.

// Then in second attempt we can implement the multiple updates per operation as
// well. As that would require a coarse grained locking on entire set or
// acquiring locks on multiple objects pred and curr and then updating them.
// later is the approach in the paper.
