package com.airwallex.rpncalculator.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public enum Operation {

    ADDITION("+", "-", 2) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) throws RPNCalculatorException {
            return secondOperand.add(firstOperand);
        }
    },

    SUBTRACTION("-", "+", 2) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) {
            return secondOperand.subtract(firstOperand);
        }
    },

    MULTIPLICATION("*", "/", 2) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) {
            return secondOperand.multiply(firstOperand);
        }
    },

    DIVISION("/", "*", 2) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) throws RPNCalculatorException {
         	if (firstOperand.compareTo(ZERO)==0)
                throw new RPNCalculatorException("Cannot divide by 0.");
            return secondOperand.divide(firstOperand,15, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    },

    SQUAREROOT("sqrt", "pow", 1) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) {
            return new BigDecimal(Math.sqrt(firstOperand.doubleValue()));
        }
    },

    POWER("pow", "sqrt", 1) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) {
            return firstOperand.pow(2);
        }
    },

    UNDO("undo", null, 0) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) throws RPNCalculatorException {
            throw new RPNCalculatorException("Invalid operation");
        }
    },

    CLEAR("clear", null, 0) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) throws RPNCalculatorException {
            throw new RPNCalculatorException("Invalid operation");
        }
    };
	
    private static final Map<String, Operation> lookup = new HashMap<String, Operation>();
    private static final BigDecimal ZERO = new BigDecimal("0");
    static {
        for (Operation operation : values()) {
            lookup.put(operation.getInstruction(), operation);
        }
    }

    private String instruction;
    private String undoOperation;
    private int subjectCount;

    Operation(String instruction, String undoOperation, int subjectCount) {
        this.instruction = instruction;
        this.undoOperation = undoOperation;
        this.subjectCount = subjectCount;
    }

    public static Operation getEnum(String value) {
        return lookup.get(value);
    }

    public abstract BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) throws RPNCalculatorException;

   

    public static Map<String, Operation> getLookup() {
		return lookup;
	}

	public String getInstruction() {
		return instruction;
	}

	public String getUndoOperation() {
		return undoOperation;
	}

	public int getSubjectCount() {
		return subjectCount;
	}

	@Override
    public String toString() {
        return instruction;
    }
}