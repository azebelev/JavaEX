package com.shpp.p2p.cs.azhebelev.assignment17;


import org.junit.Assert;
import org.junit.Test;
import java.util.*;

/**
 * Class for testing of My Collections
 * All variables case using for comparisons this expected value
 */

public class Assignment17Part1 {
    @Test
    public void getTestForMyPriorityQueue() {
        System.out.println("\n\n----------------Test of MyPriorityQueue-----------------");

        MyPriorityQueue<Integer> queue = new MyPriorityQueue<>();
        PriorityQueue<Integer> queue1 = new PriorityQueue<>();
        String s1 = "";
        String s2 = "";
        try {
            for (int i = 100; i >= 0; i--) {
                int elem = (int) (Math.random() * 100);
                queue.add(elem);
                queue1.add(elem);
            }
            System.out.println(queue);
            System.out.println(queue1);
            for (Integer n : queue) {
                if (n % 10 == 0) s1 = s1 + n;
            }
            for (Integer n : queue1) {
                if (n % 10 == 0) s2 = s2 + n;
            }
            for (int j = 0; j < 100; j++) {

                for (int i = 10; i >= 0; i--) {
                    if (queue.contains(i)) {
                        s1 = s1 + queue.peek();
                    }
                    if (queue1.contains(i)) {
                        s2 = s2 + queue1.peek();
                    }
                    queue.remove(i);
                    queue1.remove(i);
                }
                s1 = s1 + " " + queue.poll();
                s2 = s2 + " " + queue1.poll();
            }
        } catch (Exception e) {
        }
        System.out.println(s1);
        System.out.println(s2);
        Assert.assertTrue(queue.isEmpty());
        queue.add(1);
        queue.clear();
        Assert.assertTrue(queue.isEmpty());
        Assert.assertEquals(s1, s2);
        System.out.println("add, contains, remove, poll, peek : passed");
        System.out.println("----------------Test of MyPriorityQueue passed-----------------");

    }

    @Test
    public void getTestForMyHashMap() {
        System.out.println("\n\n----------------Test of MyHashmap-----------------");

        MyHashMap<String, Integer> map = new MyHashMap<>();
        HashMap<String, Integer> map1 = new HashMap<>();

        String s = "";
        String s1 = "";
        for (int i = 0; i < 100; i++) {
            for (int j = 1; j < 5; j++) {
                map.put("" + i, i / j);
                map1.put("" + i, i / j);
            }
        }
        for (int i = 0; i < 100; i += 10) {


            s = s + map.remove("" + i);
            s1 = s1 + map1.remove("" + i);
            if (map.containsKey("" + i / 2)) s = s + map.containsValue(map.containsKey("" + i / 2));
            if (map1.containsKey("" + i / 2)) s1 = s1 + map.containsValue(map.containsKey("" + i / 2));
        }
        for (MyHashMap.Entry<String, Integer> entry :
                map.entrySet()) {
            if (entry.getValue() % 2 == 0) {
                s = s + entry.getKey();
            }
        }
        for (Map.Entry<String, Integer> entry :
                map1.entrySet()) {
            if (entry.getValue() % 2 == 0) {
                s1 = s1 + entry.getKey();
            }
        }
        System.out.println(map);
        System.out.println(map1);
        map.clear();
        map1.clear();
        Assert.assertEquals(map.isEmpty(), map1.isEmpty());
        map.put(null, 1);
        map.put("", 2);
        map1.put(null, 1);
        map.put("", 1);
        Assert.assertEquals(map.get(null), map1.get(null));
        Assert.assertEquals(s, s1);
        System.out.println("put, getKey, getValue, clear, isEmpty, remove, containsKey, containsValue : passed");
        System.out.println("----------------Test of MyHashmap passed-----------------");

    }

    @Test
    public void getCheckMyArrayList() {
        System.out.println("\n\n----------------Test of MyArrayList-----------------");
        MyArrayList<Integer> list = new MyArrayList<>(20);
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        list.add(100, 1000);
        int case1 = list.get(100);
        int case2 = list.get(99);
        int case3 = list.remove(99);
        int case4 = list.size();
        Assert.assertEquals(1000, case1);
        Assert.assertEquals(99, case2);
        Assert.assertEquals(99, case3);
        Assert.assertEquals(100, case4);
        System.out.println("add , get , remove ,size : passed");
        try {
            list.set(101, 10);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundsException passed");
        }
        boolean case5 = list.contains(1000);
        int case6 = list.indexOf(-30);
        int case7 = list.lastIndexOf(0);
        Assert.assertTrue(case5);
        Assert.assertEquals(-1, case6);
        Assert.assertEquals(0, case7);
        int case8 = list.set(99, 500);
        Assert.assertEquals(1000, case8);
        System.out.println("indexOf , lastIndexOf , contains : passed");
        Object[] case9 = list.subList(0, 5).toArray();
        Assert.assertEquals(new Object[]{0, 1, 2, 3, 4}, case9);
        MyArrayList<Integer> clone = list.clone();
        int case10 = list.get(3);
        int cloneVar = clone.get(3);
        Assert.assertEquals(case10, cloneVar);
        Assert.assertNotEquals(list, clone);
        System.out.println(list);
        list.clear();
        boolean case11 = list.isEmpty();
        Assert.assertTrue(case11);
        System.out.println("clone , showArray , clear , isEmpty : passed");
        System.out.println("----------------Test of MyArrayList finished-----------------");
    }

