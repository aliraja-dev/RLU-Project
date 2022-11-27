package rlu.threads;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import rlu.sets.RluSet.RluSetInterface;
import rlu.sets.RluSet.RluThread;

public class MultiWriterThread<T> extends Thread {
    private RluThread<T> thread;
    private RluSetInterface<T> set;
    private int id;
    private long elapsed;
    private int iter;

    public MultiWriterThread(RluSetInterface<T> set, RluThread<T> thread, int iter) {

        this.thread = thread;
        this.iter = iter;
        this.set = set;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        Random rand = ThreadLocalRandom.current();
        long start = System.currentTimeMillis();

        for (int i = 0; i < iter; i++) {
            T[] items = (T[]) new Integer[2];
            for (int j = 0; j < 2; j++) {
                items[j] = (T) Integer.valueOf(rand.nextInt(100));
                // items[j] = rand.nextInt(5);
            }
            // T items[] = { 1, 5 };
            System.out.println("Multi Writer:" + set.add((T[]) items, thread));
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
