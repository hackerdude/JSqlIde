package com.hackerdude.apps.sqlide.pluginapi;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * This is an action that can be created with an accelerator key.
 */
public abstract class SqlIdeAction extends AbstractAction {

	public SqlIdeAction(String name, Icon image, KeyStroke keyStroke, int mnemonicKey) {
			super(name, image);
			putValue(MNEMONIC_KEY, new Integer(mnemonicKey));
			putValue(ACCELERATOR_KEY, keyStroke);
	}


}
