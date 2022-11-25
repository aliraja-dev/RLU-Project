package rlu.sets;

public class RluThread<T> {
    int lClock; // Local clock for each thread
    int wClock; // Write clock for each thread
    int runCounter; // To count the number of runs
    boolean isWriter; // To indicate if the thread is a writer
    RluNode<T> node; // To store the node that the thread is currently working on

    public RluThread() {
        lClock = 0;
        wClock = Integer.MAX_VALUE;
        runCounter = 0;
        node = null;
    }
    // rlu thread should be created in the JMH ScopeThread Setup state method for
    // every thread and then passed as value to every add /remove / contains call
}
