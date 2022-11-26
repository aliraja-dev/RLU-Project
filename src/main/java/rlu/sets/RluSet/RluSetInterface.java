package rlu.sets.RluSet;

public interface RluSetInterface<T> {

    /**
     * Adds item to set (no effect if already present)
     * 
     * @param item
     * @return {@code true} if added, {@code false} otherwise
     */
    boolean add(T item, RluThread<T> thread);

    boolean add(T[] item, RluThread<T> thread);

    /**
     * Removes item from set (if present)
     * 
     * @param item
     * @return {@code true} if item removed, {@code false} otherwise
     */
    // boolean remove(T item, RluThread<T> thread);

    /**
     * Checks if item is in the set
     * 
     * @param item
     * @return {@code true} if item in set, {@code false} otherwise
     */
    boolean contains(T item, RluThread<T> thread);
}
