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
package com.hackerdude.apps.sqlide.plugins.isql;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import com.hackerdude.apps.sqlide.*;
import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.dialogs.*;
import com.hackerdude.apps.sqlide.pluginapi.*;
import com.hackerdude.apps.sqlide.plugins.browser.browsejdbc.*;
import com.hackerdude.lib.*;

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
public class PluginInteractiveSQL extends AbstractVisualPlugin {

	ProgramConfig config;
	DatabaseProcess databaseProcess;
	Vector       menuItems;

	MainISQLPanel mainSQLPanel = new MainISQLPanel();

	JFileChooser loadFileChooser;
	JFileChooser saveFileChooser;

	/**
	 * JBuilder is really nice to you in design-time if you have
	 * one of these.
	 */
	public void jbInit() {
		setLayout(new BorderLayout());
		add(mainSQLPanel, BorderLayout.CENTER);
	}

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
	public String getPluginShortName() { return("SQL"); }

	/**
	 * Returns the full name of this panel
	 */
	public String getPluginName() { return "Interactive SQL"; }

	/**
	 * Returns the version of this plugin (in this case, I'm returning
	 * the CVS revision of the .java file :-)
	 */
	public String getPluginVersion() { return("$Revision$"); }

	/**
	 * Returns the current DatabaseProcess
	 */
	public DatabaseProcess getDatabaseProcess() {
		return(databaseProcess);
	}

	/**
	* Changes the Database Process this panel will use.
	*/
	public void setDatabaseProcess( DatabaseProcess proc ) {
		mainSQLPanel.setDatabaseProcess(proc);
		databaseProcess = proc;
		mainSQLPanel.setQueryText(databaseProcess.lastQuery);

	}

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
				} else { menu.add(mitem); }
		}
	}

	public boolean isActionPossible( String action ) {
		boolean theResult = false;
		if ( action.equals("Cut") ) {  }
		if ( action.equals("Copy") ) {  }
		if ( action.equals("Paste") ) {  }
		return(theResult);
	}

	public void doPaste() { mainSQLPanel.paste(); }

	public void doCut() { mainSQLPanel.cut(); }

	public void doCopy() { mainSQLPanel.copy(); }

	public void showAboutBox() {
		GPLAboutDialog gpl =
				new GPLAboutDialog(this, "Interactive SQL Panel",
				getPluginVersion(), "A syntax-highlighted Interactive SQL panel for sqlIDE.\n\n"+
				"Use the isql panel to execute queries and get resultsets.", "(C) 1999 by David Martinez.");
		gpl.actionPerformed(null);
	}


/*
	class CBHistoryListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String actionString = e.getActionCommand();
			if ( actionString.equals("comboBoxChanged") ) {
				mainSQLPanel.setQueryText(cbHistory.getSelectedItem().toString());
			}

		}
	}*/

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

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
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
			mainSQLPanel.setQueryText(loadedText);
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
				saveToFile(selectedFile, mainSQLPanel.getQueryText());
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

	public Action[] getPossibleActions() {
		ArrayList al = new ArrayList();
		al.add( mainSQLPanel.ACTION_RUN_COMMAND );
		Action[] actions = new Action[al.size()];
		actions = (Action[])al.toArray(actions);
		return actions;
	}

	public void receivePluginFocus() {
		mainSQLPanel.requestFocus();
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

	public void refreshPanel() {
		Font theFont = config.getSQLFont();
		Font resultSetFont = config.getResultSetFont();
		mainSQLPanel.refreshFromConfig();

	}

	public void setQueryText(String newText) {
		mainSQLPanel.setQueryText(newText);
	}

	public String getQueryText() {
		return mainSQLPanel.getQueryText();
	}

	public void selectCatalog(String catalogName) {
		mainSQLPanel.selectCatalog(catalogName);
	}
}
