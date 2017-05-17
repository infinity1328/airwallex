package com.airwallex.forexconverter.Utility;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static com.airwallex.forexconverter.Utility.KEYS.*;
public class PropertyLoaderTest {

	PropertyLoader props;
	@Before
	public void setUp() throws Exception {
		PropertyLoader.FILE_NAME = "common.properties";
		props = new PropertyLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPropertyLoader() {
		assertNotNull(props);
		assertEquals(props.get(currency_pairs.name()), "AUDUSD=0.8371|CADUSD=0.8711|USDCNY=6.1715|EURUSD=1.2315|GBPUSD=1.5683|NZDUSD=0.7750|USDJPY=119.95|EURCZK=27.6028|EURDKK=7.4405|EURNOK=8.6651");
		assertEquals(props.get(valid_currencies.name()), "AUD|CAD|CNY|CZK|DKK|EUR|GBP|JPY|NOK|NZD|USD");
		assertEquals(props.get(default_currency.name()), "USD");
		assertEquals(props.get(default_decimals.name()), "2");
		assertEquals(props.get(decimals.name()), "JPY0");
	}

}
