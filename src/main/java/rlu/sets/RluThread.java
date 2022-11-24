package rlu.sets;

public class RluThread<U> extends Thread {
    int lClock; // Local clock for each thread
    long id; // Thread id
    int wClock; // Write clock for each thread
    int runCounter; // To count the number of runs
    boolean isWriter; // To indicate if the thread is a writer

    public RluThread(int id) {
        this.id = Thread.currentThread().getId();
        lClock = 0;
        wClock = 0;
        runCounter = 0;
        log1 = new LinkedList<RluNode<U>.Header<U>>();
        log2 = new LinkedList<RluNode<U>.Header<U>>();
    }

   

    LinkedList<RluNode<U>> log1;
    LinkedList<RluNode<U>> log2;

    //rlu thread should be created in the JMH ScopeThread Setup state method for every thread and then passed as value to every add /remove / contains call

}
