package com.hackerdude.swing.widgets;

import javax.swing.text.*;

/**
 * A document that enforces a maximum character length.
 *
 * You can associate it with any java component that uses the Document interface
 * (such as JTextField or JTextArea).
 *
 * <P>It enforces a maximum length and beeps if the length of the
 * field will be longer than the configured length, disallowing the insert.
 * @author David Martinez
 * @version 1.0
 */
public class LengthLimitedDocument extends PlainDocument {

	public static final boolean DEBUG = false;

	private int maxLength;

	public LengthLimitedDocument(int maxLength) {
		super();
		this.maxLength = maxLength;
    }

	public void insertString(int offs, String str, AttributeSet a) throws javax.swing.text.BadLocationException {
			String pre = getText(0, offs);
			String post = "";
			int length = getLength();
			int howMany = length-offs;
			if ( offs<length)
				post =getText(offs, length-offs);
			String candidate = pre+str+post;
			if ( DEBUG ) System.out.println("String would be: "+candidate);
			if ( candidate.length() > maxLength ) {
				java.awt.Toolkit.getDefaultToolkit().beep();
				return;
			}
			super.insertString( offs,  str,  a);
    }
}