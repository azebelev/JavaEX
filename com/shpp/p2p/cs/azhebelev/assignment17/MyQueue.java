package com.shpp.p2p.cs.azhebelev.assignment17;

import java.util.NoSuchElementException;

/**
 * Class of Resizable-queue implementation , implement same methods like methods from ArrayDeque
 *
 * @param <E> type of elements in this list
 */
public class MyQueue<E> extends MyLinkedList<E> {

    /**
     * Maximum Capacity of Queue
     */
    private int maxCapacity;
    /**
     * Help to identify if required to take in to account maxCapacity
     */
    private boolean isCapacityStated = false;

    /**
     * Construct empty Queue with changeable size
     */
    MyQueue() {
    }

    /**
     * Construct empty Queue with specified size
     *
     * @param capacity value of maximum capacity
     */
    MyQueue(int capacity) {
        maxCapacity = capacity;
        isCapacityStated = true;
    }

    /**
     * Methods try to add new element to queue
     *
     * @param e offered element
     * @return true if capacity permits adding, false if capacity not permits adding
     */
    public boolean offer(E e) {
        if (!isCapacityStated) {
            add(e);
            return true;
        }
        if (isCapacityStated && size != maxCapacity) {
            add(e);
            return true;
        } else return false;
    }

    /**
     * Removes first added element of queue, throws NoSuchElementException then queue is empty
     *
     * @return removed element
     */
    public E remove() {
        if (size == 0) throw new NoSuchElementException("EMPTY QUEUE unable to delete object");
        return removeFirst();
    }

    /**
     * Getting first element of queue, throws NoSuchElementException then queue is empty
     *
     * @return first element
     */
    public E element() {
        if (size == 0) throw new NoSuchElementException("EMPTY QUEUE");
        return getFirst();
    }

    /**
     * Getting first element of queue
     *
     * @return first element or in case of empty queue null
     */
    public E peek() {
        if (size == 0) return null;
        return getFirst();
    }

    /**
     * Removes first added element of queue
     *
     * @return first element or null (in case of empty queue)
     */
    public E poll() {
        if (size == 0) return null;
        return removeFirst();
    }
}
