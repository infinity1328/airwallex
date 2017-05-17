package com.airwallex.forexconverter.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Logger;

import com.airwallex.forexconverter.Utility.CommonUtil;
import com.airwallex.forexconverter.Utility.PropertyLoader;

public class ForexProcessor {

	private static final String DELIMITER_INPUT = "\\s";
	private static final String EQUALS = "=";
	private static final String SPACE = " ";
	private static final Logger logger = Logger.getLogger(ForexProcessor.class.getCanonicalName());
	private static CommonUtil util ;
	public ForexProcessor()
	{
		PropertyLoader props = new PropertyLoader();
		util = new CommonUtil(props);
	}
	public String process(String inputs)
	{
		return fxExchange(inputs);
	}
	private String fxExchange(String input)
	{
		String[] inputParams = input.split(DELIMITER_INPUT);
		String baseCcy = null;
		String termCcy = null;
		BigDecimal amount = null;
		try {
			baseCcy = inputParams[0];
			termCcy = inputParams[3];
			amount = new BigDecimal(inputParams[1]);
		} catch (Exception e) {
			logger.info("Unable to parse the input params,"+input+" Exception::"+e.getLocalizedMessage());
			return "Unable to parse the input params,"+input+" Exception::"+e.getLocalizedMessage();
		}
		BigDecimal rate = null;
		if(util.getValidCurrencies().contains(baseCcy) && util.getValidCurrencies().contains(termCcy))
		{
			if(baseCcy.equalsIgnoreCase(termCcy))
			{
				return getEqualsMessage(baseCcy, amount);
			}
			else if( (rate= getExchangeRateForCcyPair(baseCcy, termCcy)) != null)
			{
				return getMessage(baseCcy,termCcy,amount, adjustDecimals(amount.multiply(rate),termCcy));
			}
			else
			{
				return getMessage(baseCcy,termCcy,amount,calculate(baseCcy, termCcy, null, amount, ""));
				
			}
		}
		else
		{
			return "Unable to find rate for "+baseCcy+"/"+termCcy;
		}
		
	}
	private BigDecimal calculate(String src , String dest, String destOld, BigDecimal amt, String amtCcy)
	{
		BigDecimal rate;
		String key ;
		String keyInverse;
		while(!dest.equalsIgnoreCase(amtCcy))
		{
			key = src+dest;
			keyInverse = dest+src;
			if(util.getCurrencyPairs().containsKey(key) || util.getCurrencyPairs().containsKey(keyInverse))
			{
				rate = util.getCurrencyPairs().containsKey(key) ? util.getCurrencyPairs().get(key): getRate(keyInverse);
				amt = (amt.multiply(rate)).setScale(4, RoundingMode.CEILING);
				amtCcy = dest;
				src = dest;	
				dest = destOld;
			}else if(util.getMappingPairs().containsKey(key))
			{
				destOld = dest;
				dest = util.getMappingPairs().get(key);
			}
			else 
			{
				destOld = dest;
				dest = util.getDefaultCurrency();
			}
		}
		return amt.setScale(2,RoundingMode.UP);
	}
	private BigDecimal getRate(String keyInverse)
	{
		BigDecimal inverseRate = util.getInverseRate(util.getCurrencyPairs().get(keyInverse)) ;
		return inverseRate;
	}
	private BigDecimal adjustDecimals(BigDecimal amount, String termCcy)
	{
		Integer decimalPoints= -1;
		if (null == (decimalPoints = util.getDecimalPairs().get(termCcy)))
		{
			decimalPoints = util.getDefaultDecimal();
		}
		return amount.setScale(decimalPoints, RoundingMode.HALF_UP);
	}
	private BigDecimal getExchangeRateForCcyPair(String baseCcy, String termCcy)
	{
		return util.getCurrencyPairs().get(baseCcy+termCcy);
	}
	private String getEqualsMessage(String baseCcy,BigDecimal amount)
	{
		return  getMessage( baseCcy, baseCcy,  amount, amount );
	}
	 private String getMessage(String baseCcy,String termCcy, BigDecimal amount, BigDecimal convertAmount)
	 {
		 StringBuilder outputBuilder = new StringBuilder();
			outputBuilder.append(baseCcy);
			outputBuilder.append(SPACE);
			outputBuilder.append(amount);
			outputBuilder.append(SPACE);
			outputBuilder.append(EQUALS);
			outputBuilder.append(SPACE);
			outputBuilder.append(termCcy);
			outputBuilder.append(SPACE);
			outputBuilder.append(convertAmount);
			return outputBuilder.toString();
	 }
}
