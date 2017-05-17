package com.airwallex.rpncalculator;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Logger;

import com.airwallex.rpncalculator.utility.Calculator;
import com.airwallex.rpncalculator.utility.RPNCalculatorException;

public class RPNServiceMain {
	private static Logger logger = Logger.getLogger(RPNServiceMain.class.getCanonicalName());

	public static void main(String[] args) {

		Calculator calculator = new Calculator();

		logger.info("Enter the operators and opertation, or 'exit' to quit");

		boolean keepRunning = true;
		while (keepRunning) {
			try (Scanner in = new Scanner(System.in)) {
				if (in.hasNext()) {
					String inputParams = in.nextLine();
					if ("exit".equals(inputParams)) {
						keepRunning = false;
					} else {
						try {
							calculator.process(inputParams);
						} catch (RPNCalculatorException e) {
							logger.info(e.getMessage());
						}

						Stack<BigDecimal> dataStoreValues = calculator.getDataStore();
						logger.info("Stack: ");
						for (BigDecimal value : dataStoreValues) {
							logger.info(""+value.setScale(10).stripTrailingZeros());
							logger.info(" ");
						}
						logger.info("%n");
					}
				}
			} catch (Exception s) {
				logger.info("Exception occured, Exception::"+s.getLocalizedMessage());
				
			}

		}
	}
}
