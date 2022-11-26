package rlu;

import rlu.sets.RluSet.RluCoarseSet;
import rlu.sets.RluSet.RluThread;

public class Runner<T> {

    public static void main(String[] args) throws Exception {
        runThreads(2, 2, 50);
    }

    private static void runThreads(int writers, int readers, int iters) throws Exception {
        RluCoarseSet<Integer> set = new RluCoarseSet<>();

        Thread writerThread1 = new WriterThread<>(set, new RluThread<Integer>(),
                iters);
        Thread writerThread2 = new WriterThread<Integer>(set, new RluThread<Integer>(), iters);

        Thread readerThread1 = new ReaderThread<Integer>(set, new RluThread<Integer>(), iters);
        Thread readerThread2 = new ReaderThread<Integer>(set, new RluThread<Integer>(), iters);

        writerThread1.start();
        writerThread2.start();
        readerThread1.start();
        readerThread2.start();

        writerThread1.join();
        writerThread2.join();
        readerThread1.join();
        readerThread2.join();

        // System.out.println("Writer 1 elapsed: " + ((WriterThread<Integer>)
        // writerThread1).getElapsedTime());
        System.out.println("Writer 2 elapsed: " + ((WriterThread<Integer>) writerThread2).getElapsedTime());
        System.out.println("Reader 1 elapsed: " + ((ReaderThread<Integer>) readerThread1).getElapsedTime());
        System.out.println("Reader 2 elapsed: " + ((ReaderThread<Integer>) readerThread2).getElapsedTime());

    }

}