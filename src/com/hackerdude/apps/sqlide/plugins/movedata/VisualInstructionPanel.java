package com.hackerdude.apps.sqlide.plugins.movedata;

import com.hackerdude.apps.sqlide.plugins.movedata.model.*;
import java.awt.*;
import javax.swing.*;


/**
 * This is a visual panel that allows you to edit the instructions visually.
 */
public class VisualInstructionPanel extends JPanel {
	MoveDataModel instructionModel = new MoveDataModel();
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel jPanel1 = new JPanel();
	JLabel lblInstructions = new JLabel();
	JScrollPane jScrollPane1 = new JScrollPane();
	JList lstInstructions = new JList();
	JPanel pnlAddRemove = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JPanel jPanel2 = new JPanel();
	JButton buttonAdd = new JButton();
	JPanel jPanel3 = new JPanel();
	JButton jButton2 = new JButton();



	public VisualInstructionPanel() {
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		setDefaultInstructions();
		this.setLayout(borderLayout1);
		lblInstructions.setText("Instructions");
		pnlAddRemove.setLayout(gridBagLayout1);
		buttonAdd.setMnemonic('A');
		buttonAdd.setText("Add");
		jButton2.setMnemonic('R');
		jButton2.setText("Remove");
		this.add(jPanel1, BorderLayout.NORTH);
		jPanel1.add(lblInstructions, null);
		this.add(jScrollPane1, BorderLayout.CENTER);
		this.add(pnlAddRemove, BorderLayout.EAST);
		pnlAddRemove.add(jPanel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jPanel2.add(buttonAdd, null);
		pnlAddRemove.add(jPanel3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanel3.add(jButton2, null);
		jScrollPane1.getViewport().add(lstInstructions, null);
		lstInstructions.setModel(instructionModel);


	}

	public void setDefaultInstructions() {
		DataDestinationNode defDest = new DataDestinationNode();
		defDest.setCatalog("AddrBook2");
		defDest.setCreate(true);
		DataSourceNode defSource = new DataSourceNode();
		defSource.setCatalog( "SampleAddresses");
		DefaultsNode defaults = new DefaultsNode();
		defaults.defaultDestination = defDest;
		defaults.defaultSource      = defSource;
		DataSourceNode stmt = new DataSourceNode();
		stmt.setSelectStatement( "SELECT * FROM CoolStuff, NeatStuff");
		instructionModel.addInstruction(stmt);
		DataSourceNode st2 = new DataSourceNode();
		stmt.setTableName("AddressBook");
		instructionModel.addInstruction(st2);
	}

}
