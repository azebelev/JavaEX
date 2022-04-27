package com.shpp.p2p.cs.azhebelev.assignment17.Archivator;

import com.shpp.p2p.cs.azhebelev.assignment17.MyArrayList;
import com.shpp.p2p.cs.azhebelev.assignment17.MyHashMap;

/**
 * class used for providing different operations this huffman tree
 */
public class Node implements Comparable<Node> {
    Byte b;
    int weight;
    Node left = null;
    Node right = null;

    @Override
    public int compareTo(Node o) {
        if (weight < o.weight) return -1;
        if (weight > o.weight) return 1;
        return 0;
    }

    public Node(Byte b, int weight) {
        this.weight = weight;
        this.b = b;
    }

    public Node() {

    }

    public Node(Byte b) {
        this.b = b;
    }

    public Node(Byte b, int weight, Node left, Node right) {
        this.b = b;
        this.weight = weight;
        this.left = left;
        this.right = right;
    }

    /**
     * Method find huffman codes and fill huffmanMap
     *
     * @param originalB  entered byte
     * @param code       code for byte
     * @param huffmanMap Map of codes and bytes used for archive and unarchive
     * @param isForZip   boolean for identification how to fill the Map <byte,code> or <code,byte>
     */
    public void findCode(Byte originalB, String code, MyHashMap huffmanMap, boolean isForZip) {
        if (b == originalB) {
            if (isForZip) {
                huffmanMap.put(originalB, new StringBuilder(code));
            } else {
                huffmanMap.put(code, originalB);
            }
        }
        if (left != null) {
            left.findCode(originalB, code + "0", huffmanMap, isForZip);
        }
        if (right != null) {
            right.findCode(originalB, code + "1", huffmanMap, isForZip);
        }
    }

    /**
     * Method used for preparing shape of tree and leaves for writing to file
     *
     * @param shape  special string contains information about structure of tree
     * @param leaves List of bytes encountered in oder
     */
    public void encodeTree(StringBuilder shape, MyArrayList<Byte> leaves) {
        if (b != null) {
            shape.append("0");
            leaves.add(b);
        }
        if (b == null) {
            shape.append("1");
            if (left != null) {
                left.encodeTree(shape, leaves);
            }
            if (right != null) {
                right.encodeTree(shape, leaves);
            }
        }
    }

    /**
     * method used for recovery of tree
     *
     * @param shape  special string contains information about structure of tree
     * @param leaves List of bytes
     */
    public void unzipTree(StringBuilder shape, MyArrayList<Byte> leaves) {
        if (!shape.isEmpty() && shape.charAt(0) == '1') {
            if (this.left == null) {
                shape.deleteCharAt(0);
                this.left = new Node();
                this.left.unzipTree(shape, leaves);
            }
            if (!shape.isEmpty() && shape.charAt(0) == '1') {
                if (this.right == null) {
                    shape.deleteCharAt(0);
                    this.right = new Node();
                    this.right.unzipTree(shape, leaves);
                }
            }
        }
        if (!shape.isEmpty() && shape.charAt(0) == '0') {
            if (this.left == null) {
                shape.deleteCharAt(0);
                this.left = new Node(leaves.get(0));
                leaves.remove(0);
                this.unzipTree(shape, leaves);
            }
            if (this.right == null) {
                shape.deleteCharAt(0);
                this.right = new Node(leaves.get(0));
                leaves.remove(0);
            }
        }
    }
}
