/*
 * PanelInteractiveSQL.java - An Interactive SQL panel.
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
 * along withf this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Revision: f$Revision$
 * Id: $Id$
 *
*/
package com.hackerdude.devtools.db.sqlide.plugins.isql;

import com.hackerdude.devtools.db.sqlide.pluginapi.*;
import com.hackerdude.devtools.db.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.devtools.db.sqlide.plugins.SyntaxTextArea;
import com.hackerdude.devtools.db.sqlide.*;
import com.hackerdude.devtools.db.sqlide.nodes.*;
import com.hackerdude.devtools.db.sqlide.plugins.browser.browsejdbc.*;
import com.hackerdude.devtools.db.sqlide.components.*;
import com.hackerdude.devtools.db.sqlide.dialogs.*;
import com.hackerdude.lib.*;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import java.util.*;
import java.lang.Object.*;
import java.io.*;
import java.sql.*;
import textarea.*;
import textarea.syntax.*;
import javax.swing.filechooser.*;
import javax.swing.table.*;
import com.hackerdude.lib.ExtensionFileFilter;

/**
 *   PanelInteractiveSQL is a UI for the generation of interactive
 *   sql through the backend class DatabaseProcess.
 *   The caller gives it a DatabaseProcess item and the
 *   sqlIDE configuration, and it simply generates a
 *   SQL query for every time the user clicks on "GO",
 *   putting it on the results page.
 *
 * @version $Id$
 */
public class PluginInteractiveSQL extends AbstractVisualPlugin implements IDENodeContextPluginIF {

	ProgramConfig config;
	DatabaseProcess ideprocess;
	JPanel       toolbar;
	JLabel       lblCatalogs;
	JComboBox    cbCatalogs;
	JButton      btnGo;
	JButton      btnStop;
	JPanel       exceptionPanel;
	Vector       menuItems;

	JSplitPane   pnlSQLDivider;
	JPanel       pnlQueryResults = new JPanel();
//	JTextPane    theResult;
	JCheckBox    cbAsUpdate = new JCheckBox("As Update ", false);
	JTable       theResultTable;
	SyntaxTextArea theQuery;
	JTextPane    theStatistics;

	Font theFont;
	JPanel pnlStatus = new JPanel();
	JLabel statuslabel;
	JComboBox cbHistory = new JComboBox();
	BorderLayout borderLayout1 = new BorderLayout();
	JLabel lblHistory = new JLabel();

	NodeContextCompanion companion = new NodeContextCompanion();

	JFileChooser loadFileChooser;
	JFileChooser saveFileChooser;

	Action commandRunner = new ActionCommandRunner();
	Action ACTION_DISCONNECT = new ActionDisconnect();
	Action ACTION_CONNECT    = new ActionConnect();

	/**
	 * JBuilder is really nice to you in design-time if you have
	 * one of these.
	 */
public void jbInit() {

	toolbar       = new JPanel();
	lblCatalogs  = new JLabel("Catalog:");
	cbCatalogs   = new JComboBox();
	btnGo         = new JButton(commandRunner);
	statuslabel = new JLabel("Ready.");
	btnStop       = new JButton("Stop!");
	btnStop.setMnemonic('S');
	pnlSQLDivider   = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	pnlQueryResults.setLayout(new BorderLayout());

	TextAreaDefaults defaults = TextAreaDefaults.getDefaults();
	defaults.rows = 5;
	theQuery = new SyntaxTextArea(defaults);
	SyntaxDocument sd = new SyntaxDocument();
	sd.setTokenMarker(new TSQLTokenMarker());
	theQuery.setDoubleBuffered(true);
	theQuery.setDocument(sd);
	theStatistics = new JTextPane();
	setLayout(new BorderLayout());
	pnlStatus.setLayout(borderLayout1);
	lblHistory.setDisplayedMnemonic('H');
	lblHistory.setHorizontalAlignment(SwingConstants.RIGHT);
	lblHistory.setLabelFor(cbHistory);
	lblHistory.setText("History: ");
	cbHistory.setMaximumSize(new Dimension(150, 27));
	cbHistory.setRenderer(new SQLHistoryRenderer());
	cbHistory.addActionListener(new CBHistoryListener());

	lblCatalogs.setLabelFor(cbCatalogs);
	toolbar.add(lblCatalogs);
	toolbar.add(cbCatalogs);
	toolbar.add(btnGo);
	btnStop.setEnabled(false);
	toolbar.add(btnStop);
	toolbar.add(cbAsUpdate);
	add(toolbar, BorderLayout.NORTH);
	theQuery.setMaximumSize(new Dimension(1280, 300));
	pnlSQLDivider.add(theQuery, JSplitPane.TOP);
	pnlSQLDivider.add(pnlQueryResults);
	add(pnlSQLDivider, BorderLayout.CENTER);
	this.add(pnlStatus, BorderLayout.SOUTH);
	pnlStatus.add(statuslabel, BorderLayout.WEST);
	pnlStatus.add(cbHistory, BorderLayout.EAST);
	pnlStatus.add(lblHistory, BorderLayout.CENTER);

};

