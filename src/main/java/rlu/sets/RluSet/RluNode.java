package rlu.sets.RluSet;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RluNode<T> {
    Lock lock = new ReentrantLock();
    int key;
    RluNode<T> next;
    // to store header information
    Header<T> header;
    // private boolean locked;

    public RluNode(T item, RluNode<T> next) {
        this.key = item.hashCode();
        this.next = next;
        // Initialize the header
        header = null;
        // the header with null means there is no copy of this in any writers log and
        // only when a writer creates a copy it wil update this header to point to the
        // copy
    }

    public RluNode(int key) {
        this.key = key;
        next = null;
    }

    public boolean isLocked() {
        return header != null;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

}
