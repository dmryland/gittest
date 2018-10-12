package com.ni.apps.soa.nivcg.utils;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static com.ni.apps.soa.nivcg.constants.NivcgConstants.DEFAULT_RESOURCE_BUNDLE;

/**
 * Created with IntelliJ IDEA.
 * User: NINJIUNN
 * Date: 5/9/12
 * Time: 8:37 AM
 * To change this template use File | Settings | File Templates.
 */
public final class NivcgResourceUtils {
	private static Logger logger = Logger.getLogger(NivcgResourceUtils.class);

    private NivcgResourceUtils() {
    }
	
	/**
	 * Simple method that will return a resource property value given a
	 * resource property key.  If the resource property key or resource bundle is
	 * missing then an empty string is returned.  This will make resource handling
	 * null pointer exception safe.
	 * 
	 * @param resourcePropertyKey - Resource bundle property key
	 * @return String
	 */
	public static String getFromResourceBundle(String resourcePropertyKey) {
		if(logger.isDebugEnabled()) {
			logger.debug("Executing NivcgResourceUtils.getResourceBundle()");
		}

		ResourceBundle vcgConfig = null;
		
		try {
			vcgConfig = ResourceBundle.getBundle(DEFAULT_RESOURCE_BUNDLE);
			return(vcgConfig.getString(resourcePropertyKey).trim());
		} catch(MissingResourceException xMR) {
			if(vcgConfig != null) {
				logger.error("Missing resource property key: " + resourcePropertyKey, xMR);
			} else {
				logger.error("Missing resource bundle: " + DEFAULT_RESOURCE_BUNDLE, xMR);
			}
			return("");
		}
	}

    /**
     * Simple method that will return a resource property value given a
     * resource property key.  If the resource property key or resource bundle is
     * missing then an empty string is returned.  This will make resource handling
     * null pointer exception safe.
     *
     * @param resourcePropertyKey - Resource bundle property key
     * @return String[]
     */
	public static String[] getCSVFromResourceBundle(String resourcePropertyKey) {
		if(logger.isDebugEnabled()) {
			logger.debug("Executing NivcgResourceUtils.getCSVResourceBundle()");
		}
		
		ResourceBundle vcgConfig = null;
		
		try {
			vcgConfig = ResourceBundle.getBundle(DEFAULT_RESOURCE_BUNDLE);
			if(vcgConfig.getString(resourcePropertyKey).trim().contains(",".toLowerCase().trim())) {
				String[] csvArray = vcgConfig.getString(resourcePropertyKey).trim().split(",".toLowerCase().trim());
				Arrays.sort(csvArray);
				return(csvArray);
			} else {
			    logger.error("Resource property key is not in CSV format: " + resourcePropertyKey);
				return("".split(","));
			}
		} catch(MissingResourceException xMR) {
			if(vcgConfig != null) {
				logger.error("Missing resource property key: " + resourcePropertyKey, xMR);
			} else {
				logger.error("Missing resource bundle: " + DEFAULT_RESOURCE_BUNDLE, xMR);
			}
			return("".split(","));
		}
	}
}