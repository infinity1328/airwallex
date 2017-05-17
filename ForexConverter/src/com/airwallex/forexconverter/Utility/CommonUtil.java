package com.airwallex.forexconverter.Utility;

import static com.airwallex.forexconverter.Utility.KEYS.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
public class CommonUtil {
	private PropertyLoader props ;
	private Map<String,BigDecimal> currencyPairs;
	private Set<String> validCurrencies;
	private String defaultCurrency;
	private int defaultDecimal;
	private Map<String,String> mappingPairs;
	private Map<String,Integer> decimalPairs;
	private static final String DELIMITER_HIPEN= "-";
	private static final String DELIMITER_EQUALS= "=";
	public CommonUtil(PropertyLoader props) {
		super();
		this.props = props;
		loadAllProperties();
	}
	
	private void loadAllProperties()
	{
		currencyPairs = loadPropertiesByKey(currency_pairs.name());
		decimalPairs = loadPropertiesByKeyDecimalPair(decimals.name());
		mappingPairs = loadPropertiesByKeySpec(mapping_pairs.name());
		loadValidCurrencies();
		loadOtherProperties();
	}
	

	private Map<String,BigDecimal> loadPropertiesByKey(String key)
	{
		Map<String,BigDecimal> dataStore = new HashMap<>();
		String currencyPairsList = props.get(key).toString();
		String [] currencies = splitMe(currencyPairsList,DELIMITER_HIPEN);
		String [] ccynRate;
		String ccyKey = null;
		BigDecimal amount;
		for(String ccy : currencies)
		{
			ccynRate = splitMe(ccy, DELIMITER_EQUALS);
			ccyKey = ccynRate[0];
			amount = new BigDecimal(ccynRate[1]);
			dataStore.put(ccyKey, amount.setScale(getDecimalPlaces(amount), RoundingMode.HALF_UP));
			ccyKey = ccyKey.substring(3, 6)+ccyKey.substring(0, 3);
			dataStore.put(ccyKey, getInverseRate(amount.setScale(getDecimalPlaces(amount), RoundingMode.HALF_UP)));
		}
		return dataStore;
	}
	private Map<String,Integer> loadPropertiesByKeyDecimalPair(String key)
	{
		Map<String,Integer> dataStore = new HashMap<>();
		String currencyPairsList = props.get(key).toString();
		String [] currencies = splitMe(currencyPairsList,DELIMITER_HIPEN);
		String [] ccynRate;
		String ccyKey = null;
		int decimals;
		for(String ccy : currencies)
		{
			ccynRate = splitMe(ccy, DELIMITER_EQUALS);
			ccyKey = ccynRate[0];
			decimals = Integer.parseInt(ccynRate[1]);
			dataStore.put(ccyKey, decimals);
		}
		return dataStore;
	}
	public int getDecimalPlaces(BigDecimal number)
	{
		return Math.max(0, number.stripTrailingZeros().scale());
	}
	public BigDecimal getInverseRate(BigDecimal rate)
	{
		BigDecimal one = new BigDecimal("1");
		BigDecimal inverseRate = one.setScale(4).divide(rate, RoundingMode.HALF_UP);
		return inverseRate;
	}
	private Map<String,String> loadPropertiesByKeySpec(String key)
	{
		Map<String,String> dataStore = new HashMap<>();
		String currencyPairsList = props.get(key).toString();
		String [] currencies = splitMe(currencyPairsList,DELIMITER_HIPEN);
		String [] ccynRate;
		for(String ccy : currencies)
		{
			ccynRate = splitMe(ccy, DELIMITER_EQUALS);
			dataStore.put(ccynRate[0], ccynRate[1]);
		}
		return dataStore;
	}
	
	private void loadValidCurrencies()
	{
		validCurrencies = new HashSet<>();
		String currencyPairsList = props.get(valid_currencies.name()).toString();
		String [] currencies = splitMe(currencyPairsList,DELIMITER_HIPEN);
		for(String ccy : currencies)
		{
			validCurrencies.add(ccy);
		}
		
	}
	
	private void loadOtherProperties()
	{
		defaultCurrency = props.getProperty(default_currency.name());
		defaultDecimal = Integer.valueOf(props.getProperty(default_decimals.name()));
	}
	private String[] splitMe(String value, String delimiter)
	{
		return value.split(delimiter);
	}
	public Map<String, BigDecimal> getCurrencyPairs() {
		return currencyPairs;
	}

	public Set<String> getValidCurrencies() {
		return validCurrencies;
	}

	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	public int getDefaultDecimal() {
		return defaultDecimal;
	}

	public Map<String, Integer> getDecimalPairs() {
		return decimalPairs;
	}

	public static String getDelimiterHipen() {
		return DELIMITER_HIPEN;
	}

	public static String getDelimiterEquals() {
		return DELIMITER_EQUALS;
	}

	public Map<String, String> getMappingPairs() {
		return mappingPairs;
	}
}
