package rlu.sets.ComparisonSets;

import rlu.sets.RLUBST.RLUBSTNode;
import rlu.sets.interfaces.ComparisonSet;
import java.util.concurrent.locks.ReentrantLock;

public class FineBST<T extends Comparable<T>> implements ComparisonSet<T> {
    ReentrantLock mainLock = new ReentrantLock();
    private Node<T> root;

    @Override
    public boolean add(T item) {

        if (item == null) {
            return true;
        }

        mainLock.lock();
        if (root == null) {
            this.root = new Node<>(item);
            mainLock.unlock();
            return true;
        }
        else {

            Node<T> node = new Node<>(item);
            Node<T> curr = root;
            Node<T> newParent = curr;
            if (curr == null || node == null) {
                return false;
            }

            curr.lock();
            mainLock.unlock();
            try {
                while (true) {

                    newParent = curr;
                    //Insert node to left if node<current node
                    if (node.compareTo(curr) < 0) {
                        if (curr.getLeft() == null) {
                            newParent.setLeft(node);
                            setParentNode(newParent.getLeft(), newParent);
//                        newParent.unlock();
                            return true;
                        } else {
                            curr = curr.getLeft();
                            curr.lock();
                            newParent.unlock();
                        }
                    }
                    //Insert node to right if node>current node
                    else if (node.compareTo(curr) > 0) {
                        if (curr.getRight() == null) {
                            newParent.setRight(node);
                            setParentNode(newParent.getRight(), newParent);
//                        newParent.unlock();
                            return true;
                        } else {
                            curr = curr.getRight();
                            curr.lock();
                            newParent.unlock();
                        }
                    } else {
//                    newParent.unlock();
                        return false;
                    }
                }
            } finally {
                newParent.unlock();
            }
        }
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
        Node<T> parent = curr;
        int compare = 0;

        if(curr == null)
        {
            return false;
        }
        else {
            curr.lock();
            try {
                while (curr != null) {

                    compare = curr.item.compareTo(item);
                    parent = curr;
                    if (compare > 0) {
                        curr = curr.getLeft();
                    } else if (compare < 0) {
                        curr = curr.getRight();
                    } else {
                        return true;
                    }

                    if (curr == null) {
                        return false;
                    }
                    curr.lock();
                    parent.unlock();
                }
                return false;
            } finally {
                parent.unlock();
            }
        }
    }


    private static class Node<T extends Comparable<T>> {
        T item;
        private Node<T> left;
        private Node<T> right;
        private Node<T> parent;
        public ReentrantLock lock = new ReentrantLock();


        public Node() {
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

        public int compareTo(Node<T> node) {
            if (node == null || node.item == null)
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

    public void printTree()
    {
        printTree(root);
    }

    public void printTree(Node<T> node)
    {
        if(node == null)
        {
            return;
        }
        printTree(node.getLeft());
        System.out.println(node.item.toString());
        printTree(node.getRight());
    }
}