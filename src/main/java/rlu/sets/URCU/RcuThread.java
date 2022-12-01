package rlu.sets.URCU;

import rlu.sets.RluSets.RluNode;

import java.util.ArrayList;
import java.util.List;

public class RcuThread<T> {

    RcuNode<T> node; // To store the node that the thread is currently working on

    public RcuThread() {
        node = null;
    }
}
