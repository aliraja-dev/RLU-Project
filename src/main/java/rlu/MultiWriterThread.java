package rlu;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import rlu.sets.RluSet.RluSetInterface;
import rlu.sets.RluSet.RluThread;

public class MultiWriterThread<T> extends Thread {
    private static int ID_GEN = 0;

    private RluThread<T> thread;
    private RluSetInterface<T> set;
    private int id;
    private long elapsed;
    private int iter;

    public MultiWriterThread(RluSetInterface<T> set, RluThread<T> thread, int iter) {
        id = ID_GEN++;
        this.thread = thread;
        this.iter = iter;
        this.set = set;
    }

    public static void reset() {
        ID_GEN = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        Random rand = ThreadLocalRandom.current();
        long start = System.currentTimeMillis();

        for (int i = 0; i < iter; i++) {
            int[] items = new int[10];
            for (int j = 0; j < 2; j++) {
                items[j] = rand.nextInt(100);
            }
            System.out.println("Multi Writer: " + set.add((T) items, thread));
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
