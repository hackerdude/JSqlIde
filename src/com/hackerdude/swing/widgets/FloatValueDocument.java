package com.hackerdude.swing.widgets;

import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;


/**
 * A document that only allows Floating point
 * values.
 *
 * It is useful for things such as price in a JTextField.
 *
 * @author David Martinez
 * @version $version$
 */
public class FloatValueDocument extends PlainDocument {

	public static final boolean DEBUG = false;

		/** @todo  Maybe we should have a constructor with number of decimals. */

	/**
	 * Creates a new FloatValueDocument
	 */
    public FloatValueDocument() {
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
			if ( ! candidate.equals("") && ! candidate.equals("-") && ! candidate.equals(".") ) {
				float testFloat = Float.parseFloat(candidate);
			}
			super.insertString( offs,  str,  a);
		} catch ( NumberFormatException exc ) {
		    if ( DEBUG ) exc.printStackTrace();
			java.awt.Toolkit.getDefaultToolkit().beep();
		}
    }


}