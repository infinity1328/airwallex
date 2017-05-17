package com.airwallex.forexconverter.service;

import java.util.Scanner;
import java.util.logging.Logger;

import com.airwallex.forexconverter.Utility.PropertyLoader;

public class ForexServiceMain {
	private static Logger logger = Logger.getLogger(ForexServiceMain.class.getCanonicalName());

public static void main(String[] args) {
	logger.info("Enter the values ,example AUD 100.00 in USD, or 'exit' to quit");
	PropertyLoader.FILE_NAME = "common.properties";
	ForexProcessor fxProcessor = new ForexProcessor();
	
	boolean keepRunning = true;
	String output;
	
		try (Scanner in = new Scanner(System.in)) {
			while (keepRunning) {
			if (in.hasNext()) {
				String inputParams = in.nextLine();
				if ("exit".equals(inputParams)) {
					keepRunning = false;
				} else {
					output = fxProcessor.process(inputParams);
					System.out.println(output+"\n");
				}
			}
			}
		} catch (Exception s) {
			logger.info("Exception occured, Exception::"+s.getLocalizedMessage());
			
		

	}
}
}
