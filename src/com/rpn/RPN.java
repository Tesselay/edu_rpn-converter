package com.rpn;

import java.util.HashMap;
import java.util.Map;

public class RPN {

    Map<Character, Integer> precedence = new HashMap<>();
    Map<Character, Integer> associativity = new HashMap<>();        // 0 = Right, 1 = Left

    public static void main(String[] args) {
        RPN rpn = new RPN();
        System.out.println(rpn.computeRPN("A*B+C"));
        System.out.println(rpn.computeRPN("A+B*C"));
        System.out.println(rpn.computeRPN("A*(B+C)"));
        System.out.println(rpn.computeRPN("A-B+C"));
        System.out.println(rpn.computeRPN("A*B^C+D"));
        System.out.println(rpn.computeRPN("A*(B+C*D)+E"));
    }

    public RPN() {
        init();
    }

    private String computeRPN(String formula) {
        return shuntingYard(formula);
    }

    private String shuntingYard(String infix) {
        StringBuilder output = new StringBuilder();
        StringBuilder stack = new StringBuilder();

        for (int i = 0; i < infix.length(); i++) {
            char elem = infix.charAt(i);

            if( !isOperator(elem) ){
                output.append(elem);
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
                    stack.deleteCharAt(stack.length()-1);
                }
                stack.append(elem);
            }
        }

        for( int ii = stack.toString().length() - 1; ii >= 0; ii-- ) {
            output.append(stack.charAt(ii));
        }

        return output.toString();
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
