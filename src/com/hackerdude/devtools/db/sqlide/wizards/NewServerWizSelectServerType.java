/**
 * Title:        JSqlIde<p>
 * Description:  A Java SQL Integrated Development Environment
 * <p>
 * Copyright:    Copyright (c) David Martinez<p>
 * Company:      <p>
 * @author David Martinez
 * @version 1.0
 */
package com.hackerdude.devtools.db.sqlide.wizards;

import com.hackerdude.devtools.db.sqlide.*;
import com.hackerdude.lib.ui.*;
import com.hackerdude.lib.*;
import com.hackerdude.devtools.db.sqlide.servertypes.*;
import com.hackerdude.devtools.db.sqlide.dataaccess.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.sql.Driver;
import java.beans.*;

/**
 * New Server wizard - Select server type page.
 */
public class NewServerWizSelectServerType extends WizardPage {
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel jPanel1 = new JPanel();
	JLabel jLabel1 = new JLabel();
	BorderLayout borderLayout2 = new BorderLayout();
	JPanel jPanel2 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JPanel jPanel3 = new JPanel();
	JLabel jLabel2 = new JLabel();
	JComboBox cmbServerType = new JComboBox();
	BorderLayout borderLayout3 = new BorderLayout();
	JPanel jPanel4 = new JPanel();
	BorderLayout borderLayout4 = new BorderLayout();
	JLabel lblHostName = new JLabel();
	JTextField fHostName = new JTextField();
	JPanel jPanel5 = new JPanel();
	BorderLayout borderLayout5 = new BorderLayout();
	JLabel lblURL = new JLabel();
	JTextField fURL = new JTextField();
	JPanel jPanel6 = new JPanel();
	BorderLayout borderLayout6 = new BorderLayout();
	JLabel lblClassName = new JLabel();
	JTextField fClassName = new JTextField();
	JPanel jPanel7 = new JPanel();
	JLabel lblFileName = new JLabel();
	JTextField fFileName = new JTextField();
	BorderLayout borderLayout7 = new BorderLayout();
	JButton btnBrowse = new JButton();
	JPanel jPanel8 = new JPanel();
	BorderLayout borderLayout8 = new BorderLayout();
	JLabel jLabel3 = new JLabel();
	JTextField fServerTitle = new JTextField();
	JLabel lblDriverInfo = new JLabel();
	JPanel jPanel9 = new JPanel();
	BorderLayout borderLayout9 = new BorderLayout();
	JTextField fCatalogName = new JTextField();
	JLabel jLabel4 = new JLabel();


   public NewServerWizSelectServerType() {
	  try {
		 jbInit();
	  }
	  catch(Exception e) {
		 e.printStackTrace();
	  }
   }

