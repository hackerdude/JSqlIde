package com.hackerdude.apps.sqlide.dialogs;

import com.hackerdude.apps.sqlide.plugins.SyntaxTextArea;
import java.awt.*;
import java.io.*;
import textarea.TextAreaDefaults;
import textarea.syntax.TSQLTokenMarker;
import javax.swing.*;

/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
 * @author David Martinez
 * @version 1.0
 */

public class SQLFilePreviewPanel extends JPanel {

	public SyntaxTextArea sqlTextArea = new SyntaxTextArea(TextAreaDefaults.getDefaults());

	public final static int MAX_PREVIEW_LINES=10;

	BorderLayout borderLayout1 = new BorderLayout();
	JPanel jPanel1 = new JPanel();
	JLabel lblCurrentContents = new JLabel();

	public SQLFilePreviewPanel() {
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
	private void jbInit() throws Exception {
		this.setLayout(borderLayout1);
		lblCurrentContents.setText("Current Contents");
		this.add(sqlTextArea);
		this.add(jPanel1, BorderLayout.NORTH);
		jPanel1.add(lblCurrentContents, null);
		sqlTextArea.setEditable(false);
		sqlTextArea.setTokenMarker(new TSQLTokenMarker());
		sqlTextArea.setText("SQL Preview Window");
	}

	public static TextAreaDefaults getTextAreaDefaults() {
		TextAreaDefaults defaults = new TextAreaDefaults();
		defaults.rows=4;
		defaults.cols=30;
		return defaults;
	}

	public void setCurrentFile(File file) throws IOException {
		// Read the first 10 lines of the file.
		lblCurrentContents.setText("Current contents of "+file.getName());
		BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		StringBuffer sb = new StringBuffer();
		String line = buf.readLine();
		int lines = 1;
		while ( line!= null && lines < MAX_PREVIEW_LINES ) {
			sb.append(line).append("\n");
			line = buf.readLine();
		}
		if ( line != null ) sb.append("... continues...");
		sqlTextArea.setText(sb.toString());
	}

	public void clearContents() {
		sqlTextArea.setText("");
	}

}
