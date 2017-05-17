package com.airwallex.forexconverter.Utility;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertyLoader extends Properties {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7442190640481351696L;
	private static final  Logger logger = Logger.getLogger(PropertyLoader.class.getCanonicalName());
	public static String FILE_NAME;
	

	public PropertyLoader()
	{
		loadProperties();
	}
	
	private void loadProperties()
	{
		try(InputStream input = this.getClass().getClassLoader().getResourceAsStream(FILE_NAME);)
		{
			super.load(input);
		} catch (Exception e) {
			logger.info("Unable to load property file "+FILE_NAME+" ,Exception::"+e.getLocalizedMessage());
		}
	}

	
		
}
