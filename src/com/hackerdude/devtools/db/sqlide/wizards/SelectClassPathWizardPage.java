package com.hackerdude.devtools.db.sqlide.wizards;

import com.hackerdude.lib.ui.WizardPage;
import com.hackerdude.devtools.db.sqlide.dialogs.*;
import java.awt.*;
import com.hackerdude.devtools.db.sqlide.dataaccess.ConnectionConfig;
import javax.swing.*;

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


	public void setDatabaseSpec(ConnectionConfig spec) {
		classpathPanel.setDatabaseSpec(spec);
	}


	public void toNextPage() {

		classpathPanel.applyToModel();

	}
}