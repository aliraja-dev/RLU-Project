package rlu.sets;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;


public class RluCoarseSet<T> implements Set<T> {
   
    AtomicInteger gClock; // Global clock shared by all threads
    Thread[] globalThreads; // To identify Active threads
//In the global setup of JMH state, make sure we add all the threads to this global threads array

    private RluNode<T> head;

    public RluSet(int threads) {
        gClock = new AtomicInteger(0);
        this.globalThreads = new Thread[threads];
        head = new RluNode<>(Integer.MIN_VALUE);
        head.next = new RluNode<>(Integer.MAX_VALUE);
    }

    @Override
    public boolean add(T item, RluThread<T> rluThread) {
        int key = item.hashCode();
        synchronized(this){
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
       
            //TODO rlu_commit_write_log will call synchronize, writebackand unlock and in end the rlu swap write.
    
            RluNode<T> node = new RluNode<>(item, curr);
            pred.next = node;
            return true;

        }
       

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
    public boolean contains(T item, RluThread<T> ctx) {
        int key = item.hashCode();
       ctx.lClock = gClock.get();
        // register yourself as an active thread
        long threadId = Thread.currentThread().getId();
        ctx.runCounter++;
        globalThreads[threadId] = Thread.currentThread();

        RluNode<T> pred = head;
        RluNode<T> curr = pred.next;
        while (curr.key < key) {
            pred = curr;
            curr = curr.next;
        }
        // this is where we start the rLU logic, and we only go until dereference for contains in a lock free traversal
        // check if the object is locked 
        if (curr.isLocked()) {
            //find the id of the thread that locked the object
            // then compare your lclock wth that thrreads wclock
            // if your lclock is greater than or equal to the wclock then you can steal copy from the thread's log

            else return key == curr.key;
        }
        return key == curr.key;
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

