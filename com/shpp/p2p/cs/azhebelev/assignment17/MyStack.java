package com.shpp.p2p.cs.azhebelev.assignment17;

/**
 * Class of Resizable-array implementation , implement same methods like methods from Stack
 *
 * @param <E> type of elements in this list
 */
public class MyStack<E> extends MyArrayList<E> {

    /**
     * Getting last pushed element of Stack
     *
     * @return last pushed element of Stack
     */
    public synchronized E peek() {
        return this.get(size() - 1);
    }

    /**
     * Removes last pushed element of stack
     *
     * @return removed element
     */
    public synchronized E pop() {
        return remove(size() - 1);
    }

    /**
     * Adding element to stack
     *
     * @param e element for adding
     * @return added element
     */
    public synchronized E push(E e) {
        add(e);
        return e;
    }

    /**
     * Method searches specified Object
     *
     * @param e specified Object
     * @return the 1-based position from the top of the stack where the object is located ,
     * or - 1 in case of no such Object
     */
    public synchronized int search(E e) {
        int position = lastIndexOf(e);
        return position = position == -1 ? -1 : size() - position;
    }
}