	/**
	 * Constructor.
	 */
	public PluginInteractiveSQL() {
	}

	public Icon getPluginIcon() {
		return ProgramIcons.getInstance().findIcon("images/Binocular.gif");
	}

	public void initPlugin() {
		menuItems = new Vector();
		config = ProgramConfig.getInstance();
		jbInit();
		refreshPanel();
	}

	public void freePlugin() {
		menuItems.clear();
	}

	/**
	 * Returns the short name of this panel.
	 */
	public String getPluginShortName() { return("SQL"); };

	/**
	 * Returns the full name of this panel
	 */
	public String getPluginName() { return "Interactive SQL"; }

	/**
	 * Returns the version of this plugin (in this case, I'm returning
	 * the CVS revision of the .java file :-)
	 */
	public String getPluginVersion() { return("$Revision$"); };

	/**
	 * Returns the current DatabaseProcess
	 */
	public DatabaseProcess getDatabaseProcess() {
	return(ideprocess);
	};

	/**
	 * Changes the Database Process this panel will use.
	 */
	public void setDatabaseProcess( DatabaseProcess proc ) {
		ideprocess = proc;
		theQuery.setText(ideprocess.lastQuery);
		try {
			ArrayList dbs = ideprocess.getCatalogs();
			for(int i=0;i<dbs.size();i++) cbCatalogs.addItem(dbs.get(i));
		} catch ( SQLException exc ) {}
		DBChangeListener cbListener = new DBChangeListener();
		cbCatalogs.addActionListener(cbListener);

	};

	/**
	 * Hides/Shows the menus for this panel.
	 *
	 */
	public void setVisibleMenus( boolean value, JMenu menu ) {
		Enumeration en = menuItems.elements();
		while ( en.hasMoreElements() ) {
			JComponent mitem = (JComponent)en.nextElement();
			mitem.setVisible(value);
			if ( value == false ) {
			menu.remove(mitem);
			} else { menu.add(mitem); };
		}
	}

