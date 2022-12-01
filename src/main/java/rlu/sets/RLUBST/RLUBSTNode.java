package rlu.sets.RLUBST;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RLUBSTNode<T extends Comparable<T>> implements Comparable<RLUBSTNode<T>> {

    public ReentrantLock lock;
    RLUBSTNode<T> node;
    // to store header information
     HeaderBST<T> header;
    // private boolean locked;
    public T item;
    private RLUBSTNode<T> left;
    private RLUBSTNode<T> right;
    private RLUBSTNode<T> parent;

    public RLUBSTNode() {
        this.left = null;
        this.right = null;
        this.parent = null;
        this.item = null;
        header = null;
        lock = new ReentrantLock();

    }
    public RLUBSTNode(T item) {
        this.item = item;
        header = null;
        this.left = null;
        this.right = null;
        this.parent = null;
        lock = new ReentrantLock();

    }


    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public boolean isLocked() {
        return header != null;
    }

    public int compareTo(RLUBSTNode<T> node)
    {
        if(node==null || node.item==null)
            return -1;

        return this.item.compareTo(node.item);
    }

    public RLUBSTNode(T item, RLUBSTNode<T> parent) {
        this.item = item;
        this.parent = parent;
    }

    public RLUBSTNode(T item, RLUBSTNode<T> left, RLUBSTNode<T> right) {
        this.item = item;
        this.left = left;
        this.right = right;
    }

    public void setData(T item) {
        this.item = item;
    }

    public void setLeft(RLUBSTNode<T> left) {
        this.left = left;
    }

    public void setRight(RLUBSTNode<T> right) {
        this.right = right;
    }

    public void setParent(RLUBSTNode<T> parent) {
        this.parent = parent;
    }

    public T getData() {
        return this.item;
    }

    public RLUBSTNode<T> getLeft() {
        return this.left;
    }

    public RLUBSTNode<T> getRight() {
        return this.right;
    }

    public RLUBSTNode<T> getParent() {
        return parent;
    }

}