    @Test
    public void getCheckMyStack() {
        System.out.println("\n\n----------------Test of MyStack-----------------");
        MyStack<String> stack = new MyStack<>();
        for (int i = 0; i < 100; i++) {
            String s = "*" + i;
            stack.push(s);
        }
        System.out.println(stack);
        for (int i = 0; i < 100; i++) {
            stack.pop();
        }
        Assert.assertTrue(stack.isEmpty());
        System.out.println("peek , push , pop , isEmpty : passed");
        String s = stack.push("Hello");
        stack.push("");
        Assert.assertEquals("Hello", s);
        Assert.assertEquals(2, stack.search("Hello"));
        Assert.assertEquals("", stack.peek());
        System.out.println("search , peek : passed");
        System.out.println("----------------Test of MyStack passed-----------------");
    }

    @Test
    public void getCheckForMyLinkedList() {
        System.out.println("\n\n----------------Test of MyLinkedList-----------------");
        MyLinkedList<String> list = new MyLinkedList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) list.add(i, "*" + i);
            else list.add("*" + i);
        }
        list.addFirst("Hello");
        list.add("Bye");
        System.out.println(list);
        Assert.assertEquals("Hello", list.getFirst());
        Assert.assertEquals("Bye", list.getLast());
        Assert.assertEquals(102, list.size());
        System.out.println("add,addFirst,getFirst,getLast,showArray : passed");
        String case1 = null;
        String case2 = null;
        for (int i = 0; i < 101; i++) {
            if (i == 0) {
                case1 = list.removeFirst();
                continue;
            }
            if (i == 1) {
                case2 = list.removeLast();
                continue;
            }
            if (i % 2 == 0) list.removeLast();
            else list.removeFirst();
        }
        list.remove(0);
        Assert.assertEquals(0, list.size());
        Assert.assertEquals("Hello", case1);
        Assert.assertEquals("Bye", case2);
        Assert.assertTrue(list.isEmpty());
        System.out.println("remove , removeFirst , removeLast , isEmpty : passed");
        for (int i = 0; i < 3; i++) {
            list.add("*" + i);
        }
        list.set(1, "Hello");

        Assert.assertEquals("*0", list.getFirst());
        Assert.assertEquals("*2", list.getLast());
        String case3 = list.pollFirst();
        String case4 = list.pollLast();
        Assert.assertEquals("*0", case3);
        Assert.assertEquals("*2", case4);
        Assert.assertEquals("Hello", list.get(0));
        Assert.assertEquals(new Object[]{"Hello"}, list.toArray());
        Assert.assertEquals("Hello", list.remove(0));
        list.add("");
        list.clear();
        Assert.assertTrue(list.isEmpty());
        System.out.println("pollLast , pollFirst , get , getLast , getFirst ,toArray , clear : passed");
        System.out.println("----------------Test of MyLinkedList passed-----------------");
    }

    @Test
    public void getCheckForMyQueue() {
        System.out.println("\n\n----------------Test of MyQueue-----------------");

        MyQueue<Integer> queue = new MyQueue<>();
        for (int i = 0; i < 100; i++) {
            queue.offer(i);
        }
        int case1 = queue.remove();
        Assert.assertEquals(0, case1);
        queue.clear();
        Assert.assertTrue(queue.isEmpty());
        try {
            queue.remove();
        } catch (NoSuchElementException e) {
            System.out.println("NoSuchElementException from method remove of empty MyQueue : passed");
        }
        try {
            queue.element();
        } catch (NoSuchElementException e) {
            System.out.println("NoSuchElementException from method element of empty MyQueue : passed");
        }
        Assert.assertNull(queue.peek());
        Assert.assertNull(queue.poll());
        queue = new MyQueue<>(10);
        for (int i = 0; i < 10; i++) {
            queue.offer(i);
        }
        Assert.assertFalse(queue.offer(10));
        System.out.println("poll , peek , offer : passed");
        System.out.println("----------------Test of MyQueue passed-----------------");
    }
}
