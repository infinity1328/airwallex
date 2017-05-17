package com.airwallex.forexconverter.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.airwallex.forexconverter.Utility.CommonUtil;
import com.airwallex.forexconverter.Utility.PropertyLoader;

public class ForexProcessorTest {
	PropertyLoader props;
	CommonUtil util ;
	ForexProcessor fx ;
	@Before
	public void setUp() throws Exception {
		PropertyLoader.FILE_NAME = "common.properties";
		fx = new ForexProcessor();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAUD100InUSD() {
		String inputs = "AUD 100.00 in USD";
		assertEquals("AUD 100.00 = USD 83.71", fx.process(inputs));
	}
	
	@Test
	public void testAUD100InAUD() {
		String inputs = "AUD 100.00 in AUD";
		assertEquals("AUD 100.00 = AUD 100.00", fx.process(inputs));
	}
	
	@Test
	public void testKRW1000inFJD() {
		String inputs = "KRW 1000.00 in FJD";
		assertEquals("Unable to find rate for KRW/FJD", fx.process(inputs));
	}
	
	@Test
	public void testAUD100inDKK() {
		String inputs = "AUD 100.00 in DKK";
		assertEquals("AUD 100.00 = DKK 505.76", fx.process(inputs));
	}
	
	@Test
	public void testJPY100inUSD () {
		String inputs = "JPY 100 in USD";
		assertEquals("JPY 100 = USD 0.83", fx.process(inputs));
	}
	
	@Test
	public void testUSD100inJPY () {
		String inputs = "USD 0.83 in JPY";
		assertEquals("USD 0.83 = JPY 100", fx.process(inputs));
	}

	@Test
	public void testInvalidInput() {
		String inputs = "AUD null in USD";
		assertEquals("Unable to parse the input params", fx.process(inputs).split(",")[0]);
	}

}
