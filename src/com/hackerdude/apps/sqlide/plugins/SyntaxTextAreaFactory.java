package com.hackerdude.apps.sqlide.plugins;

import java.util.Properties;

import com.hackerdude.apps.sqlide.ProgramConfig;

/**
 * A factory class for SQLIDE text areas.
 */
public class SyntaxTextAreaFactory {

	public static SyntaxTextArea createTextArea() {
		return createTextArea(ProgramConfig.getInstance().getUserInterfaceProperties());
	}

	public static SyntaxTextArea createTextArea(Properties configuration) {
		SyntaxTextArea result = new SyntaxTextArea(configuration);
		return result;
	}

}