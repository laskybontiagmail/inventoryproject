package com.aiur.utilities;

import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aiur.utilities.ejb.EJB3;

/**
 * Application property.
 */
public class ApplicationProperty {
	
	private static Logger logger = LogManager.getLogger(EJB3.class);
	private static Properties properties;

	static {
		properties = new Properties();
		try {
				InputStream stream = ApplicationProperty.class.getResourceAsStream("/application.properties");
			properties.load(stream);
			stream.close();
			
		} catch (Exception exc) {
			logger.error(exc);
		}
	}


	/**
	 * Get the property from application property file.
	 * @param property - property
	 * @return {String}
	 */
	public static String get(String property) {
		return properties.getProperty(property);
	}

	/**
	 * Set the property from application property file.
	 * @param key - key
	 * @param value - value
	 */
	public static void set(String key, String value) {
		properties.put(key, value);
	}
	
}
