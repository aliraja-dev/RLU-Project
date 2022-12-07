package rlu.threads;

import rlu.sets.RLUBST.RLUCoarseSetBST;
import rlu.sets.RLUBST.RluThread;
import rlu.sets.interfaces.Sorted;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ReaderBST<T extends Comparable<T>> extends Thread {
    private static int ID_GEN = 0;

    private RluThread<T> thread;

    private Sorted<T> set;
    private int id;
    private long elapsed;
    private int iter;

    public ReaderBST(Sorted<T> set, RluThread<T> thread, int iter) {
        id = ID_GEN++;
        this.thread = thread;
        this.iter = iter;
        this.set = set;
    }

    public static void reset() {
        ID_GEN = 0;
    }

    @Override
    public void run() {
        Random rand = ThreadLocalRandom.current();
        int item;
        long start = System.currentTimeMillis();

        for (int i = 0; i < iter; i++) {
            item = rand.nextInt(20);
//            item = 5;
            System.out.println("Reader Iteration: " + item + set.contains((T) Integer.valueOf(item), thread));

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
