package rlu.Runners;

import rlu.sets.URCU.CoarseSetURCU;
import rlu.sets.URCU.FineSetURCU;
import rlu.sets.URCU.RcuThread;
import rlu.threads.RcuReader;
import rlu.threads.RcuWriter;

public class RunnerURCU<T> {

    public static void main(String[] args) throws Exception {
        runThreads(2, 2, 20);
    }

    private static void runThreads(int writers, int readers, int iters) throws Exception {
//        CoarseSetURCU<Integer> set = new CoarseSetURCU<>();
        FineSetURCU<Integer> set = new FineSetURCU<>();


        RcuWriter<Integer> writerThread1 = new RcuWriter<>(set, new RcuThread<Integer>(),
                iters);
//        RcuWriter<Integer> writerThread2 = new RcuWriter<>(set, new RcuThread<Integer>(), iters);

        RcuReader<Integer> readerThread1 = new RcuReader<>(set, new RcuThread<Integer>(), iters);
        RcuReader<Integer> readerThread2 = new RcuReader<>(set, new RcuThread<Integer>(), iters);

        writerThread1.start();
//        writerThread2.start();
        readerThread1.start();
        readerThread2.start();

        writerThread1.join();
//        writerThread2.join();
        readerThread1.join();
        readerThread2.join();

//        System.out.println("Writer 1 elapsed: " + ((WriterThread<Integer>) writerThread1).getElapsedTime());
//        System.out.println("Writer 2 elapsed: " + ((WriterThread<Integer>) writerThread2).getElapsedTime());
//        System.out.println("Reader 1 elapsed: " + ((ReaderThread<Integer>) readerThread1).getElapsedTime());
//        System.out.println("Reader 2 elapsed: " + ((ReaderThread<Integer>) readerThread2).getElapsedTime());

    }

}