package rlu.sets.URCU;

import rlu.sets.ComparisonSets.FineSet;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

public class FineSetURCU<T> implements RcuSetInterface<T>{
    private RcuNode<T> head;
    private static int MAX_THREADS = 128;
    final static long NOT_READING = Long.MAX_VALUE;
    final AtomicLong reclaimerVersion = new AtomicLong(0);
    final AtomicLongArray readersVersion = new AtomicLongArray(MAX_THREADS);

    int threadCount = 0;

    @SuppressWarnings("unchecked")
    public FineSetURCU() {
        head = new RcuNode<T>(Integer.MIN_VALUE);
        head.next = new RcuNode<T>(Integer.MAX_VALUE);
        for (int i = 0; i < MAX_THREADS; i++) readersVersion.set(i, NOT_READING);
    }

    @SuppressWarnings("unchecked")
    public FineSetURCU(int threads) {
        head = new RcuNode<>(Integer.MIN_VALUE);
        head.next = new RcuNode<>(Integer.MAX_VALUE);
//        MAX_THREADS = threads;
//        threadCount = threads;
        for (int i = 0; i < MAX_THREADS; i++) readersVersion.set(i, NOT_READING);
    }

    @Override
    public boolean add(T item, RcuThread<T> ctx) {
        int key = item.hashCode();
        head.lock();
        RcuNode<T> pred = head;

        try {
            RcuNode<T> curr = pred.next;
            curr.lock();

            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }

                if (curr.key == key) {
                    return false;
                }
                //            RcuNode<T> node = new RcuNode<>(item, curr);
                //            pred.next = node;
                synchronize_rcu(pred, item, curr);
                return true;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    @Override
    public boolean remove(T item, RcuThread<T> thread) {
        return false;
    }

    @Override
    public boolean contains(T item, RcuThread<T> ctx) {
        int key = item.hashCode();
//        long threadId = Thread.currentThread().getId();
        int tid = getTID();
        boolean result = false;

        head.lock();
        RcuNode<T> pred = head;
        try{
            RcuNode<T> curr = pred.next;
            curr.lock();
            try{
                rcu_read_lock(tid);
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                rcu_read_unlock(tid);
//                result = ;
                return key == curr.key;

            } finally {
                curr.unlock();
            }
        }
         finally {
            pred.unlock();
        }
    }

    public static int getTID() {

        return (int) (Thread.currentThread().getId() % MAX_THREADS);
    }

    public void rcu_read_lock(final int tid) {

        final long rv = reclaimerVersion.get();
        readersVersion.set(tid, rv);
        final long nrv = reclaimerVersion.get(); // check again

        if (rv != nrv) {
//            System.out.println("Done: "+tid);
            readersVersion.lazySet(tid, nrv);
        }

    }

    public void rcu_read_unlock(final int tid) {

        readersVersion.set(tid, NOT_READING);
    }

    void synchronize_rcu(RcuNode<T> pred, T newItem, RcuNode<T> next) {
        RcuNode<T> newNode = new RcuNode<>(newItem, next);
        final long waitForVersion = reclaimerVersion.incrementAndGet();

        for (int i = 0; i < MAX_THREADS; i++) {

            while (readersVersion.get(i) < waitForVersion) {

//                System.out.println("Pending reader: "+i+"##################");
            };// spin
        }
        pred.next = newNode;

    }

}
