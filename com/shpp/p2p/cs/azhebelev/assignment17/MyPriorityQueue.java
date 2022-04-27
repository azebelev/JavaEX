package com.shpp.p2p.cs.azhebelev.assignment17;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Class for saving data in oder according to comparator, data located in array in spacial oder
 * of priority heap(tree with root which have greatest priority and leaves(right,left) with less
 * priority , such leaves could be a parent for next leaves with extra less priority and so on...
 * Sorting inside queue is made in a shot period of time because algorithm check only levels
 * of tree not all leaves
 *
 * @param <E> Generic for element of MyPriorityQueue
 */
@SuppressWarnings("unchecked")

public class MyPriorityQueue<E> implements Iterable<E> {
    private int heapSize;
    private Object[] queue;
    private final Comparator<? super E> comparator;
    private static int sizeOfInnerArray = 15;

    /**
     * Constructor with initial capacity
     *
     * @param initialCapacity initial length of inner array
     * @param comparator      comparator for saving object
     */
    public MyPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        this.comparator = comparator;
        if (initialCapacity < 1)
            throw new IllegalArgumentException();
        this.queue = new Object[initialCapacity];
    }

    /**
     * Constructor with default initial capacity and priority of natural ordering
     */
    public MyPriorityQueue() {
        this(sizeOfInnerArray, null);
    }

    /**
     * Constructor with stated comparator
     *
     * @param comparator stated comparator
     */
    public MyPriorityQueue(Comparator<? super E> comparator) {
        this(sizeOfInnerArray, comparator);
    }

    /**
     * Method add new data and perform sorting
     *
     * @param addedOb not null object for adding
     */
    public void add(E addedOb) {
        if (addedOb == null) throw new NullPointerException();
        if (heapSize == sizeOfInnerArray) increaseCapacity();
        int position = heapSize;
        siftUp(position, addedOb);
        heapSize++;
    }

    /**
     * Method choose correct method(with comparator or without comparator)
     *
     * @param position initial position for check parents
     * @param addedOb  not null object for adding
     */
    private void siftUp(int position, E addedOb) {
        if (comparator != null) siftUpUsingComparator(position, addedOb, comparator, queue);
        else siftUpComparable(position, addedOb, queue);
    }

    /**
     * Method check parent and lift up position of item in priority heap according to comparator
     *
     * @param position start position of leaves
     * @param addedOb  added object
     * @param queue    inner array of data
     * @param <E>      generic for object
     */
    private static <E> void siftUpComparable(int position, E addedOb, Object[] queue) {
        Comparable<? super E> comparator = (Comparable<? super E>) addedOb;

        queue[position] = addedOb;
        int parentPos = (position - 1) >>> 1;
        while (position > 0) {
            if (comparator.compareTo((E) queue[parentPos]) >= 0) break;
            Object temp = queue[parentPos];
            queue[parentPos] = queue[position];
            queue[position] = temp;
            position = parentPos;
            parentPos = (position - 1) >>> 1;
        }
    }

    /**
     * Method check parent and lift up position of item in priority heap according to comparator
     *
     * @param position start position of leaves
     * @param addedOb  added object
     * @param queue    inner array of data
     * @param <E>      generic for object
     */
    private static <E> void siftUpUsingComparator(int position, E addedOb,
                                                  Comparator<? super E> comparator, Object[] queue) {
        queue[position] = addedOb;
        int parentPos = (position - 1) >>> 1;
        while (position > 0) {
            if (comparator.compare(addedOb, (E) queue[parentPos]) >= 0) break;
            Object temp = queue[parentPos];
            queue[parentPos] = queue[position];
            queue[position] = temp;
            position = parentPos;
            parentPos = (position - 1) >>> 1;
        }
    }

    /**
     * Method returns object of greatest priority
     *
     * @return object of greatest priority
     */
    public E peek() {
        return (E) queue[0];
    }

    /**
     * Method returns index of stated object
     *
     * @param o object for search
     * @return index of stated object
     */
    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < heapSize; i++)
                if (o.equals(queue[i]))
                    return i;
        }
        return -1;
    }

    /**
     * Method checks if where is an object or not
     *
     * @param o object for search
     * @return true object exist, false not exist
     */
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    /**
     * Method check if queue is empty or not
     *
     * @return true queue is empty, false not empty
     */
    public boolean isEmpty() {
        return heapSize == 0;
    }

    /**
     * Method remove first element of queue and return it
     *
     * @return first element of queue
     */
    public E poll() {
        return removeAt(0);
    }

    /**
     * Method remove stated object
     *
     * @param o object which should be removed
     * @return true then object removed successfully otherwise false
     */
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i == -1)
            return false;
        else {
            removeAt(i);
            return true;
        }
    }

    /**
     * Method remove object of stated index
     *
     * @param index index of object which should be removed
     * @return removed object
     */
    private E removeAt(int index) {
        if (index >= heapSize) throw new NullPointerException();
        --heapSize;
        E removedOb = (E) queue[index];
        if (heapSize == index) queue[index] = null;
        else {
            Object moved = queue[index] = queue[heapSize];
            queue[heapSize] = null;
            siftDown(index);
            if (queue[index] == moved) siftUp(index, (E) moved);
        }
        return removedOb;
    }

    /**
     * Method chose correct method for sift down operations(with comparator or not)
     *
     * @param index start position of check
     */
    private void siftDown(int index) {
        if (comparator != null)
            siftDownUsingComparator(index, queue, heapSize, comparator);
        else
            siftDownComparable(index, queue, heapSize);
    }

    /**
     * Method check last item (tried like parent) in queue from stated position for priority if priority less
     * then children(leaves) it will change with them by places
     *
     * @param parent     index of item tried as parent for comparison
     * @param queue      array of priority heap
     * @param heapSize   length of priority heap
     * @param comparator comparator for comparison
     */
    private void siftDownUsingComparator(int parent, Object[] queue, int heapSize,
                                         Comparator<? super E> comparator) {
        int leftChild = (parent << 1) + 1;
        int rightChild = (parent << 1) + 2;
        E temp;
        if (leftChild > heapSize && comparator.compare((E) queue[parent], (E) queue[leftChild]) > 0) {
            temp = (E) queue[parent];
            queue[parent] = queue[leftChild];
            queue[leftChild] = temp;
            siftDownUsingComparator(leftChild, queue, heapSize, comparator);
        }
        if (rightChild > heapSize && comparator.compare((E) queue[parent], (E) queue[leftChild]) > 0) {
            temp = (E) queue[parent];
            queue[parent] = queue[rightChild];
            queue[rightChild] = temp;
            siftDownUsingComparator(rightChild, queue, heapSize, comparator);
        }
    }

    /**
     * Method check last item (tried like parent) in queue from stated position for priority if priority less
     * then children(leaves) it will change with them by places. Comparison without comparator using natural
     * ordering
     *
     * @param parent   index of item tried as parent for comparison
     * @param queue    array of priority heap
     * @param heapSize length of priority heap
     */
    private static <E> void siftDownComparable(int parent, Object[] queue, int heapSize) {
        int leftChild = (parent << 1) + 1;
        int rightChild = (parent << 1) + 2;
        E temp;
        if (rightChild < heapSize && ((Comparable<? super E>) queue[parent]).compareTo((E) queue[rightChild]) > 0) {
            temp = (E) queue[parent];
            queue[parent] = queue[rightChild];
            queue[rightChild] = temp;
            siftDownComparable(rightChild, queue, heapSize);
        }
        if (leftChild < heapSize && ((Comparable<? super E>) queue[parent]).compareTo((E) queue[leftChild]) > 0) {
            temp = (E) queue[parent];
            queue[parent] = queue[leftChild];
            queue[leftChild] = temp;
            siftDownComparable(leftChild, queue, heapSize);
        }
    }

    /**
     * Create array with length of priority heap length
     *
     * @return array with length of priority heap length
     */
    public Object[] toArray() {
        return Arrays.copyOf(queue, heapSize);
    }

    /**
     * returns size of priority queue
     *
     * @return size of priority queue
     */
    public int size() {
        return heapSize;
    }

    /**
     * vanishes all data from priority queue
     */
    public void clear() {
        for (int i = 0; i < heapSize; i++) {
            queue[i] = null;
        }
        heapSize = 0;
    }

    /**
     * reformat inner array to bigger size
     */
    private void increaseCapacity() {
        sizeOfInnerArray = (sizeOfInnerArray << 1) + 1;
        queue = Arrays.copyOf(queue, sizeOfInnerArray);
    }

    /**
     * implementation of iteration
     *
     * @return Iterator
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < heapSize;
            }

            @Override
            public E next() {
                ++index;
                return (E) (queue[index - 1]);
            }
        };
    }

    /**
     * Method creates string of data of priority queue
     *
     * @return string of data of priority queue
     */
    public String toString() {
        StringBuilder stringQ = new StringBuilder();
        if (heapSize > 0) {
            stringQ = new StringBuilder("" + "[");
            for (int i = 0; i < heapSize - 1; i++) {
                stringQ.append(queue[i]).append(", ");
            }
            stringQ.append(queue[heapSize - 1]).append("]");
        }
        return stringQ.toString();
    }
}