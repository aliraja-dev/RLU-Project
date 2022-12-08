package rlu.sets.RluSets;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleObjRluFineSet<T> implements RluSetInterface<T> {

    AtomicInteger gClock; // Global clock shared by all threads
    RluThread<T>[] globalThreads; // Holds Thread Instances

    private RluNode<T> head;

    @SuppressWarnings("unchecked")
    public SingleObjRluFineSet() {
        gClock = new AtomicInteger(0);
        globalThreads = (RluThread<T>[]) Array.newInstance(RluThread.class, 100);
        head = new RluNode<T>(Integer.MIN_VALUE);
        head.next = new RluNode<T>(Integer.MAX_VALUE);
    }

    @SuppressWarnings("unchecked")
    public SingleObjRluFineSet(int threads) {
        gClock = new AtomicInteger(0);
        globalThreads = (RluThread<T>[]) Array.newInstance(RluThread.class, threads);
        head = new RluNode<>(Integer.MIN_VALUE);
        head.next = new RluNode<>(Integer.MAX_VALUE);
    }

    @Override
    public boolean add(T item, RluThread<T> ctx) {
        int key = item.hashCode();
        ctx.lClock = gClock.get();
        ctx.isWriter = true;
        globalThreads[(int) Thread.currentThread().getId()] = ctx;
        head.lock();
        RluNode<T> pred = head;
        try {
            RluNode<T> curr = pred.next;
            curr.lock();
            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (key == curr.key) {
                    return false;
                }
                RluNode<T> node = new RluNode<>(item, curr);
                ctx.node = node;
                curr.header = new Header<T>(Thread.currentThread().getId());
                ctx.runCounter++;
                ctx.wClock = gClock.get() + 1;
                gClock.getAndIncrement();
                waitForOldReadersToFinishReading();
                pred.next = ctx.node;
                ctx.wClock = Integer.MAX_VALUE;
                return true;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
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
                RluNode<T> stolenCurrNode = globalThreads[(int) curr.header.threadId].node;
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

    @Override
    public boolean add(T[] item, RluThread<T> thread) {
        // TODO Auto-generated method stub
        return false;
    }
}
