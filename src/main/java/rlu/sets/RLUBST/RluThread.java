package rlu.sets.RLUBST;


import java.util.ArrayList;
import java.util.List;

public class RluThread<T extends Comparable<T>> {
    int lClock; // Local clock for each thread
    int wClock; // Write clock for each thread
    int runCounter; // To count the number of runs
    boolean isWriter; // To indicate if the thread is a writer

    RLUBSTNode<T> nodeBST; // To store the node that the thread is currently working on
    RLUBSTNode<T> nodeBSTLeft; // To store the node that the thread is currently working on
    RLUBSTNode<T> nodeBSTRight; // To store the node that the thread is currently working on


    public RluThread() {
        lClock = 0;
        wClock = Integer.MAX_VALUE;
        runCounter = 0;
        nodeBST = null;
        nodeBSTLeft = null;
        nodeBSTRight = null;
    }
    // rlu thread should be created in the JMH ScopeThread Setup state method for
    // every thread and then passed as value to every add /remove / contains call
}
