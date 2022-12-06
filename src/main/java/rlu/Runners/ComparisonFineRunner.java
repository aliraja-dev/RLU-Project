//package rlu.Runners;
//
//import rlu.sets.ComparisonSets.FineBST;
//import rlu.sets.ComparisonSets.Threads;
//import rlu.sets.RluSets.RluFineSet;
//import rlu.sets.RluSets.RluThread;
//import rlu.sets.interfaces.ComparableSet;
//import rlu.threads.ComparisonFineReader;
//import rlu.threads.ComparisonFineWriter;
//import rlu.threads.ReaderThread;
//import rlu.threads.WriterThread;
//
//public class ComparisonFineRunner {
//    public static void main(String[] args) throws Exception {
//        runThreads(2, 2, 10);
//    }
//
//    private static void runThreads(int writers, int readers, int iters) throws Exception {
//        FineBST<Integer> set = new FineBST<>();
//
//        ComparisonFineWriter<Integer> writerThread1 = new ComparisonFineWriter<>(set, new Threads<Integer>(),
//                iters);
//        ComparisonFineWriter<Integer> writerThread2 = new ComparisonFineWriter<>(set, new Threads<Integer>(), iters);
//
//        ComparisonFineReader<Integer> readerThread1 = new ComparisonFineReader<>(set, new Threads<Integer>(), iters);
//        ComparisonFineReader<Integer> readerThread2 = new ComparisonFineReader<>(set, new Threads<Integer>(), iters);
//
//        writerThread1.start();
////        writerThread2.start();
//        readerThread1.start();
////        readerThread2.start();
//
//        writerThread1.join();
////        writerThread2.join();
//        readerThread1.join();
////        readerThread2.join();
//
//        set.toString();
////        System.out.println("Writer 1 elapsed: " + ((WriterThread<Integer>) writerThread1).getElapsedTime());
////        System.out.println("Writer 2 elapsed: " + ((WriterThread<Integer>) writerThread2).getElapsedTime());
////        System.out.println("Reader 1 elapsed: " + ((ReaderThread<Integer>) readerThread1).getElapsedTime());
////        System.out.println("Reader 2 elapsed: " + ((ReaderThread<Integer>) readerThread2).getElapsedTime());
//
//    }
//
//}
