package com.hackerdude.apps.sqlide.plugins.usrmgr;

import java.util.*;
import java.io.*;

/**
 * This is a command factory that maintains a list of usermanager commands
 * for different supported drivers.
 */
public class UsrMgmtCommandFactory {

	private static final String PROPERTIES_SUFFIX = ".properties";
	private static final String COMMANDS_FILE_PREFIX = "usermanager.commands.";

	private static final String PROP_SUPPORTED_DRIVERS = "usermanager.supporteddrivers.properties";

	private static final HashMap USERMANAGER_COMMANDS = new HashMap();

	private static final Properties PROPS_SUPPORTED_DRIVERS = new Properties();

	static {
		try {
			InputStream stream = UsrMgmtCommandFactory.class.getResourceAsStream(PROP_SUPPORTED_DRIVERS);
			PROPS_SUPPORTED_DRIVERS.load(stream);
		} catch (IOException ex) {
		}
	}


	public static synchronized Properties getCommandsFor(String supportedDriver) {
		Properties props = get(supportedDriver);
		if ( props == null ) {
			props = read(supportedDriver);
		}
		return props;
	}

	private static synchronized Properties get(String supportedDriver) {
		Properties result = (Properties)USERMANAGER_COMMANDS.get(supportedDriver);
		return result;
	}

	private static synchronized Properties read(String supportedDriver) {
		Properties result = new Properties();
		USERMANAGER_COMMANDS.put(supportedDriver,result);
		try {
			InputStream stream = UsrMgmtCommandFactory.class.getResourceAsStream(COMMANDS_FILE_PREFIX + supportedDriver + PROPERTIES_SUFFIX);
			if (stream != null) {
				result.load(stream);
			}
		} catch (IOException ex) {
		}
		return result;
	}

	public static String getSupportedDriver(String driverClassName) {
		return PROPS_SUPPORTED_DRIVERS.getProperty(driverClassName);
	}

}