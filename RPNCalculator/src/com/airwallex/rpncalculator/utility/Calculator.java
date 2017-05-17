package com.airwallex.rpncalculator.utility;

import java.math.BigDecimal;
import java.util.Stack;

import com.airwallex.rpncalculator.obj.OperationData;

public class Calculator {

	private static final String DELIMITER_INPUT = "\\s";
	private Stack<BigDecimal> dataStore = new Stack<BigDecimal>();
	private Stack<OperationData> operationStore = new Stack<OperationData>();
	private int tokenIndex = 0;

	private BigDecimal getValue(String str) {
		try {
			return new BigDecimal(str);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	private void processToken(String token, boolean isUndo) throws RPNCalculatorException {
		BigDecimal value = getValue(token);
		if (value == null) {
			performOperation(token, isUndo);
		} else {
			dataStore.push(value);
			if (!isUndo) {
				operationStore.push(null);
			}
		}
	}

	private void performOperation(String operation, boolean isUndo) throws RPNCalculatorException {

		if (dataStore.isEmpty()) {
			throw new RPNCalculatorException("empty stack");
		}

		Operation operator = Operation.getEnum(operation);
		if (operator == null) {
			throw new RPNCalculatorException("invalid operator");
		}

		if (operator == Operation.CLEAR) {
			clearStore();
			return;
		}

		if (operator == Operation.UNDO) {
			isUndoLastInstruction();
			return;
		}

		if (operator.getSubjectCount() > dataStore.size()) {
			throwInvalidOperand(operation);
		}

		BigDecimal firstOperand = dataStore.pop();
		BigDecimal secondOperand = (operator.getSubjectCount() > 1) ? dataStore.pop() : null;
		BigDecimal result = operator.calculate(firstOperand, secondOperand);

		if (result != null) {
			dataStore.push(result);
			if (!isUndo) {
				operationStore.push(new OperationData(Operation.getEnum(operation), firstOperand));
			}
		}

	}

	private void isUndoLastInstruction() throws RPNCalculatorException {
		if (operationStore.isEmpty()) {
			throw new RPNCalculatorException("no operations to isUndo");
		}

		OperationData previousOperation = operationStore.pop();
		if (previousOperation == null) {
			dataStore.pop();
		} else {
			process(previousOperation.getUndoOperation(), true);
		}
	}

	private void clearStore() {
		dataStore.clear();
		operationStore.clear();
	}

	private void throwInvalidOperand(String operator) throws RPNCalculatorException {
		throw new RPNCalculatorException(
				String.format("operator %s (position: %d): insufficient parameters", operator, tokenIndex));
	}

	public void process(String input) throws RPNCalculatorException {
		process(input, false);
	}

	private void process(String input, boolean isUndo) throws RPNCalculatorException {
		if (input == null) {
			throw new RPNCalculatorException("Input cannot be null.");
		}
		tokenIndex = 0;
		String[] inputParams = input.split(DELIMITER_INPUT);
		for (String inputParam : inputParams) {
			tokenIndex++;
			processToken(inputParam, isUndo);
		}
	}

	public Stack<BigDecimal> getDataStore() {
		return dataStore;
	}

	public BigDecimal getDataStore(int index) {
		return dataStore.get(index).setScale(10).stripTrailingZeros();
	}

	public Stack<OperationData> getOperationStore() {
		return operationStore;
	}

	public void setOperationStore(Stack<OperationData> operationStore) {
		this.operationStore = operationStore;
	}
}
