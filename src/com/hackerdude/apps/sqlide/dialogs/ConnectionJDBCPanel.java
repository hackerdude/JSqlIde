package com.hackerdude.apps.sqlide.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.hackerdude.apps.sqlide.xml.hostconfig.*;


public class ConnectionJDBCPanel extends JPanel {

	SqlideHostConfig databaseSpec;

    private BorderLayout mainBL = new BorderLayout();
    private JPanel pnlPoliteName = new JPanel();
    private JPanel pnlDefaultCatalog = new JPanel();
    private JPanel pnlURLForm = new JPanel();
    private JLabel lblPoliteName = new JLabel();
    private JTextField fDefaultCatalog = new JTextField();
    private JLabel lblDriverMessage = new JLabel();
    private JTextField fURLForm = new JTextField();
    private JPanel pnlJDBC = new JPanel();
    private GridBagLayout gbagJDBC = new GridBagLayout();
    private JPanel pnlDriver = new JPanel();
    private JLabel lblDriver = new JLabel();
    private JTextField fDriver = new JTextField();
    private BorderLayout borderLayout9 = new BorderLayout();
    private BorderLayout borderLayout5 = new BorderLayout();
    private JLabel jLabel1 = new JLabel();
    private BorderLayout borderLayout4 = new BorderLayout();
    private BorderLayout borderLayout3 = new BorderLayout();
    private JLabel lblURLForm = new JLabel();
    private JTextField fDisplayName = new JTextField();
    private JLabel lblSQLGeneration = new JLabel();
    private JPanel pnlSQLGenerationPanel = new JPanel();
    private JCheckBox cbSupportsDotNotation = new JCheckBox();
    private GridBagLayout gridBagLayout2 = new GridBagLayout();

    public ConnectionJDBCPanel() {
        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
        pnlPoliteName.setLayout(borderLayout4);
        this.setLayout(mainBL);
        pnlDefaultCatalog.setLayout(borderLayout9);
        pnlURLForm.setLayout(borderLayout5);
        lblPoliteName.setLabelFor(fDisplayName);
        lblPoliteName.setText("Display Name: ");
        lblDriverMessage.setText("This driver is neat");
        fURLForm.setFocusAccelerator('U');
        pnlJDBC.setLayout(gbagJDBC);
        pnlDriver.setLayout(borderLayout3);
        lblDriver.setLabelFor(fDriver);
        lblDriver.setText("Driver: ");
        fDriver.setFocusAccelerator('D');
        fDriver.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(FocusEvent e) {
				fDriver_focusLost(e);
				}
		});
        jLabel1.setDisplayedMnemonic('D');
        jLabel1.setLabelFor(fDefaultCatalog);
        jLabel1.setText("Default Catalog: ");
        lblURLForm.setLabelFor(fURLForm);
        lblURLForm.setText("URL:");
        lblSQLGeneration.setText("SQL Generation:");
        pnlSQLGenerationPanel.setLayout(gridBagLayout2);
        cbSupportsDotNotation.setText("Dot Notation Supported");
        pnlPoliteName.add(lblPoliteName, BorderLayout.WEST);
        pnlPoliteName.add(fDisplayName, BorderLayout.CENTER);
        pnlDefaultCatalog.add(fDefaultCatalog, BorderLayout.CENTER);
        pnlDefaultCatalog.add(jLabel1, BorderLayout.WEST);
        pnlURLForm.add(lblURLForm, BorderLayout.NORTH);
        pnlURLForm.add(fURLForm, BorderLayout.CENTER);
        pnlJDBC.add(pnlURLForm,         new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 250, 0));
        pnlJDBC.add(pnlDriver,         new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 250, 0));
        pnlJDBC.add(pnlPoliteName,         new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        pnlJDBC.add(pnlDefaultCatalog,         new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        pnlJDBC.add(pnlSQLGenerationPanel,         new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        pnlSQLGenerationPanel.add(lblSQLGeneration,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        pnlSQLGenerationPanel.add(cbSupportsDotNotation, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pnlJDBC.add(lblDriverMessage,   new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
        pnlDriver.add(lblDriver, BorderLayout.WEST);
        pnlDriver.add(fDriver, BorderLayout.CENTER);
		add(pnlJDBC);
    }
    void fDriver_focusGained(FocusEvent e) {

    }

	public void setDatabaseSpec(SqlideHostConfig spec) {
		this.databaseSpec = spec;
	}

	public void applyToModel() {
		databaseSpec.getJdbc().setDriver(fDriver.getText());
		databaseSpec.setName(fDisplayName.getText());
//		databaseSpec.setDefaultCatalog(fDefaultCatalog.getText());
//		databaseSpec.setDbIntfClassName(fIDEManager.getText());
		databaseSpec.getJdbc().setUrl(fURLForm.getText());
		databaseSpec.getGeneral().setSupportsDotNotation(cbSupportsDotNotation.isSelected());
	}


	public void readFromModel() {
		fDriver.setText(databaseSpec.getJdbc().getDriver());
		fDisplayName.setText(databaseSpec.getName());
//		fDefaultCatalog.setText(databaseSpec.getDefaultCatalog());
//		fIDEManager.setText(databaseSpec.getDbIntfClassName());
		fURLForm.setText(databaseSpec.getJdbc().getUrl());
		cbSupportsDotNotation.setSelected(databaseSpec.getGeneral().getSupportsDotNotation());
		updateMessageLabel();
	}

	void fDriver_focusLost(FocusEvent e) {
		databaseSpec.getJdbc().setDriver(fDriver.getText());
		updateMessageLabel();
	}

	void updateMessageLabel() {
		String userMessage = "";//databaseSpec.getUserMessage();/** @todo Reimplement */
		lblDriverMessage.setText(userMessage);
		if ( userMessage.toLowerCase().startsWith("warning:") ) {
				lblDriverMessage.setForeground(new Color(255, 0, 0));
		} else {
			lblDriverMessage.setForeground(lblDriver.getForeground());
		}
	}


}
