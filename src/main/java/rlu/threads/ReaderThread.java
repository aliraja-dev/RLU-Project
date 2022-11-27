package rlu.threads;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import rlu.sets.RluSet.RluSetInterface;
import rlu.sets.RluSet.RluThread;

public class ReaderThread<T> extends Thread {

    private RluThread<T> thread;
    private RluSetInterface<T> set;
    private int id;
    private long elapsed;
    private int iter;

    public ReaderThread(RluSetInterface<T> set, RluThread<T> thread, int iter) {
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
            System.out.println("Reader Iteration: " + item + set.contains((T) Integer.valueOf(item), thread));
            // System.out.println("Reader Iteration: " + 5 + set.contains((T)
            // Integer.valueOf(5), thread));

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
