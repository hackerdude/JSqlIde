package com.hackerdude.apps.sqlide.plugins.isql;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import java.io.*;
import com.hackerdude.apps.sqlide.components.*;
import com.hackerdude.apps.sqlide.*;

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
	private JEditorPane statusLog = new JEditorPane("text/html", "");
	private JScrollPane spStatus = new JScrollPane();

	public static final String PROPERTY_QUERY_REFRESH_REQUEST = "DATA_READ_REQUEST";

	public static final String PROPERTY_SQL_EXCEPTION_REPORTED = "SQL_EXCEPTION_OCURRED";

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
		statusLog.setText("<P>Ready.");
		splitTableAndLog.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pnlResultsPanel.setLayout(blBorderLayout);
		btnCommit.setMnemonic('C');
		pnlUpdateButtonBar.setLayout(gbGridBagLayout);
		btnInsert.setMnemonic('I');
		btnDelete.setMnemonic('D');
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

	public void clearResultSetModel() {
		setResultSetModel(new DefaultTableColumnModel(), new DefaultTableModel(0,0), null);
	}

	public void clearStatusText() {
		statusLog.setText("");
	}

	public void addStatusText(String text) {
		String newText = "<FONT COLOR=\"BLACK\">"+text + "</FONT>\n";
		_addText(newText);
	}

	public void addWarningText(String text) {

		String newWarning = "<FONT COLOR=\"RED\"><B>"+text+"</B></FONT>\n";
		_addText(newWarning);
	}

	public synchronized void _addText(String newText) {
		try {
			StringReader reader = new StringReader(newText);
			int position = statusLog.getDocument().getLength()-1;
			statusLog.getEditorKit().read(reader, statusLog.getDocument(), position);
			reader.close();
		}
		catch (Exception ex) {
		}

	}
	class DeleteRowAction extends AbstractAction {
		public DeleteRowAction() {
			super("Delete", ProgramIcons.getInstance().findIcon("images/DeleteRow.gif"));
		}

		public void actionPerformed(ActionEvent evt) {
			try {
				deleteRow(tblResults.getSelectedRow());
			}
			catch (SQLException ex) {
				logSQLException(ex);
			}

		}
	}

	class InsertRowAction extends AbstractAction {
		public InsertRowAction() {
			super("Insert", ProgramIcons.getInstance().findIcon("images/NewRow.gif"));
		}

		public void actionPerformed(ActionEvent evt) {
			if ( tblResults.getModel() instanceof ScrollableResultSetTableModel ) {
				prepareInsertRow();
			}
		}
	}

	class RollBackAction extends AbstractAction {
		public RollBackAction() {
			super("Rollback", ProgramIcons.getInstance().findIcon("images/DataQuery.gif"));
		}

		public void actionPerformed(ActionEvent evt) {
			try {
				rollBack();
				fireTableDataChanged();
			}
			catch (SQLException ex) {
				logSQLException(ex);
			}
		}
	}

	class CommitAction extends AbstractAction {
		public CommitAction() {
			super("Commit", ProgramIcons.getInstance().findIcon("images/Save.gif"));
		}
		public void actionPerformed(ActionEvent evt) {
			try {
				saveInsertions();
				resultSet.getStatement().getConnection().commit();
			}
			catch (SQLException ex) {
				firePropertyChange(PROPERTY_SQL_EXCEPTION_REPORTED, null, ex);
				addWarningText("Could Not Commit!");
				logSQLException(ex);
				try {
					//resultSet.getStatement().getConnection().rollback();
					rollBack();
					addWarningText("Edits have been rolled back.");
				} catch (SQLException ex1) {
					firePropertyChange(PROPERTY_SQL_EXCEPTION_REPORTED, null, ex1);
					addWarningText("Could not Roll Back!");
					logSQLException(ex);
				}
			}
		}
	}


	private void fireTableDataChanged() {
		((AbstractTableModel)tblResults.getModel()).fireTableDataChanged();
	}

	private void deleteRow(int row) throws SQLException {
		((ScrollableResultSetTableModel)tblResults.getModel()).deleteRow(row);
	}


	private void prepareInsertRow() {
		((ScrollableResultSetTableModel)tblResults.getModel()).prepareInsertRow();
	}

	private void saveInsertions() throws SQLException {
		((ScrollableResultSetTableModel)tblResults.getModel()).saveInsertions();
		tblResults.getSelectionModel().setSelectionInterval(tblResults.getRowCount()-1,tblResults.getRowCount()-1 );
	}

	private void rollBack() throws SQLException {
		((ScrollableResultSetTableModel)tblResults.getModel()).rollBack();
		firePropertyChange(PROPERTY_QUERY_REFRESH_REQUEST, false, true);
	}

	public void logSQLException(SQLException exc) {
		StringBuffer buffer = new StringBuffer();
		String message = exc.getMessage();
		buffer.append(message);
		SQLException nextException = exc;
		while ( ( nextException = nextException.getNextException() ) != null ) {
			buffer.append("\n").append(nextException.getMessage());
		}
		addWarningText(buffer.toString());
	}


}