package com.hackerdude.apps.sqlide.plugins.isql;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.swing.table.*;

import javax.swing.*;
import javax.swing.table.*;

import com.hackerdude.apps.sqlide.*;
import com.hackerdude.apps.sqlide.components.*;
import com.hackerdude.apps.sqlide.dialogs.*;
import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.plugins.*;
import textarea.*;

/**
 * The main Interactive SQL Panel.
 * @copyright (C) 1998-2002 Hackerdude (David Martinez). All Rights Reserved.
 * @author David Martinez
 * @version 1.0
 */
public class MainISQLPanel extends JPanel {

	public final Action ACTION_RUN_COMMAND = new ActionCommandRunner();
	public final Action ACTION_VIEW_CLOB   = new ActionViewText();

	private DatabaseProcess ideprocess;
	final DBChangeListener cbListener = new DBChangeListener();

	private ResultSetPanel resultSetPanel = new ResultSetPanel();
	private SyntaxTextArea sqlTextArea    = new SyntaxTextArea(TextAreaDefaults.getDefaults());

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


    public MainISQLPanel() {
        try {
            jbInit();
			cbCatalogs.addActionListener(cbListener);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }


    void jbInit() throws Exception {
        this.setLayout(blMainLayout);
        pnlCenter.setLayout(borderLayout1);
        jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        lblCatalog.setText("Catalog:");
        btnGo.setText("Go!");
        cbAsUpdate.setText("As Update");
		cbAsUpdate.setMnemonic('U');
        this.add(pnlBottomPanel, BorderLayout.SOUTH);
        this.add(pnlTopPanel, BorderLayout.NORTH);
        pnlTopPanel.add(lblCatalog, null);
        pnlTopPanel.add(cbCatalogs, null);
        pnlTopPanel.add(btnGo, null);
        pnlTopPanel.add(cbAsUpdate, null);
        this.add(pnlCenter, BorderLayout.CENTER);
        pnlCenter.add(jSplitPane1,  BorderLayout.CENTER);
		jSplitPane1.add(sqlTextArea, JSplitPane.TOP);
		jSplitPane1.add(resultSetPanel, JSplitPane.BOTTOM);
        jSplitPane1.setDividerLocation(100);
    }

	/**
	 * This executes the current query for this interactive SQL Window.
	 */
	public void executeCurrentQuery() {
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		resultSetPanel.addStatusText( "Executing Query..." );
		String queryText = sqlTextArea.getText();
//		cbHistory.addItem(new SQLHistoryItem(queryText));
		Object db = cbCatalogs.getSelectedItem();
		if ( db != null ) ideprocess.changeCatalog( db.toString() );
		try {
			boolean asUpdate = cbAsUpdate.isSelected();
			QueryResults queryResults = ideprocess.runQuery(queryText, asUpdate, false, false );
			sqlTextArea.setText(ideprocess.lastQuery);
			Font theFont = ProgramConfig.getInstance().getResultSetFont();
			ResultSetColumnModel newColumnModel = new ResultSetColumnModel(queryResults, theFont, this);
			setDefaultEditors(ideprocess.getTableModel(), newColumnModel);
			TableModel tableModel = ideprocess.getTableModel();
			resultSetPanel.setResultSetModel(newColumnModel,tableModel);
			resultSetPanel.addWarningText( "Ran Query." );
		} catch ( SQLException exc ) {
			JOptionPane.showMessageDialog(this, "SQL Exception: "+exc, "SQL Exception", JOptionPane.ERROR_MESSAGE);
		} finally {
			setCursor(Cursor.getDefaultCursor());
		}
	}

	class ActionCommandRunner extends AbstractAction {
		public ActionCommandRunner() {
			super("Go!", ProgramIcons.getInstance().findIcon("images/VCRPlay.gif"));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Event.CTRL_MASK, false));
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
	public void setDatabaseProcess( DatabaseProcess proc ) {
		ideprocess = proc;
		try {
			ArrayList dbs = ideprocess.getCatalogs();
			for(int i=0;i<dbs.size();i++) {
				cbCatalogs.addItem(dbs.get(i));
			}
		} catch ( SQLException exc ) {
		}

	}


	public void refreshFromConfig() {
		Font theFont = ProgramConfig.getInstance().getSQLFont();
		sqlTextArea.setFont(theFont);
		sqlTextArea.getPainter().setFont(theFont);
//		if ( ( theResultTable != null ) && (resultSetFont != null)  ) {
//			theResultTable.setFont(resultSetFont);
//		}
	}


	class DBChangeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String actionString = e.getActionCommand();
			if ( ideprocess!= null && actionString.equals("comboBoxChanged") ) ideprocess.changeCatalog(cbCatalogs.getSelectedItem().toString() );
		}
	}

	public void selectCatalog(String catalogName) {
		cbCatalogs.setSelectedItem(catalogName);
	}
	public JPanel getExceptionPanel(String msg, Exception exc) {
		String exceptionString = exc.toString();
		String exceptionText = getExceptionText(exc);
		JPanel panel = new JPanel(new BorderLayout());
		JLabel message = new JLabel("<HTML>"+exceptionString+"<P><PRE>"+exceptionText+"</PRE>", ProgramIcons.getInstance().findIcon("images/Error.gif"), JLabel.CENTER);
		JLabel label = new JLabel();
		JLabel lbl = new JLabel(msg, ProgramIcons.getInstance().findIcon("images/Error.gif"), JLabel.CENTER);
		panel.add(lbl, BorderLayout.NORTH);
		panel.add(message, BorderLayout.CENTER);
		return panel;
	}

	public String getExceptionText(Exception exc ) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8192);
		PrintWriter pw = new PrintWriter(bos) {
			public void println(String item) {
				super.println("<P>"+item);

			}
		};
		exc.printStackTrace(pw);
		String exception = new String(bos.toByteArray());
		return exception;
	}

	public void setDefaultEditors(TableModel model, TableColumnModel columnModel) {
		for ( int i=0; i<columnModel.getColumnCount(); i++ ) {
			TableColumn column = columnModel.getColumn(i);
			System.out.println(column);
			System.out.println(model.getColumnClass(i));

			if ( java.sql.Clob.class.isAssignableFrom(model.getColumnClass(i)) ) {
				column.setCellRenderer(new ClobCellRenderer());
				/** @todo Come up with a cell editor using the Clob editor dialog. */
				// column.setCellEditor(new ClobCellEditor());
			}
		}
	}


	class ClobCellRenderer implements TableCellRenderer {
		JLabel editLabel = new JLabel("Text",ProgramIcons.getInstance().getServerIcon(), JLabel.LEFT);

		public ClobCellRenderer() {
			super();
			editLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					ACTION_VIEW_CLOB.actionPerformed(new ActionEvent(this,1, "") );
				}
			});
		}

		public Component getTableCellRendererComponent(JTable table,
													   Object value,
													   boolean isSelected,
													   boolean hasFocus,
													   int row,
                                               int column) {
			return editLabel;
		}

	}

	class ActionViewText extends AbstractAction {
		ClobEditorDialog editorDialog = new ClobEditorDialog();
		public ActionViewText() {
			super("(Text) ", ProgramIcons.getInstance().getServerIcon());
		}
		public void actionPerformed(ActionEvent evt) {

			editorDialog.showClobEditor(SqlIdeApplication.getFrame(), "Sample!",null );
		}
	}

}
