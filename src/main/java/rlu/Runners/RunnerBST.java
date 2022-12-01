package rlu.Runners;

import rlu.sets.RLUBST.RLUCoarseSetBST;
import rlu.sets.RLUBST.RluThread;
import rlu.threads.ReaderBST;
import rlu.threads.WriterBST;

public class RunnerBST<T>{
    public static void main(String[] args) throws Exception {
        runThreads(2, 2, 10);
    }

    private static void runThreads(int writers, int readers, int iters) throws Exception {
        RLUCoarseSetBST<Integer> set = new RLUCoarseSetBST<>();

        Thread writerThread1 = new WriterBST<>(set, new RluThread<Integer>(), iters);
        Thread writerThread2 = new WriterBST<>(set, new RluThread<Integer>(), iters);

        Thread readerThread1 = new ReaderBST<>(set, new RluThread<Integer>(), iters);
        Thread readerThread2 = new ReaderBST<>(set, new RluThread<Integer>(), iters);

        writerThread1.start();
        readerThread1.start();
//        writerThread2.start();
        readerThread2.start();

        writerThread1.join();
        readerThread1.join();
//        writerThread2.join();
        readerThread2.join();

//        System.out.println("Writer 1 elapsed: " + ((WriterBST<Integer>) writerThread1).getElapsedTime());
//        System.out.println("Writer 2 elapsed: " + ((WriterBST<Integer>) writerThread2).getElapsedTime());
//        System.out.println("Reader 1 elapsed: " + ((ReaderBST<Integer>) readerThread1).getElapsedTime());
//        System.out.println("Reader 2 elapsed: " + ((ReaderBST<Integer>) readerThread2).getElapsedTime());

    }

}

