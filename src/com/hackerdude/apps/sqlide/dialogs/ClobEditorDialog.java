package com.hackerdude.apps.sqlide.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import com.hackerdude.swing.SwingUtils;
import com.hackerdude.apps.sqlide.SqlIdeApplication;

public class ClobEditorDialog extends JDialog {
	private JPanel pnlMainPanel = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private ClobEditorPanel pnlClobEditor = new ClobEditorPanel();
	private JPanel jPanel1 = new JPanel();
	private JButton jButton1 = new JButton();

	public ClobEditorDialog(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			jbInit();
			pack();
			jButton1.setAction(
					new AbstractAction() {
				public void actionPerformed(ActionEvent evt) {
					hide();
				}
			}
			);
			jButton1.setText("Close");

		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public ClobEditorDialog() {
		this(null, "", false);
	}
	private void jbInit() throws Exception {
		pnlMainPanel.setLayout(borderLayout1);
		jButton1.setText("Close");
		getContentPane().add(pnlMainPanel);
		pnlMainPanel.add(pnlClobEditor, BorderLayout.CENTER);
		pnlMainPanel.add(jPanel1,  BorderLayout.SOUTH);
		jPanel1.add(jButton1, null);
	}

	public void showClobEditor(Frame owner, String title, String fieldName, Clob clob) {
		ClobEditorDialog dialog = new ClobEditorDialog(owner, title, true);
		dialog.pnlClobEditor.setClob(fieldName, clob);
		Dimension appDimension = SqlIdeApplication.getInstance().getFrame().getSize();

		dialog.setSize((int)appDimension.getWidth()/2, (int)appDimension.getHeight() / 2);
		Point center = SwingUtils.getCenteredWindowPoint(this);
		dialog.setLocation(center);
		dialog.show();
	}
}