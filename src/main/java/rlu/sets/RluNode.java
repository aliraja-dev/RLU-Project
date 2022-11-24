package rlu.sets;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RluNode<U> {
    private final Lock lock = new ReentrantLock();
    int key;
    RluNode<U> next;
    // to store header information
    Header<U> header;

    public RluNode(U item, RluNode<U> next) {
        this.key = item.hashCode();
        this.next = next;
        // Initialize the header
        header = new Header<>(Thread.currentThread().getId(), this);
    }

    public RluNode(int key) {
        this.key = key;
        next = null;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    class Header<U> {
        long threadId;
        RluNode<U> actualNode;
        private final boolean isCopy = true; // to indicate this is a copy

        public Header(long threadId, RluNode<U> actualNode) {
            this.threadId = threadId;
            this.actualNode = actualNode;
        }
    }
}
