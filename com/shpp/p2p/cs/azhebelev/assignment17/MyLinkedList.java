package com.shpp.p2p.cs.azhebelev.assignment17;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class of Resizable-list implementation , implement same methods like methods from LinkedList
 *
 * @param <E> type of elements in this list
 */
public class MyLinkedList<E> implements Iterable<E> {

    /**
     * size of list
     */
    protected int size;
    /**
     * First MyNode Object
     */
    private MyNode first;
    /**
     * last MyNode Object
     */
    private MyNode last;


    /**
     * Constructs empty list
     */
    MyLinkedList() {

    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int index = 0;
            MyNode item = first;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                index++;
                MyNode current = item;
                item = item.next;
                return current.item;

            }
        };
    }

    /**
     * Inner class for link creation
     */
    private class MyNode {

        // Value of MyNode also value of list item
        E item;

        // link for previous MyNode
        MyNode next;

        //link for next MyNode
        MyNode prev;

        /**
         * Constructs Object MyNode by specified links
         *
         * @param prev link for previous MyNode
         * @param elem value of item in list
         * @param next link for next MyNode
         */
        MyNode(MyNode prev, E elem, MyNode next) {
            this.prev = prev;
            this.item = elem;
            this.next = next;
        }
    }

    /**
     * Adds new element to list
     *
     * @param e element for adding
     */
    public void add(E e) {
        MyNode l = last;
        MyNode newNode = new MyNode(l, e, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else l.next = newNode;
        size++;
    }

    /**
     * Adds element to the beginning of list
     *
     * @param e element for adding
     */
    public void addFirst(E e) {
        if (size == 0) {
            add(e);
        } else {
            MyNode newNode = new MyNode(null, e, node(0));
            first = newNode;
            newNode.next.prev = newNode;
            size++;
        }
    }

    /**
     * Adds element to specified position by index
     *
     * @param index position of insert
     * @param e     element for inserting
     */
    public void add(int index, E e) {
        if (index < 0 || index > size + 1) throw new IndexOutOfBoundsException(
                "Incorrect index " + index + " for size " + size);
        if (index == size) {
            add(e);
            return;
        }
        if (index == 0) {
            addFirst(e);
            return;
        }
        MyNode n = node(index);
        MyNode newNode = new MyNode(n.prev, e, n);
        n.prev.next = newNode;
        n.prev = newNode;
        size++;
    }

    /**
     * Method for size defining
     *
     * @return size of list
     */
    public int size() {
        return size;
    }

    /**
     * Method used by another methods then it is required to be within size
     * <p>
     * trows IndexOutOfBoundsException
     *
     * @param index checked index
     */
    private void checkSize(int index) {
        if (index > size - 1 || index < 0)
            throw new IndexOutOfBoundsException("Incorrect index " + index + " for size " + size);
    }

    /**
     * Finds MyNode of specified index
     *
     * @param index index MyNode
     * @return MyNode of specified index
     */
    public MyNode node(int index) {
        checkSize(index);
        MyNode elem;
        if (index < (size >> 1)) {
            elem = first;
            for (int i = 0; i < index; i++) {
                elem = elem.next;
            }
        } else {
            elem = last;
            for (int i = size - 1; i > index; i--) {
                elem = elem.prev;
            }
        }
        return elem;
    }

    /**
     * Getting of element of list by specified index
     *
     * @param index index of element of list
     * @return element of list by specified index
     */
    public E get(int index) {
        checkSize(index);
        MyNode nodeOfIndex = node(index);
        return nodeOfIndex.item;
    }

    /**
     * Changes of list element to new value
     *
     * @param index index of changing element
     * @param e     new value of list element
     */
    public void set(int index, E e) {
        checkSize(index);
        node(index).item = e;
    }

    /**
     * Checks if list is empty
     *
     * @return true - empty list , false - not empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes last list element , throws IndexOutOfBoundsException if list is empty
     *
     * @return value of removed element
     */
    public E removeLast() {
        if (size == 0) throw new IndexOutOfBoundsException(" List is EMPTY unable to delete Object ");
        E e;
        if (size > 1) {
            e = last.item;
            last.prev.next = null;
            last = last.prev;
            size--;
        } else {
            e = last.item;
            size = 0;
            first = null;
            last = null;
        }
        return e;
    }

    /**
     * Removes list element by index
     *
     * @param index index of removing element
     * @return removed element
     */
    public E remove(int index) {
        checkSize(index);
        E e;
        if (size == 1 || index == 0) {

            return removeFirst();
        }
        if (size == index + 1)
            return removeLast();
        else {
            MyNode nodeOfIndex = node(index);
            e = nodeOfIndex.item;
            nodeOfIndex.prev.next = nodeOfIndex.next;
            nodeOfIndex.next.prev = nodeOfIndex.prev;
            size--;
        }
        return e;
    }

    /**
     * Removes first list element , throws NoSuchElementException if list is empty
     *
     * @return removed element
     */
    public E removeFirst() {
        if (size == 0) throw new NoSuchElementException(" List is EMPTY unable to delete Object ");
        E e;
        if (size > 1) {
            e = first.item;
            first.next.prev = null;
            first = first.next;
            size--;
        } else {
            e = first.item;
            size = 0;
            first = null;
            last = null;
        }
        return e;
    }

    /**
     * Removes list element by index
     *
     * @param index index of removing element
     * @return removed element
     */
    public E removeAt(int index) {
        checkSize(index);
        E e;
        if (size == 1 || index == 0) {

            return removeFirst();
        }
        if (size == index + 1)
            return removeLast();
        else {
            MyNode nodeOfIndex = node(index);
            e = nodeOfIndex.item;
            nodeOfIndex.prev.next = nodeOfIndex.next;
            nodeOfIndex.next.prev = nodeOfIndex.prev;
            size--;
        }
        return e;
    }

    /**
     * Getting first element from list, throws IndexOutOfBoundsException if lis is empty
     *
     * @return first element in list
     */
    public E getFirst() {
        if (size == 0) throw new IndexOutOfBoundsException(" List is EMPTY unable to delete Object ");
        return first.item;
    }

    /**
     * Getting last element from list, throws IndexOutOfBoundsException if lis is empty
     *
     * @return last element in list
     */
    public E getLast() {
        if (size == 0) throw new IndexOutOfBoundsException(" List is EMPTY unable to delete Object ");
        return last.item;
    }

    /**
     * Remove first list element, throws IndexOutOfBoundsException in case of empty list
     *
     * @return removed element
     */
    public E pollFirst() {
        if (size == 0) throw new IndexOutOfBoundsException(" List is EMPTY unable to delete Object ");
        E f = first.item;
        removeFirst();
        return f;
    }

    /**
     * Remove last list element, throws IndexOutOfBoundsException in case of empty list
     *
     * @return removed element
     */
    public E pollLast() {
        if (size == 0) throw new IndexOutOfBoundsException(" List is EMPTY unable to delete Object ");
        E l = last.item;
        removeLast();
        return l;
    }

    public void clear() {
        size = 0;
        first = null;
        last = null;
    }

    /**
     * Refactors to Object array
     *
     * @return Object array
     */
    public Object[] toArray() {
        Object[] array = new Object[size];
        int count = 0;
        for (MyNode x = first; x != null; x = x.next) {
            array[count] = x.item;
            count++;
        }
        return array;
    }

    /**
     * Shows all item of list
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        if (!this.isEmpty()) {
            Object[] objects = this.toArray();
            s.append("[ ");
            for (int i = 0; i < objects.length - 1; i++) {
                s.append(objects[i]).append(", ");
            }
            s.append(objects[objects.length - 1]).append(" ]");
        }
        return s.toString();
    }
}
