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

/**
 * Dialog to configure the database connection profile
 * (DatabaseSpec) visually.
 * @version $Id$
 */
public class DlgDBSpecConfig extends JDialog {

	public final Action ACTION_NEW_CONNECTION = new NewConnectionAction();
	public final Action ACTION_DELETE_CONNECTION = new DeleteConnectionAction();

	public final Action ACTION_JAR_BROWSE = new ActionBrowseForJar();

	public final Action ACTION_OK = new OKAction();
	public final Action ACTION_CANCEL = new CancelAction();

	DatabaseSpec spec;
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel pnlOkCancel = new JPanel();
	JButton btnCancel = new JButton(ACTION_CANCEL);
	JButton btnOk = new JButton(ACTION_OK);
	JTabbedPane jTabbedPane1 = new JTabbedPane();
	JPanel jPanel1 = new JPanel();
	JPanel pnlJDBC = new JPanel();
	JPanel pnlConnection = new JPanel();
	GridBagLayout gbagJDBC = new GridBagLayout();
	JPanel pnlJarFileName = new JPanel();
	JButton btnJarBrowse = new JButton(ACTION_JAR_BROWSE);
	JTextField fJarFile = new JTextField();
	BorderLayout borderLayout2 = new BorderLayout();
	JPanel pnlDriver = new JPanel();
	JLabel lblDriver = new JLabel();
	JTextField fDriver = new JTextField();
	BorderLayout borderLayout3 = new BorderLayout();
	JLabel lblDriverJarFile = new JLabel();
	JPanel pnlURLForm = new JPanel();
	JLabel lblURLForm = new JLabel();
	JTextField fURLForm = new JTextField();
	JPanel pnlIDEManager = new JPanel();
	JLabel lblIdeManager = new JLabel();
	JTextField fIDEManager = new JTextField();
	BorderLayout borderLayout5 = new BorderLayout();
	BorderLayout borderLayout6 = new BorderLayout();
	BorderLayout borderLayout7 = new BorderLayout();
	JLabel lblConnection = new JLabel();
	JPanel jPanel2 = new JPanel();
	BorderLayout borderLayout8 = new BorderLayout();
	ConnPropertiesTableModel connModel = null;
	JPanel jPanel3 = new JPanel();
	JButton btnNewConnection = new JButton(ACTION_NEW_CONNECTION);
	JButton btnDeleteConnection = new JButton();
	JScrollPane jScrollPane1 = new JScrollPane();
	JTable tblConnectionParams = new JTable();
	JLabel lblDriverMessage = new JLabel();
	JPanel pnlPoliteName = new JPanel();
	JLabel lblPoliteName = new JLabel();
	JTextField fDisplayName = new JTextField();
	BorderLayout borderLayout4 = new BorderLayout();
	JPanel jPanel5 = new JPanel();
	BorderLayout borderLayout10 = new BorderLayout();
	JLabel lblFileName = new JLabel();
	JTextField fFileName = new JTextField();
	JButton btnSpecBrowse = new JButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
	JPanel jPanel4 = new JPanel();
	JLabel jLabel1 = new JLabel();
	JTextField fDefaultCatalog = new JTextField();
	BorderLayout borderLayout9 = new BorderLayout();

	/**
	 * readFromSpec() fills in the UI from the Database Spec object.
	 *
	*/
	public void readFromSpec() {

		File tmp = new File(spec.getFileName());
		// Only allow editing of filenames if the file already exists.
		fFileName.setEnabled( ! tmp.exists() );
		btnSpecBrowse.setVisible( ! tmp.exists() );
		fFileName.setText(spec.getFileName());
		fDriver.setText(spec.getDriverName());
		fJarFile.setText(spec.getJarFileName());
		fURLForm.setText(spec.getURL());
		fDisplayName.setText(spec.getPoliteName());
		fIDEManager.setText(spec.getDbIntfClassName());
		fDefaultCatalog.setText(spec.getDefaultCatalog());
		setConnectionPropsModel();
		updateMessageLabel();
		setModal(true);
	};

	/**
	 * writeToSpec() updates the Database spec with the UI's edited contents.
	 *
	*/
	public void writeToSpec() {
		spec.setDriverName(fDriver.getText());
		spec.setJarFileName(fJarFile.getText());
		spec.setURL(fURLForm.getText());
		spec.setPoliteName(fDisplayName.getText());
		spec.setDbIntfClassName(fIDEManager.getText());
		spec.setDefaultCatalog(fDefaultCatalog.getText());
		applyfromModel();
	}

