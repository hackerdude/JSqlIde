/*
 *   DlgIDEConfigure.java - Dialog that allows the user to
 *   configure the IDE visually.
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
 * $Id$
 * $Revision$
 *
*/
package com.hackerdude.apps.sqlide.dialogs;

import com.hackerdude.apps.sqlide.ProgramConfig;
import com.hackerdude.apps.sqlide.ProgramIcons;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.UIManager.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;
import java.util.*;
import java.lang.Object.*;
import java.lang.Exception.*;
import java.io.*;
import javax.swing.event.*;
import com.hackerdude.apps.sqlide.dataaccess.*;

/**
 * This is a dialog that allows us to configure the
 * sqlIDE visually.
 *
 * @version $Id$
 */
public class DlgIDEConfigure extends JDialog implements Observer {

	public static int OK     = 0;
	public static int CANCEL = 1;

	JTabbedPane tbPane;
	DlgPanelGeneralPage pgGeneral;
	DlgPanelServersPage pgServers;
	DlgPanelEditorPage  pgEditor;

	JButton mainOk, mainCancel;

//	ProgramConfig config;

	public int result = CANCEL;


	/**
	 * IDE Configuration Dialog constructor.
	 *
	 * @param theconfig The configuration object we want to change.
	 */
	public DlgIDEConfigure(Frame owner, String title) {

	  super(owner, title, true);
//      config = theConfig;
//      setConfiguration(theConfig);
	  jbInit();
//      pgGeneral.setConfiguration(theConfig);
//      pgServers.setConfiguration(theConfig);
//      pgEditor.setConfiguration(theConfig);

	}


	public void update(Observable obs, Object obj) {
		pgEditor.readFromModel();
		pgGeneral.readFromModel();
		pgServers.readFromModel();
	}

	public void jbInit() {
		getContentPane().setLayout(new BorderLayout());
		tbPane    = new JTabbedPane();
		pgGeneral = new DlgPanelGeneralPage();
		pgServers = new DlgPanelServersPage();
		pgEditor = new DlgPanelEditorPage();
		tbPane.addChangeListener(new javax.swing.event.ChangeListener() {

		 public void stateChanged(ChangeEvent e) {
			tbPane_stateChanged(e);
		 }
		});
		tbPane.add(pgServers, "Servers");
		tbPane.add(pgGeneral, "General");
		tbPane.add(pgEditor, "Editor");

		mainOk    = new JButton(new ActionOK());

		mainCancel   = new JButton(new ActionCANCEL());

		mainOk.setMnemonic('K');
		mainCancel.setMnemonic('C');

		getContentPane().add(tbPane, BorderLayout.CENTER);

		JPanel bottomstuff = new JPanel();
		bottomstuff.add(mainOk);
		bottomstuff.add(mainCancel);
		getContentPane().add(bottomstuff, BorderLayout.SOUTH);

	}

	/**
	 * This takes the configuration object and calls the methods to change
	 * things according to what has been entered on the widgets.
	 */
	public void applyToModel() {

		pgEditor.applyToModel();
		ProgramConfig.getInstance().setUILookandFeel( pgGeneral.cbLookFeel.getSelectedItem().toString() );
		ProgramConfig.getInstance().saveConfiguration();
		try {
			HostConfigRegistry.getInstance().saveSqlideHostConfigs();
		} catch ( IOException exc ) {
			JOptionPane.showMessageDialog(this, "Could not save a host config:"+exc.toString(), "Could not save a host config", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Reload the configuration in case we have changed something and
	 * don't want to keep it.
	 */
	public void cancelConfiguration() {
		// Since the config of this box has not changed, but the one
		// on the DatabaseSpecs might have, I need to reload the db specs
		// in order to cancel the configuration.
		HostConfigRegistry.getInstance().readSqlideHostConfigs();
	};

	/**
	 * Update the User Interface from the configuration item. Basically
	 * reads all the correct values for the widgets.
	 */
	public void readFromModel() {

		pgGeneral.readFromModel();
		pgEditor.readFromModel();
		pgServers.readFromModel();

	}



	// Quick-Test this dialog
	public static void main(String s[]) {
	  showConfigurationDialog(null);
	}


	/**
	 * Show the configuration dialog
	 * object loaded.
	 */
	public static boolean showConfigurationDialog(Frame owner) {

		DlgIDEConfigure configuration = new DlgIDEConfigure(owner, "sqlIDE Configuration");
		configuration.pack();
		Dimension screen = configuration.getToolkit().getScreenSize();
		configuration.setLocation((screen.getSize().width - configuration.getSize().width) / 2,
				(screen.getSize().height - configuration.getSize().height) / 2);
		configuration.setSize(400,250);
		configuration.readFromModel();
		configuration.setModal(true);
		configuration.show();

		return configuration.result == OK;

	}

	void tbPane_stateChanged(ChangeEvent e) {
		if ( tbPane.getSelectedComponent() == pgEditor ) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
			this.setCursor(Cursor.getDefaultCursor());
		}
	}


	class ActionOK extends AbstractAction {
		public ActionOK() { super("OK", ProgramIcons.getInstance().findIcon("images/Check.gif")); }
		public void actionPerformed(ActionEvent ev) {
			applyToModel();
			result = OK;
			hide();
		}
	}
	class ActionCANCEL extends AbstractAction {
		public ActionCANCEL() { super("Cancel", ProgramIcons.getInstance().findIcon("images/Error.gif")); }
		public void actionPerformed(ActionEvent ev) {
			cancelConfiguration();
			result = CANCEL;
			hide();
		}
	}

}

