package com.hackerdude.apps.sqlide.dialogs;

import java.awt.*;
import javax.swing.*;
import java.sql.*;

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
        jButton1.setText("OK");
        getContentPane().add(pnlMainPanel);
        pnlMainPanel.add(pnlClobEditor, BorderLayout.CENTER);
        pnlMainPanel.add(jPanel1,  BorderLayout.SOUTH);
        jPanel1.add(jButton1, null);
    }

	public void showClobEditor(Frame owner, String title, Clob clob) {
		ClobEditorDialog dialog = new ClobEditorDialog(owner, title,true);
		dialog.pnlClobEditor.setClob(clob);
		dialog.show();
	}
}