	/**
	 * Refreshes the Panel's UI from the configuration.
	 * Use when the configuration has changed.
	 */
	public void refreshPanel() {
		theFont = new Font(config.getSQLFontName(), Font.PLAIN, config.getSQLFontSize() );
		theQuery.setFont(theFont);
		theQuery.getPainter().setFont(theFont);
/*		if ( theResult != null) {
			theResult.setFont(theFont);
			theStatistics.setFont(theFont);
		}*/
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

	public boolean isActionPossible( String action ) {
		boolean theResult = false;
		if ( action.equals("Cut") ) {  };
		if ( action.equals("Copy") ) {  };
		if ( action.equals("Paste") ) {  };
		return(theResult);
	};

	public void doPaste() { theQuery.paste(); }

	public void doCut() { theQuery.cut(); }

	public void doCopy() { theQuery.copy(); }

	public void showAboutBox() {
			GPLAboutDialog gpl =
			new GPLAboutDialog(this, "Interactive SQL Panel",
					   getPluginVersion(),
		  "A syntax-highlighted Interactive SQL panel for sqlIDE.\n\n"+
				  "Use the isql panel to execute queries and get resultsets.",
			   "(C) 1999 by David Martinez.");
			gpl.actionPerformed(null);
	};


	/* *************************************************************
	 *   Inner Classes.
	 * *************************************************************
	 */

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

   class ActionDisconnect extends AbstractAction {
		public ActionDisconnect() { super("Disconnect", ProgramIcons.getInstance().findIcon("images/UnPlug.gif")); }
		public void actionPerformed(ActionEvent evt) { ideprocess.doDisconnect();}
   }

   class ActionConnect extends AbstractAction {
		public ActionConnect() { super("Connect", ProgramIcons.getInstance().findIcon("images/Plug.gif")); }
		public void actionPerformed(ActionEvent evt) { ideprocess.doConnect();}
   }

   /**
	* This executes the current query for this interactive SQL Window.
	*/
public void executeCurrentQuery() {
	setCursor(new Cursor(Cursor.WAIT_CURSOR));
	statuslabel.setText( "Executing Query..." );
	String queryText = theQuery.getText();
	cbHistory.addItem(new SQLHistoryItem(queryText));
	Object db = cbCatalogs.getSelectedItem();
	if ( db != null ) ideprocess.changeCatalog( db.toString() );
	try {
		boolean asUpdate = cbAsUpdate.isSelected();
		pnlQueryResults.removeAll();
		ideprocess.runQuery(queryText, asUpdate, false, false );
		if ( theResultTable != null ) {
			theResultTable = null;
		}
		theQuery.setText(ideprocess.lastQuery);
		ResultSetColumnModel newColumnModel = new ResultSetColumnModel(ideprocess.getLastQueryResults(), theFont, this);
		theResultTable = new JTable(ideprocess.getTableModel(), newColumnModel);
		theResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		theResultTable.setMinimumSize(new Dimension(newColumnModel.getAggregatedWidths(), 70));
		theResultTable.validate();
		JScrollPane spTheResult = new JScrollPane(theResultTable); //, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnlQueryResults.add(spTheResult, BorderLayout.CENTER);
		pnlQueryResults.add(new JLabel("<HTML>"+theQuery.getText(), ProgramIcons.getInstance().findIcon("images/Check.gif"),JLabel.CENTER ), BorderLayout.NORTH);
		theResultTable.setFont(theFont);

/*		Enumeration enum = theResultTable.getColumnModel().getColumns();
		while ( enum.hasMoreElements() ) {
			TableColumn thisColumn =(TableColumn)enum.nextElement();
			thisColumn.sizeWidthToFit();
		} */
		statuslabel.setText( "Ready." );
		spTheResult.validate();
		pnlQueryResults.validate();
	} catch ( SQLException exc ) {
		exceptionPanel = getExceptionPanel("SQL Exception:", exc );
		pnlQueryResults.add(exceptionPanel, BorderLayout.CENTER);
	}
	setCursor(Cursor.getDefaultCursor());
}

	class DBChangeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String actionString = e.getActionCommand();
			if ( ideprocess!= null && actionString.equals("comboBoxChanged") ) ideprocess.changeCatalog(cbCatalogs.getSelectedItem().toString() );
		}
	}

	class CBHistoryListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		String actionString = e.getActionCommand();
		if ( actionString.equals("comboBoxChanged") ) {
				theQuery.setText(cbHistory.getSelectedItem().toString());
		}

	}
	}

	public class SQLHistoryItem {

	  String aQuery;

	  public SQLHistoryItem( String sqlQuery ) {
		aQuery = sqlQuery;
	  }

	  public String toString() {
		return aQuery;
	  }

	  // Todo: How can I figure out what the optimal max size will be depending on the proportion of the fonts?
	  public String abbrev() {
		int MAX_QLENGTH = 25;
		if ( aQuery.length() > MAX_QLENGTH ) {
		return aQuery.substring(0, MAX_QLENGTH);
		} else return aQuery;
	  }

	}

	/**
	 * SQL History Renderer - Renders the sql history components.
	 */
	class SQLHistoryRenderer extends JLabel implements ListCellRenderer {
	  int length;

	  public SQLHistoryRenderer() { }

	  public Component getListCellRendererComponent(
		 JList list,
		 Object value,
		 int index,
		 boolean isSelected,
		 boolean cellHasFocus)
	  {

		 if ( value != null ) {
		   try {
			  SQLHistoryItem sqlitem = (SQLHistoryItem)value;
			  setText(sqlitem.abbrev());
		   } catch ( ClassCastException exc ) {
		   }
		 }
		 return this;
	  }
 }


  public void doOpenFile() {
	if ( loadFileChooser == null ) {
	   loadFileChooser = SQLIDEDialogFactory.createSQLFileChooser(ProgramConfig.getInstance().getSaveDirectory());;
	   loadFileChooser.setFileFilter(getFileFilter());
	   loadFileChooser.setDialogTitle("Open SQL File");
	}
	int selected = loadFileChooser.showOpenDialog(this);
	if ( selected == loadFileChooser.APPROVE_OPTION ) {
	   String loadedText = loadFromFile(loadFileChooser.getSelectedFile());
	   theQuery.setText(loadedText);
	} else { System.out.println("[PanelInteractiveSQL] File Open Cancelled"); }

  }

  private String loadFromFile(File file) {
	 String result = null;
	 try {
	   BufferedReader br = new BufferedReader( new FileReader(file) );
	   StringBuffer buffer = new StringBuffer(new Long(file.length()).intValue());
	   String currentLine = br.readLine();
	   while ( currentLine != null ) {
		  buffer.append(currentLine).append("\n");
		  currentLine = br.readLine();
	   }
	   result = buffer.toString();
	 } catch ( FileNotFoundException exc ) {
	   exc.printStackTrace();
	 } catch ( IOException exc2 ) {
	   exc2.printStackTrace();
	 } finally { return result; }

  }


	public void doSaveFile() {
		doSaveFileAs();
	}

	public void doSaveFileAs() {
	if ( saveFileChooser == null ) {
	   saveFileChooser = SQLIDEDialogFactory.createSQLFileChooser(ProgramConfig.getInstance().getSaveDirectory());
	   saveFileChooser.setDialogTitle("Save SQL File");
	   saveFileChooser.setFileFilter(getFileFilter());
	}
	int selected = saveFileChooser.showSaveDialog(this);
	if ( selected == saveFileChooser.APPROVE_OPTION ) {
	   File selectedFile = saveFileChooser.getSelectedFile();
	   if ( ( ! selectedFile.exists() ) && selectedFile.getName().indexOf(".") < 0 ) { selectedFile = new File(selectedFile.getAbsolutePath()+".sql"); }
	   if (  selectedFile.exists() &&
		   ! (JOptionPane.showConfirmDialog(this, "Are you sure you want to overwrite file "+selectedFile.getName()+"?",
			  "Overwrite File?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION ) ) {
			 return;
	   }
	   System.out.println("[PanelInteractiveSQL] Writing to file "+selectedFile.getName());
	   try {
	   saveToFile(selectedFile, theQuery.getText());
	   } catch (Exception exc2) {
		  JOptionPane.showMessageDialog(this, exc2.toString(), "I/O Error", JOptionPane.ERROR_MESSAGE);
	   }
	}

  }


  private void saveToFile(File file, String text) throws IOException {
	 PrintWriter pw = new PrintWriter(new FileWriter(file));
	 pw.print(text);
	 pw.close();
  }

  public javax.swing.filechooser.FileFilter getFileFilter() {
	 String[] filters = { "sql", "txt" };
	 String description = "SQL or Text Files (*.sql, *.txt)";
	 ExtensionFileFilter flt = new ExtensionFileFilter(filters, description);
	 return flt;
  }

	class ActionCommandTyper extends AbstractAction {
		public ActionCommandTyper(String statement, Icon icon) { super(statement, icon); }

		public void actionPerformed(ActionEvent ae) {
			String statement = ae.getActionCommand();
			theQuery.setText(statement);
			receivePluginFocus();
		}
	}

	class ActionCatalogChanger extends AbstractAction {
		String catalogName;
		public ActionCatalogChanger(String label, String catalogName, Icon icon) { super(label, icon); this.catalogName = catalogName; }

		public void actionPerformed(ActionEvent ae) {
			cbCatalogs.setSelectedItem(catalogName);
		}
	}


	public Action[] getPossibleActions() {
		ArrayList al = new ArrayList();
		al.add( ACTION_CONNECT );
		al.add( ACTION_DISCONNECT );
		al.add( commandRunner );
		Action[] actions = new Action[al.size()];
		actions = (Action[])al.toArray(actions);
		return actions;
	}

	public void receivePluginFocus() {
		theQuery.requestFocus();
	}

	public Action[] getActionsFor(NodeIDEBase[] nodes) {
		return companion.getActionsFor(nodes);
	}

	/**
	 * This is a companion Inner class to help implement the node context
	 * stuff.
	 */
	protected class NodeContextCompanion {
		public Action[] getActionsFor(NodeIDEBase[] nodes) {
			if ( nodes.length != 1 ) return new Action [0];
			NodeIDEBase node = nodes[0];
			ArrayList al = new ArrayList();
			if ( node instanceof ItemTableNode ) {
				ItemTableNode tableItem = (ItemTableNode)node;

				String objectName;
				if ( tableItem.getCatalogName() == null || tableItem.getCatalogName().equals("") )
					objectName = tableItem.toString();
				else objectName = tableItem.getCatalogName()+"."+tableItem.toString();

				String statement = "SELECT * FROM "+objectName;
				ActionCommandTyper typer = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/Sheet.gif"));
				al.add(typer);

				statement = "SELECT COUNT(*) FROM "+objectName;
				ActionCommandTyper countTyper = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/Gauge.gif"));
				al.add(countTyper);

				statement = "INSERT INTO "+objectName+" VALUES ";
				ActionCommandTyper ins = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/NewRow.gif"));
				al.add(ins);

				statement = "DELETE FROM "+objectName+" WHERE ";
				ActionCommandTyper del = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/DeleteRow.gif"));
				al.add(del);

			}
			if ( node instanceof ItemCatalogNode ) {
				ActionCatalogChanger changer = new ActionCatalogChanger("Change to "+node.toString(), node.toString(), ProgramIcons.getInstance().getDatabaseIcon());
				al.add(changer);
			}


			if ( node instanceof ItemTableColumnNode ) {
				ItemTableColumnNode columnItem = (ItemTableColumnNode)node;
				String objectName;
				if ( columnItem.getCatalogName() == null || columnItem.getCatalogName().equals("") )
					objectName = columnItem.getTableName();
				else objectName = columnItem.getCatalogName()+"."+columnItem.getTableName();

//				String objectName = columnItem.getCatalogName()+"."+columnItem.getTableName();
				String columnName = columnItem.getColumnName();

				String statement;
				ActionCommandTyper typer;

				statement = "SELECT MIN("+columnName+") FROM "+objectName;
				typer = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/minus.gif"));
				al.add(typer);

				statement = "SELECT MAX("+columnName+") FROM "+objectName;
				typer = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/plus.gif"));
				al.add(typer);

				statement = "SELECT COUNT("+columnName+") , MIN("+columnName+") , MAX("+columnName+")  FROM "+objectName;
				typer = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/List.gif"));
				al.add(typer);


				statement = "SELECT "+columnName+" FROM "+objectName+"  GROUP BY "+columnName;
				typer = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/Column.gif"));
				al.add(typer);

				statement = "SELECT * FROM "+objectName+" WHERE "+columnName;
				typer = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/Binocular.gif"));
				al.add(typer);

				statement = "SELECT * FROM "+objectName+" ORDER BY "+columnName;
				typer = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/Sheet.gif"));
				al.add(typer);


			}

			Action[] actions = new Action[al.size()];
			actions = (Action[])al.toArray(actions);
			return actions;
		}
	}

	public boolean executeStandardAction(ActionEvent e) {
		if ( e.getActionCommand().equalsIgnoreCase("Cut") ) {	doCut();	return true; }
		else if  ( e.getActionCommand().equalsIgnoreCase("Copy") )  { doCopy(); return true; }
		else if ( e.getActionCommand().equalsIgnoreCase("Paste") ) { doPaste(); return true; }
		else if ( e.getActionCommand().equalsIgnoreCase("Open") ) { doOpenFile(); return true; }
		else if ( e.getActionCommand().equalsIgnoreCase("Save") ) { doSaveFile(); return true; }
		else if ( e.getActionCommand().equalsIgnoreCase("Save As") ) { doSaveFileAs(); return true; }
		return false;
	}


}
