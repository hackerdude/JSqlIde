/*
 * PanelTableEditor.java - A table generator/modifier.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Revision: $Revisfion: 1.3 $
 * Id: $Id$
 *
 */
package com.hackerdude.apps.sqlide.plugins.tableedit;

import com.hackerdude.apps.sqlide.pluginapi.*;
import com.hackerdude.apps.sqlide.*;
import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.lib.*;
import com.hackerdude.apps.sqlide.plugins.browser.*;
import com.hackerdude.apps.sqlide.plugins.browser.browsejdbc.*;
import com.hackerdude.swing.SwingUtils;
import java.sql.*;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;
import java.util.*;
import java.lang.Object.*;
import java.io.*;
import textarea.*;
import textarea.syntax.*;

/**
 * PanelTableEditor is a UI for the creation of tables
 * interactively. It can read in the available data types
 * and allow the user to visually compose a create table or
 * alter table statemet. It will have a "preview" window
 * that will allow the user to preview the SQL statement.
 *
 * @copyright (C) 1998-2002 Hackerdude (David Martinez). All Rights Reserved.
 * @author David Martinez
 * @version 1.0
 */
public class PluginTableEditor extends JPanel
	  implements IDEVisualPluginIF {

	static JInternalFrame frame;
	JFrame frm;
	ProgramConfig conf;
	DatabaseProcess ideprocess;
	Vector menuItems;
	JPanel topBar;
	JComboBox cbDatabases;
	ArrayList fields;
	JEditTextArea theStatement;
	JTextField fldTableName;
	FieldCollectionModel fieldCollection;
	TableColumnsEditor tbFields;
	JButton btnSQL;

	ArrayList originalFields = null;

	Action ACTION_INSERTFIELD = new ActionInsertField();
	Action ACTION_DELETEFIELD = new ActionDeleteField();
	Action ACTION_PREVIEW = new ActionPreview();

	Action ACTION_CREATETABLE = new ActionCreateTable();

/**
	 * Constructor.
	 */
	public PluginTableEditor() {
	}

	public void initPlugin() {
		conf = ProgramConfig.getInstance();
		menuItems = new Vector();
		theStatement = new JEditTextArea();
		fields = new ArrayList();
		jbInit();
	}

	public void freePlugin() {

	}

	public Icon getPluginIcon() {
		return ProgramIcons.getInstance().findIcon("images/NewColumn.gif");
	}

/**
	 * JBuilder is really nice to you in design-time if you have
	 * one of these.
	 */
	public void jbInit() {
		topBar = new JPanel();
		topBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel bottomBar = new JPanel();
		JLabel lblDatabases = new JLabel("Database:");
		cbDatabases = new JComboBox();
		JLabel lblTableName = new JLabel("Table Name:");
		fldTableName = new JTextField(15);
		fieldCollection = new FieldCollectionModel(fields, this);
		tbFields = new TableColumnsEditor(fieldCollection);
		fieldCollection.setupDataTypeEditor(tbFields);
		JScrollPane scPane = new JScrollPane(tbFields);

		tbFields.registerKeyboardAction(ACTION_INSERTFIELD, "InsField", KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), WHEN_FOCUSED);
		tbFields.registerKeyboardAction(ACTION_DELETEFIELD, "DelField", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), WHEN_FOCUSED);

		btnSQL = new JButton(ACTION_PREVIEW);

		lblTableName.setDisplayedMnemonic('N');
		lblTableName.setLabelFor(fldTableName);
		lblDatabases.setDisplayedMnemonic('D');
		lblDatabases.setLabelFor(cbDatabases);
		btnSQL.setMnemonic('P');
		btnSQL.setText("Preview/Create");
		topBar.add(lblDatabases);
		topBar.add(cbDatabases);
		topBar.add(lblTableName);
		topBar.add(fldTableName);

		bottomBar.add(btnSQL);

		setLayout(new BorderLayout());
		add(topBar, BorderLayout.NORTH);

		add(scPane, BorderLayout.CENTER);
		add(bottomBar, BorderLayout.SOUTH);

	}

	public String getPluginName() {
		return "Table Editor";
	}

