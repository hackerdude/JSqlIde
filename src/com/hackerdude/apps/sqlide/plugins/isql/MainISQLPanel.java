package com.hackerdude.apps.sqlide.plugins.isql;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.hackerdude.apps.sqlide.ProgramConfig;
import com.hackerdude.apps.sqlide.ProgramIcons;
import com.hackerdude.apps.sqlide.SqlIdeApplication;
import com.hackerdude.apps.sqlide.components.ResultSetColumnModel;
import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.dataaccess.QueryResults;
import com.hackerdude.apps.sqlide.dialogs.ClobEditorDialog;
import com.hackerdude.apps.sqlide.plugins.SyntaxTextArea;
import com.hackerdude.apps.sqlide.plugins.SyntaxTextAreaFactory;
import com.hackerdude.apps.sqlide.xml.HostConfigFactory;
import com.hackerdude.apps.sqlide.xml.hostconfig.QueryHistory;
import com.hackerdude.swing.table.ModalButtonCellEditor;

/**
 * The main Interactive SQL Panel.
 * 
 * @author David Martinez
 * @version 1.0
 */
public class MainISQLPanel extends JPanel {

	public final Action ACTION_RUN_COMMAND = new ActionCommandRunner();
	public final Action ACTION_VIEW_CLOB = new ActionViewText();

	public final Action ACTION_HISTORY_PREV = new HistoryPreviousAction();
	public final Action ACTION_HISTORY_NEXT = new HistoryNextAction();

	private DatabaseProcess ideprocess;
	final DBChangeListener cbListener = new DBChangeListener();

	private ResultSetPanel resultSetPanel = new ResultSetPanel();
	private SyntaxTextArea sqlTextArea = SyntaxTextAreaFactory.createTextArea();
	ModalButtonCellEditor buttonCellEditor = new ModalButtonCellEditor(ACTION_VIEW_CLOB);
	private BorderLayout blMainLayout = new BorderLayout();
	private JPanel pnlBottomPanel = new JPanel();
	private JPanel pnlTopPanel = new JPanel();
	private JPanel pnlCenter = new JPanel();
	private JSplitPane jSplitPane1 = new JSplitPane();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JLabel lblCatalog = new JLabel();
	private JComboBox cbCatalogs = new JComboBox();
	private JButton btnGo = new JButton(ACTION_RUN_COMMAND);
	private JCheckBox cbAsUpdate = new JCheckBox();

	private int historyBackCount = 0;
	private JCheckBox cbUpdatable = new JCheckBox();

