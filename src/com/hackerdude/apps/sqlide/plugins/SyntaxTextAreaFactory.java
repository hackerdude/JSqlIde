package com.hackerdude.apps.sqlide.plugins;

import com.hackerdude.apps.sqlide.*;
import java.util.*;
import java.awt.*;

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