package rlu.sets;

public class RluThread<U> extends Thread {
     int lClock; // Local clock for each thread
     int wClock; // Write clock for each thread
     int runCounter; // To count the number of runs
   boolean isWriter; // To indicate if the thread is a writer
    LinkedList<RluNode<U>.Header<U>> log1;
    LinkedList<RluNode<U>.Header<U>> log2;
    public RluThread(int id) {
        lClock = 0;
        wClock = Integer.MAX_VALUE;
        runCounter = 0;
        log1 = new LinkedList<RluNode<U>.Header<U>>();
        log2 = new LinkedList<RluNode<U>.Header<U>>();
    }
    //rlu thread should be created in the JMH ScopeThread Setup state method for every thread and then passed as value to every add /remove / contains call
}