	public final PropertyChangeListener QUERY_REFRESH_LISTENER = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			executeCurrentQuery();
		}
	};

	public final PropertyChangeListener SQL_EXCEPTION_LISTENER = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			SQLException exception = (SQLException)evt.getNewValue();
			resultSetPanel.logSQLException(exception);
			resultSetPanel.addStatusText("Please correct and try again.");
		}
	};

	public MainISQLPanel() {
		try {
			jbInit();
			cbCatalogs.addActionListener(cbListener);
			resultSetPanel.setViewClobAction(ACTION_VIEW_CLOB);
			InputMap inputMap = sqlTextArea.getTextArea().getInputMap();
			ActionMap actionMap = sqlTextArea.getTextArea().getActionMap();
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_MASK), "historyPrev");
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_MASK), "historyNext");
			actionMap.put("historyPrev", ACTION_HISTORY_PREV);
			actionMap.put("historyNext", ACTION_HISTORY_NEXT);
			resultSetPanel.addPropertyChangeListener(ResultSetPanel.PROPERTY_QUERY_REFRESH_REQUEST, QUERY_REFRESH_LISTENER);
			resultSetPanel.addPropertyChangeListener(ResultSetPanel.PROPERTY_SQL_EXCEPTION_REPORTED, SQL_EXCEPTION_LISTENER);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {

		sqlTextArea.setMinimumSize(new Dimension(200, 150));
		this.setLayout(blMainLayout);
		pnlCenter.setLayout(borderLayout1);
		jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		lblCatalog.setText("Catalog:");
		btnGo.setText("Go!");
		cbAsUpdate.setText("As Update");
		cbAsUpdate.setMnemonic('U');
		cbUpdatable.setActionCommand("updatable");
		cbUpdatable.setText("Updatable");
		this.add(pnlBottomPanel, BorderLayout.SOUTH);
		this.add(pnlTopPanel, BorderLayout.NORTH);
		pnlTopPanel.add(lblCatalog, null);
		pnlTopPanel.add(cbCatalogs, null);
		pnlTopPanel.add(btnGo, null);
		pnlTopPanel.add(cbAsUpdate, null);
		pnlTopPanel.add(cbUpdatable, null);
		this.add(pnlCenter, BorderLayout.CENTER);
		pnlCenter.add(jSplitPane1, BorderLayout.CENTER);
		jSplitPane1.add(sqlTextArea, JSplitPane.TOP);
		jSplitPane1.add(resultSetPanel, JSplitPane.BOTTOM);
		jSplitPane1.setDividerLocation(100);
	}

	/**
	 * This executes the current query for this interactive SQL Window.
	 */
	public void executeCurrentQuery() {

		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		resultSetPanel.addStatusText("Executing Query. Please Wait...");
		final String queryText = sqlTextArea.getText();
		Object db = cbCatalogs.getSelectedItem();
		if (db != null) ideprocess.changeCatalog(db.toString());
		final boolean asUpdate = cbAsUpdate.isSelected();
		final boolean updatable = cbUpdatable.isSelected();
		ACTION_RUN_COMMAND.setEnabled(false);
		new Thread() {
			public void run() {
				try {
					final QueryResults queryResults = ideprocess.runQuery(queryText, asUpdate, updatable);
					sqlTextArea.setText(ideprocess.lastQuery);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							addToHistoryIfNeeded(queryText);
							displayResults(asUpdate, updatable, queryResults);
						}
					});

				} catch (final SQLException exc) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							resultSetPanel.logSQLException(exc);
							resultSetPanel.clearResultSetModel();
						}
					});
					//JOptionPane.showMessageDialog(this, "SQL Exception: " + exc, "SQL Exception", JOptionPane.ERROR_MESSAGE);.
				} finally {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							setCursor(Cursor.getDefaultCursor());
							ACTION_RUN_COMMAND.setEnabled(true);
						}
					});
				}
			}
		}.start();
	}

	/**
	 * This function displays the QueryResults in the results table.
	 * If it cannot do it, it clears the current results and logs a message.
	 * @param asUpdate True if this was an update.
	 * @param updatable True if the query was updatable.
	 * @param queryResults The results of the query.
	 */
	private void displayResults(boolean asUpdate, boolean updatable, QueryResults queryResults)  {
		try {
			if (queryResults != null && queryResults.getResultSet() != null) {
				Font theFont = ProgramConfig.getInstance().getResultSetFont();
				ResultSetColumnModel newColumnModel = new ResultSetColumnModel(
					  queryResults, theFont, this);
				setDefaultEditors(ideprocess.getTableModel(), newColumnModel);
				TableModel tableModel = ideprocess.getTableModel();
				ResultSet resultSet = queryResults.getResultSet();
				updatable = ! (resultSet.getConcurrency() == ResultSet.CONCUR_READ_ONLY);
				resultSetPanel.setResultSetModel(newColumnModel, tableModel, resultSet);
				setClobEditors(queryResults, newColumnModel);
			}
			String message = "Ran " + (asUpdate ? " update" : "query ") + (updatable ? " (<B>updatable result obtained</B>)" : "");
			if (queryResults != null)
				message = message + ". Elapsed time " + formatElapsedTime(queryResults.getElapsedTime());
			resultSetPanel.addStatusText(message);
			if (queryResults != null)
				if (asUpdate)
					resultSetPanel.addStatusText("Rows updated: " + queryResults.getRowsAffected());
		} catch (SQLException exc) {
			resultSetPanel.logSQLException(exc);
			resultSetPanel.clearResultSetModel();
		}
	}


	public String formatElapsedTime(long elapsedTime) {
		long millis = elapsedTime;
		millis = millis>1000?elapsedTime % 1000:millis;
		long seconds = millis / 1000;
		seconds = seconds > 60 ? seconds % 60 : seconds;
		long minutes = seconds / 60;
		Object[] PARAMS = {
			  new Long(minutes), new Long(seconds), new Long(millis)
		};

		String format = "{2} msec";
		if ( seconds > 0 ) format = "{1} seconds "+format;
		if ( minutes > 0 ) format = "{0} minutes"+format;
		return MessageFormat.format(format, PARAMS);
	}

	private void addToHistoryIfNeeded(String query) {
		// Add to the history if the last query is different than the current one.
		QueryHistory queryHistory = ideprocess.getHostConfiguration().getQueryHistory();
		if (queryHistory == null) {
			queryHistory = new QueryHistory();
			ideprocess.getHostConfiguration().setQueryHistory(queryHistory);
		}
		String lastQueryInHistory = queryHistory.getHistoryItemCount() < 1 ? "" : queryHistory.getHistoryItem(queryHistory.getHistoryItemCount() - 1);
		if (!lastQueryInHistory.equalsIgnoreCase(query))
			queryHistory.addHistoryItem(query);
		try {
			HostConfigFactory.saveSqlideHostConfig(ideprocess.getHostConfiguration());
		} catch (IOException ex) {
		}

	}

	private void setClobEditors(QueryResults queryResults,
								ResultSetColumnModel columnModel) {

		for (int i = 0; i < queryResults.getColumnSQLTypes().length; i++) {
			if ( queryResults.getColumnSQLTypes()[i] == java.sql.Types.CLOB ||
				 queryResults.getColumnSQLTypes()[i] == java.sql.Types.BLOB) {
				columnModel.getColumn(i).setCellEditor(buttonCellEditor);
			}
		}

	}

