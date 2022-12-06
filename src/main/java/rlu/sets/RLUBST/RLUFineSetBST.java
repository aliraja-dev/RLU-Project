package rlu.sets.RLUBST;

import rlu.sets.interfaces.Sorted;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RLUFineSetBST<T extends Comparable<T>> implements Sorted<T> {

    AtomicInteger gClock; // Global clock shared by all threads
    RluThread<T>[] globalThreads; // To identify Active threads
    private RLUBSTNode<T> root = null;
    ReentrantLock mainLock;

    public RLUFineSetBST() {
        gClock = new AtomicInteger(0);
        globalThreads = (RluThread<T>[]) Array.newInstance(RluThread.class, 100);
        mainLock = new ReentrantLock();
    }

    public RLUFineSetBST(int threads) {
        gClock = new AtomicInteger(0);
        globalThreads = (RluThread<T>[]) Array.newInstance(RluThread.class, 100);
        mainLock = new ReentrantLock();

    }

    @Override
    public boolean add(T item, RluThread<T> ctx) {
        if (item == null) {
            return true;
        }
        ctx.lClock = gClock.get();
        ctx.isWriter = true;
        globalThreads[(int) Thread.currentThread().getId()] = ctx;

        mainLock.lock();
        if (root == null) {
            this.root = new RLUBSTNode<>(item);
            mainLock.unlock();
            return true;
        }
        else {
            RLUBSTNode<T> node = new RLUBSTNode<>(item);
            RLUBSTNode<T> curr = root;
            RLUBSTNode<T> newParent = null;

            if (curr == null || node == null)
                return false;

            curr.lock();
            mainLock.unlock();

            try {
                while (true) {
                    newParent = curr;
                    //Insert node to left if node<current node
                    if (node.compareTo(curr) < 0) {
                        if (curr.getLeft() == null) {
                            RLUlogic(node, curr, ctx, 0);
                            newParent.setLeft(ctx.nodeBSTLeft);
                            setParentNode(newParent.getLeft(), newParent);
                            commit_write();
                            ctx.wClock = Integer.MAX_VALUE;
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
                            RLUlogic(node, curr, ctx, 1);
                            commit_write();
                            newParent.setRight(ctx.nodeBSTRight);
                            setParentNode(newParent.getRight(), newParent);
                            ctx.wClock = Integer.MAX_VALUE;
                            return true;
                        } else {
                            curr = curr.getRight();
                            curr.lock();
                            newParent.unlock();
                        }
                    } else {
                        return false;
                    }
                }
            } finally{
                newParent.unlock();
            }
        }
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
        return false;
    }

    @Override
    public boolean contains(T item, RluThread<T> ctx) {
        int left_right = 0;
        ctx.lClock = gClock.get();
        ctx.runCounter++;
        long threadId = Thread.currentThread().getId();
        globalThreads[(int) threadId] = ctx;
        RLUBSTNode<T> curr = root;
        RLUBSTNode<T> parent = curr;
        int compare = 0;

        if(curr == null)
        {
            ctx.runCounter++;
            return false;
        }
        else {
                while (curr != null) {

                    compare = curr.item.compareTo(item);
                    parent = curr;

                    if (compare > 0) {
                        curr = curr.getLeft();
                        left_right = 0;
                    } else if (compare < 0) {
                        curr = curr.getRight();
                        left_right = 1;
                    } else {
                        ctx.runCounter++;
                        return true;
                    }

                    if (curr == null) {
                        curr = checkLock(item, parent, ctx, left_right);
                        if(curr == null)
                        {
                            break;
                        }
                    }
                }

                ctx.runCounter++;
                return false;
        }
    }

    private RLUBSTNode<T> checkLock(T item, RLUBSTNode<T> curr, RluThread<T> ctx, int left_right)
    {

        if (curr.isLocked()) {

            if (ctx.lClock >= globalThreads[(int) curr.header.threadId].wClock) {

                RLUBSTNode<T> stolenNodeLeft = globalThreads[(int) curr.header.threadId].nodeBSTLeft;
                RLUBSTNode<T> stolenNodeRight = globalThreads[(int) curr.header.threadId].nodeBSTRight;
                if(left_right == 0)
                {
//                    System.out.println("\n***Stolen left: " + stolenNodeLeft + " from thread: "
//                            + curr.header.threadId + " by thread: " + threadId);
                    return stolenNodeLeft;
                }
                else
                {
//                    System.out.println("\n***Stolen right: " + stolenNodeRight + " from thread: "
//                            + curr.header.threadId + " by thread: " + threadId);
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

    public void printTree()
    {
        printTree(root);
    }

    public void printTree(RLUBSTNode<T> node)
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
