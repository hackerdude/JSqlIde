/*
 *   DlgPanelServersPage.java - "Servers" Page of the
 *   configuration dialog.
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
 * Revision: $Revision$
 * Id      : $Id$
 *
 */

package com.hackerdude.apps.sqlide.dialogs;
import com.hackerdude.apps.sqlide.*;
import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.wizards.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.UIManager.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;

import java.lang.Object.*;
import java.lang.Exception.*;
import java.io.*;
import com.hackerdude.apps.sqlide.xml.hostconfig.*;
import com.hackerdude.apps.sqlide.xml.*;


/**
 * Servers Page of the configuration dialog.
 *
 * @version $Id$
 */
public class DlgPanelServersPage extends JPanel {

	JTable      tbServers;
	JButton     svrAdd, svrEdit, svrDelete;

	ConfigServersTableModel model;
	Action SERVER_ADD = new ActionAddServer();
	Action SERVER_EDIT = new ActionEditServer();
	Action SERVER_REMOVE = new ActionRemoveServer();

	/**
	 * To get a server editing page, just create using this constructor,
	 * and sending the config object you want to use.
	 */
	public DlgPanelServersPage() {

			setLayout(new BorderLayout());

			model = new ConfigServersTableModel();
			model.addTableModelListener(new TableModelListener() {
				public void tableChanged(TableModelEvent evt) {
					SqlIdeApplication.getInstance().refreshPanels();
				}
			});

			tbServers = new JTable(model);
			svrAdd    = new JButton(SERVER_ADD);
			svrEdit   = new JButton(SERVER_EDIT);
			svrDelete = new JButton(SERVER_REMOVE);
			svrAdd.setMnemonic('A');
			svrEdit.setMnemonic('E');
			svrDelete.setMnemonic('D');
			JPanel sideButtons = new JPanel();
			sideButtons.setLayout(new BoxLayout(sideButtons, BoxLayout.Y_AXIS));
			sideButtons.add(svrAdd);
			sideButtons.add(svrEdit);
			sideButtons.add(svrDelete);

			//al = new ServerButtonsAL(tbServers);
			//svrAdd.addActionListener(al);
			//svrEdit.addActionListener(al);
			//svrDelete.addActionListener(al);

			add( new JLabel("Servers"), BorderLayout.NORTH);
			add( new JScrollPane( tbServers ), BorderLayout.CENTER );
			add(sideButtons, BorderLayout.EAST);

	}

	public void readFromModel() {
		model.fireTableDataChanged();
	}

	/**
	 * Table model for the servers page.
	 */
	public class ConfigServersTableModel extends AbstractTableModel {

			Vector hostConfigurations;

			public ConfigServersTableModel( ) {
					super();
					hostConfigurations = new Vector();
			}

			public int getRowCount() {
					return HostConfigRegistry.getInstance().getConnectionCount();
			};

			public int getColumnCount() { return(1); };

			public boolean isCellEditable(int row, int col) {
					return(false);  // The editing is done via buttons
			}

			// Don't modify anywthing
			public void setValueAt( Object aValue, int row, int col ) {};

			public Object getValueAt( int row, int column ) {
					return HostConfigRegistry.getInstance().getDbConfigName(row);
			};

			public String getColumnName( int column ) {
					return(new String("Server Specifications"));
			};

	}


	/**
	 * Shows the database spec editor.
	 */
	class ActionEditServer extends AbstractAction {
		public ActionEditServer() {
			super("Edit", ProgramIcons.getInstance().findIcon("images/EditBook.gif"));
		}
		public void actionPerformed(ActionEvent ev) {
			int itemNo = tbServers.getSelectedRow();
			SqlideHostConfig spec = null;
			if ( itemNo >= 0 ) { spec  = HostConfigRegistry.getInstance().getSqlideHostConfig(itemNo); }
			if ( spec == null ) {
				JOptionPane.showMessageDialog(null, "You have no connection selected.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			DlgConnectionConfig.showConfigurationDialog( SqlIdeApplication.getFrame(), spec );
			try {
				HostConfigFactory.saveSqlideHostConfig(spec);
			}
			catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Could not save configuration "+spec.getName()+":\n"+"Could not write to "+spec.getFileName()+ex.toString(), "Error while saving", JOptionPane.ERROR_MESSAGE);
			}
			readFromModel();
		}
	}

	/**
	 * This action adds a new server spec. It
	 * launches the new server wizard.
	 */
	class ActionAddServer extends AbstractAction {
		public ActionAddServer() { super("New", ProgramIcons.getInstance().findIcon("images/NewPlug.gif"));}

		public void actionPerformed(ActionEvent ev) {
			try {
				NewServerWizard wiz = NewServerWizard.showWizard(true);
				if ( wiz.result == wiz.OK ) {
					SqlideHostConfig spec = wiz.getDBSpec();
					spec.setFileName(createUniqueFileName(spec));
					HostConfigFactory.saveSqlideHostConfig(spec);
					HostConfigRegistry.getInstance().addSqlideHostConfig(spec);
					readFromModel();
				}
			}
			catch (IOException ex) {
				JOptionPane.showMessageDialog(DlgPanelServersPage.this, ex.toString(), "Error saving server", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private String createUniqueFileName(SqlideHostConfig hostConfig) {
		String fileName = hostConfig.getName();
		fileName = removeIllegalChars(fileName);
		String directory = ProgramConfig.getInstance().getUserProfilePath();

		String proposedFileName = directory+File.separator+fileName;
		boolean alreadyExists = new File(proposedFileName+HostConfigFactory.PROP_DB_CONFIG_SUFFIX).exists();

		if ( alreadyExists ) {
			proposedFileName = proposedFileName+"1";
			alreadyExists = new File(proposedFileName+HostConfigFactory.PROP_DB_CONFIG_SUFFIX).exists();
		}
		int i=1;
		while ( alreadyExists ) {
			proposedFileName = proposedFileName.substring(0,proposedFileName.length()-1)+ ++i;
			alreadyExists = new File(proposedFileName+HostConfigFactory.PROP_DB_CONFIG_SUFFIX).exists();
		}
		return proposedFileName+HostConfigFactory.PROP_DB_CONFIG_SUFFIX;
	}

	private String removeIllegalChars(String fileName) {
		String result = fileName;
		result = result.replace('?', '_');
		result = result.replace('/', '_');
		result = result.replace('\\', '_');
		result = result.replace(' ', '_');
		return result;
	}

	/**
	 * This action removes the server from the database server specification.
	 */
	class ActionRemoveServer extends AbstractAction {
		public ActionRemoveServer() { super("Delete", ProgramIcons.getInstance().findIcon("images/UnPlug.gif")); }
		public void actionPerformed(ActionEvent ev) {
			int itemNo = tbServers.getSelectedRow();
			SqlideHostConfig spec = null;
			if ( itemNo >= 0 ) { spec  = HostConfigRegistry.getInstance().getSqlideHostConfig(itemNo); }
			if ( spec == null ) {
				JOptionPane.showMessageDialog(null, "You have no database selected.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if ( JOptionPane.showConfirmDialog(svrDelete, "Delete "+spec.getName()+"?",
									"Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE )
							== JOptionPane.YES_OPTION ) {
				File f = new File(spec.getFileName());
				if ( f.delete() ) {
					HostConfigRegistry.getInstance().removeSqlideHostConfig(spec);
					readFromModel();
				} else {
					JOptionPane.showMessageDialog(svrDelete, "Couldn't delete "+spec.getFileName(), "Error deleting file", JOptionPane.ERROR_MESSAGE);
				}
			}

		}
	}

}

