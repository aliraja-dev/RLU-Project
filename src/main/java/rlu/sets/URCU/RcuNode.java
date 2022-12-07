package rlu.sets.URCU;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RcuNode<T> {
    Lock lock = new ReentrantLock();
    int key;
    RcuNode<T> next;

    public RcuNode(T item, RcuNode<T> next) {
        this.key = item.hashCode();
        this.next = next;
    }

    public RcuNode(int key) {
        this.key = key;
        next = null;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

}
