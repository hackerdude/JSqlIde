package com.hackerdude.devtools.db.sqlide.plugins.movedata;

import java.awt.*;
import javax.swing.*;
import com.hackerdude.devtools.db.sqlide.ProgramConfig;
import textarea.JEditTextArea;
import textarea.syntax.HTMLTokenMarker;

/**
 * This is a source view for editing the Data Mover instructions.
 */
public class InstructionSourceEditor extends JPanel {
	BorderLayout borderLayout1 = new BorderLayout();
	JEditTextArea editor = new JEditTextArea();

	public InstructionSourceEditor() {
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	private void jbInit() throws Exception {
		editor.setTokenMarker(new HTMLTokenMarker(false));
		editor.setText("<MOVEDATA>\n</MOVEDATA>\n<COPYDATA>\n</COPYDATA>");
		this.setLayout(borderLayout1);
		this.add(editor, BorderLayout.CENTER);

	}


}