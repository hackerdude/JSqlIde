package com.hackerdude.apps.sqlide.plugins.pgsql.storedproc;

import java.awt.*;
import textarea.*;
import javax.swing.*;
import com.hackerdude.apps.sqlide.plugins.*;

/**
 * Stored Procedure Editor Panel.
 */
public class EditorPanel extends JPanel {

	BorderLayout borderLayout1 = new BorderLayout();
	SyntaxTextArea area = SyntaxTextAreaFactory.createTextArea();
	JPanel pnlInformation = new JPanel();
	JLabel lblStatus = new JLabel();
	BorderLayout borderLayout2 = new BorderLayout();

	public EditorPanel() {
	try {
		jbInit();
	}
	catch(Exception ex) {
		ex.printStackTrace();
	}
	}
	void jbInit() throws Exception {
		this.setLayout(borderLayout1);
		lblStatus.setText("Ready.");
		pnlInformation.setLayout(borderLayout2);
		add(area, BorderLayout.CENTER);
		this.add(pnlInformation,  BorderLayout.SOUTH);
		pnlInformation.add(lblStatus, BorderLayout.CENTER);
	}
}