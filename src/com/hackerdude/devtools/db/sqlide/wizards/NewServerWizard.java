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

import com.hackerdude.lib.ui.*;
import java.util.HashMap;
import com.hackerdude.devtools.db.sqlide.*;
import com.hackerdude.devtools.db.sqlide.dataaccess.*;
import java.awt.Dimension;
import java.io.File;


/**
 * This is a server Wizard used to create new servers.
 */
public class NewServerWizard extends Wizard {

   NewServerWizSelectServerType pageNewServer;
   ServerDetailsWizardPage      pageServerDetails;
   DatabaseSpec spec;

   public NewServerWizard(boolean modal) {
	  super("New Server Profile",
		"This wizard will guide you step by step on how to add a server profile "
	   +"to your configuration.", modal
		 );
	  pageNewServer = new NewServerWizSelectServerType();
	  pageServerDetails = new ServerDetailsWizardPage();
	  pageNewServer.setWizard(this);
	  pageServerDetails.setWizard(this);
	  WizardPage[] pages = new WizardPage[2];
	  pages[0] = pageNewServer;
	  pages[1] = pageServerDetails;
	File defaultFile = new File(DatabaseSpec.DEFAULT_DBSPEC_FILENAME);
	if ( ! defaultFile.exists() ) {
		setFileName(DatabaseSpec.DEFAULT_DBSPEC_FILENAME);
		pageNewServer.disableFileSelection();
	}
	  setPages(pages);

   }

   public DatabaseSpec getDBSpec() { return spec; }

   public void setFileName(String fileName) {
	  pageNewServer.fFileName.setText(fileName);
   }

   public void setServerType(String serverType) {
	  pageServerDetails.setServerType(serverType);
   }

   public void setJDBCURL(String URL) {
	  pageNewServer.setURL(URL);
   }

   public void setClassName(String className) {
	  pageNewServer.setClassName(className);
   }

   public void setProperties(HashMap properties) {
	  pageServerDetails.setServerProperties(properties);
   }

   public void setServerTitle(String title) {
	pageNewServer.setServerTitle(title);
   }

   public void doneWizard() {
	  spec = DatabaseSpecFactory.createDatabaseSpec();
	  spec.setFileName(pageNewServer.fFileName.getText());
	  spec.setURL(pageNewServer.fURL.getText());
	  spec.setPoliteName(pageNewServer.cmbServerType.getSelectedItem().toString()+" on "+pageNewServer.fHostName.getText());
	  spec.setDriverName(pageNewServer.fClassName.getText());
	  spec.setConnectionProperties(pageServerDetails.propertiesModel.getProperties());
	  spec.setDefaultCatalog(pageNewServer.fCatalogName.getText());
	  setVisible(false);
   }

   public String getFileName() {
	  return pageNewServer.fFileName.getText();
   }

   public static NewServerWizard showWizard(boolean modal) {
	  NewServerWizard wiz = new NewServerWizard(modal);
	  wiz.setEnabled(true);
	  wiz.pack();
	Dimension screen = wiz.getToolkit().getScreenSize();
	wiz.setLocation( ( screen.getSize().width - wiz.getSize().width) / 2,(screen.getSize().height - wiz.getSize().height) / 2);
	  wiz.show();
	  return wiz;
   }

   public static void main(String[] args) {
	  showWizard(true);
   }
}