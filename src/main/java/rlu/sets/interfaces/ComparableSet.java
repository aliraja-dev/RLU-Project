package rlu.sets.interfaces;

public interface ComparableSet <T extends Comparable<T>>{

    boolean add(T item);
    boolean remove(T item);

    boolean contains(T item);
}