/**
	 * This action runs the current command.
	 */
	class ActionCommandRunner extends AbstractAction {
		public ActionCommandRunner() {
			super("Go!", ProgramIcons.getInstance().findIcon("images/VCRPlay.gif"));
			putValue(ACCELERATOR_KEY,
					 KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Event.CTRL_MASK, false));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_G));
		}

		public void actionPerformed(ActionEvent evt) {
			executeCurrentQuery();
		}
	}

	public void setQueryText(String queryText) {
		sqlTextArea.setText(queryText);
	}

	public String getQueryText() {
		return sqlTextArea.getText();
	}

	public void cut() {
		sqlTextArea.cut();
	}

	public void copy() {
		sqlTextArea.copy();

	}

	public void paste() {
		sqlTextArea.paste();
	}

	public void setDatabaseProcess(DatabaseProcess proc) {
		ideprocess = proc;
		try {
			ArrayList dbs = ideprocess.getCatalogs();
			for (int i = 0; i < dbs.size(); i++) {
				cbCatalogs.addItem(dbs.get(i));
			}
			lblCatalog.setVisible(cbCatalogs.getItemCount()>0);
			cbCatalogs.setVisible(cbCatalogs.getItemCount()>0);
		} catch (SQLException exc) {
		}

	}

	public void refreshFromConfig() {
		sqlTextArea.fireConfigurationChanged();
	}

	class DBChangeListener
		  implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String actionString = e.getActionCommand();
			if (ideprocess != null && actionString.equals("comboBoxChanged"))
				ideprocess.changeCatalog(cbCatalogs.getSelectedItem().toString());
		}
	}

	public void selectCatalog(String catalogName) {
		cbCatalogs.setSelectedItem(catalogName);
	}

	public JPanel getExceptionPanel(String msg, Exception exc) {
		String exceptionString = exc.toString();
		String exceptionText = getExceptionText(exc);
		JPanel panel = new JPanel(new BorderLayout());
		JLabel message = new JLabel("<HTML>" + exceptionString + "<P><PRE>" +
									exceptionText + "</PRE>",
									ProgramIcons.
									getInstance().findIcon("images/Error.gif"),
									JLabel.CENTER);
		JLabel label = new JLabel();
		JLabel lbl = new JLabel(msg,
								ProgramIcons.getInstance().findIcon(
			  "images/Error.gif"),
								JLabel.CENTER);
		panel.add(lbl, BorderLayout.NORTH);
		panel.add(message, BorderLayout.CENTER);
		return panel;
	}

	public String getExceptionText(Exception exc) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8192);
		PrintWriter pw = new PrintWriter(bos) {
			public void println(String item) {
				super.println("<P>" + item);

			}
		};
		exc.printStackTrace(pw);
		String exception = new String(bos.toByteArray());
		return exception;
	}

	public void setDefaultEditors(TableModel model, TableColumnModel columnModel) {
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			TableColumn column = columnModel.getColumn(i);
//			System.out.println(column);
//			System.out.println(model.getColumnClass(i));

			if (java.sql.Clob.class.isAssignableFrom(model.getColumnClass(i))) {
				column.setCellRenderer(new ClobCellRenderer());
				/** @todo Come up with a cell editor using the Clob editor dialog. */
				// column.setCellEditor(new ClobCellEditor());
			}
		}
	}

	class ClobCellRenderer
		  implements TableCellRenderer {
		JButton viewButton = new JButton("Text",
										 ProgramIcons.getInstance().getServerIcon()); //, JLabel.LEFT);

		public ClobCellRenderer() {
			super();
			viewButton.setAction(ACTION_VIEW_CLOB);
		}

		public Component getTableCellRendererComponent(JTable table,
			  Object value,
			  boolean isSelected,
			  boolean hasFocus,
			  int row,
			  int column) {
			return viewButton;
		}

	}

	class ActionViewText extends AbstractAction {
		ClobEditorDialog editorDialog = new ClobEditorDialog();
		public ActionViewText() {
			super("(Large) ", ProgramIcons.getInstance().getServerIcon());
		}

		public void actionPerformed(ActionEvent evt) {
			int column = resultSetPanel.tblResults.getSelectedColumn();
			int row = resultSetPanel.tblResults.getSelectedRow();
			Object object = resultSetPanel.tblResults.getModel().getValueAt(row,
				  column);
			String fieldName = resultSetPanel.tblResults.getModel().getColumnName(
				  column);
			if (object instanceof Clob) {
				Clob clob = (Clob) object;
				editorDialog.showClobEditor(SqlIdeApplication.getFrame(),
											"Clob View for " + fieldName, fieldName,
											clob);
			} else if (object instanceof Blob) {
				Blob blob = (Blob) object;
				editorDialog.showBlobEditor(SqlIdeApplication.getFrame(),
											"Blob View for " + fieldName, fieldName,
											blob);

			}

		}
	}

	class HistoryPreviousAction extends AbstractAction {
		public void actionPerformed(ActionEvent evt) {
			QueryHistory queryHistory = ideprocess.getHostConfiguration().getQueryHistory();
			if (queryHistory == null)
				return;
			int historyIndex = queryHistory.getHistoryItemCount() - 1 + historyBackCount--;
			if (historyIndex > queryHistory.getHistoryItemCount() - 1)
				return;
			if (historyIndex < 0) {
				historyBackCount = -1; return;
			}
			String historyText = queryHistory.getHistoryItem(historyIndex);
			setQueryText(historyText);
		}
	}

	class HistoryNextAction extends AbstractAction {
		public void actionPerformed(ActionEvent evt) {
			QueryHistory queryHistory = ideprocess.getHostConfiguration().getQueryHistory();
			if (queryHistory == null)
				return;
			int historyIndex = queryHistory.getHistoryItemCount() + historyBackCount++;
			if (historyIndex > queryHistory.getHistoryItemCount() - 1) {
				historyBackCount = 0;
				setQueryText("");
				return;
			}
			String historyText = queryHistory.getHistoryItem(historyIndex);
			setQueryText(historyText);
		}
	}

}