package com.hackerdude.devtools.db.sqlide.wizards;

import com.hackerdude.lib.ui.WizardPage;
import com.hackerdude.devtools.db.sqlide.dialogs.*;
import java.awt.*;
import com.hackerdude.devtools.db.sqlide.dataaccess.DatabaseSpec;

public class SelectClassPathWizardPage extends WizardPage {

    private BorderLayout borderLayout1 = new BorderLayout();
	DBSpecClassPathPanel classpathPanel = new DBSpecClassPathPanel();

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


	public void setDatabaseSpec(DatabaseSpec spec) {
		classpathPanel.setDatabaseSpec(spec);
	}


	public void toNextPage() {

		classpathPanel.applyToModel();

	}
}