/**
	 * Returns the short name of this panel.
	 */
	public String getPluginShortName() {
		return ("Table");
	}

/**
	 * Returns the version of this plugin (in this case, I'm returning
	 * the CVS revision of the .java file :-)
	 */
	public String getPluginVersion() {
		return ("$Revision$");
	}

/**
	 * Returns the current DatabaseProcess
	 */
	public DatabaseProcess getDatabaseProcess() {
		return (ideprocess);
	}

/**
	 * Changes the Database Process this panel will use.
	 */
	public void setDatabaseProcess(DatabaseProcess proc) {
		ideprocess = proc;
		try {
			ArrayList dbs = ideprocess.getCatalogs();
			for (int i = 0; i < dbs.size(); i++)
				cbDatabases.addItem(dbs.get(i));
		}
		catch (SQLException exc) {}
		fieldCollection.refreshTypes(cbDatabases);
	}

/**
	 * Hides/Shows the menus
	 */
	public void setVisibleMenus(boolean value, JMenu menu) {
		Enumeration en = menuItems.elements();
		while (en.hasMoreElements()) {
			JComponent mitem = (JComponent) en.nextElement();
			mitem.setVisible(value);
			if (value == false) {
				menu.remove(mitem);
			}
			else {
				menu.add(mitem);
			}
		}
	}

	public void showAboutBox() {
		GPLAboutDialog gpl = new GPLAboutDialog(this, "Easy Table Editor",
												getPluginVersion(),
												"An easy to use table generator for sqlIDE.\n\n" +
												"Use the table generator to help generate the necessary statements to create your tables.",
												"(C) 1999 by David Martinez.");
		gpl.actionPerformed(null);
	}

/**
	 * Returns true if the specified action is possible at this point in time.
	 */
	public boolean isActionPossible(String action) {
		// TODO: Implement
		boolean theResult = false;
		if (action.equals("Cut")) {}
		if (action.equals("Copy")) {}
		if (action.equals("Paste")) {}
		return (theResult);
	}

	public void refreshPanel() {
	}

