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
    public boolean add(T item) {
        int key = item.hashCode();

        synchronized (this) {
            RluNode<T> pred = head;
            RluNode<T> curr = pred.next;

            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }

            if (key == curr.key) {
                return false;
            }
            // todo this is where we need to add the RLU code,
            RluNode<T> RluNode = new RluNode<>(item, curr);
            pred.next = RluNode;
            return true;
        }
    }

    @Override
    public boolean remove(T item) {
        int key = item.hashCode();

        synchronized (this) {
            RluNode<T> pred = head;
            RluNode<T> curr = pred.next;

            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            // this is where we need to add the rlu modify code
            if (key == curr.key) {
                pred.next = curr.next;
                return true;
            }

            return false;
        }
    }

    @Override
    public boolean contains(T item) {
        int key = item.hashCode();
        // todo remove the lock and implement the rlu_dereference here
        synchronized (this) {
            RluNode<T> pred = head;
            RluNode<T> curr = pred.next;

            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            // this is where we need to add the rlu_dereference code
            return key == curr.key;
        }
    }

}