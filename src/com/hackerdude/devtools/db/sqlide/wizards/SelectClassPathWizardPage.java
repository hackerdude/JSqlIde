package com.hackerdude.devtools.db.sqlide.wizards;

import com.hackerdude.lib.ui.WizardPage;
import com.hackerdude.devtools.db.sqlide.dialogs.*;
import java.awt.*;
import com.hackerdude.devtools.db.sqlide.dataaccess.ConnectionConfig;

public class SelectClassPathWizardPage extends WizardPage {

    private BorderLayout borderLayout1 = new BorderLayout();
	ConnectionClassPathPanel classpathPanel = new ConnectionClassPathPanel();

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
		add(classpathPanel, BorderLayout.CENTER);
    }


	public void setDatabaseSpec(ConnectionConfig spec) {
		classpathPanel.setDatabaseSpec(spec);
	}


	public void toNextPage() {

		classpathPanel.applyToModel();

	}
}