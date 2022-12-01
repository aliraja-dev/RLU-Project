package rlu.threads;

import rlu.sets.RluSets.RluSetInterface;
import rlu.sets.RluSets.RluThread;
import rlu.sets.URCU.CoarseSetURCU;
import rlu.sets.URCU.RcuThread;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RcuWriter<T> extends Thread{
    private RcuThread<T> thread;
    private CoarseSetURCU<T> set;
    private int id;
    private long elapsed;
    private int iter;

    public RcuWriter(CoarseSetURCU<T> set, RcuThread<T> thread, int iter) {
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
            item = rand.nextInt(5);
//            set.add((T) Integer.valueOf(item), thread);
            System.out.println("Writer: " + item + set.add((T) Integer.valueOf(item), thread));
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
