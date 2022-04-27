package com.shpp.p2p.cs.azhebelev.assignment17.Calculator;

import com.shpp.p2p.cs.azhebelev.assignment17.MyArrayList;
import com.shpp.p2p.cs.azhebelev.assignment17.MyHashMap;


import java.util.InputMismatchException;
import java.util.Scanner;
/*
Program imitate calculator which receives arguments from console .Uses OPN method for calculation.
First time user should enter formula and variables or only formula and after that could
change the variables. Class uses opposite polish notation algorithm(OPN)
 */
public class Assignment11Part1 {
    public static void main(String[] args) {
        Assignment11Part1 launch = new Assignment11Part1();
        launch.run(args);
    }

    // List of variables
    MyHashMap<String, Double> varList = new MyHashMap<>();
    //Formula for calculation received from user as first argument
    MyArrayList<String> inputtedFormula = new MyArrayList<>();
    //Object of class Calculator
    Calculator calculator = new Calculator();

    /**
     * method launches all function of Class Assignment11Part1
     *
     * @param args arguments received from user
     */
    private void run(String[] args) {
        try {
            identificationVar(args);
            formulaToArray(args);
            formulaToOpn();
            calculateAndRequest();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("INCORRECT FORMULA INPUT");
            System.exit(-1);
        }
    }

    /**
     * Method used for identification of Variables according to position 0f "="
     *
     * @param args :arguments inputted by user
     */
    private void identificationVar(String[] args) {
        if (args.length > 1) {
            try {
                for (int i = 1; i < args.length; i++) {
                    String lineOfVar = args[i];
                    int posOfEquals = lineOfVar.indexOf('=');
                    String nameOfVar = lineOfVar.substring(0, posOfEquals).replace(" ", "");
                    double value = Double.parseDouble(lineOfVar.substring(posOfEquals + 1));
                    varList.put(nameOfVar, value);
                }
            } catch (Exception e) {
                System.out.println("INCORRECT VARIABLE INSTALLATION ");
                System.exit(-1);
            }
        }
    }

    /**
     * Method transform String to convenient ArrayList format
     *
     * @param args :arguments inputted by user
     */
    private void formulaToArray(String[] args) {
        String formulaLine = args[0].replaceAll(" ", "");
        int indexStart = 0;
        for (int i = 0; i < formulaLine.length(); i++) {
            String stringBetweenOperand = formulaLine.substring(indexStart, i);
            switch (formulaLine.charAt(i)) {
                case '+', '^', '/', '*', '(', ')': {
                    if (!stringBetweenOperand.isEmpty()) {
                        inputtedFormula.add(stringBetweenOperand);
                    }
                    inputtedFormula.add("" + formulaLine.charAt(i));
                    indexStart = i + 1;
                }
                break;
                case '-': {
                    if (stringBetweenOperand.isEmpty()) {
                        inputtedFormula.add("unar");
                        indexStart = i + 1;
                    } else {
                        inputtedFormula.add(stringBetweenOperand);
                        indexStart = i + 1;
                        inputtedFormula.add("" + formulaLine.charAt(i));
                    }
                }
            }
        }
        if ((!formulaLine.substring(indexStart).isEmpty())) inputtedFormula.add(formulaLine.substring((indexStart)));
        System.out.println(inputtedFormula);
    }

    /**
     * Method execute main circle of program : calculate and interact to user
     */
    private void calculateAndRequest() {
        do {
            System.out.println("RESULT = " + calculate());
        } while (!askToFinish());
    }

    /**
     * Method implements interaction with user
     *
     * @return boolean if user want to continue or not
     */
    private boolean askToFinish() {
        try {
            if (varList.isEmpty()) return true;
            Scanner in = new Scanner(System.in);
            System.out.print("enter new value of variables or exit \n continue press \"c\" exit press anything else ");
            String answer = in.nextLine();
            if (answer.equals("c")) {
                for (String varName : varList.keySet()) {
                    System.out.println("Enter new value of  " + varName);
                    varList.put(varName, in.nextDouble());
                }
            } else System.exit(-1);
        } catch (InputMismatchException e) {
            System.out.println("INCORRECT VARIABLE INSTALLATION TRY AGAIN");
            askToFinish();
        }
        return false;
    }

