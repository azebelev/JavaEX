package com.shpp.p2p.cs.azhebelev.assignment17.Archivator;

import com.shpp.p2p.cs.azhebelev.assignment17.MyArrayList;
import com.shpp.p2p.cs.azhebelev.assignment17.MyHashMap;
import com.shpp.p2p.cs.azhebelev.assignment17.MyPriorityQueue;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;


/**
 * Class used for archive and unarchive of files .By default start operation with "test.txt"
 */
public class Assignment15Part1 {

    public static void main(String[] args) {
        Assignment15Part1 a = new Assignment15Part1();
        a.run(args);
    }

    /**
     * used for count of spent time
     */
    long t = System.currentTimeMillis();
    /**
     * Object used for creation of code for each byte according to huffman algorithm
     */Node tree = new Node();
    /**
     * size of encoded tree
     */
    short shapeSize;
    /**
     * Map used to archive and unarchive bytes , contains huffman codes
     */
    MyHashMap huffmanMap = new MyHashMap();
    /**
     * Buffer for BufferedInputReader
     */
     int BUFFER = 32000;
    /**
     * Map  <all bytes encountered in file , frequency of encountering>
     */
    MyHashMap<Byte, Integer> mapFrequencies = new MyHashMap<>();
    /**
     * Masks for bit
     */
    byte[] masks = {-128, 64, 32, 16, 8, 4, 2, 1};
    /**
     * variable used during zip process
     */
    byte currentByte = 0;
    /**
     * index of next bit for filling
     */
    int indexOfCurrentBit = 0;
    /**
     * Object used during unzipping , collects bits and after founding accordance in huffmanMap
     * become - "";
     */
    StringBuilder bits = new StringBuilder();

    /**
     * run the class
     *
     * @param args Could contain special flags(archive or unarchive) , inbound file ,outbound file
     */
    private void run(String[] args) {
        String fileIn;
        String fileOut;
        if (args.length == 0) {

            fileIn = "src/com/shpp/p2p/cs/azhebelev/assignment14/assets/test.txt";
            fileOut = "src/com/shpp/p2p/cs/azhebelev/assignment14/assets/test.par";
            System.exit(archive(fileIn, fileOut));
        }
        if (args[0].equals("-u")) {
            fileIn = args[1];
            fileOut = args[2];
            System.exit(unArchive(fileIn, fileOut));
        }
        if (args[0].equals("-a")) {
            fileIn = args[1];
            fileOut = args[2];
            System.exit(archive(fileIn, fileOut));
        }
        if (args.length == 1) {
            fileIn = args[0];
            if (args[0].substring(args[0].lastIndexOf(".") + 1).equals("par")) {
                if (fileIn.substring(0, fileIn.lastIndexOf(".")).lastIndexOf(".") != -1) {
                    fileOut = fileIn.substring(0, fileIn.lastIndexOf("."));
                } else {
                    fileOut = fileIn.substring(0, fileIn.lastIndexOf(".")) + ".uar";
                }
                System.exit(unArchive(fileIn, fileOut));
            } else {
                fileOut = fileIn.substring(0, fileIn.lastIndexOf(".")) + ".par";
                System.exit(archive(fileIn, fileOut));
            }
        }
        if (args.length == 2) {
            fileIn = args[0];
            fileOut = args[1];
            if (fileIn.substring(fileIn.lastIndexOf(".") + 1).equals("par")) {
                System.exit(unArchive(fileIn, fileOut));
            } else System.exit(archive(fileIn, fileOut));
        }
        System.out.println("INCORRECT INPUT : MORE THEN 2 ARGUMENTS");
        System.exit(-1);
    }

