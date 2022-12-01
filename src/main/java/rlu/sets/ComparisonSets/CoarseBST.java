package rlu.sets.ComparisonSets;

import rlu.sets.interfaces.ComparableSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseBST <T extends Comparable<T>> implements ComparableSet<T> {
    private Lock lock;
    private Node<T> root;

    public CoarseBST() {
        this.root = null;
        lock = new ReentrantLock();
    }

    public CoarseBST(int threads) {

        this.root = null;
        lock = new ReentrantLock();
    }

    @Override
    public boolean add(T item) {

        if (item == null) {
            return true;
        }
        synchronized (this) {

            if (root == null) {
                this.root = new Node<>(item);
                return true;
            }

            Node<T> node = new Node<>(item);
            boolean result = insert(node, root, null);

            return result;
        }
    }

    private boolean insert(Node<T> addNode, Node<T> curr, Node<T> parent) {

        if (curr == null || addNode == null)
            return false;

        //Insert node to left if node<current node
        if (addNode.compareTo(curr) < 0) {
            if (curr.getLeft() == null) {
                curr.setLeft(addNode);
                setParentNode(curr.getLeft(), curr);
                return true;
            } else {
                return insert(addNode, curr.getLeft(), curr);
            }
        }
        //Insert node to right if node>current node
        else if (addNode.compareTo(curr) > 0) {
            if (curr.getRight() == null) {
                curr.setRight(addNode);
                setParentNode(curr.getRight(), curr);
                return true;
            } else {
                return insert(addNode, curr.getRight(), curr);
            }
        }
        return false;
    }

    public void setParentNode(Node<T> newNode, Node<T> newParent) {
        newNode.setParent(newParent);
    }

    @Override
    public boolean remove(T item) {
        return true;
    }

    public String toString() {
        Node<T> node = this.root;
        if (node == null)
            return "[]";
        else
            return "[" + node.getData() + printString(node.getLeft())
                    + printString(node.getRight()) + "]";
    }

    private String printString(Node<T> node) {
        if (node == null)
            return " ";
        else
            return "[" + node.getData() + printString(node.getLeft())
                    + printString(node.getRight()) + "]";
    }

    @Override
    public boolean contains(T item) {

        Node<T> curr = root;
        Node<T> pred = root;

        if(curr == null)
        {
            return false;
        }

        while (curr != null) {

            int compare = curr.item.compareTo(item);

            if (compare > 0) {
                pred = curr;
                curr = curr.getLeft();
            } else if (compare < 0) {
                pred = curr;
                curr = curr.getRight();
            } else {
                return true;
            }

            if (curr == null) {
                break;
            }
        }

        return false;
    }

    private static class Node<T extends Comparable<T>> {
        T item;
        private Node<T> left;
        private Node<T> right;
        private Node<T> parent;
        public ReentrantLock lock;


        public Node(){
            this.item = null;
            this.left = null;
            this.right = null;
            this.parent = null;
        }
        public Node(T item) {
            this.item = item;
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        public void lock() {
            lock.lock();
        }

        public void unlock() {
            lock.unlock();
        }

        public int compareTo(Node<T> node)
        {
            if(node==null || node.item==null)
                return -1;

            return this.item.compareTo(node.item);
        }

        public Node(T item, Node<T> parent) {
            this.item = item;
            this.parent = parent;
        }

        public Node(T item, Node<T> left, Node<T> right) {
            this.item = item;
            this.left = left;
            this.right = right;
        }

        public void setData(T item) {
            this.item = item;
        }

        public void setLeft(Node<T> left) {
            this.left = left;
        }

        public void setRight(Node<T> right) {
            this.right = right;
        }

        public void setParent(Node<T> parent) {
            this.parent = parent;
        }

        public T getData() {
            return this.item;
        }

        public Node<T> getLeft() {
            return this.left;
        }

        public Node<T> getRight() {
            return this.right;
        }

        public Node<T> getParent() {
            return parent;
        }
    }
}

