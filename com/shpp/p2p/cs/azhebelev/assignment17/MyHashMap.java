package com.shpp.p2p.cs.azhebelev.assignment17;

import java.util.*;

/**
 * Class used to provide user with associative array of keys and values , which could fined for a constant
 * time due to hashCode() method (according to this , class chooses the same index during putting
 * new key-value and during getting key-value because keys would have same result after using hashCode()
 * on them)
 *
 * @param <K> Generic for keys
 * @param <V> Generic for values
 */
@SuppressWarnings("unchecked")
public class MyHashMap<K, V> {

    /**
     * Default length of inner array
     */
    private final int DEFAULT_CAPACITY = 15;

    /**
     * Length of inner array
     */
    private int capacity;

    /**
     * Coefficient shows when it is required to refactor data to more capacious array ,
     * when overage number of objects inside array will be more then 75 % of it length
     * the capacity will grow up
     */
    private final double LOAD_FACTOR = 0.75;

    /**
     * number of saved objects
     */
    private int size;

    /**
     * Inner array of objects Entry<key,value>
     */
    private Entry<K, V>[] table;

    /**
     * limit after which inner array of daa should be refactored
     */
    private int capacityToLoad;

    /**
     * Constructor with  DEFAULT_CAPACITY
     */
    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        capacityToLoad = (int) (capacity * LOAD_FACTOR);
        table = new Entry[capacity];
    }

    /**
     * Constructor with stated capacity
     *
     * @param initialCapacity initial size of array
     */
    MyHashMap(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        capacity = initialCapacity;
        capacityToLoad = (int) (capacity * LOAD_FACTOR);
        table = new Entry[capacity];
    }

    /**
     * Inner Class, convenient for saving key-value, also have link to next Entry by the case of collusion
     *
     * @param <K> Generic for key
     * @param <V> Generic for value
     */
    public static class Entry<K, V> {

        private final int hash;
        private final K key;
        private V value;
        private Entry<K, V> next;

        /**
         * Constructor for Entry
         *
         * @param hash  hash code for key
         * @param key   key
         * @param value value
         * @param next  link to next Entry by the case of collision
         */
        Entry(int hash, K key, V value, Entry<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        //method return key value
        public final K getKey() {
            return this.key;
        }

        //method return value
        public final V getValue() {
            return this.value;
        }

        //comparison for keys
        public final boolean equals(Object entryKey) {
            return (Objects.equals(entryKey, this.key));
        }

        //method for printing Entry
        public final String toString() {
            return key + "=" + value;
        }
    }

    /**
     * Method returns hash code for object first 16 bit without changes and other bits will be change
     * according to XOR operation it decreases possibility of collision
     *
     * @param key key
     * @return hash code for object
     */
    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * Method choose array index for putting or getting Entry according to length of array and
     * hash code of object
     *
     * @param hash   modified hash code
     * @param length length of inner array
     * @return index for item in array
     */
    private int indexForHash(int hash, int length) {
        return hash & (length - 1);
    }

    /**
     * method put new Entry<key,value> to calculated position of array, in case of already occupied
     * position link it to previous Entry
     *
     * @param key   key
     * @param value value
     * @return value of puttied Entry
     */
    public V put(K key, V value) {
        int hash = hash(key);
        int i = indexForHash(hash, table.length);
        Entry<K, V> newNode = new Entry<>(hash, key, value, null);
        if (table[i] == null) {
            table[i] = newNode;
            size++;
        } else {
            Entry<K, V> x = table[i];
            while (true) {
                if (hash == x.hash && x.equals(key)) {
                    V oldValue = x.value;
                    x.value = newNode.value;
                    return oldValue;
                }
                if (x.next == null) {
                    x.next = newNode;
                    ++size;
                    break;
                }
                x = x.next;
            }
            if (size >= capacityToLoad) resize();
        }
        return null;
    }

    /**
     * Method increase length of inner array and reformat all data to new positions of array according to
     * ne length of array
     */
    private void resize() {
        capacity = (capacity << 1) + 1;
        capacityToLoad = (int) (capacity * LOAD_FACTOR);
        Entry<K, V>[] newTable = new Entry[capacity];
        for (Entry<K, V> bucket : table) {
            if (bucket == null) continue;
            for (Entry<K, V> currentNode = bucket; currentNode != null; currentNode = currentNode.next) {
                fillNewTable(newTable, currentNode.key, currentNode.value);
            }
        }
        table = newTable;
    }

    /**
     * Method equal to put() but without size check
     *
     * @param newTable new inner array of appropriate length
     * @param key      key of Entry
     * @param value    value of Entry
     */
    private void fillNewTable(Entry<K, V>[] newTable, K key, V value) {
        int hash = hash(key);
        int i = indexForHash(hash, newTable.length);
        Entry<K, V> newNode = new Entry<>(hash, key, value, null);
        if (newTable[i] == null) {
            newTable[i] = newNode;
        } else {
            Entry<K, V> x = newTable[i];
            while (true) {
                if (hash == x.hash && x.equals(key)) {
                    x.value = newNode.value;
                    break;
                }
                if (x.next == null) {
                    x.next = newNode;
                    break;
                }
                x = x.next;
            }
        }
    }

    /**
     * Method finds Entry by key
     *
     * @param key key
     * @return Entry of requested key
     */
    private Entry<K, V> getEntry(Object key) {
        int hash = hash(key);
        int i = indexForHash(hash, table.length);
        Entry<K, V> currentNode = table[i];
        if (currentNode == null) return null;
        if (key == null) {
            for (; currentNode != null; currentNode = currentNode.next) {
                if (currentNode.key == null) return currentNode;
            }
        } else {
            for (; currentNode != null; currentNode = currentNode.next) {
                if (hash == currentNode.hash && currentNode.equals(key)) return currentNode;
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        return getEntry(key) != null;
    }

    public V get(Object key) {
        Entry<K, V> currentNode;
        return (currentNode = getEntry(key)) == null ? null : currentNode.value;
    }

    public V remove(Object key) {
        int hash = hash(key);
        int i = indexForHash(hash, table.length);
        Entry<K, V> currentNode = table[i];
        if (currentNode == null) return null;
        Entry<K, V> prevNode = null;

        if (key == null) {
            for (; currentNode != null; currentNode = currentNode.next) {
                if (currentNode.key == null) {
                    if (prevNode == null) table[i] = currentNode.next;
                    else prevNode.next = currentNode.next;
                    size--;
                    return currentNode.value;
                }
                prevNode = currentNode;
            }
        } else {
            for (; currentNode != null; currentNode = currentNode.next) {
                if (hash == currentNode.hash && currentNode.equals(key)) {
                    if (prevNode == null) table[i] = currentNode.next;
                    else prevNode.next = currentNode.next;
                    size--;
                    return currentNode.value;
                }
                prevNode = currentNode;
            }
        }
        return null;
    }

    /**
     * Method checks if Map has Entry with requested value
     *
     * @param value value of Map
     * @return true - value found , otherwise false
     */
    public boolean containsValue(Object value) {
        V val;
        if (table != null && size != 0) {
            for (Entry<K, V> elem : table) {
                for (; elem != null; elem = elem.next) {
                    if ((val = elem.value) == value || (value != null && value.equals(val))) return true;
                }
            }
        }
        return false;
    }

    /**
     * Method vanish all previous data
     */
    public void clear() {
        table = new Entry[capacity];
        size = 0;
    }

    /**
     * Method for printing all Entries
     *
     * @return string of Entries
     */
    public String toString() {
        StringBuilder s = new StringBuilder("[");
        for (Entry<K, V> elem : this.entrySet()) {
            s.append(elem.key).append("=").append(elem.value).append(" ;");
        }
        s = new StringBuilder(s.substring(0, s.length() - 1) + "]");
        return s.toString();
    }

    public int size() {
        return size;
    }

    /**
     * Check Map Empty or not
     *
     * @return true - no data inside otherwise false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /*
    Next methods and objects are used for implementation of Iteration
     */

    /**
     * Method creates object EntrySet which is iterable by Entries
     *
     * @return object EntrySet which is iterable by Entries
     */
    public Set<Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    //object which is iterable by Entries
    final class EntrySet extends AbstractSet<Entry<K, V>> {
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return size;
        }
    }

    /**
     * Method creates object KeySet which is iterable by keys
     *
     * @return object KeySet which is iterable by keys
     */
    public Set<K> keySet() {
        return new KeySet();
    }

    //object which is iterable by keys
    final class KeySet extends AbstractSet<K> {

        @Override
        public Iterator<K> iterator() {
            return new EntryKeyIterator();
        }

        @Override
        public int size() {
            return size;
        }
    }

    /**
     * Method creates object Values which is iterable by values
     *
     * @return object Values which is iterable by values
     */
    public Set<V> values() {
        return new Values();
    }

    //object which is iterable by values
    final class Values extends AbstractSet<V> {

        @Override
        public Iterator<V> iterator() {
            return new EntryValuesIterator();
        }

        @Override
        public int size() {
            return size;
        }
    }

    /**
     * Method iterates value of EntrySet
     */
    final class EntryValuesIterator extends HashIterator implements Iterator<V> {
        public final V next() {
            return nextEntry().value;
        }

    }

    /**
     * Method iterates Entries of EntrySet
     */
    final class EntryIterator extends HashIterator implements Iterator<Entry<K, V>> {
        public final Entry<K, V> next() {
            return nextEntry();
        }
    }

    /**
     * Method iterates key of EntrySet
     */
    final class EntryKeyIterator extends HashIterator implements Iterator<K> {
        public final K next() {
            return nextEntry().key;
        }
    }

    /**
     * Method iterates Entries of EntrySet
     */
    abstract class HashIterator {
        private int currentIndex = 0;
        Entry<K, V> nextItem = table[0];
        int indexInTab = 0;

        /**
         * Method check the end of array
         */
        public boolean hasNext() {
            return currentIndex < size;
        }

        /**
         * Method find next Entry
         *
         * @return next Entry
         */
        public Entry<K, V> nextEntry() {
            for (; indexInTab < table.length; indexInTab++) {
                if (nextItem != null) {
                    Entry<K, V> node = nextItem;
                    nextItem = nextItem.next;
                    ++currentIndex;
                    return node;
                } else nextItem = table[indexInTab + 1];
            }
            return null;
        }

        /**
         * Method remove current Entry
         */
        public void removing() {
            remove(nextItem.key);
        }
    }
}

