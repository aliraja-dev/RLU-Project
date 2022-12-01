package rlu.sets.URCU;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RcuNode<T> {
    Lock lock = new ReentrantLock();
    int key;
    RcuNode<T> next;
    // to store header information
    // private boolean locked;

    public RcuNode(T item, RcuNode<T> next) {
        this.key = item.hashCode();
        this.next = next;
        // Initialize the header
//        header = null;
        // the header with null means there is no copy of this in any writers log and
        // only when a writer creates a copy it wil update this header to point to the
        // copy
    }

    public RcuNode(int key) {
        this.key = key;
        next = null;
    }

//    public boolean isLocked() {
//        return header != null;
//    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

}
