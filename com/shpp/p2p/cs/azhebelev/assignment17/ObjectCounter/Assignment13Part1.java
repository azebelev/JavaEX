package com.shpp.p2p.cs.azhebelev.assignment17.ObjectCounter;

import com.shpp.p2p.cs.azhebelev.assignment17.MyArrayList;
import com.shpp.p2p.cs.azhebelev.assignment17.MyHashMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class used for calculation number of object witch depicted on image.
 * Could separate slightly stuck together objects.
 * Used algorithm is breadth-first search.
 * On default start work with test image
 */
public class Assignment13Part1 {
    /**
     * Initiate the program
     *
     * @param args:path to file
     */
    public static void main(String[] args) {
        Assignment13Part1 launch = new Assignment13Part1();
        launch.run(args);
    }

    /**
     * Variable(percent value of height) defines size of lines for background search
     * from edges to center
     */
    protected double searchDepth = 5;

    /**
     * Percent value of image area witch should not be identified as object
     */
    protected double garbageValue = 0.05;

    /**
     * Percent from biggest object which should not be ignored
     */
    protected double ignoredObject = 15;

    /**
     * limit of distinction for each color component to compare with background
     */
    protected double tolerance = 60;

    /**
     * size of image
     */
    private int width, height;

    /**
     * counter of object pixels
     */
    private int pixelCounter = 0;
    /**
     * percent from approximate height Of object used for cleared edges of object
     */
    protected double clearing = 7;
    /**
     * list used for save coordinates for breadth-first search algorithm
     */
    private MyArrayList<Point> path = new MyArrayList<>();

    /**
     * Main method connects common methods of class
     *
     * @param args:path of image
     */
    private void run(String[] args) {

        BufferedImage img = readImage(args);
        Color backGround = findBackground(img);
        //matrix of information about pixels(true - background;false - object)
        boolean[][] matrix = createMatrixForBFS(backGround, img);
        showParsedPicture(matrix);
        clearEdges(matrix);
        showParsedPicture(matrix);
        int numberOfObjects = calculateNumbersOfObjects(matrix);
        System.out.println(" NUMBER OF OBJECTS IS " + numberOfObjects);
    }

