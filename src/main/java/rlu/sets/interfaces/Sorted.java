package rlu.sets.interfaces;
import rlu.sets.RLUBST.RluThread;

public interface Sorted<T extends Comparable<T>> {

    public boolean add(T item, RluThread<T> ctx);
    public boolean remove(T item, RluThread<T> ctx);
    public boolean contains(T item, RluThread<T> ctx);

}

