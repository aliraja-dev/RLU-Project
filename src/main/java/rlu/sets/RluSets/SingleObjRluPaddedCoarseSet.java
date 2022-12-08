package rlu.sets.RluSets;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleObjRluPaddedCoarseSet<T> implements RluSetInterface<T> {

    AtomicInteger gClock; // Global clock shared by all threads
    RluThread<T>[] globalThreads; // To identify Active threads
    int padding;
    private RluNode<T> head;

    @SuppressWarnings("unchecked")
    public SingleObjRluPaddedCoarseSet() {
        gClock = new AtomicInteger(0);
        globalThreads = (RluThread<T>[]) Array.newInstance(RluThread.class, 100);
        head = new RluNode<T>(Integer.MIN_VALUE);
        head.next = new RluNode<T>(Integer.MAX_VALUE);
    }

    @SuppressWarnings("unchecked")
    public SingleObjRluPaddedCoarseSet(int padding) {
        gClock = new AtomicInteger(0);
        globalThreads = (RluThread<T>[]) Array.newInstance(RluThread.class, 100);
        head = new RluNode<>(Integer.MIN_VALUE);
        head.next = new RluNode<>(Integer.MAX_VALUE);
        this.padding = padding;
    }

    public boolean add(T item, RluThread<T> ctx) {
        int key = item.hashCode();
        synchronized (this) {
            ctx.lClock = gClock.get();
            ctx.isWriter = true;
            globalThreads[(int) Thread.currentThread().getId() % padding] = ctx;
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
            curr.header = new Header<T>(Thread.currentThread().getId());
            ctx.runCounter++;
            ctx.wClock = gClock.get() + 1;
            gClock.getAndIncrement();
            waitForOldReadersToFinishReading();
            pred.next = ctx.node;
            ctx.wClock = Integer.MAX_VALUE;
            return true;
        }

    }

    public boolean contains(T item, RluThread<T> ctx) {
        int key = item.hashCode();
        ctx.lClock = gClock.get();
        ctx.runCounter++;
        // register yourself as an active thread
        long threadId = Thread.currentThread().getId();
        globalThreads[(int) threadId % padding] = ctx;
        RluNode<T> pred = head;
        RluNode<T> curr = pred.next;
        while (curr.key < key) {
            pred = curr;
            curr = curr.next;
        }

        if (curr.isLocked()) {
            if (ctx.lClock >= globalThreads[(int) curr.header.threadId % padding].wClock) {
                RluNode<T> stolenCurrNode = globalThreads[(int) curr.header.threadId % padding].node;
                ctx.runCounter++;
                // ! check for null pointer
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
                if (globalThreads[i % padding] != null && globalThreads[i % padding].runCounter % 2 != 0
                        && globalThreads[i % padding].isWriter == false
                        && globalThreads[i % padding].lClock <= gClock.get()) {
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