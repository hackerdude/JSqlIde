package com.hackerdude.devtools.db.sqlide.plugins.isql;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class ResultSetPanel extends JPanel {
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel pnlBottom = new JPanel();
    private JLabel lblStatus = new JLabel();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JPanel jPanel2 = new JPanel();
    private JLabel lblStatement = new JLabel();
    private BorderLayout borderLayout4 = new BorderLayout();
    private JTable tblResults = new JTable();
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
        lblStatus.setText("Ready.");
        pnlBottom.setLayout(borderLayout2);
        lblStatement.setText("Statement");
        jPanel2.setLayout(borderLayout4);
        this.add(pnlBottom, BorderLayout.SOUTH);
        pnlBottom.add(lblStatus,  BorderLayout.WEST);
        this.add(resultScroll, BorderLayout.CENTER);
        this.add(jPanel2, BorderLayout.NORTH);
        jPanel2.add(lblStatement, BorderLayout.CENTER);
    }


	public void setResultSetModel(TableColumnModel newColumnModel, TableModel newTableModel) {
		tblResults.setModel(newTableModel);
		tblResults.setColumnModel(newColumnModel);
		tblResults.updateUI();
		resultScroll.validate();

	}

	public void setStatusText(String text) {
		lblStatus.setText(text);
	}


}