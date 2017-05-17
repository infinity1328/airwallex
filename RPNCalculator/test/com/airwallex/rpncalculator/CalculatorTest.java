package com.airwallex.rpncalculator;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import com.airwallex.rpncalculator.utility.Calculator;
import com.airwallex.rpncalculator.utility.RPNCalculatorException;

public class CalculatorTest {

    @Test
    public void testAritmeticOperators() throws Exception {
        Calculator calculator = new Calculator();

        calculator.process("5 2");
        assertEquals(new BigDecimal(5), calculator.getDataStore().get(0));
        assertEquals(new BigDecimal(2), calculator.getDataStore().get(1));

        // substraction
        calculator.process("clear");
        calculator.process("5 2 -");
        assertEquals(1, calculator.getDataStore().size());
        assertEquals(new BigDecimal(3), calculator.getDataStore().get(0));
        calculator.process("3 -");
        assertEquals(1, calculator.getDataStore().size());
        assertEquals(new BigDecimal(0), calculator.getDataStore().get(0));

        // negative
        calculator.process("clear");
        calculator.process("1 2 3 4 5 *");
        assertEquals(4, calculator.getDataStore().size());
        calculator.process("clear 3 4 -");
        assertEquals(1, calculator.getDataStore().size());
        assertEquals(new BigDecimal(-1), calculator.getDataStore().get(0));


        // division
        calculator.process("clear");
        calculator.process("7 12 2 /");
        assertEquals(new BigDecimal(7), calculator.getDataStore().get(0));
        assertEquals(new BigDecimal(6), calculator.getDataStore().get(1));
        calculator.process("*");
        assertEquals(1, calculator.getDataStore().size());
        assertEquals(new BigDecimal(42), calculator.getDataStore().get(0));
        calculator.process("4 /");
        assertEquals(1, calculator.getDataStore().size());
        assertEquals(new BigDecimal(10.5), calculator.getDataStore().get(0));

        //multiplication
        calculator.process("clear");
        calculator.process("1 2 3 4 5");
        calculator.process("* * * *");
        assertEquals(1, calculator.getDataStore().size());
        assertEquals(new BigDecimal(120), calculator.getDataStore().get(0));

    }

    @Test
    public void testSqrt() throws Exception {
        Calculator calculator = new Calculator();
        calculator.process("2 sqrt");
        calculator.process("clear 9 sqrt");
        assertEquals(1, calculator.getDataStore().size());
        assertEquals(new BigDecimal("3"), calculator.getDataStore().get(0));
    }

    @Test
    public void testInsuficientParameters() {
        Calculator calculator = new Calculator();
        try {
            calculator.process("1 2 3 * 5 + * * 6 5");
        } catch (RPNCalculatorException e) {
            assertEquals("operator * (position: 8): insufficient parameters", e.getMessage());
        }
        assertEquals(1, calculator.getDataStore().size());
        assertEquals(new BigDecimal(11), calculator.getDataStore().get(0));
    }

    @Test
    public void testUndo() throws Exception {
        Calculator calculator = new Calculator();
        calculator.process("5 4 3 2");
        assertEquals(4, calculator.getDataStore().size());
        calculator.process("undo undo *");
        assertEquals(1, calculator.getDataStore().size());
        assertEquals(new BigDecimal(20), calculator.getDataStore().get(0));
        calculator.process("5 *");
        assertEquals(1, calculator.getDataStore().size());
        assertEquals(new BigDecimal(100), calculator.getDataStore().get(0));
        calculator.process("undo");
        assertEquals(2, calculator.getDataStore().size());
        assertEquals(new BigDecimal(20).setScale(10).stripTrailingZeros(), calculator.getDataStore(0));
        assertEquals(new BigDecimal(5), calculator.getDataStore(1));
        calculator.process("+ undo - undo / undo * undo sqrt undo pow undo");
        assertEquals(2, calculator.getDataStore().size());
        assertEquals(new BigDecimal(2E+1).setScale(10).stripTrailingZeros(), calculator.getDataStore(0));
        assertEquals(new BigDecimal(5), calculator.getDataStore().get(1));
    }

    @Test(expected = RPNCalculatorException.class)
    public void testOnlyOperators() throws Exception {
        Calculator calculator = new Calculator();
        calculator.process("+ +");
    }

    @Test(expected = RPNCalculatorException.class)
    public void testInvalidCharacters() throws Exception {
        Calculator calculator = new Calculator();
        calculator.process("2 a +");
    }

    @Test(expected = RPNCalculatorException.class)
    public void testNoSpaces() throws Exception {
        Calculator calculator = new Calculator();
        calculator.process("22+");
    }

    @Test(expected = RPNCalculatorException.class)
    public void testNoSpaces2() throws Exception {
        Calculator calculator = new Calculator();
        calculator.process("2 2+ 3");
    }

    @Test(expected = RPNCalculatorException.class)
    public void testDivideByZero() throws Exception {
        Calculator calculator = new Calculator();
        calculator.process("1 0 /");
    }

    @Test(expected = RPNCalculatorException.class)
    public void testNullInput() throws Exception {
        Calculator calculator = new Calculator();
        calculator.process(null);
    }
}
