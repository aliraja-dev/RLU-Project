package rlu.threads;

import rlu.sets.ComparisonSets.Threads;
import rlu.sets.RluSets.RluSetInterface;
import rlu.sets.RluSets.RluThread;
import rlu.sets.interfaces.ComparableSet;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ComparisonFineWriter<T extends Comparable<T>> extends Thread {
    private Threads<T> thread;
    private ComparableSet set;
    private int id;
    private long elapsed;
    private int iter;

    public ComparisonFineWriter(ComparableSet set, Threads<T> thread, int iter) {
        this.thread = thread;
        this.iter = iter;
        this.set = set;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        Random rand = ThreadLocalRandom.current();
        int item;
        long start = System.currentTimeMillis();

        for (int i = 0; i < iter; i++) {
            item = rand.nextInt(10);
             System.out.println("Writer: " + item + set.add((Comparable) Integer.valueOf(item), thread));
//            set.add((Comparable) Integer.valueOf(item));

        }

        long end = System.currentTimeMillis();
        elapsed = end - start;
    }

    public int getThreadId() {
        return id;
    }

    public long getElapsedTime() {
        return elapsed;
    }
}
