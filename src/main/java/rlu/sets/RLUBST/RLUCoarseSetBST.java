package rlu.sets.RLUBST;
import rlu.sets.interfaces.Sorted;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RLUCoarseSetBST<T extends Comparable<T>> implements Sorted<T> {

    AtomicInteger gClock; // Global clock shared by all threads
    RluThread<T>[] globalThreads; // To identify Active threads
    private Lock lock;
    private RLUBSTNode<T> root = null;

    public RLUCoarseSetBST() {
        lock = new ReentrantLock();
        gClock = new AtomicInteger(0);
        globalThreads = (RluThread<T>[]) Array.newInstance(RluThread.class, 100);
    }

    public RLUCoarseSetBST(int threads) {
        lock = new ReentrantLock();
        gClock = new AtomicInteger(0);
        globalThreads = (RluThread<T>[]) Array.newInstance(RluThread.class, 100);
    }

    @Override
    public boolean add(T item, RluThread<T> ctx) {

        if (item == null) {
            return true;
        }
        synchronized (this) {

            ctx.lClock = gClock.get();
            ctx.isWriter = true;

            globalThreads[(int) Thread.currentThread().getId()] = ctx;

            if (root == null) {
                this.root = new RLUBSTNode<>(item);
                return true;
            }

            RLUBSTNode<T> node = new RLUBSTNode<>(item);
            boolean result = insert(node, root, null, ctx);

            return result;
            }
    }

    private boolean insert(RLUBSTNode<T> addNode, RLUBSTNode<T> curr, RLUBSTNode<T> parent, RluThread<T> ctx) {

        if (curr == null || addNode == null)
            return false;

        //Insert node to left if node<current node
        if (addNode.compareTo(curr) < 0) {
            if (curr.getLeft() == null) {
                RLUlogic(addNode, curr, ctx, 0);
                curr.setLeft(ctx.nodeBSTLeft);
                setParentNode(curr.getLeft(), curr);
                commit_write();
                ctx.wClock = Integer.MAX_VALUE;
                return true;
            } else {
                return insert(addNode, curr.getLeft(), curr, ctx);
            }
        }
        //Insert node to right if node>current node
        else if (addNode.compareTo(curr) > 0) {
            if (curr.getRight() == null) {
                RLUlogic(addNode, curr, ctx, 1);
                commit_write();
                curr.setRight(ctx.nodeBSTRight);
                setParentNode(curr.getRight(), curr);
                ctx.wClock = Integer.MAX_VALUE;
                return true;
            } else {
                return insert(addNode, curr.getRight(), curr, ctx);
            }
        }
        return false;
    }


    public void RLUlogic(RLUBSTNode<T> addNode, RLUBSTNode<T> curr, RluThread<T> ctx, int left_right)
    {
        if(left_right == 0) //leftnode = 0, rightnode = 1
        {
            ctx.nodeBSTLeft = addNode;
            ctx.nodeBSTRight = null;
        }
        else {
            ctx.nodeBSTRight = addNode;
            ctx.nodeBSTLeft = null;
        }

        curr.header = new HeaderBST<T>(Thread.currentThread().getId());

        ctx.runCounter++;
        ctx.wClock = gClock.get() + 1;
        gClock.getAndIncrement();
    }

    public void setParentNode(RLUBSTNode<T> newNode, RLUBSTNode<T> newParent) {
        newNode.setParent(newParent);
    }

    @Override
    public boolean remove(T item, RluThread<T> ctx) {
        return true;
    }


    public String toString() {
        RLUBSTNode<T> node = this.root;
        if (node == null)
            return "[]";
        else
            return "[" + node.getData() + printString(node.getLeft())
                    + printString(node.getRight()) + "]";
    }

    private String printString(RLUBSTNode<T> node) {
        if (node == null)
            return " ";
        else
            return "[" + node.getData() + printString(node.getLeft())
                    + printString(node.getRight()) + "]";
    }

    @Override
    public boolean contains(T item, RluThread<T> ctx) {
        int left_right = 0;
        ctx.lClock = gClock.get();
        ctx.runCounter++;
        long threadId = Thread.currentThread().getId();
        globalThreads[(int) threadId] = ctx;
        RLUBSTNode<T> curr = root;
        RLUBSTNode<T> pred = root;

        if(curr == null)
        {
            ctx.runCounter++;
            return false;
        }

        while (curr != null) {

            int compare = curr.item.compareTo(item);

            if (compare > 0) {
                pred = curr;
                curr = curr.getLeft();
                left_right = 0;
            } else if (compare < 0) {
                pred = curr;
                curr = curr.getRight();
                left_right = 1;
            } else {
                ctx.runCounter++;
                return true;
            }

            if (curr == null) {
                curr = checkLock(item, pred, ctx, left_right);
            }
        }

        ctx.runCounter++;
        return false;
    }

    private RLUBSTNode<T> checkLock(T item, RLUBSTNode<T> curr, RluThread<T> ctx, int left_right)
    {
        if (curr.isLocked()) {

            if (ctx.lClock >= globalThreads[(int) curr.header.threadId].wClock) {

                RLUBSTNode<T> stolenNodeLeft = globalThreads[(int) curr.header.threadId].nodeBSTLeft;
                RLUBSTNode<T> stolenNodeRight = globalThreads[(int) curr.header.threadId].nodeBSTRight;
                if(left_right == 0)
                {
                    return stolenNodeLeft;
                }
                else
                {
                    return stolenNodeRight;
                }
            }
            else {
                if(left_right == 0)
                    return curr.getLeft();
                else return curr.getRight();
            }

        }

        if(left_right == 0)
            return curr.getLeft();
        else return curr.getRight();

    }

    private void commit_write() {
        boolean priorReader = false;
        do {
            priorReader = false;
            for (int i = 0; i < globalThreads.length; i++) {
                if (globalThreads[i] != null) {
                    if (globalThreads[i] != null && globalThreads[i].runCounter % 2 != 0
                            && globalThreads[i].isWriter == false
                            && globalThreads[i].lClock <= gClock.get()) {

                       priorReader = true;
                       break;
                    }
                }
            }
        } while (priorReader);
    }

}
