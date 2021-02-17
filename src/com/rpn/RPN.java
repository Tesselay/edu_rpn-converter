package com.rpn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.parseDouble;

public class RPN {

    Map<Character, Integer> precedence = new HashMap<>();
    Map<Character, Integer> associativity = new HashMap<>();        // 0 = Right, 1 = Left

    public static void main(String[] args) {
        RPN rpn = new RPN();
        System.out.println(rpn.computeRPN("x*4+12", 2d));       // result = 20
        System.out.println(rpn.computeRPN("3+x*6", 4d));        // result = 27
        System.out.println(rpn.computeRPN("2*(x+1)", 6d));      // result = 14
        System.out.println(rpn.computeRPN("x-4+4", 8d));        // result = 8
        System.out.println(rpn.computeRPN("2*7^x+3", 2d));     // result =
        System.out.println(rpn.computeRPN("x*(x+4*15)+2", 12d));
    }

    public RPN() {
        init();
    }

    private double computeRPN(String formula, double x) {
        ArrayList<String> postfixTerm = shuntingYard(formula);
        for ( int i = 0; i < postfixTerm.size(); i++ ){
            if (postfixTerm.get(i).equals("x")) {
                postfixTerm.set(i, String.valueOf(x));
            }
        }
        System.out.println(postfixTerm);

        double result = 0;
        for ( int i = 0; i < postfixTerm.size(); i++) {

            if ( isOperator(postfixTerm.get(i).toCharArray()[0]) ) {

                if ( postfixTerm.get(i).toCharArray()[0] == '+' ) {
                    result = parseDouble(postfixTerm.get(i-2)) + parseDouble(postfixTerm.get(i-1));
                    postfixTerm.set(i-2, String.valueOf(result));
                    postfixTerm.remove(i);
                    postfixTerm.remove(i-1);
                    i = 0;
                } else if ( postfixTerm.get(i).toCharArray()[0] == '-' ) {
                    result = parseDouble(postfixTerm.get(i-2)) - parseDouble(postfixTerm.get(i-1));
                    postfixTerm.set(i-2, String.valueOf(result));
                    postfixTerm.remove(i);
                    postfixTerm.remove(i-1);
                    i = 0;
                } else if ( postfixTerm.get(i).toCharArray()[0] == '*' ) {
                    result = parseDouble(postfixTerm.get(i-2)) * parseDouble(postfixTerm.get(i-1));
                    postfixTerm.set(i-2, String.valueOf(result));
                    postfixTerm.remove(i);
                    postfixTerm.remove(i-1);
                    i = 0;
                } else if ( postfixTerm.get(i).toCharArray()[0] == '/' ) {
                    result = parseDouble(postfixTerm.get(i-2)) / parseDouble(postfixTerm.get(i-1));
                    postfixTerm.set(i-2, String.valueOf(result));
                    postfixTerm.remove(i);
                    postfixTerm.remove(i-1);
                    i = 0;
                } else if ( postfixTerm.get(i).toCharArray()[0] == '^' ) {
                    result = Math.pow(parseDouble(postfixTerm.get(i-2)), parseDouble(postfixTerm.get(i-1)));
                    postfixTerm.set(i-2, String.valueOf(result));
                    postfixTerm.remove(i);
                    postfixTerm.remove(i-1);
                    i = 0;
                }

            }

        }

        return result;
    }

    private ArrayList<String> shuntingYard(String infix) {
        StringBuilder output = new StringBuilder();
        StringBuilder stack = new StringBuilder();

        for (int i = 0; i < infix.length(); i++) {
            char elem = infix.charAt(i);

            if( !isOperator(elem) ){
                output.append(elem);
                try{
                    if (!isOperator(infix.charAt(i+1)) ) {
                        ;
                    } else {
                        output.append(" ");
                    }
                }catch(StringIndexOutOfBoundsException e){
                    output.append(" ");
                }
            }
            else if( elem == '(' ) {
                stack.append(elem);
            }
            else if( elem == ')' ) {
                for( int ii = stack.toString().length() - 1; ii >= 0; ii-- ) {
                    if( stack.charAt(ii) == '(') {
                        stack.deleteCharAt(ii);
                        break;
                    }
                    output.append(stack.charAt(ii));
                    output.append(" ");
                    stack.deleteCharAt(ii);
                }
            }
            else if( isOperator(elem) && (stack.length() == 0 || stack.charAt(stack.length()-1) == '(' ) ) {
                stack.append(elem);
            }
            else if( stack.length() > 0 && ((precedence.get(elem) > precedence.get(stack.charAt(stack.length()-1))) || (precedence.get(elem) == precedence.get(stack.charAt(stack.length()-1)) && associativity.get(elem) == 0)) ) {
                stack.append(elem);
            }
            else if( stack.length() > 0 && ((precedence.get(elem) < precedence.get(stack.charAt(stack.length()-1))) || (precedence.get(elem) == precedence.get(stack.charAt(stack.length()-1)) && associativity.get(elem) == 1)) ) {
                while( stack.length() > 0 && ((precedence.get(elem) < precedence.get(stack.charAt(stack.length()-1))) || (precedence.get(elem) == precedence.get(stack.charAt(stack.length()-1)) && associativity.get(elem) == 1)) ) {
                    output.append(stack.charAt(stack.length()-1));
                    output.append(" ");
                    stack.deleteCharAt(stack.length()-1);
                }
                stack.append(elem);
            }
        }

        for( int ii = stack.toString().length() - 1; ii >= 0; ii-- ) {
            output.append(stack.charAt(ii));
            output.append(" ");
        }

        ArrayList<String> returnOutput = new ArrayList<>(Arrays.asList(output.toString().split(" ")));          // I know it's hacky but can't be bothered to refactor this whole function

        return returnOutput;
    }

    private boolean isOperator(char symbol) {
        char[] operators = {'+', '-', '*', '/', '(', ')', '^'};

        for( char operator: operators ) {
            if( operator == symbol ){
                return true;
            }
        }

        return false;
    }

    private void init() {
        precedence.put('^', 2);
        precedence.put('*', 1);
        precedence.put('/', 1);
        precedence.put('+', 0);
        precedence.put('-', 0);

        associativity.put('^', 0);
        associativity.put('*', 1);
        associativity.put('/', 1);
        associativity.put('+', 1);
        associativity.put('-', 1);
    }

}
