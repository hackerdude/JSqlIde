package com.hackerdude.apps.sqlide.plugins.isql;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

public class ResultSetPanel extends JPanel {
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel jPanel2 = new JPanel();
    private JLabel lblStatement = new JLabel();
    private BorderLayout borderLayout4 = new BorderLayout();
    private JTable tblResults = new JTable();
    private JSplitPane splitTableAndLog = new JSplitPane();
    private JEditorPane statusLog = new JEditorPane();
    private JScrollPane spStatus = new JScrollPane();
    private JScrollPane resultScroll = new JScrollPane(tblResults);

    public ResultSetPanel() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
		tblResults.setToolTipText("This is the result table.");
        tblResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setLayout(borderLayout1);
        lblStatement.setText("Statement");
        jPanel2.setLayout(borderLayout4);
        statusLog.setToolTipText("");
        statusLog.setText("Ready.");
		spStatus.getViewport().add(statusLog);
        this.add(jPanel2,  BorderLayout.NORTH);
        jPanel2.add(lblStatement, BorderLayout.CENTER);
		splitTableAndLog.setOrientation( JSplitPane.VERTICAL_SPLIT );
        splitTableAndLog.add(resultScroll, JSplitPane.TOP);
		splitTableAndLog.add(spStatus, JSplitPane.BOTTOM);
		this.add(splitTableAndLog,  BorderLayout.CENTER);
    }


	public void setResultSetModel(TableColumnModel newColumnModel, TableModel newTableModel) {
		tblResults.setModel(newTableModel);
		tblResults.setColumnModel(newColumnModel);
		tblResults.updateUI();
		resultScroll.validate();

	}

	public void clearStatusText() {
		statusLog.setText("");
	}

	public void addStatusText(String text) {
		String newText = statusLog.getText()+"\n"+text;
		statusLog.setText(newText);
	}

	public void addWarningText(String text) {
		StringBuffer warningText = new StringBuffer(statusLog.getText());
		int firstSelIX = warningText.length();
		int lastSelIX = firstSelIX + text.length()+1;
		warningText.append('\n').append(text);
		statusLog.setText(warningText.toString());
		statusLog.select(firstSelIX, lastSelIX);
		statusLog.setSelectionColor(Color.red);
	}



}
