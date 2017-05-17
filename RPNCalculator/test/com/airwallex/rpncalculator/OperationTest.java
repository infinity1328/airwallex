package com.airwallex.rpncalculator;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import org.junit.Test;

import com.airwallex.rpncalculator.utility.Operation;
import com.airwallex.rpncalculator.utility.RPNCalculatorException;

public class OperationTest {

    @Test
    public void testCalculate() throws RPNCalculatorException {
        Random r = new Random();
        BigDecimal firstOperand = new BigDecimal(528);
        BigDecimal secondOperand = new BigDecimal(658);
        assertEquals(secondOperand.add(firstOperand), Operation.ADDITION.calculate(firstOperand, secondOperand));
        assertEquals(secondOperand.subtract(firstOperand), Operation.SUBTRACTION.calculate(firstOperand, secondOperand));
        assertEquals(secondOperand.multiply(firstOperand), Operation.MULTIPLICATION.calculate(firstOperand, secondOperand));
        assertEquals(secondOperand.divide(firstOperand,15, RoundingMode.HALF_UP).stripTrailingZeros(), Operation.DIVISION.calculate(firstOperand, secondOperand));
        assertEquals(new BigDecimal(pow(firstOperand.doubleValue(), 2.0)), Operation.POWER.calculate(firstOperand, null));
        assertEquals(new BigDecimal(sqrt(secondOperand.doubleValue())), Operation.SQUAREROOT.calculate(secondOperand, null));
    }

    @Test(expected = RPNCalculatorException.class)
    public void testDivideByZero() throws RPNCalculatorException {
        Operation.DIVISION.calculate(new BigDecimal(0), new BigDecimal(0));
    }

    @Test(expected = RPNCalculatorException.class)
    public void testInvalidOperations() throws RPNCalculatorException {
        Operation.UNDO.calculate(new BigDecimal(0), new BigDecimal(0));
        Operation.CLEAR.calculate(new BigDecimal(0), new BigDecimal(0));
    }
}
