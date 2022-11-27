package rlu.threads;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import rlu.sets.RluSets.RluSetInterface;
import rlu.sets.RluSets.RluThread;

public class WriterThread<T> extends Thread {
    private RluThread<T> thread;
    private RluSetInterface<T> set;
    private int id;
    private long elapsed;
    private int iter;

    public WriterThread(RluSetInterface<T> set, RluThread<T> thread, int iter) {
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
            item = rand.nextInt(100);
            // System.out.println("Writer: " + item + set.add((T) Integer.valueOf(item),
            // thread));
            set.add((T) Integer.valueOf(item), thread);
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
