package com.hackerdude.devtools.db.sqlide.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.hackerdude.devtools.db.sqlide.dataaccess.ConnectionConfig;

public class ConnectionJDBCPanel extends JPanel {


	ConnectionConfig databaseSpec;

    private BorderLayout mainBL = new BorderLayout();
    private JTextField fFileName = new JTextField();
    private JPanel pnlPoliteName = new JPanel();
    private JPanel jPanel5 = new JPanel();
    private JPanel jPanel4 = new JPanel();
    private JPanel pnlIDEManager = new JPanel();
    private JLabel lblIdeManager = new JLabel();
    private JPanel pnlURLForm = new JPanel();
    private JTextField fIDEManager = new JTextField();
    private JLabel lblPoliteName = new JLabel();
    private JTextField fDefaultCatalog = new JTextField();
    private JLabel lblDriverMessage = new JLabel();
    private JTextField fURLForm = new JTextField();
    private JPanel pnlJDBC = new JPanel();
    private BorderLayout borderLayout10 = new BorderLayout();
    private GridBagLayout gbagJDBC = new GridBagLayout();
    private JPanel pnlDriver = new JPanel();
    private JLabel lblFileName = new JLabel();
    private JLabel lblDriver = new JLabel();
    private JTextField fDriver = new JTextField();
    private BorderLayout borderLayout9 = new BorderLayout();
    private JButton btnSpecBrowse = new JButton();
    private BorderLayout borderLayout6 = new BorderLayout();
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
        fFileName.setFocusAccelerator('n');
        fFileName.setToolTipText("");
        this.setLayout(mainBL);
        jPanel5.setLayout(borderLayout10);
        jPanel4.setLayout(borderLayout9);
        pnlIDEManager.setLayout(borderLayout6);
        lblIdeManager.setToolTipText("");
        lblIdeManager.setLabelFor(fIDEManager);
        lblIdeManager.setText("IDE Manager: ");
        pnlURLForm.setLayout(borderLayout5);
        fIDEManager.setFocusAccelerator('I');
        lblPoliteName.setLabelFor(fDisplayName);
        lblPoliteName.setText("Display Name: ");
        lblDriverMessage.setText("This driver is neat");
        fURLForm.setFocusAccelerator('U');
        pnlJDBC.setLayout(gbagJDBC);
        pnlDriver.setLayout(borderLayout3);
        lblFileName.setLabelFor(fFileName);
        lblFileName.setText("File Name: ");
        lblDriver.setLabelFor(fDriver);
        lblDriver.setText("Driver: ");
        fDriver.setFocusAccelerator('D');
        fDriver.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(FocusEvent e) {
				fDriver_focusLost(e);
				}
		});
        btnSpecBrowse.setFont(new java.awt.Font("Dialog", 1, 9));
        btnSpecBrowse.setText("Browse...");
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
        jPanel5.add(lblFileName, BorderLayout.WEST);
        jPanel5.add(fFileName, BorderLayout.CENTER);
        jPanel5.add(btnSpecBrowse, BorderLayout.EAST);
        jPanel4.add(fDefaultCatalog, BorderLayout.CENTER);
        jPanel4.add(jLabel1, BorderLayout.WEST);
        pnlIDEManager.add(lblIdeManager, BorderLayout.WEST);
        pnlIDEManager.add(fIDEManager, BorderLayout.CENTER);
        pnlURLForm.add(lblURLForm, BorderLayout.NORTH);
        pnlURLForm.add(fURLForm, BorderLayout.CENTER);
        pnlJDBC.add(pnlURLForm,        new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 250, 0));
        pnlJDBC.add(pnlDriver,        new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 250, 0));
        pnlJDBC.add(pnlIDEManager,        new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 250, 0));
        pnlJDBC.add(pnlPoliteName,        new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        pnlJDBC.add(jPanel5,        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        pnlJDBC.add(jPanel4,        new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        pnlJDBC.add(pnlSQLGenerationPanel,        new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        pnlSQLGenerationPanel.add(lblSQLGeneration,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        pnlSQLGenerationPanel.add(cbSupportsDotNotation, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pnlJDBC.add(lblDriverMessage,  new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
        pnlDriver.add(lblDriver, BorderLayout.WEST);
        pnlDriver.add(fDriver, BorderLayout.CENTER);
		add(pnlJDBC);
    }
    void fDriver_focusGained(FocusEvent e) {

    }

	public void setDatabaseSpec(ConnectionConfig spec) {
		this.databaseSpec = spec;
	}

	public void applyToModel() {
		databaseSpec.setDriverClassName(fDriver.getText());
		databaseSpec.setPoliteName(fDisplayName.getText());
		databaseSpec.setDefaultCatalog(fDefaultCatalog.getText());
		databaseSpec.setFileName(fFileName.getText());
		databaseSpec.setDbIntfClassName(fIDEManager.getText());
		databaseSpec.setJDBCURL(fURLForm.getText());
		databaseSpec.setSupportsDotNotation(cbSupportsDotNotation.isSelected());
	}


	public void readFromModel() {
		fDriver.setText(databaseSpec.getDriverClassName());
		fDisplayName.setText(databaseSpec.getPoliteName());
		fDefaultCatalog.setText(databaseSpec.getDefaultCatalog());
		fFileName.setText(databaseSpec.getFileName());
		fIDEManager.setText(databaseSpec.getDbIntfClassName());
		fURLForm.setText(databaseSpec.getJDBCURL());
		cbSupportsDotNotation.setSelected(databaseSpec.isSupportsDotNotation());
		updateMessageLabel();
	}

	void fDriver_focusLost(FocusEvent e) {
		databaseSpec.setDriverClassName(fDriver.getText());
		updateMessageLabel();
	}

	void updateMessageLabel() {
		String userMessage = databaseSpec.getUserMessage();
		lblDriverMessage.setText(userMessage);
		if ( userMessage.toLowerCase().startsWith("warning:") ) {
				lblDriverMessage.setForeground(new Color(255, 0, 0));
		} else {
			lblDriverMessage.setForeground(lblDriver.getForeground());
		}
	}


}