package rlu.sets;

import java.util.LinkedList;

public class RluThread<T> extends Thread {
    int lClock; // Local clock for each thread
    int wClock; // Write clock for each thread
    int runCounter; // To count the number of runs
    boolean isWriter; // To indicate if the thread is a writer
    LinkedList<RluNode<T>.Header<T>> log1;
    LinkedList<RluNode<T>.Header<T>> log2;

    public RluThread(long id) {
        lClock = 0;
        wClock = Integer.MAX_VALUE;
        runCounter = 0;
        log1 = new LinkedList<>();
        log2 = new LinkedList<>();
    }
    // rlu thread should be created in the JMH ScopeThread Setup state method for
    // every thread and then passed as value to every add /remove / contains call
}
