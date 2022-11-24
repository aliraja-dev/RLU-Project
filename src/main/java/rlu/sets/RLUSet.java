package rlu.sets;

import java.util.concurrent.atomic.AtomicInteger;

public class RluSet<T> implements Set<T> {
    AtomicInteger gClock; // Global clock shared by all threads
    Thread[] threads; // To identify Active threads

    private final RluNode<T> head;

    public RluSet(int threads) {
        gClock = new AtomicInteger(0);
        this.threads = new Thread[threads];
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
        // this is where we start the rLU logic
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
        // this is where we start the rLU logic
        return key == curr.key;
    }

    private void rlu_reader_lock(RluThread<T> rluThread) {
        rluThread.isWrite = false;
        rluThread.runCounter++;
        //do we need a memory fence here ?

        rluThread.lClock = gClock.get();

    }

    private void rlu_dereference(RluThread thread, RluNode<T> pred){
        //check if the node is a copy
        if(pred.header.isCopy()){
            //check if the copy is stale
            if(pred.header.threadId != thread.id){
                //check if the copy is stale
                if(pred.header.actualNode.wClock > thread.wClock){
                    //if the copy is stale, we need to create a new copy
                    RluNode<T> newNode = new RluNode<>(pred.header.actualNode.key, pred.header.actualNode.next);
                    //update the header
                    pred.header = newNode.header;
                    //update the pred.next
                    pred.next = newNode;
                }
            }
        }
    }

}