   private void jbInit() throws Exception {
	  this.setLayout(borderLayout1);
	  jLabel1.setText("Basic Server Information");
	  jPanel1.setLayout(borderLayout2);
	  jPanel2.setLayout(gridBagLayout1);
	  jLabel2.setDisplayedMnemonic('T');
	  jLabel2.setLabelFor(cmbServerType);
	  jLabel2.setText("Select Server type:");
	  jPanel3.setLayout(borderLayout3);
	  jPanel4.setLayout(borderLayout4);
	  lblHostName.setDisplayedMnemonic('N');
	  lblHostName.setLabelFor(fHostName);
	  lblHostName.setText("Host Name:");
	  fHostName.setText("localhost");
	  cmbServerType.addActionListener(new java.awt.event.ActionListener() {
		 public void actionPerformed(ActionEvent e) {
			cmbServerType_actionPerformed(e);
		 }
	  });
	  jPanel5.setLayout(borderLayout5);
	  lblURL.setDisplayedMnemonic('U');
	  lblURL.setText("URL: ");
	  jPanel6.setLayout(borderLayout6);
	  lblClassName.setDisplayedMnemonic('C');
	  lblClassName.setText("Class Name: ");
	  lblFileName.setDisplayedMnemonic('F');
	  lblFileName.setLabelFor(fFileName);
	  lblFileName.setText("File Name:");
	  fFileName.setToolTipText("");
	  jPanel7.setLayout(borderLayout7);
	  btnBrowse.setFont(new java.awt.Font("Dialog", 1, 10));
	  btnBrowse.setMnemonic('B');
	  btnBrowse.setText("Browse...");
	  btnBrowse.addActionListener(new java.awt.event.ActionListener() {
		 public void actionPerformed(ActionEvent e) {
			btnBrowse_actionPerformed(e);
		 }
	  });


/*      fClassName.addActionListener(new java.awt.event.ActionListener() {
		 public void actionPerformed(ActionEvent ev) {
			if ( wizard != null ) wizard.updateControlState();
		 }
	  });
	  fFileName.addActionListener(new java.awt.event.ActionListener() {
		 public void actionPerformed(ActionEvent ev) {
			if ( wizard != null ) wizard.updateControlState();
		 }
	  });*/

	jPanel8.setLayout(borderLayout8);
	jLabel3.setToolTipText("");
	jLabel3.setDisplayedMnemonic('S');
	jLabel3.setLabelFor(fServerTitle);
	jLabel3.setText("Server Title: ");
	lblDriverInfo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDriverInfo.setText("Driver Version: 0.0");
		fClassName.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				fClassName_propertyChange(e);
			}
		});
		jPanel9.setLayout(borderLayout9);
		jLabel4.setDisplayedMnemonic('D');
		jLabel4.setLabelFor(fCatalogName);
		jLabel4.setText("Default Catalog: ");
		this.add(jPanel1, BorderLayout.NORTH);
	jPanel1.add(jLabel1, BorderLayout.CENTER);
	this.add(jPanel2, BorderLayout.CENTER);
	jPanel2.add(jPanel3,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 100, 0));
	jPanel3.add(jLabel2, BorderLayout.WEST);
	jPanel3.add(cmbServerType, BorderLayout.CENTER);
	jPanel2.add(jPanel4,     new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 100, 0));
	jPanel4.add(lblHostName, BorderLayout.WEST);
	jPanel4.add(fHostName, BorderLayout.CENTER);
	jPanel2.add(jPanel5,     new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 100, 0));
	jPanel5.add(lblURL, BorderLayout.WEST);
	jPanel5.add(fURL, BorderLayout.CENTER);
	jPanel2.add(jPanel6,     new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 100, 0));
	jPanel6.add(lblClassName, BorderLayout.WEST);
	jPanel6.add(fClassName, BorderLayout.CENTER);
		jPanel7.add(lblFileName, BorderLayout.WEST);
		jPanel7.add(fFileName, BorderLayout.CENTER);
		jPanel7.add(btnBrowse, BorderLayout.EAST);
	jPanel2.add(jPanel8,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 100, 0));
	jPanel8.add(jLabel3, BorderLayout.WEST);
	jPanel8.add(fServerTitle, BorderLayout.CENTER);
		jPanel2.add(lblDriverInfo,     new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jPanel2.add(jPanel9,    new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
			,GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jPanel9.add(fCatalogName, BorderLayout.CENTER);
		jPanel9.add(jLabel4, BorderLayout.WEST);
		jPanel2.add(jPanel7, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
			,GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 100, 0));
	addServerTypes();
   }

   /**
	* Adds all the known server types to the combo boxes.
	*
	*/
   public void addServerTypes() {
		Collection serverTypes = ServerTypeRegistryCache.getInstance().getServerTypes();
		Iterator it = serverTypes.iterator();
		while ( it.hasNext() ) {
			String thisType = (String)it.next();
			try {
				Class thisClass = Class.forName(thisType);
				ServerType newType = (ServerType)thisClass.newInstance();
				cmbServerType.addItem(newType);
			} catch ( Exception exc ) {
				System.out.println("[NewServerWizSelectServerType] Warning: Could not load/instantiate "+thisType);
			}
		}

   }





   public void setURL(String url) {
	  fURL.setText(url);
   }

   public void setClassName(String className) {
	  fClassName.setText(className);
   }

   public void setServerTitle(String title) {
	fServerTitle.setText(title);
   }


   void cmbServerType_actionPerformed(ActionEvent e) {
	  if ( wizard != null ) {
		 NewServerWizard wiz = (NewServerWizard)wizard;
		 ServerType selectedItem = (ServerType)cmbServerType.getSelectedItem();
		 wiz.setServerType(selectedItem.toString());
		 wiz.setJDBCURL(selectedItem.getURL());
		 wiz.setServerTitle(selectedItem.getServerTitle());
		 wiz.setProperties(selectedItem.getProperties());
		 wiz.setClassName(selectedItem.getClassName());
		 fCatalogName.setText(selectedItem.getDefaultCatalog());
		 updateDriverInfo();
		 wiz.updateControlState();
	  }
   }

   public void updateDriverInfo() {
	  try {
		Driver drv = (Driver)Class.forName(fClassName.getText()).newInstance();
		String driverVersion = "Driver V"+drv.getMajorVersion()+"."+drv.getMinorVersion();
		if ( ! drv.jdbcCompliant() ) {
			driverVersion = driverVersion+" WARNING: Not fully JDBC compliant";
			lblDriverInfo.setForeground(Color.red);
		} else {
			lblDriverInfo.setForeground(lblClassName.getForeground());
		}
		lblDriverInfo.setText(driverVersion);

	  } catch ( Exception exc ) {
		lblDriverInfo.setText("ERROR: Unable to instantiate - Please add driver JAR to classpath");
		lblDriverInfo.setForeground(Color.red);
	  }
   }


   void btnBrowse_actionPerformed(ActionEvent e) {
	 executeFileSelection();
   }

  public void executeFileSelection() {
	 JFileChooser saveFileChooser;
	 saveFileChooser = new JFileChooser();
	 saveFileChooser.setDialogTitle("Save SQL File");
	 saveFileChooser.setFileFilter(getFileFilter());
	 saveFileChooser.setCurrentDirectory(new File(ProgramConfig.getInstance().getPropsPath()));
	 saveFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

	int selected = saveFileChooser.showSaveDialog(this);
	if ( selected == saveFileChooser.APPROVE_OPTION ) {
	   File selectedFile = saveFileChooser.getSelectedFile();
	   if ( ( ! selectedFile.exists() ) && selectedFile.getName().indexOf(".") < 0 ) { selectedFile = new File(selectedFile.getAbsolutePath()+".db.xml"); }
		fFileName.setText(selectedFile.getAbsolutePath());
		if ( wizard != null ) wizard.updateControlState();
	}

  }

  public boolean nextPageOK() {
	 String className = fClassName.getText();
	 String fileName = fFileName.getText();
	 return ( ! className.equals("") && ! fileName.equals("") );
  }

  public javax.swing.filechooser.FileFilter getFileFilter() {

	 String exFFSuffix = DatabaseSpec.prop_db_configsuffix;
	 if ( exFFSuffix.startsWith(".") ) exFFSuffix = exFFSuffix.substring(1);
	 String[] filters = { exFFSuffix };
	 String description = "SQLIDE Configuration Files";
	 ExtensionFileFilter flt = new ExtensionFileFilter(filters, description);
	 return flt;
  }

   public void toNextPage() throws VetoWizardPageChange {
	  File selectedFile = new File ( fFileName.getText() );
	   if (  selectedFile.exists() &&
		   ! (JOptionPane.showConfirmDialog(this, "Are you sure you want to overwrite file "+selectedFile.getName()+"?",
			  "Overwrite File?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION ) ) {
			 throw new VetoWizardPageChange();
	   }
   }

   public boolean isFileSelectionEnabled() { return fFileName.isEnabled(); }

   public void enableFileSelection() { fFileName.enable(); btnBrowse.enable(); }

   public void disableFileSelection() { fFileName.disable(); btnBrowse.disable(); }

	void fClassName_propertyChange(PropertyChangeEvent e) {
		updateDriverInfo();
	}


}