package rlu.sets.RluSet;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class RluMultiObjCoarseSet<T> implements RluSetInterface<T> {
    AtomicInteger gClock;
    RluThread<T>[] globalThreads;

    private RluNode<T> head;

    @SuppressWarnings("unchecked")
    public RluMultiObjCoarseSet() {
        gClock = new AtomicInteger(0);
        globalThreads = (RluThread<T>[]) Array.newInstance(RluThread.class, 100);
        head = new RluNode<T>(Integer.MIN_VALUE);
        head.next = new RluNode<T>(Integer.MAX_VALUE);
    }

    @SuppressWarnings("unchecked")
    public RluMultiObjCoarseSet(int threads) {
        gClock = new AtomicInteger(0);
        globalThreads = (RluThread<T>[]) Array.newInstance(RluThread.class, 100);
        head = new RluNode<>(Integer.MIN_VALUE);
        head.next = new RluNode<>(Integer.MAX_VALUE);
    }

    @Override
    public boolean add(T item, RluThread<T> ctx) {
        int key = item.hashCode();

        synchronized (this) {
            ctx.lClock = gClock.get();
            ctx.isWriter = true;
            globalThreads[(int) Thread.currentThread().getId()] = ctx;
            RluNode<T> pred = head;
            RluNode<T> curr = pred.next;
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            if (key == curr.key) {
                return false;
            }
            RluNode<T> node = new RluNode<>(item, curr);
            ctx.node = node;
            curr.header = new Header<T>(Thread.currentThread().getId(), curr, node);
            ctx.runCounter++;
            ctx.wClock = gClock.get() + 1;
            gClock.getAndIncrement();
            waitForOldReadersToFinishReading();
            pred.next = ctx.node;
            ctx.wClock = Integer.MAX_VALUE;
            return true;
        }
    }

    @Override
    public boolean add(T[] items, RluThread<T> ctx) {
        LinkedList<Header<T>> headers = new LinkedList<>();
        // System.out.println("Adding " + items.length + " items");
        // int key = item.hashCode();
        synchronized (this) {
            ctx.lClock = gClock.get();
            ctx.isWriter = true;
            globalThreads[(int) Thread.currentThread().getId()] = ctx;
            RluNode<T> pred = head;
            RluNode<T> curr = pred.next;
            for (T item : items) {
                int key = item.hashCode();
                // System.out.println("key: " + key);
                while (curr.key < key) {
                    pred = curr;
                    curr = curr.next;
                }
                if (key == curr.key) {
                    return false;
                }
                RluNode<T> node = new RluNode<>(item, curr);
                // !ctx.node = node;
                curr.header = new Header<T>(Thread.currentThread().getId(), curr, node);
                headers.add(curr.header);
            }
            ctx.runCounter++;
            ctx.wClock = gClock.get() + 1;
            gClock.getAndIncrement();
            waitForOldReadersToFinishReading();
            // now write the header.copy to the respective actual nodes
            for (T item : items) {
                int key = item.hashCode();
                pred = head;
                curr = pred.next;
                while (curr.key < key) {
                    pred = curr;
                    curr = curr.next;
                }
                pred.next = headers.remove().copy;
                // ! curr.header = null;
                // System.out.println("Pred.next: " + pred.next);
            }
            // pred.next = ctx.node;
            ctx.wClock = Integer.MAX_VALUE;
            return true;
        }
    }

    @Override
    public boolean contains(T item, RluThread<T> ctx) {
        int key = item.hashCode();
        ctx.lClock = gClock.get();
        ctx.runCounter++;
        long threadId = Thread.currentThread().getId();
        globalThreads[(int) threadId] = ctx;
        RluNode<T> pred = head;
        RluNode<T> curr = pred.next;
        while (curr.key < key) {
            pred = curr;
            curr = curr.next;
        }
        if (curr.isLocked()) {
            if (ctx.lClock >= globalThreads[(int) curr.header.threadId].wClock) {
                // !RluNode<T> stolenCurrNode = globalThreads[(int) curr.header.threadId].node;
                RluNode<T> stolenCurrNode = curr.header.copy;
                ctx.runCounter++;
                System.out.println("\n***Stolen Key: " + stolenCurrNode.key + " from thread: "
                        + curr.header.threadId + " by thread: " + threadId);
                return stolenCurrNode.key == key;
            } else {
                ctx.runCounter++;
                return key == curr.key;
            }
        }
        ctx.runCounter++;
        return key == curr.key;
    }

    private void waitForOldReadersToFinishReading() {
        boolean priorReader = false;
        do {
            priorReader = false;
            for (int i = 0; i < globalThreads.length; i++) {
                if (globalThreads[i] != null && globalThreads[i].runCounter % 2 != 0
                        && globalThreads[i].isWriter == false
                        && globalThreads[i].lClock <= gClock.get()) {
                    priorReader = true;
                    break;
                }
            }
        } while (priorReader);
    }
}