	public DlgDBSpecConfig( JFrame frame, DatabaseSpec s ) {
		super( frame );
		spec = s;
		readFromSpec();
		if ( frame != null ) frame.setIconImage( ProgramIcons.getInstance().getDatabaseIcon().getImage() );
		jbInit();
		updateMessageLabel();
		btnSpecBrowse.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
				fFileName.setText(selectFileName(fFileName.getText(), DatabaseSpec.prop_db_configsuffix, "Database Properties File", false));
		}
		});
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
		pnlJDBC.setLayout(gbagJDBC);
		pnlConnection.setLayout(borderLayout7);
		btnJarBrowse.setEnabled(true);
		btnJarBrowse.setFont(new java.awt.Font("Dialog", 1, 9));
		btnJarBrowse.setMnemonic('B');
		btnJarBrowse.setText("Browse...");
		pnlJarFileName.setLayout(borderLayout2);
		lblDriver.setLabelFor(fDriver);
		lblDriver.setText("Driver: ");
		pnlDriver.setLayout(borderLayout3);
		lblDriverJarFile.setEnabled(false);
		lblDriverJarFile.setLabelFor(fJarFile);
		lblDriverJarFile.setText("JAR File Name: ");
		lblURLForm.setLabelFor(fURLForm);
		lblURLForm.setText("URL:");
		lblIdeManager.setToolTipText("");
		lblIdeManager.setLabelFor(fIDEManager);
		lblIdeManager.setText("IDE Manager: ");
		pnlURLForm.setLayout(borderLayout5);
		pnlIDEManager.setLayout(borderLayout6);
		fIDEManager.setFocusAccelerator('I');
		fJarFile.setEnabled(false);
		fJarFile.setFocusAccelerator('F');
		fURLForm.setFocusAccelerator('U');
		fDriver.setFocusAccelerator('D');
		fDriver.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(FocusEvent e) {
				fDriver_focusLost(e);
				}
		});
		lblConnection.setText("Connection Parameters");
		jPanel2.setLayout(borderLayout8);
		pnlJarFileName.setEnabled(true);
		btnNewConnection.setMnemonic('N');
		btnNewConnection.setText("New");
		jPanel3.setLayout(gridBagLayout1);
		btnDeleteConnection.setMnemonic('D');
		btnDeleteConnection.setText("Delete");

		lblDriverMessage.setText("This driver is neat");
		lblPoliteName.setLabelFor(fDisplayName);
		lblPoliteName.setText("Display Name: ");
		pnlPoliteName.setLayout(borderLayout4);
		jPanel5.setLayout(borderLayout10);
		lblFileName.setLabelFor(fFileName);
		lblFileName.setText("File Name: ");
		fFileName.setToolTipText("");
		fFileName.setFocusAccelerator('n');
		btnSpecBrowse.setFont(new java.awt.Font("Dialog", 1, 9));
		btnSpecBrowse.setText("Browse...");
		jLabel1.setDisplayedMnemonic('D');
		jLabel1.setLabelFor(fDefaultCatalog);
		jLabel1.setText("Default Catalog: ");
		jPanel4.setLayout(borderLayout9);
		this.getContentPane().add(pnlOkCancel, BorderLayout.SOUTH);
		pnlOkCancel.add(btnOk, null);
		pnlOkCancel.add(btnCancel, null);
		this.getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
		pnlDriver.add(lblDriver, BorderLayout.WEST);
		pnlDriver.add(fDriver, BorderLayout.CENTER);
		pnlJDBC.add(pnlURLForm,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 250, 0));
		pnlJDBC.add(pnlDriver,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 250, 0));
		pnlURLForm.add(lblURLForm, BorderLayout.NORTH);
		pnlURLForm.add(fURLForm, BorderLayout.CENTER);
		pnlJDBC.add(pnlIDEManager,  new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 250, 0));
		pnlIDEManager.add(lblIdeManager, BorderLayout.WEST);
		pnlIDEManager.add(fIDEManager, BorderLayout.CENTER);
		pnlJDBC.add(pnlJarFileName,  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 0), 250, 0));
		pnlJarFileName.add(btnJarBrowse, BorderLayout.EAST);
		pnlJarFileName.add(fJarFile, BorderLayout.CENTER);
		pnlJarFileName.add(lblDriverJarFile, BorderLayout.NORTH);
		pnlJDBC.add(pnlPoliteName,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		pnlPoliteName.add(lblPoliteName, BorderLayout.WEST);
		pnlPoliteName.add(fDisplayName, BorderLayout.CENTER);
		pnlJDBC.add(jPanel5,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jPanel5.add(lblFileName, BorderLayout.WEST);
		jPanel5.add(fFileName, BorderLayout.CENTER);
		jPanel5.add(btnSpecBrowse, BorderLayout.EAST);
		pnlJDBC.add(jPanel4,   new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jPanel4.add(fDefaultCatalog, BorderLayout.CENTER);
		jPanel4.add(jLabel1,  BorderLayout.WEST);
		pnlJDBC.add(lblDriverMessage,  new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
		pnlConnection.add(lblConnection, BorderLayout.NORTH);
		pnlConnection.add(jPanel2, BorderLayout.SOUTH);
		pnlConnection.add(jPanel3, BorderLayout.EAST);
		jPanel3.add(btnNewConnection, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 12, 0));
		jPanel3.add(btnDeleteConnection, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 143, 5), 0, 0));
		pnlConnection.add(jScrollPane1, BorderLayout.CENTER);
		jTabbedPane1.add(pnlJDBC,  "JDBC");
		jTabbedPane1.add(pnlConnection, "Connection");
		jScrollPane1.getViewport().add(tblConnectionParams, null);
		getContentPane().add(jPanel1, BorderLayout.NORTH);

		setConnectionPropsModel();

	}

	// Test function
	public static void main( String[] args ) {
		showConfigurationDialog( null, DatabaseSpecFactory.createDatabaseSpec() );
	}

	/**
	 * Shows the configuration dialog.
	 */
	public static void showConfigurationDialog(JFrame frame, DatabaseSpec s) {

//		JFrame frame = new JFrame("Database Spec: "+s.getPoliteName());
		DlgDBSpecConfig configuration = new DlgDBSpecConfig( frame , s );
		configuration.setModal(true);
		configuration.setTitle("Database Spec: "+s.getPoliteName());
		Dimension screen = configuration.getToolkit().getScreenSize();
		configuration.pack();
		Dimension configDim = configuration.getSize();

		int x = new Double(( screen.getWidth() - configDim.getWidth() )/ 2).intValue();
		int y = new Double((screen.getHeight() - configDim.getHeight()) / 2).intValue();
		configuration.setLocation(x,y);
		configuration.updateMessageLabel();
		configuration.setVisible(true);
	}

	public String selectFileName( String fileName, String extension, String description, boolean allowDirs ) {
		String outFileName = fileName;
		JFileChooser fc = new JFileChooser(outFileName);
		fc.setFileFilter( new myFileFilter( description, extension, allowDirs ) );
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION ) {
			try {
				outFileName = fc.getSelectedFile().getCanonicalPath();
			} catch( IOException io ) {
				System.err.println("Error getting canonical path. Will return absolute path");
				outFileName = fc.getSelectedFile().getAbsolutePath();
			}
		}
		return(outFileName);
	}

	private class myFileFilter extends javax.swing.filechooser.FileFilter {
		String desc;
		String ext;
		boolean allowdir;
		public myFileFilter( String description, String extension, boolean dirs ) {
				desc = description;
			ext = extension;
				allowdir = dirs;
		}

		public String getDescription() { return(desc); };

		public boolean accept( File f ) {
				boolean bAccept = false;
				bAccept = f.toString().endsWith(ext);
				if ( allowdir ) bAccept = bAccept | f.isDirectory();
				return( bAccept );
		}
	}


	private void setConnectionPropsModel() {
		connModel = new ConnPropertiesTableModel(spec.getConnectionProperties(), "Parameter");
		tblConnectionParams.setModel(connModel);
	}

	/**
	 * TODO: Apply the data model for connection Properties
	 */
	private void applyfromModel() {
		spec.setConnectionProperties(connModel.getProperties());
	}

	class OKAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) { writeToSpec(); dispose(); }
	}

	class CancelAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) { dispose(); }
	}

	class NewConnectionAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			connModel.addRow();
		}
	}

	class DeleteConnectionAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			connModel.removeRow(tblConnectionParams.getSelectedRow());
		}
	}
	void fDriver_focusLost(FocusEvent e) {
		spec.setDriverName(fDriver.getText());
		updateMessageLabel();
	}

	void updateMessageLabel() {
		String userMessage = spec.getUserMessage();
		lblDriverMessage.setText(userMessage);
		if ( userMessage.toLowerCase().startsWith("warning:") ) {
				lblDriverMessage.setForeground(new Color(255, 0, 0));
		} else {
			lblDriverMessage.setForeground(lblDriver.getForeground());
		}
	}

	class ActionBrowseForJar extends AbstractAction {
		public void actionPerformed(ActionEvent evt) {
			fJarFile.setText(selectFileName(fJarFile.getText(),".jar", "Java Archive File", true));
		}
	}

}

/*

  $Log$
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
