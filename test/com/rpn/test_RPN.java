package com.rpn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class test_RPN {

    @Test
    @DisplayName("precedence and associativity is set correctly on init")
    void precedenceAssociativitySet() {
        RPN rpn = new RPN();

        assertEquals(2, rpn.precedence.get('^'));
        assertEquals(1, rpn.precedence.get('*'));
        assertEquals(1, rpn.precedence.get('/'));
        assertEquals(0, rpn.precedence.get('+'));
        assertEquals(0, rpn.precedence.get('-'));

        assertEquals(0, rpn.associativity.get('^'));
        assertEquals(1, rpn.associativity.get('*'));
        assertEquals(1, rpn.associativity.get('/'));
        assertEquals(1, rpn.associativity.get('+'));
        assertEquals(1, rpn.associativity.get('-'));
    }

    @Test
    @DisplayName("operator check includes all defined operators")
    void operatorCheck() {
        RPN rpn = new RPN();

        char[] operators = {'+', '-', '*', '/', '(', ')', '^'};
        char[] trashValues = {'2', '4', 'x', 'f', '´', 'L', 'p'};

        for( char c : operators )
            assumeTrue(rpn.isOperator(c));

        for( char c : trashValues )
            assumeFalse(rpn.isOperator(c));

    }

    @Test
    @DisplayName("infix is converted correctly to postifx")
    void shuntingYard() {
        RPN rpn = new RPN();

        ArrayList<String> postfixTerm = rpn.shuntingYard("A*B+C");
        ArrayList<String> result = new ArrayList<>();
        result.add("A");
        result.add("B");
        result.add("*");
        result.add("C");
        result.add("+");
        assertEquals(result, postfixTerm);

        postfixTerm = rpn.shuntingYard("A*(B+C*D)+E");
        result.clear();
        result.add("A");
        result.add("B");
        result.add("C");
        result.add("D");
        result.add("*");
        result.add("+");
        result.add("*");
        result.add("E");
        result.add("+");
        assertEquals(result, postfixTerm);
    }

    @Test
    @DisplayName("terms are calculated correctly")
    void rpnCalculation() {
        RPN rpn = new RPN();

        assertEquals(20, rpn.computeRPN("x*4+12", 2d));
        assertEquals(27, rpn.computeRPN("3+x*6", 4d));
        assertEquals(14, rpn.computeRPN("2*(x+1)", 6d));
        assertEquals(8, rpn.computeRPN("x-4+4", 8d));
        assertEquals(101, rpn.computeRPN("2*7^x+3", 2d));
        assertEquals(866, rpn.computeRPN("x*(x+4*15)+2", 12d));
    }
}