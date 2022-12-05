package rlu.sets.interfaces;

import rlu.sets.ComparisonSets.Threads;
import rlu.sets.RluSets.RluThread;

public interface ComparableSet <T extends Comparable<T>>{

    boolean add(T item, Threads<T> ctx);
    boolean remove(T item, Threads<T> ctx);

    boolean contains(T item, Threads<T> ctx);

}
