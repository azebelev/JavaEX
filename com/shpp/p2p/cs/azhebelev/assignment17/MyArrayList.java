package com.shpp.p2p.cs.azhebelev.assignment17;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Class of Resizable-array implementation , implement same methods like methods from ArrayList
 *
 * @param <E> type of elements in this list
 */
public class MyArrayList<E> implements Iterable<E>{

    //array for containing data
    private Object[] data;

    //default value of data length
    private final int DEFAULT_SIZE = 15;

    //length of  data array
    private int size;

    //index of next Object which could be added , the same with numbers of all added Objects
    private int nextIndex = 0;

    /**
     * Construct empty list with initial capacity of 7
     */
    public MyArrayList() {
        size = DEFAULT_SIZE;
        data = new Object[size];
    }

    /**
     * Construct empty list with initial capacity of initialSize
     *
     * @param initialSize initial size of array
     */
    MyArrayList(int initialSize) {
        size = initialSize;
        data = new Object[size];
    }

    /**
     * Adding of Object to list
     *
     * @param e Object for adding
     */
    public void add(E e) {
        if (nextIndex == size) increaseCapacity();
        data[nextIndex] = e;
        nextIndex++;
    }

    /**
     * Inserting Object to position index
     *
     * @param index position of insert
     * @param e     Object for inserting
     */
    public void add(int index, E e) {
        if (index > nextIndex) throw new ArrayIndexOutOfBoundsException(
                index + " out of bound length " + nextIndex);
        if (nextIndex == size) increaseCapacity();
        Object[] part = Arrays.copyOfRange(data, index, nextIndex);
        System.arraycopy(part, 0, data, index + 1, part.length);
        data[index] = e;
        nextIndex++;
    }

    //Method for increasing of capacity then array is full
    private void increaseCapacity() {

        size = (size << 1) + 1;
        data = Arrays.copyOf(data, size);
    }

    /**
     * Method for receiving Object of specified index from list
     *
     * @param index index of got Object
     * @return Object of specified index
     */
    public E get(int index) {
        return (E) data[index];
    }

    /**
     * Method for identification of size of list
     *
     * @return size of list
     */
    public int size() {
        return nextIndex;
    }

    /**
     * Method change value of Object by index
     *
     * @param index of array item
     * @param e     previous value of item by index
     */
    public E set(int index, E e) {
        if (index > nextIndex) throw new IndexOutOfBoundsException();
        E oldValue = this.get(index);
        data[index] = e;
        return oldValue;
    }

    /**
     * Method checks list if it is empty
     *
     * @return if it is empty true else false
     */
    public boolean isEmpty() {
        return nextIndex == 0;
    }

    /**
     * Find index of firstly encountered specified Object
     *
     * @param e Obgect for search
     * @return index of specified Object if no specified Object returns -1
     */
    public int indexOf(E e) {
        if (e != null) {
            for (int i = 0; i < nextIndex; i++) {
                if (data[i].equals(e)) return i;
            }
        } else {
            for (int i = 0; i < nextIndex; i++) {
                if (data[i] == null) return i;
            }
        }
        return -1;
    }

    /**
     * Find index of lastly encountered specified Object
     *
     * @param e Obgect for search
     * @return index of specified Object if no specified Object returns -1
     */
    public int lastIndexOf(E e) {
        if (e != null) {
            for (int i = nextIndex - 1; i >= 0; i--) {
                if (data[i].equals(e)) return i;
            }
        } else {
            for (int i = nextIndex - 1; i >= 0; i--) {
                if (data[i] == null) return i;
            }
        }
        return -1;
    }

    /**
     * Check if there is specified Object
     *
     * @param e specified Object
     * @return true - Object exist in list , false - Object not exist in list
     */
    public boolean contains(E e) {
        return this.indexOf(e) != -1;
    }

    /**
     * Made empty list
     */
    public void clear() {
        size = DEFAULT_SIZE;
        data = new Object[size];
        nextIndex = 0;
    }

    /**
     * Cut out part from list
     *
     * @param start  start index including such item
     * @param finish finish index excluding such item
     * @return part of list determined by start and finish
     */
    public MyArrayList<E> subList(int start, int finish) {
        if (finish > nextIndex) throw new IndexOutOfBoundsException("finish more then MyArrayList size");
        MyArrayList<E> listPart = new MyArrayList<>();
        for (int i = start; i < finish; i++) {
            listPart.add(this.get(i));
        }
        return listPart;
    }

    /**
     * Remove item of list by index
     *
     * @param index index of item which will be removed
     * @return removed item value
     */
    public E remove(int index) {
        if (index >= nextIndex) throw new IndexOutOfBoundsException(
                "index " + index + " out of bound size " + nextIndex);
        Object[] part = Arrays.copyOfRange(data, index + 1, nextIndex);
        E e = (E) data[index];
        System.arraycopy(part, 0, data, index, part.length);
        data[nextIndex - 1] = null;
        nextIndex--;
        return e;
    }

    /**
     * Shows all item of list
     */
    public String toString() {
        String s ="";
        if (!this.isEmpty()) {

            s = s +"[";
            for (int i = 0; i < nextIndex - 1; i++) {
                s = s + this.get(i) + ",";
            }
            s = s + this.get(nextIndex - 1) + "]";
        } else System.out.println("EMPTY");
        return s;
    }

    /**
     * Make the same object with different link
     *
     * @return clone of Object
     */
    public MyArrayList<E> clone() {
        MyArrayList<E> clone = new MyArrayList<>();
        clone.data = this.data;
        clone.size = this.size;
        clone.nextIndex = this.nextIndex;
        return clone;
    }

    /**
     * Refactors to Object array
     *
     * @return Object array
     */
    public Object[] toArray() {
        Object[] array = new Object[nextIndex];
        for (int i = 0; i < nextIndex; i++) {
            array[i] = this.get(i);
        }
        return array;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < nextIndex;
            }

            @Override
            public E next() {
                ++index;
                return (E) data[index - 1];
            }
            public void removing() {
                remove();
            }
        };
    }
}