    /**
     * Method transform inputted formula to format appropriate to OPN
     */
    private void formulaToOpn() {
        //operandStack collect operands and functions and release them in required oder for OPN algorithm
        MyArrayList<String> operandStack = new MyArrayList<>();
        //exitStack receive numbers and operand with functions in right oder
        MyArrayList<String> exitStack = new MyArrayList<>();

        for (String item : inputtedFormula) {
            if (isNumber(item) || varList.containsKey(item)) {
                exitStack.add(item);
                continue;
            }
            if (item.equals("^") || item.equals("unar") || item.equals("(")
                    || (Calculator.functionsList.containsKey(item) && item.length() > 1) ) {
                operandStack.add(item);
                continue;
            }
            if (item.equals("+") || item.equals("-")) {
                //Push all from operandStack to exitStack until "("
                while (!operandStack.isEmpty() && !operandStack.get(operandStack.size() - 1).equals("(")) {
                    exitStack.add(operandStack.get(operandStack.size() - 1));
                    operandStack.remove(operandStack.size() - 1);
                }
                operandStack.add(item);
                continue;
            }
            if (item.equals(")")) {
                //Push all from operandStack to exitStack until "(" and delete "()"
                while (!operandStack.isEmpty() && !operandStack.get(operandStack.size() - 1).equals("(")) {
                    exitStack.add(operandStack.get(operandStack.size() - 1));
                    operandStack.remove(operandStack.size() - 1);
                }
                operandStack.remove(operandStack.size() - 1);
                //Check for any function before "(" and push function to exitStack
                if (!operandStack.isEmpty() &&
                        Calculator.functionsList.containsKey(operandStack.get(operandStack.size() - 1)) &&
                        operandStack.get(operandStack.size() - 1).length() > 1) {
                    exitStack.add(operandStack.get(operandStack.size() - 1));
                    operandStack.remove(operandStack.size() - 1);
                }
                continue;
            }
            if (item.equals("*") || item.equals("/")) {
                //push "^", "/", "*" to exitStack
                while (!operandStack.isEmpty() && operandStack.get(operandStack.size() - 1).equals("^") ||
                        !operandStack.isEmpty() && operandStack.get(operandStack.size() - 1).equals("/") ||
                        !operandStack.isEmpty() && operandStack.get(operandStack.size() - 1).equals("*")) {
                    exitStack.add(operandStack.get(operandStack.size() - 1));
                    operandStack.remove(operandStack.size() - 1);
                }
                operandStack.add(item);
            }
        }
        //Push all remaining item from operandStack to exitStack
        for (int i = operandStack.size() - 1; i >= 0; i--) {
            exitStack.add(operandStack.get(i));
        }
        inputtedFormula = exitStack;
    }

    /**
     * Method execute calculation according to required operators and functions
     * using HashMap from class Calculator.
     */
    private String calculate() {
        MyArrayList<String> formula;
        formula = addVars((MyArrayList<String>) inputtedFormula.clone());
        MyArrayList<String> result = new MyArrayList<>();
        try {
            for (String item : formula) {
                if (isNumber(item)) {
                    result.add(item);
                } else {
                    if (item.length() > 1) {
                        result.set(result.size() - 1,
                                Calculator.functionsList.get(item).count("", result.get(result.size() - 1)));
                    } else {
                        result.set(result.size() - 1,Calculator.functionsList.get(item)
                                .count(result.get(result.size() - 2), result.get(result.size() - 1)));
                        result.remove(result.size() - 2);
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("INCORRECT FORMULA INPUT");
            System.exit(-1);
        }
        return result.get(0);
    }

    /**
     * Method adds values of variables
     * @param formula :formula inputted by user
     * @return formula with values of variables
     */
    private MyArrayList<String> addVars(MyArrayList<String> formula) {
        for (int i = 0; i < formula.size(); i++) {
            if (varList.containsKey(formula.get(i))) {
                formula.set(i, varList.get(formula.get(i)).toString());
            }
        }
        return formula;
    }

    /**
     * Method uses for identification o number
     * @return boolean if it is number or not
     */
    private boolean isNumber(String item) {
        try {
            Double.parseDouble(item);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}