package com.hackerdude.apps.sqlide.plugins.isql;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

/**
 * The panel with query results.
 * @copyright (C) 1998-2002 Hackerdude (David Martinez). All Rights Reserved.
 * @author David Martinez
 * @version 1.0
 */
public class ResultSetPanel extends JPanel {
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel pnlTitle = new JPanel();
	private JLabel lblStatement = new JLabel();
	private BorderLayout borderLayout4 = new BorderLayout();
	public JTable tblResults = new JTable();
	private JSplitPane splitTableAndLog = new JSplitPane();
	private JEditorPane statusLog = new JEditorPane();
	private JScrollPane spStatus = new JScrollPane();

	public final Action ACTION_COMMIT = new CommitAction();
	public final Action ACTION_ROLLBACK = new RollBackAction();
	public final Action ACTION_DELETE   = new DeleteRowAction();
	public final Action ACTION_INSERT   = new InsertRowAction();


	Action viewClobAction;
	JPanel pnlResultsPanel = new JPanel();
	BorderLayout blBorderLayout = new BorderLayout();
	JScrollPane resultScroll = new JScrollPane();
	JPanel pnlUpdateButtonBar = new JPanel();
	GridBagLayout gbGridBagLayout = new GridBagLayout();
	JButton btnCommit = new JButton(ACTION_COMMIT);
	JButton btnInsert = new JButton(ACTION_INSERT);
	JButton btnDelete = new JButton(ACTION_DELETE);
	JButton btnRollback = new JButton(ACTION_ROLLBACK);

	ResultSet resultSet;

	public ResultSetPanel() {
		try {
			jbInit();
			pnlUpdateButtonBar.setVisible(false);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setViewClobAction(Action action) {
		viewClobAction = action;
	}

	void jbInit() throws Exception {
		tblResults.setToolTipText("This is the result table.");
		tblResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setLayout(borderLayout1);
		lblStatement.setText("Statement");
		pnlTitle.setLayout(borderLayout4);
		statusLog.setToolTipText("");
		statusLog.setText("Ready.");
		splitTableAndLog.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pnlResultsPanel.setLayout(blBorderLayout);
		btnCommit.setMnemonic('C');
		btnCommit.setText("Commit");
		pnlUpdateButtonBar.setLayout(gbGridBagLayout);
		btnInsert.setMnemonic('I');
		btnInsert.setText("Insert");
		btnDelete.setMnemonic('D');
		btnDelete.setText("Delete");
		btnRollback.setText("Rollback");
    spStatus.getViewport().add(statusLog);
		this.add(pnlTitle, BorderLayout.NORTH);
		pnlTitle.add(lblStatement, BorderLayout.CENTER);
		splitTableAndLog.add(spStatus, JSplitPane.BOTTOM);
		splitTableAndLog.add(pnlResultsPanel, JSplitPane.LEFT);
		this.add(splitTableAndLog, BorderLayout.CENTER);
		pnlResultsPanel.add(resultScroll, BorderLayout.CENTER);
		pnlResultsPanel.add(pnlUpdateButtonBar, BorderLayout.EAST);
		pnlUpdateButtonBar.add(btnDelete,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		pnlUpdateButtonBar.add(btnInsert,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    pnlUpdateButtonBar.add(btnCommit,          new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(20, 5, 0, 5), 0, 0));
    pnlUpdateButtonBar.add(btnRollback, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		tblResults.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					viewClobAction.actionPerformed(null);
				}
			}

		});
		resultScroll.getViewport().add(tblResults);
		statusLog.setPreferredSize(new Dimension(getWidth(), 80));
		statusLog.setMinimumSize(new Dimension(getWidth(), 80));
		splitTableAndLog.setDividerLocation(-80);

	}

	public void setResultSetModel(TableColumnModel newColumnModel, TableModel newTableModel, ResultSet resultSet) {
		tblResults.setModel(newTableModel);
		tblResults.setColumnModel(newColumnModel);
		this.resultSet = resultSet;
		tblResults.updateUI();
		resultScroll.validate();
		try {
			pnlUpdateButtonBar.setVisible( resultSet.getConcurrency() == ResultSet.CONCUR_UPDATABLE );

		}
		catch (Exception ex) {

		}

	}

	public void clearStatusText() {
		statusLog.setText("");
	}

	public void addStatusText(String text) {
		String newText = statusLog.getText() + "\n" + text;
		statusLog.setText(newText);
	}

	public void addWarningText(String text) {
		StringBuffer warningText = new StringBuffer(statusLog.getText());
		int firstSelIX = warningText.length();
		int lastSelIX = firstSelIX + text.length() + 1;
		warningText.append('\n').append(text);
		statusLog.setText(warningText.toString());
		statusLog.select(firstSelIX, lastSelIX);
		statusLog.setSelectionColor(Color.red);
	}

	class DeleteRowAction extends AbstractAction {
		public void actionPerformed(ActionEvent evt) {
			try {
				resultSet.deleteRow();
			}
			catch (Exception ex) {
			}

		}
	}

	class InsertRowAction extends AbstractAction {
		public void actionPerformed(ActionEvent evt) {
			try {
				resultSet.insertRow();
			}
			catch (Exception ex) {
			}

		}
	}

	class RollBackAction extends AbstractAction {
		public void actionPerformed(ActionEvent evt) {
			try {
				resultSet.getStatement().getConnection().rollback();
			}
			catch (Exception ex) {
			}
		}
	}

	class CommitAction extends AbstractAction {
		public void actionPerformed(ActionEvent evt) {
			try {
				resultSet.getStatement().getConnection().commit();
			}
			catch (Exception ex) {
			}
		}
	}

}