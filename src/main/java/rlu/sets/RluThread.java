package rlu.sets;

public class RluThread<U> extends Thread {
    int lClock; // Local clock for each thread
    long id; // Thread id
    int wClock; // Write clock for each thread
    int runCounter; // To count the number of runs

    public RluThread(int id) {
        this.id = Thread.currentThread().getId();
        lClock = 0;
        wClock = 0;
        runCounter = 0;
    }

    RluNode<U>[] log1;
    RluNode<U>[] log2;
}
