package com.hackerdude.apps.sqlide.wizards;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import com.hackerdude.apps.sqlide.dialogs.ConnectionClassPathPanel;
import com.hackerdude.apps.sqlide.xml.hostconfig.SqlideHostConfig;
import com.hackerdude.lib.ui.WizardPage;

public class SelectClassPathWizardPage extends WizardPage {

    private BorderLayout borderLayout1 = new BorderLayout();
	ConnectionClassPathPanel classpathPanel = new ConnectionClassPathPanel();
    JLabel lblExplanation = new JLabel();

    public SelectClassPathWizardPage() {
        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
		lblExplanation.setText("<HTML><P>JDBC Drivers live in JAR files. Use the Add and remove buttons to set the"+
							   "JAR files that your JDBC Database driver needs to have available while running.");
        add(classpathPanel, BorderLayout.CENTER);
        this.add(lblExplanation, BorderLayout.NORTH);
    }

	public void setDatabaseSpec(SqlideHostConfig spec) {
		classpathPanel.setDatabaseSpec(spec);
	}


	public void toNextPage() {

		classpathPanel.applyToModel();

	}
}
