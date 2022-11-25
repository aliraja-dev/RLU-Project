package rlu;

import rlu.sets.RluCoarseSet;
import rlu.sets.RluThread;

public class ReaderThread<T> extends Thread {
    private static int ID_GEN = 0;

    private RluThread<T> thread;
    private RluCoarseSet<T> set;
    private int id;
    private long elapsed;
    private int iter;

    public ReaderThread(RluCoarseSet<T> set, RluThread<T> thread, int iter) {
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
        long start = System.currentTimeMillis();

        for (int i = 0; i < iter; i++)
            set.contains((T) Integer.valueOf(i), thread);

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
