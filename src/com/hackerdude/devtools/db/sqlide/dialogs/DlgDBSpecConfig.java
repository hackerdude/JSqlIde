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
package com.hackerdude.devtools.db.sqlide.dialogs;

import com.hackerdude.devtools.db.sqlide.dataaccess.*;
import com.hackerdude.devtools.db.sqlide.ProgramIcons;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.UIManager.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;
import java.util.*;
import java.lang.Object.*;
import java.lang.Exception.*;
import java.io.*;
import javax.swing.event.*;
import com.hackerdude.devtools.db.sqlide.ProgramConfig;

/**
 * Dialog to configure the database connection profile
 * (DatabaseSpec) visually.
 * @version $Id$
 */
public class DlgDBSpecConfig extends JDialog {

	public final Action ACTION_OK = new OKAction();
	public final Action ACTION_CANCEL = new CancelAction();

	DatabaseSpec databaseSpec;

	BorderLayout borderLayout1 = new BorderLayout();
	JPanel pnlOkCancel = new JPanel();
	JButton btnCancel = new JButton(ACTION_CANCEL);
	JButton btnOk = new JButton(ACTION_OK);
	JTabbedPane tabbedPane = new JTabbedPane();
	JPanel topPanel = new JPanel();

	DbSpecJDBCPanel pnlJDBC = new DbSpecJDBCPanel();
	DBSpecConnectionPanel pnlConnectionPanel = new DBSpecConnectionPanel();
	DBSpecClassPathPanel pnlClasspathPanel = new DBSpecClassPathPanel();

	/**
	 * readFromSpec() fills in the UI from the Database Spec object.
	 *
	*/
	public void readFromSpec() {
		/** @todo Figure this thing out here. */
		File tmp = new File(databaseSpec.getFileName());
		// Only allow editing of filenames if the file already exists.
//		fFileName.setEnabled( ! tmp.exists() );
//		btnSpecBrowse.setVisible( ! tmp.exists() );
//		fFileName.setText(spec.getFileName());
//		fDriver.setText(spec.getDriverName());
//		fURLForm.setText(spec.getURL());
//		fDisplayName.setText(spec.getPoliteName());
//		fIDEManager.setText(spec.getDbIntfClassName());
//		fDefaultCatalog.setText(spec.getDefaultCatalog());
//		setConnectionPropsModel();
//		updateMessageLabel();
//		setModal(true);
	};

	/**
	 * writeToSpec() updates the Database spec with the UI's edited contents.
	 *
	*/
	public void aplpyToModel() {
		pnlJDBC.applyToModel();
		pnlConnectionPanel.applyToModel();
	}

	public DlgDBSpecConfig( JFrame frame, DatabaseSpec spec ) {
		super( frame );
		setDatabaseSpec(spec);
		readFromSpec();
		if ( frame != null ) frame.setIconImage( ProgramIcons.getInstance().getDatabaseIcon().getImage() );
		jbInit();
		pnlConnectionPanel.readFromModel();
		pnlJDBC.readFromModel();
	}

	public void setDatabaseSpec( DatabaseSpec spec ) {
		this.databaseSpec = spec;
		pnlConnectionPanel.setDatabaseSpec(spec);
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
		DatabaseSpec spec = ProgramConfig.getInstance().getDefaultDatabaseSpec();
		showConfigurationDialog( null, spec);
	}

	/**
	 * Shows the configuration dialog.
	 */
	public static void showConfigurationDialog(JFrame frame, DatabaseSpec s) {

		DlgDBSpecConfig configuration = new DlgDBSpecConfig( frame , s );
		configuration.setModal(true);
		configuration.setTitle("Database Spec: "+s.getPoliteName());
		Dimension screen = configuration.getToolkit().getScreenSize();
		configuration.pack();
		Dimension configDim = configuration.getSize();

		int x = new Double(( screen.getWidth() - configDim.getWidth() )/ 2).intValue();
		int y = new Double((screen.getHeight() - configDim.getHeight()) / 2).intValue();
		configuration.setLocation(x,y);
		configuration.setVisible(true);
	}

	class OKAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) { aplpyToModel(); dispose(); }
	}

	class CancelAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) { dispose(); }
	}



}

/*

  $Log$
  Revision 1.3  2002/08/14 16:07:18  davidmartinez
  Now database specs use several files and a URL classloader in order to load the classes.

  Revision 1.2  2002/08/13 22:37:38  davidmartinez
  Completely broke the JarClassloader :-)

  Revision 1.1.1.1  2001/09/07 02:51:19  davidmartinez
  Initial Checkin of the Alpha tree

  Revision 1.3  2000/05/08 22:25:15  david
  Tweaks so that Jar Packaging will work. Also new-user settings bug fixes.

  Revision 1.2  2000/05/05 22:51:33  david
  Implement a connection pool; Completely reworked DatabaseSpec and the
  DlgDbSpecConfig to work with a user defined set of name/value pairs;
  Added a history feature to the Interactive SQL window; Promoted
  refreshConfiguration to required by the Panel Interface.

  Revision 1.1.1.1  2000/05/02 22:36:49  david
  Initial Import

  Revision 1.7  1999/12/15 09:10:18  david
  Lots of fixes, now Database specs can be added and deleted; changed
  expand/collapse icons switched.

  Revision 1.6  1999/10/25 15:20:37  david
  Moved files to a flat JSqlIde package, in preparation for the
  makefile and package organization.

  Revision 1.5  1999/10/23 05:00:49  david
  Added FileChoosers for browsing buttons on dbspec configs, fixed Jar files
  edit boxes.

  Revision 1.4  1999/10/19 22:54:02  david
  This is now a modal JDialog.

  Revision 1.3  1999/10/19 01:07:54  david
  added javadoc comment

  Revision 1.2  1999/10/19 00:39:26  david
  Added Revision and Log


 */
