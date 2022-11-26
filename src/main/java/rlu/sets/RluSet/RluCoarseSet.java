package rlu.sets.RluSet;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;

public class RluCoarseSet<T> implements RluSetInterface<T> {

    AtomicInteger gClock; // Global clock shared by all threads
    RluThread<T>[] globalThreads; // To identify Active threads
    // In the global setup of JMH state, make sure we add all the threads to this
    // global threads array

    private RluNode<T> head;

    @SuppressWarnings("unchecked")
    public RluCoarseSet() {
        gClock = new AtomicInteger(0);
        globalThreads = (RluThread<T>[]) Array.newInstance(RluThread.class, 100);
        head = new RluNode<T>(Integer.MIN_VALUE);
        head.next = new RluNode<T>(Integer.MAX_VALUE);
    }

    @SuppressWarnings("unchecked")
    public RluCoarseSet(int threads) {
        gClock = new AtomicInteger(0);
        globalThreads = (RluThread<T>[]) Array.newInstance(RluThread.class, 100);
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

            RluNode<T> node = new RluNode<>(item, curr);
            // store this node in your own log
            ctx.node = node;
            // dont update the next pointer of pred yet apply quiescent state of RLU
            // ! curr.header = new Header<T>(Thread.currentThread().getId(), curr);
            curr.header = new Header<T>(Thread.currentThread().getId());
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
            pred.next = ctx.node;
            ctx.wClock = Integer.MAX_VALUE;
            // ! ctx.isWriter = false;
            // ! globalThreads[(int) Thread.currentThread().getId()] = null;
            // now we need to remove the thread from the global threads array
            // globalThreads[(int) Thread.currentThread().getId()] = null;
            // do we need to implement the rlu swap write here?
            return true;

        }

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
                // ! globalThreads[(int) threadId] = null;
                return stolenCurrNode.key == key;
            } else {
                // if your lclock is less than the wclock then you need to wait
                // wait until the wclock is greater than or equal to your lclock
                // then steal the copy from the thread's log
                // then unlock the object
                // then return true or false
                ctx.runCounter++;
                return key == curr.key;
            }

        }
        ctx.runCounter++;
        return key == curr.key;
    }

    private void commit_write() {
        boolean priorReader = false;
        do {
            priorReader = false;
            for (int i = 0; i < globalThreads.length; i++) {
                if (globalThreads[i] != null && globalThreads[i].runCounter % 2 != 0
                        && globalThreads[i].isWriter == false
                        && globalThreads[i].lClock <= gClock.get()) {
                    priorReader = true;
                    break;

                }
            }
        } while (priorReader);
        // ! maybe this is the deadlock point
    }
}