package com.hackerdude.devtools.db.sqlide.pluginapi;

import javax.swing.AbstractAction;

import javax.swing.Icon;
import javax.swing.event.*;
import javax.swing.KeyStroke;
import java.awt.event.*;

/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
 * @author David Martinez
 * @version 1.0
 */

public abstract class SqlIdeAction extends AbstractAction {

	public SqlIdeAction(String name, Icon image, KeyStroke keyStroke, int mnemonicKey) {
			super(name, image);
			putValue(MNEMONIC_KEY, new Integer(mnemonicKey));
			putValue(ACCELERATOR_KEY, keyStroke);

	}


}