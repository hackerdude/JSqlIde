/*
 *   DlgDBSpecConfig.java - Dialog that allows the user to
 *   configure the Database Specifications visually.
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

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.hackerdude.apps.sqlide.ProgramIcons;
import com.hackerdude.apps.sqlide.dataaccess.HostConfigRegistry;
import com.hackerdude.apps.sqlide.xml.hostconfig.SqlideHostConfig;
import com.hackerdude.swing.SwingUtils;

/**
 * Dialog to configure the database connection profile
 * (DatabaseSpec) visually.
 * @version $Id$
 */
public class DlgConnectionConfig extends JDialog {

	public final Action ACTION_OK = new OKAction();
	public final Action ACTION_CANCEL = new CancelAction();

	SqlideHostConfig SqlideHostConfig;

	BorderLayout borderLayout1 = new BorderLayout();
	JPanel pnlOkCancel = new JPanel();
	JButton btnCancel = new JButton(ACTION_CANCEL);
	JButton btnOk = new JButton(ACTION_OK);
	JTabbedPane tabbedPane = new JTabbedPane();
	JPanel topPanel = new JPanel();

	ConnectionJDBCPanel pnlJDBC = new ConnectionJDBCPanel();
	ConnectionParametersPanel pnlConnectionPanel = new ConnectionParametersPanel();
	ConnectionClassPathPanel pnlClasspathPanel = new ConnectionClassPathPanel();

	/**
	 * readFromSpec() fills in the UI from the Database Spec object.
	 *
	*/
	public void readFromModel() {
	}

	/**
	 * writeToSpec() updates the Database spec with the UI's edited contents.
	 *
	*/
	public void applyToModel() {
		pnlClasspathPanel.applyToModel();
		pnlJDBC.applyToModel();
		pnlConnectionPanel.applyToModel();
	}

	public DlgConnectionConfig( JFrame frame, SqlideHostConfig spec ) {
		super( frame );
		setSqlideHostConfig(spec);
		readFromModel();
		if ( frame != null ) frame.setIconImage( ProgramIcons.getInstance().getDatabaseIcon().getImage() );
		jbInit();
		pnlConnectionPanel.readFromModel();
		pnlJDBC.readFromModel();
	}

	public void setSqlideHostConfig( SqlideHostConfig spec ) {
		this.SqlideHostConfig = spec;
		pnlConnectionPanel.setSqlideHostConfig(spec);
		pnlJDBC.setDatabaseSpec(spec);
		pnlClasspathPanel.setDatabaseSpec(spec);
	}

	public void jbInit() {
		this.setTitle("");
		this.getContentPane().setLayout(borderLayout1);
		btnCancel.setToolTipText("");
		btnCancel.setActionCommand("cancel");
		btnCancel.setMnemonic('C');
		btnCancel.setText("Cancel");
		btnOk.setMnemonic('K');
		btnOk.setText("Ok");
		this.getContentPane().add(pnlOkCancel, BorderLayout.SOUTH);
		pnlOkCancel.add(btnOk, null);
		pnlOkCancel.add(btnCancel, null);
		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.add(pnlJDBC,  "JDBC");
		tabbedPane.add(pnlClasspathPanel, "Class Path");
		tabbedPane.add(pnlConnectionPanel, "Connection");
		getContentPane().add(topPanel, BorderLayout.NORTH);


	}

	// Test function
	public static void main( String[] args ) {
		SqlideHostConfig spec = HostConfigRegistry.getInstance().getDefaultHostConfig();
		showConfigurationDialog( null, spec);
	}

	/**
	 * Shows the configuration dialog.
	 */
	public static void showConfigurationDialog(JFrame frame, SqlideHostConfig s) {

		DlgConnectionConfig configuration = new DlgConnectionConfig( frame , s );
		configuration.setModal(true);
		configuration.setTitle("Database Spec: "+s.getName());
		configuration.pack();
		Point location = SwingUtils.getCenteredWindowPoint(configuration);
		configuration.setLocation(location);
		configuration.show();

	}

	class OKAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			applyToModel();
			hide();
		}
	}

	class CancelAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			hide();
		}
	}



}
