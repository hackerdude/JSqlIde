/**
 * Title:        JSqlIde<p>
 * Description:  A Java SQL Integrated Development Environment
 * <p>
 * Copyright:    Copyright (c) David Martinez<p>
 * Company:      <p>
 * @author David Martinez
 * @version 1.0
 */
package com.hackerdude.apps.sqlide.wizards;

import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;

import javax.swing.JFrame;

import com.hackerdude.apps.sqlide.ProgramConfig;
import com.hackerdude.apps.sqlide.SqlIdeApplication;
import com.hackerdude.apps.sqlide.xml.HostConfigFactory;
import com.hackerdude.apps.sqlide.xml.hostconfig.SqlideHostConfig;
import com.hackerdude.lib.ui.Wizard;
import com.hackerdude.lib.ui.WizardPage;



/**
 * This is a server Wizard used to create new servers.
 */
public class NewServerWizard extends Wizard {

	NewServerWizSelectServerType pageNewServer;
	ServerDetailsWizardPage      pageServerDetails;
	SelectClassPathWizardPage    pageSelectClassPath;

	SqlideHostConfig databaseSpec;

	public NewServerWizard(JFrame owner, boolean modal) {
		super(owner, "New Server Profile",
			  "Add a new server profile to your environment", modal );
		databaseSpec = HostConfigFactory.createHostConfig();
		pageNewServer = new NewServerWizSelectServerType();
		pageServerDetails = new ServerDetailsWizardPage();
		pageSelectClassPath = new SelectClassPathWizardPage();
		pageServerDetails.setWizard(this);
		pageSelectClassPath.setWizard(this);
		pageNewServer.setWizard(this);
		pageNewServer.setDatabaseSpec(databaseSpec);
//		pageNewServer.cmbServerType.setSelectedIndex(0);
		pageSelectClassPath.setDatabaseSpec(databaseSpec);
		WizardPage[] pages = new WizardPage[3];
		pages[0] = pageSelectClassPath;
		pages[1] = pageNewServer;
		pages[2] = pageServerDetails;
		File defaultFile = new File(HostConfigFactory.DEFAULT_DBSPEC);
		if ( ! defaultFile.exists() ) {
			setFileName(HostConfigFactory.DEFAULT_DBSPEC);
			pageNewServer.setFileNameEnabled(false);
		}
		setPages(pages);
	}

	public SqlideHostConfig getDBSpec() { return databaseSpec; }

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
		/** @todo Resolve this correctly. fFileName is only a base filename. It no longer has a path or anything. */
		String baseFileName = pageNewServer.fFileName.getText();
		String fileName = ProgramConfig.getUserProfilePath()+baseFileName+".db.xml";
		databaseSpec.setFileName(fileName);
		databaseSpec.getJdbc().setUrl(pageNewServer.fURL.getText());
		databaseSpec.setName(pageNewServer.cmbServerType.getSelectedItem().toString()+" on "+pageNewServer.fHostName.getText());
		databaseSpec.getJdbc().setDriver(pageNewServer.fClassName.getText());
		databaseSpec.getJdbc().setConnectionProperties(HostConfigFactory.mapToConnectionProperties(pageServerDetails.propertiesModel.getProperties()));
//		databaseSpec.setDefaultCatalog(pageNewServer.fCatalogName.getText());
		setVisible(false);
	}
	public String getFileName() {
		return pageNewServer.fFileName.getText();
	}

	public static NewServerWizard showWizard(boolean modal) {
		NewServerWizard wiz = new NewServerWizard(SqlIdeApplication.getFrame(), modal);
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
