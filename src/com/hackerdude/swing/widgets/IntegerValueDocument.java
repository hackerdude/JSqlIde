package com.hackerdude.swing.widgets;

import javax.swing.text.*;

/**
 * A document which only allows an integer value
 *
 * @author David Martinez
 * @version 1.0
 */
public class IntegerValueDocument extends PlainDocument {

	public static final boolean DEBUG = false;

    public IntegerValueDocument() {
		super();
    }

    public void insertString(int offs, String str, AttributeSet a) throws javax.swing.text.BadLocationException {
		try {
			String pre = getText(0, offs);
			String post = "";
			int length = getLength();
			int howMany = length-offs;
			if ( offs<length)
				post =getText(offs, length-offs);
			String candidate = pre+str+post;
			if ( DEBUG ) System.out.println("String would be: "+candidate);
			if ( ! candidate.equals("") && ! candidate.equals("-") ) {
				int testInt = Integer.parseInt(candidate);
			}
			super.insertString( offs,  str,  a);
		} catch ( NumberFormatException exc ) {
		    if ( DEBUG ) exc.printStackTrace();
			java.awt.Toolkit.getDefaultToolkit().beep();
		}
    }
}