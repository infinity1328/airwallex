package com.airwallex.rpncalculator.obj;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.airwallex.rpncalculator.utility.Operation;
import com.airwallex.rpncalculator.utility.RPNCalculatorException;

public class OperationData {
    private Operation operation;
    private BigDecimal value;
    private static final int DECIMAL_PLACE = 15;
     
    public OperationData(Operation operation,  BigDecimal value) {
        this.operation = operation;
        this.value = value.setScale(DECIMAL_PLACE, RoundingMode.UP);
    }

    public String getUndoOperation() throws RPNCalculatorException {
        if (operation.getSubjectCount() < 1)
            throw new RPNCalculatorException(String.format("Not a Valid operation  %s", operation.getInstruction()));

        return (operation.getSubjectCount() < 2) ?
                String.format("%s", operation.getUndoOperation()) :
                String.format("%f %s %f", value, operation.getUndoOperation(), value);
    }
}