    /**
     * Method download file and archive it
     *
     * @param fileIn  inbound file name
     * @param fileOut outbound file name
     * @return -1 in case of exception and 0 if success
     */
    private int archive(String fileIn, String fileOut) {
        long sumOfWrittenBites = 0;
        long sizeOfFile;
        try {
            sizeOfFile = createUniqueByteList(fileIn);

            if (mapFrequencies.size() < 2) {
                System.out.println("TO SMALL SIZE OF FILE FOR ARCHIVING BY HUFFMAN ALGORITHM");
                System.exit(-1);
            } else fillMap();

            FileInputStream inputStream = new FileInputStream(fileIn);
            FileOutputStream outStream = new FileOutputStream(fileOut);
            DataOutputStream dos = new DataOutputStream(outStream);
            byte[] encryptedTree = encryptTree();
            dos.writeShort(shapeSize);
            outStream.write(encryptedTree);
            BufferedInputStream bis = new BufferedInputStream(inputStream,BUFFER);

            byte[] buffer = new byte[BUFFER];

            while (bis.available() > 0) {
                if (bis.available() < buffer.length) {
                    buffer = new byte[bis.available()];
                }

                bis.read(buffer);
                byte[] encryptedBytes = encryptBytes(buffer);
                outStream.write(encryptedBytes);
                sumOfWrittenBites += encryptedBytes.length;
            }
            if (indexOfCurrentBit != 0) {
                byte[] lastByteInf = {currentByte,(byte)(indexOfCurrentBit)};
                outStream.write(lastByteInf);
                sumOfWrittenBites += lastByteInf.length;
            }
            sumOfWrittenBites += 2 + shapeSize / Byte.SIZE + huffmanMap.size();

            dos.close();
            bis.close();

            System.out.println("time spent  " + (System.currentTimeMillis() - t) / 1000.0 + " s");
            System.out.println("size before = " + sizeOfFile);
            System.out.println("size after = " + sumOfWrittenBites);
            System.out.println("Effectiveness = " +
                    ((double) (sizeOfFile - sumOfWrittenBites) / sizeOfFile * 100)
                    + " %");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    /**
     * Method creates Unique Byte list
     * @param fileIn
     * @return
     * @throws IOException
     */
    private long createUniqueByteList(String fileIn) throws IOException {
        long sizeOfFile = 0;
        BufferedInputStream bisFindUniqueBytes = new BufferedInputStream(new FileInputStream(fileIn),BUFFER);
        byte[] buffer = new byte[BUFFER];
        while (bisFindUniqueBytes.available() > 0) {
            if (bisFindUniqueBytes.available() < buffer.length) buffer = new byte[bisFindUniqueBytes.available()];

            sizeOfFile += buffer.length;
            bisFindUniqueBytes.read(buffer);
            findUniqueBytes(buffer);
        }
        bisFindUniqueBytes.close();
        return sizeOfFile;
    }

    /**
     * Method creates Map<all bytes encountered , frequency of them > ,build Huffman tree according to this
     * map and by using of tree fill huffmanMap<Byte,huffman code for this byte.
     */
    private void fillMap() {

        MyPriorityQueue<Node> nodes = new MyPriorityQueue<>();
        for (Byte b :
                mapFrequencies.keySet()) {
            nodes.add(new Node(b, mapFrequencies.get(b)));
        }

        while (nodes.size() > 1) {
            Node left = nodes.poll();
            Node right = nodes.poll();
            Node parentNode = new Node(null, left.weight + right.weight, left, right);
            nodes.add(parentNode);
        }
        tree = nodes.poll();
        for (Byte b :
                mapFrequencies.keySet()) {
            tree.findCode(b, "", huffmanMap, true);
        }
    }

    private void findUniqueBytes(byte[] downLoadedBytes) {
        for (byte b :
                downLoadedBytes) {
            Integer count = mapFrequencies.get(b);
            mapFrequencies.put(b, count == null ? 1 : ++count);
        }
    }

    /**
     * Method encrypt one array of byte to another
     *
     * @param downLoadedBytes original bytes of inbound file
     * @return encrypted bytes
     */
    private byte[] encryptBytes(byte[] downLoadedBytes) {

        MyArrayList<Byte> encodedByte = new MyArrayList<>();
        for (byte b :
                downLoadedBytes) {
            StringBuilder code = (StringBuilder) huffmanMap.get(b);
           int indexInsideCode = 0;
            while (indexInsideCode < code.length()) {

               if (code.charAt(indexInsideCode) == '1')
                   currentByte |= masks[indexOfCurrentBit];
               indexOfCurrentBit++;
               indexInsideCode++;

               if (indexOfCurrentBit == 8) {
                   encodedByte.add(currentByte);
                   indexOfCurrentBit = 0;
                   currentByte = 0;
               }
            }
        }
        byte[] bytes = new byte[encodedByte.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = encodedByte.get(i);
        }
        return bytes;
    }

    /**
     * Method encrypt tree shape and it's leaves to array
     *
     * @return array of encrypted tree shape and leaves
     */
    private byte[] encryptTree() {
        MyArrayList<Byte> leaves = new MyArrayList<>();
        StringBuilder shape = new StringBuilder();
        tree.encodeTree(shape, leaves);
        shapeSize = (short) shape.length();
        // 8 it is size of Byte in bits
        byte[] encodedTree = new byte[(int) (Math.ceil(shapeSize / 8.0) + leaves.size())];
        int i = 0;
        while (i < shapeSize / 8) {
            encodedTree[i] = (byte) (Integer.parseInt(shape.substring(i * 8, i * 8 + 8), 2));
            i++;
        }
        if (shapeSize % 8 != 0) {
            StringBuilder balance = new StringBuilder(shape.substring(i * 8));
            while (balance.length() < 8) {
                balance.append("0");
            }
            encodedTree[i] = (byte) (Integer.parseInt(balance.toString(), 2));
            i++;
        }
        for (Byte b : leaves) {
            encodedTree[i] = b;
            i++;
        }
        return encodedTree;
    }

    /**
     * Method download file and unarchive it
     *
     * @param fileIn  inbound file name
     * @param fileOut outbound file name
     * @return -1 in case of exception and 0 if success
     */
    private int unArchive(String fileIn, String fileOut) {
        try {
            FileInputStream inStream = new FileInputStream(fileIn);
            FileOutputStream outStream = new FileOutputStream(fileOut);
            BufferedInputStream bis = new BufferedInputStream(inStream,BUFFER);
            boolean isTreeCreated = false;
            byte[] bytes = new byte[BUFFER];
            boolean isLastReading = false;
            long sizeOfZip = 0;
            long sizeOfExtracted = 0;
            byte[] extractedBytes;
            while (bis.available() > 0) {
                if(bis.available() < bytes.length) {
                    bytes = new byte[bis.available()];
                    isLastReading = true;
                }
                sizeOfZip += bytes.length;
                bis.read(bytes);

                if (!isTreeCreated) {
                    extractedBytes = extractBytes(cutOutTree(bytes), isLastReading);
                    sizeOfExtracted += extractedBytes.length;
                    outStream.write(extractedBytes);

                    isTreeCreated = true;
                } else {
                    extractedBytes = extractBytes(bytes, isLastReading);
                    sizeOfExtracted += extractedBytes.length;
                    outStream.write(extractedBytes);
                }
            }
            bis.close();
            outStream.close();

            System.out.println("time spent  " + (System.currentTimeMillis() - t) / 1000.0 + " s");
            System.out.println("size before = " + sizeOfZip);
            System.out.println("size after = " + sizeOfExtracted);
            System.out.println("Effectiveness = " +
                    (((double) (sizeOfExtracted - sizeOfZip) / sizeOfZip) * 100) + " %");

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    /**
     * Method cut out all information concerned tree , recovers tree and fill huffman Map
     *
     * @param downLoadedBytes byte of downloaded file
     * @return all bytes except bytes containing information of tree
     */
    private byte[] cutOutTree(byte[] downLoadedBytes) {

        shapeSize = ByteBuffer.wrap(downLoadedBytes, 0, 2).getShort();
        System.out.println(shapeSize);

        byte[] bytesOfShape = Arrays.copyOfRange(downLoadedBytes, 2,
                2 + (int) Math.ceil(shapeSize / 8.0));
        StringBuilder shape = new StringBuilder();
        int numbersOfLeaves = 0;
        for (byte b : bytesOfShape) {

            for (byte mask : masks) {
                if ((b & mask) == mask) {
                    shape.append("1");
                } else {
                    shape.append("0");
                }
            }
        }
        shape.delete(shapeSize, shape.length());

        for (int i = 0; i < shape.length(); i++) {
            if (shape.charAt(i) == '0') numbersOfLeaves++;
        }
        // 2 it is size of shot number
        byte[] leavesTemp = Arrays.copyOfRange(downLoadedBytes,
                2 + bytesOfShape.length, 2 + bytesOfShape.length + numbersOfLeaves);
        MyArrayList<Byte> leaves = new MyArrayList<>();
        for (byte b : leavesTemp) {
            leaves.add(b);
        }
        tree.unzipTree(shape.deleteCharAt(0), leaves);

        for (byte b : leavesTemp) {
            tree.findCode(b, "", huffmanMap, false);
        }
        return Arrays.copyOfRange(downLoadedBytes, 2 + bytesOfShape.length + numbersOfLeaves,
                downLoadedBytes.length);
    }

    /**
     * Method convert one array of byte to another by using huffman Map
     *
     * @return unarchived byte array
     */
    private byte[] extractBytes(byte[] bytesBuffer, boolean isLastReading) {
        MyArrayList<Byte> unzippedList = new MyArrayList<>();
        //unarchive all bytes except last two , because last contains inf about useful bits
        // in before last byte
        int lastBitCheck = 0;
        if(isLastReading) lastBitCheck = 2;
        for (int i = 0; i < bytesBuffer.length - lastBitCheck; i++) {

            for (byte mask : masks) {
                if ((bytesBuffer[i] & mask) == mask) {
                    bits.append("1");
                } else {
                    bits.append("0");
                }
                if (huffmanMap.containsKey(bits.toString())) {
                    unzippedList.add((Byte) huffmanMap.get(bits.toString()));
                    bits = new StringBuilder("");
                }
            }
        }

        fillLastByte(bytesBuffer, isLastReading, unzippedList);

        byte[] unzippedBytes = new byte[unzippedList.size()];
        for (int i = 0; i < unzippedBytes.length; i++) {
            unzippedBytes[i] = unzippedList.get(i);
        }
        return unzippedBytes;
    }

    /**
     *
     * @param bytesBuffer Array of current buffered bytes of file
     * @param isLastReading boolean used if required to check last two bytes
     *                      if it is last reading the last byte will contain information how many useful
     *                      bits in pre last byte
     * @param unzippedList ArrayList of unzipped bytes
     */
    private void fillLastByte(byte[] bytesBuffer, boolean isLastReading,MyArrayList<Byte> unzippedList) {
        if (isLastReading) {

            for (int i = 0; i < bytesBuffer[bytesBuffer.length - 1]; i++) {
                if ((bytesBuffer[bytesBuffer.length - 2] & masks[i]) == masks[i]) {
                    bits.append("1");
                } else {
                    bits.append("0");
                }
                if (huffmanMap.containsKey(bits.toString())) {
                    unzippedList.add((Byte) huffmanMap.get(bits.toString()));
                    bits = new StringBuilder();
                }
            }
        }
    }
}
