package com.shpp.p2p.cs.azhebelev.assignment17.Calculator;

import com.shpp.p2p.cs.azhebelev.assignment17.MyHashMap;

;

/*
Class for implementation of all necessary operators and functions
 */
public class Calculator {

    interface IAction {
        /**
         * Method will make requested calculation for two or one numbers in String format
         * @param preLast Before last item of result ArrayList
         * @param last    last item of result ArrayList
         * @return new value of last item of result ArrayList
         */
        String count(String preLast, String last);
    }

    //HashMap which contain methods for each function
    static MyHashMap<String, IAction> functionsList = new MyHashMap<>();

    public Calculator() {
        functionsList.put("unar", unar);
        functionsList.put("cos", cos);
        functionsList.put("sin", sin);
        functionsList.put("acos", acos);
        functionsList.put("asin", asin);
        functionsList.put("log2", log2);
        functionsList.put("log10", log10);
        functionsList.put("tan", tan);
        functionsList.put("atan", atan);
        functionsList.put("sqrt", sqrt);
        functionsList.put("+", plus);
        functionsList.put("-", minus);
        functionsList.put("*", multi);
        functionsList.put("/", div);
        functionsList.put("^", pow);

};
    IAction cos = new IAction() {
        @Override
        public String count(String preLast, String last) {
            return "" + Math.cos(Double.parseDouble(last));
        }
    };
    IAction sin = new IAction() {
        @Override
        public String count(String preLast, String last) {
            return "" + Math.sin(Double.parseDouble(last));
        }
    };
    IAction tan = new IAction() {
        @Override
        public String count(String preLast, String last) {
            return "" + Math.tan(Double.parseDouble(last));
        }
    };
    IAction asin = new IAction() {
        @Override
        public String count(String preLast, String last) {
            double num = Double.parseDouble(last);
            if (num < -1 || num > 1) {
                System.out.println("incorrect value for asin");
                System.exit(-1);
            }
            return "" + Math.asin(num);
        }
    };
    IAction acos = new IAction() {
        @Override
        public String count(String preLast, String last) {
            double num = Double.parseDouble(last);
            if (num < -1 || num > 1) {
                System.out.println("incorrect value for acos");
                System.exit(-1);
            }
            return "" + Math.acos(num);
        }
    };
    IAction log2 = new IAction() {
        @Override
        public String count(String preLast, String last) {
            double num = Double.parseDouble(last);
            if (num <= 0) {
                System.out.println("incorrect value for log2");
                System.exit(-1);
            }
            return "" + Math.log(num)/Math.log(2);
        }
    };
    IAction log10 = (preLast, last) -> {
        double num = Double.parseDouble(last);
        if (num <= 0) {
            System.out.println("incorrect value for log10");
            System.exit(-1);
        }
        return "" + Math.log10(num);
    };
    IAction sqrt = new IAction() {
        @Override
        public String count(String preLast, String last) {
            double num = Double.parseDouble(last);
            if (num <= 0) {
                System.out.println("incorrect value for sqrt");
                System.exit(-1);
            }
            return "" + Math.sqrt(num);
        }
    };
    IAction atan = new IAction() {
        @Override
        public String count(String preLast, String last) {
            return "" + Math.atan(Double.parseDouble(last));
        }
    };
    IAction plus = new IAction() {
        @Override
        public String count(String preLast, String last) {
            return "" + (Double.parseDouble(preLast) + Double.parseDouble(last));
        }
    };
    IAction minus = new IAction() {
        @Override
        public String count(String preLast, String last) {
            return "" + (Double.parseDouble(preLast) - Double.parseDouble(last));
        }
    };
    IAction multi = new IAction() {
        @Override
        public String count(String preLast, String last) {
            return "" + (Double.parseDouble(preLast) * Double.parseDouble(last));
        }
    };
    IAction div = new IAction() {
        @Override
        public String count(String preLast, String last) {
            if(Double.parseDouble(last) == 0) {
                System.out.println("division by 0 is not excepted");
                System.exit(-1);
            }
            return "" + (Double.parseDouble(preLast) / Double.parseDouble(last));
        }
    };
    IAction pow = new IAction() {
        @Override
        public String count(String preLast, String last) {
            return "" + Math.pow(Double.parseDouble(preLast), Double.parseDouble(last));
        }
    };
    IAction unar = new IAction() {
        @Override
        public String count(String preLast, String last) {
            return "" + Double.parseDouble(last) * (-1);
        }
    };
}
