package rlu.sets.URCU;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

public class CoarseSetURCU<T> implements RcuSetInterface<T> {

    private RcuNode<T> head;
    private static int MAX_THREADS = 128;
    final static long NOT_READING = Long.MAX_VALUE;
    final AtomicLong reclaimerVersion = new AtomicLong(0);
    final AtomicLongArray readersVersion = new AtomicLongArray(MAX_THREADS);

    int threadCount = 0;

    @SuppressWarnings("unchecked")
    public CoarseSetURCU() {
        head = new RcuNode<T>(Integer.MIN_VALUE);
        head.next = new RcuNode<T>(Integer.MAX_VALUE);
        for (int i = 0; i < MAX_THREADS; i++) readersVersion.set(i, NOT_READING);
    }

    @SuppressWarnings("unchecked")
    public CoarseSetURCU(int threads) {
        head = new RcuNode<>(Integer.MIN_VALUE);
        head.next = new RcuNode<>(Integer.MAX_VALUE);
        for (int i = 0; i < MAX_THREADS; i++) readersVersion.set(i, NOT_READING);
    }


    @Override
    public boolean add(T item, RcuThread<T> ctx) {
        int key = item.hashCode();
        synchronized (this) {

            RcuNode<T> pred = head;
            RcuNode<T> curr = pred.next;


            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }

            if (key == curr.key) {
                return false;
            }

            synchronize_rcu(pred, item, curr);

            return true;
        }

    }

    @Override
    public boolean remove(T item, RcuThread<T> thread) {
        return false;
    }
    @Override
    public boolean contains(T item, RcuThread<T> ctx) {
        int key = item.hashCode();

        int tid = getTID();

        rcu_read_lock(tid);

        RcuNode<T> pred = head;
        RcuNode<T> curr = pred.next;

        while (curr.key < key) {
            pred = curr;
            curr = curr.next;
        }

        return rcu_read_unlock(tid, key, curr);

    }

    public static int getTID() {

        return (int) (Thread.currentThread().getId() % MAX_THREADS);
    }

    public void rcu_read_lock(final int tid) {

        final long rv = reclaimerVersion.get();
        readersVersion.set(tid, rv);
        final long nrv = reclaimerVersion.get(); // check again

        if (rv != nrv) {
            readersVersion.lazySet(tid, nrv);
        }

    }

    public boolean rcu_read_unlock(final int tid, int key, RcuNode<T> curr ) {

        boolean result = key == curr.key;
        readersVersion.set(tid, NOT_READING);
        return result;

    }

    void synchronize_rcu(RcuNode<T> pred, T newItem, RcuNode<T> next) {
        RcuNode<T> newNode = new RcuNode<>(newItem, next);
        final long waitForVersion = reclaimerVersion.incrementAndGet();

        for (int i = 0; i < MAX_THREADS; i++) {

            while (readersVersion.get(i) < waitForVersion) {

//                System.out.println("Pending reader: "+i+"##################");
            };// spin
            pred.next = newNode;
        }

    }
}