    /**
     * Method categorise colors within the two lines of specified sizes and find average
     *
     * @param img:image for background search iteration
     * @return average color
     */
    private Color findBackground(BufferedImage img) {
        MyHashMap<Color, Integer> sameColorList = new MyHashMap<>();
        searchDepth = searchDepth / 100;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height * searchDepth; y++) {
                fillSameColorList(x, y, sameColorList, img);
            }
            for (int y = (int) (height - height * searchDepth); y < height; y++) {
                fillSameColorList(x, y, sameColorList, img);
            }
        }
        return findAverageColor(sameColorList);
    }

    /**
     * Method defines average color
     *
     * @param sameColorList contains colors and number of such colors
     * @return average color
     */
    private Color findAverageColor(MyHashMap<Color, Integer> sameColorList) {
        int numberOfColors = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        int alpha = 0;
        for (Color color : sameColorList.keySet()) {
            numberOfColors = numberOfColors + sameColorList.get(color);
            red = red + color.getRed() * sameColorList.get(color);
            green = green + color.getGreen() * sameColorList.get(color);
            blue = blue + color.getBlue() * sameColorList.get(color);
            alpha = alpha + color.getAlpha() * sameColorList.get(color);
        }
        red = (int) (red / numberOfColors);
        green = (int) (green / numberOfColors);
        blue = (int) (blue / numberOfColors);
        alpha = (int) (alpha / numberOfColors);
        return new Color(red, green, blue, alpha);
    }

    /**
     * Method fill Map by colors and increase counter of each Color then meet it
     *
     * @param x:                    X coordinate of pixel
     * @param y:                    Y coordinate of pixel
     * @param sameColorList:Map(key object Color,value number of pixels with such Color)
     * @param img:image
     */
    private void fillSameColorList(int x, int y, MyHashMap<Color, Integer> sameColorList, BufferedImage img) {
        Color color = new Color(img.getRGB(x, y), true);

        if (sameColorList.containsKey(color)) {
            sameColorList.put(color, sameColorList.get(color) + 1);
        } else sameColorList.put(color, 1);
    }

    /**
     * Method fill array "matrix" according to background identification
     *
     * @param backGround:Color of background
     * @param img:image
     * @return array contains information about each pixel(true = background pixel, false = object pixel)
     */
    private boolean[][] createMatrixForBFS(Color backGround, BufferedImage img) {
        boolean[][] matrix = new boolean[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                matrix[y][x] = isBackground(x, y, backGround, img);
            }
        }
        return matrix;
    }

    /**
     * Method defines whether it is background pixel or not
     *
     * @param x:               X coordinate of pixel
     * @param y:               Y  coordinate of pixil
     * @param backGround:color of background
     * @param img:image
     * @return true: Color within measures specified by backGround and tolerance(means background pixel)
     * false: out of limit( means object pixel)
     */
    private boolean isBackground(int x, int y, Color backGround, BufferedImage img) {
        Color currentColor = new Color(img.getRGB(x, y), true);
        int redC = currentColor.getRed();
        int greenC = currentColor.getGreen();
        int blueC = currentColor.getBlue();
        int alphaC = currentColor.getAlpha();
        int redB = backGround.getRed();
        int greenB = backGround.getGreen();
        int blueB = backGround.getBlue();
        int alphaB = backGround.getAlpha();

        return ((redC <= redB + tolerance) && (redC >= redB - tolerance)) &&

                ((greenC <= greenB + tolerance) && (greenC >= greenB - tolerance)) &&

                ((blueC <= blueB + tolerance) && (blueC >= blueB - tolerance)) &&

                ((alphaC <= alphaB + tolerance) && (alphaC >= alphaB - tolerance));
    }

    /**
     * Method finds edges of objects and clean according to them horizontal line of defined size
     *
     * @param matrix array of information about each pixel(true = background pixel, false = object pixel)
     */
    private void clearEdges(boolean[][] matrix) {
        boolean[][] cloneOfMatrix = cloneOff(matrix);
        MyArrayList<Point> edgePoints = new MyArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!cloneOfMatrix[y][x]) {

                    path.add(new Point(x, y));
                    findEdges(edgePoints, cloneOfMatrix);
                    while (!path.isEmpty()) {
                        bfs(cloneOfMatrix);
                    }
                    int valueForClear = (int) (Math.sqrt(pixelCounter) * clearing / 100);
                    for (Point edge : edgePoints) {
                        for (int i = 0; i < valueForClear; i++) {
                            if (edge.y - i - 1 >= 0 && edge.y + i - 1 < matrix.length) {
                                matrix[edge.y + i - 1][edge.x - 1] = true;
                                matrix[edge.y - i - 1][edge.x - 1] = true;
                            }
                        }
                    }
                    pixelCounter = 0;
                    edgePoints.clear();
                }
            }
        }
    }

    /**
     * Method defines edges of objects by using "bug" method . If it is object pixel iteration
     * turn to left else to right
     *
     * @param edgePoints    list of points of object edges
     * @param cloneOfMatrix the same as matrix but bigger by one pixel on all sides to avoid outOfBound
     */
    private void findEdges(MyArrayList<Point> edgePoints, boolean[][] cloneOfMatrix) {
        Point prev = new Point(path.get(0).x, path.get(0).y);
        Point next = new Point(prev.x + 1, prev.y);
        while (!next.equals(path.get(0))) {
            if (!cloneOfMatrix[next.y][next.x]) {
                edgePoints.add(new Point(next.x, next.y));
                // from right
                if (prev.x - next.x < 0) {
                    prev = next;
                    next = new Point(next.x, next.y - 1);
                    continue;
                }
                //from left
                if (prev.x - next.x > 0) {
                    prev = next;
                    next = new Point(next.x, next.y + 1);
                    continue;
                }
                // from down
                if (prev.y - next.y < 0) {
                    prev = next;
                    next = new Point(next.x + 1, next.y);
                    continue;
                }
                //from up
                if (prev.y - next.y > 0) {
                    prev = next;
                    next = new Point(next.x - 1, next.y);
                }
            } else {
                //from right
                if (prev.x - next.x < 0) {
                    prev = next;
                    next = new Point(next.x, next.y + 1);
                    continue;
                }
                //from left
                if (prev.x - next.x > 0) {
                    prev = next;
                    next = new Point(next.x, next.y - 1);
                    continue;
                }
                //from down
                if (prev.y - next.y < 0) {
                    prev = next;
                    next = new Point(next.x - 1, next.y);
                    continue;
                }
                //from up
                if (prev.y - next.y > 0) {
                    prev = next;
                    next = new Point(next.x + 1, next.y);
                }
            }
        }
    }

    /**
     * Make the same as matrix but bigger by one pixel from all sides to avoid outOfBound in future
     *
     * @param matrix array of information about each pixel(true = background pixel, false = object pixel)
     * @return same as matrix but bigger by one pixel from all sides to avoid outOfBound in future
     */
    private boolean[][] cloneOff(boolean[][] matrix) {
        boolean[][] cloneMatrix = new boolean[matrix.length + 2][matrix[0].length + 2];
        for (int i = 0; i < cloneMatrix.length; i++) {
            for (int j = 0; j < cloneMatrix[0].length; j++) {
                cloneMatrix[i][j] = true;
            }
        }
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                cloneMatrix[y + 1][x + 1] = matrix[y][x];
            }
        }
        return cloneMatrix;
    }

    /**
     * Method iterates matrix item and count number of objects
     * initiate method bfs(matrix) then encounter not background pixel(matrix item = false)
     *
     * @param matrix :array contains information about each pixel(true = background pixel,
     *               false = object pixel)
     * @return number of object
     */
    private int calculateNumbersOfObjects(boolean[][] matrix) {
        //array of number of pixel for each object
        MyArrayList<Integer> objectList = new MyArrayList<>();
        double maxObjectSize = 0;
        garbageValue = width * height * garbageValue / 100;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if (!matrix[y][x]) {
                    path.add(new Point(x, y));
                    while (!path.isEmpty()) {
                        bfs(matrix);
                    }
                    if (pixelCounter > garbageValue) {
                        objectList.add(pixelCounter);
                        pixelCounter = 0;
                    }
                }
            }
        }
        for (Integer pixelValue :
                objectList) {
            if (maxObjectSize < pixelValue)
                maxObjectSize = pixelValue;
        }

        int numberOfObject = 0;
        for (Integer pixelValue : objectList) {
            if (pixelValue >= maxObjectSize * ignoredObject / 100) numberOfObject++;
        }

        return numberOfObject;
    }

    /**
     * method check adjacent pixels ,
     * file it to ArrayList "path" and mark it in "matrix" as background.
     * return then no pixels in list "path"
     *
     * @param matrix:array contains information about each pixel(true = background pixel,
     *                            false = object pixel)
     */
    private void bfs(boolean[][] matrix) {

        int x = path.get(0).x;
        int y = path.get(0).y;
        pixelCounter++;
        path.remove(0);
        int[] xValue = {x + 1, x, x - 1, x};
        int[] yValue = {y, y + 1, y, y - 1};

        for (int i = 0; i < 4; i++) {
            if (xValue[i] >= 0 && xValue[i] < width && yValue[i] >= 0 && yValue[i] < height &&
                    !matrix[yValue[i]][xValue[i]]) {

                path.add(new Point(xValue[i], yValue[i]));
                matrix[yValue[i]][xValue[i]] = true;
            }
        }
    }

    /**
     * Method draws image to show how program observe the objects
     *
     * @param matrix:array contains information about each pixel(true = background pixel,
     *                     false = object pixel)
     */
    private void showParsedPicture(boolean[][] matrix) {
        BufferedImage programVision = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if (matrix[y][x]) {
                    programVision.setRGB(x, y, new Color(0, 0, 0).getRGB());
                } else {
                    programVision.setRGB(x, y, new Color(45, 2, 254).getRGB());
                }
            }
        }

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(width + 10, height + 30);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(programVision, 0, 0, null);
            }
        };

        frame.add(panel);
        frame.setVisible(true);
    }

    /**
     * Method reads image
     *
     * @param args: path to image
     * @return image
     */
    private BufferedImage readImage(String[] args) {
        BufferedImage img = null;

        try {
            String filePath = "src/com/shpp/p2p/cs/azhebelev/assignment13/assets/test.png";
            if (args.length != 0) filePath = args[0];
            img = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.out.println("INVALID PATH INPUT");
        }
        width = img.getWidth();
        height = img.getHeight();
        return img;
    }
}
