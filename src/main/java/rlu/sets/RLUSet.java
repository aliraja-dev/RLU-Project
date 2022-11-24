package rlu.sets;

import java.util.concurrent.atomic.AtomicInteger;

public class RluSet<T> implements Set<T> {
    AtomicInteger gClock; // Global clock shared by all threads
    Thread[] globalThreads; // To identify Active threads
//In the global setup of JMH state, make sure we add all the threads to this global threads array

    private final RluNode<T> head;

    public RluSet(int threads) {
        gClock = new AtomicInteger(0);
        this.globalThreads = new Thread[threads];
        // Initialize the head RluNode
        head = new RluNode<>(Integer.MIN_VALUE);
        head.next = new RluNode<>(Integer.MAX_VALUE);
    }

    @Override
    public boolean add(T item, RluThread<T> rluThread) {
        int key = item.hashCode();

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
        rlu_reader_lock(rluThread);
        rlu_dereference(rluThread, pred);
        Rlu_try_lock(rluThread, pred);
        rlu_reader_unlock();
        rlu_commit_write_log();

        //TODO rlu_commit_write_log will call synchronize, writebackand unlock and in end the rlu swap write.

        RluNode<T> node = new RluNode<>(item, curr);
        pred.next = node;
        return true;

    }

    @Override
    public boolean remove(T item) {
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
    public boolean contains(T item) {
        int key = item.hashCode();
        head.lock();
        RluNode<T> pred = head;

        RluNode<T> curr = pred.next;

        while (curr.key < key) {
            pred = curr;
            curr = curr.next;
        }
        // this is where we start the rLU logic, and we only go until dereference for contains in a lock free traversal
        return key == curr.key;
    }

    private void rlu_reader_lock(RluThread<T> rluThread) {
        rluThread.isWrite = false;
        rluThread.runCounter++;
        //do we need a memory fence here ?

        rluThread.lClock = gClock.get();

    }

    private RluNode<T> rlu_dereference(RluThread thread, RluNode<T> pred){
        if(!pred.isLocked()){return pred;}

        if (pred.header== null){
            return pred;
        }
        //check if you 

        if(pred.header.threadId == thread.id){
            return pred.header.actualNode;
        }

    //read from globalThreads array to check if the thread w clock <= l clock
   
        if(globalThreads[(int)pred.header.threadId].wClock <= thread.lClock){
            return pred.header.actualNode;
        }

        return pred;
    }

    private void rlu_writer_lock(RluThread<T> rluThread) {
        rluThread.isWrite = true;
        rluThread.runCounter++;
        //do we need a memory fence here ?

        rluThread.lClock = gClock.get();
        rluThread.wClock = rluThread.lClock;
    }

    private void rlu_writer_unlock(RluThread<T> rluThread) {
        rluThread.isWrite = false;
        rluThread.runCounter++;
        //do we need a memory fence here ?

        rluThread.lClock = gClock.get();
        rluThread.wClock = rluThread.lClock;
    }

    private void Rlu_try_lock(RluThread<T> thread, RluNode<T> pred){
        thread.isWriter = true;
        //read actual object and copy i.e. the header 
        //TODO 

     
    }

}

// if(globalThreads[(int)pred.header.threadId].getState() == Thread.State.TERMINATED){
//     return pred.header.actualNode;
// }


// add and remove will have same logic. and same order of methods for rLU
// the contains will not call the rlu_try_lock after its done with dereference method in a lock free contains

//! the RLU paper implementation allows multiple updates in a single write operation, which i am nt sure if i can do that in first attempt.
// My first attempt is a single object update holding fine grained locks on just pred and curr. 

// Then in second attempt we can implement the multiple updates per operation as well. As that would require a coarse grained locking on entire set or acquiring locks on multiple objects pred and curr and then updating them. later is the approach in the paper. 