/**
	 * Executes an action by application's request.
	 */
	public boolean executeAction(String action) {
		// TODO: Implement
		//System.out.println("PanelTableEditor executed "+action);
		return false;
		//if (action.equals("Cut") )    theQuery.cut();
		//if (action.equals("Copy") )   theQuery.copy();
		//if (action.equals("Paste") )  theQuery.paste();
	}

	public void showPreview() {

		frm = new JFrame("Preview/Create");
		frm.setIconImage(SqlIdeApplication.getFrame().getIconImage());
		SyntaxDocument sd = new SyntaxDocument();
		sd.setTokenMarker(new TSQLTokenMarker());
		theStatement.setDocument(sd);
		Font theFont = conf.getSQLFont();
		theStatement.setFont(theFont);
		theStatement.getPainter().setFont(theFont);

		frm.getContentPane().setLayout(new BorderLayout());
		Panel okPanel = new Panel();
		JButton okButton = new JButton(ACTION_CREATETABLE);
		JButton cancelButton = new JButton("Cancel");
		okButton.setMnemonic('C');
		cancelButton.setMnemonic('C');

		cancelButton.addActionListener(
			  new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				frm.hide();
			}
		});

		okPanel.add(okButton);
		okPanel.add(cancelButton);

		String statement;
		if ( originalFields == null ) statement = fieldCollection.writeCreateStatement(fldTableName.getText());
		else statement = fieldCollection.writeAlterStatement(fldTableName.getText(), originalFields);
		theStatement.setText(statement);
		frm.getContentPane().add(theStatement, BorderLayout.CENTER);
		frm.getContentPane().add(okPanel, BorderLayout.SOUTH);
		frm.pack();
		Point centerPoint = SwingUtils.getCenteredWindowPoint(frm);
		frm.setLocation(centerPoint);
		frm.show();

	}

	class ActionCreateTable extends AbstractAction {
		public ActionCreateTable() {
			super("Create!");
		}

		public void actionPerformed(ActionEvent ae) {
			ideprocess.changeCatalog( (String) cbDatabases.getSelectedItem());
			try {
				ideprocess.runQuery(theStatement.getText(), true, false, false);
			}
			catch (java.sql.SQLException exc) {
				JOptionPane.showMessageDialog(null, exc.toString(), "SQL Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	// Set up the editor for the file cells.
	private void setUpComboBoxEditor(JTable table) {
		//First, set up the button that brings up the dialog.
		final JButton button = new JButton(""); // {
		button.setBackground(Color.white);
		button.setBorderPainted(false);
		button.setMargin(new Insets(0, 0, 0, 0));
	}

	public void receivePluginFocus() {
		tbFields.requestFocus();
	}

	/*  public Action[] getActionsFor(NodeIDEBase node) {
		ArrayList al = new ArrayList();
		if (node instanceof ItemTableNode)
		  al.add(new ActionEditTable(node.toString()));
		Action[] actions = new Action[al.size()];
		actions = (Action[]) al.toArray(actions);
		return actions;
}*/

	public Action[] getPossibleActions() {
		ArrayList al = new ArrayList();
		al.add(ACTION_INSERTFIELD);
		al.add(ACTION_DELETEFIELD);
		al.add(ACTION_PREVIEW);
		Action[] actions = new Action[al.size()];
		actions = (Action[]) al.toArray(actions);
		return actions;
	}

	class ActionInsertField extends AbstractAction {
		public ActionInsertField() {
			super("New Column", ProgramIcons.getInstance().findIcon("images/NewColumn.gif"));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.SHIFT_MASK | KeyEvent.CTRL_MASK));

		}

		public void actionPerformed(ActionEvent ev) {
			TableField tableField = new TableField();
			tableField.fieldType = fieldCollection.classFldType;
			fieldCollection.insertField(tableField);
		}
	}

	class ActionDeleteField extends AbstractAction {
		public ActionDeleteField() {
			super("Delete Column", ProgramIcons.getInstance().findIcon("images/DeleteColumn.gif"));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_D));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.SHIFT_MASK | KeyEvent.CTRL_MASK));
		}

		public void actionPerformed(ActionEvent ev) {
			int row = tbFields.getEditingRow();
			fieldCollection.deleteField(tbFields.getEditingRow());
		}
	}

	class ActionPreview extends AbstractAction {
		public ActionPreview() {
			super("Preview/Create...", ProgramIcons.getInstance().getGoIcon());
		}

		public void actionPerformed(ActionEvent ev) {
			showPreview();
		}
	}

	public boolean executeStandardAction(ActionEvent evt) {
		/** @todo Implement. */
		return false;
	}

	public void setTableNode(ItemTableNode tableNode) throws SQLException {
		originalFields = new ArrayList();
		fldTableName.setText(tableNode.toString());
		Connection conn = null;
		ResultSet resultSet = null;
		try {
			conn = tableNode.getDatabaseProcess().getConnection();
			resultSet = tableNode.readColumns(conn);
			fieldCollection.deleteField(0);
			while ( resultSet.next() ) {
				TableField tableField = new TableField();
				tableField.fieldName = resultSet.getString("COLUMN_NAME");
				tableField.fieldLen  = new Integer(resultSet.getInt("COLUMN_SIZE"));
				tableField.fieldType = new TableFieldType(resultSet.getString("TYPE_NAME"));
				tableField.canBeNull = new Boolean(resultSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable );
				tableField.primaryKey = new Boolean(false);
				if ( (! (tableField.fieldName == null)) && !tableField.fieldName.equals("") )
					fieldCollection.insertField(tableField);
				tableField.readOnly = true;
				TableField original = (TableField)tableField.clone();
				originalFields.add(original);
			}
		} finally {
			if ( resultSet != null ) try { resultSet.close(); } catch ( Throwable thr) {}
		    tableNode.getDatabaseProcess().returnConnection(conn);
		}

	}

}