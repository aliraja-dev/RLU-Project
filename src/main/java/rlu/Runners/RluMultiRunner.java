package rlu.Runners;

import rlu.sets.RluSets.MultiObjRluCoarseSet;
import rlu.sets.RluSets.RluThread;
import rlu.threads.MultiWriterThread;
import rlu.threads.ReaderThread;

public class RluMultiRunner {
    public static void main(String[] args) throws Exception {
        runThreads(2, 2, 10000);
    }

    private static void runThreads(int writers, int readers, int iters) throws Exception {
        MultiObjRluCoarseSet<Integer> set = new MultiObjRluCoarseSet<>();

        MultiWriterThread<Integer> writerThread1 = new MultiWriterThread<>(set, new RluThread<Integer>(),
                iters);
        MultiWriterThread<Integer> writerThread2 = new MultiWriterThread<>(set, new RluThread<Integer>(), iters);

        ReaderThread<Integer> readerThread1 = new ReaderThread<>(set, new RluThread<Integer>(), iters);
        ReaderThread<Integer> readerThread2 = new ReaderThread<>(set, new RluThread<Integer>(), iters);

        writerThread1.start();
        writerThread2.start();
        readerThread1.start();
        readerThread2.start();

        writerThread1.join();
        writerThread2.join();
        readerThread1.join();
        readerThread2.join();

        System.out.println("Writer 1 elapsed: " + ((MultiWriterThread<Integer>) writerThread1).getElapsedTime());
        System.out.println("Writer 2 elapsed: " + ((MultiWriterThread<Integer>) writerThread2).getElapsedTime());
        System.out.println("Reader 1 elapsed: " + ((ReaderThread<Integer>) readerThread1).getElapsedTime());
        System.out.println("Reader 2 elapsed: " + ((ReaderThread<Integer>) readerThread2).getElapsedTime());

    }
}
