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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.hackerdude.apps.sqlide.dialogs.ConnPropertiesTableModel;
import com.hackerdude.lib.ui.WizardPage;

public class ServerDetailsWizardPage extends WizardPage {

	public final Action ACTION_ADD = new AddAction();
	public final Action ACTION_REMOVE = new RemoveAction();

	private String serverType;
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel jPanel1 = new JPanel();
	JLabel jLabel1 = new JLabel();
	BorderLayout borderLayout2 = new BorderLayout();
	JScrollPane jScrollPane1 = new JScrollPane();
	JTable jTable1 = new JTable();
	ConnPropertiesTableModel propertiesModel = new ConnPropertiesTableModel(new HashMap(), "Properties");
	JPanel jPanel2 = new JPanel();
	JButton btnAdd = new JButton(ACTION_ADD);
	JButton btnRemove = new JButton(ACTION_REMOVE);
	GridBagLayout gridBagLayout1 = new GridBagLayout();

	public ServerDetailsWizardPage() {
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

   public void setServerType(String type) {
	  serverType = type;
	  jLabel1.setText(type+" - Connection Parameters");
   }

   public String getServerType() {
	  return serverType;
   }

   private void jbInit() throws Exception {
	  this.setLayout(borderLayout1);
	  jLabel1.setText("Connection Parameters");
	  jPanel1.setLayout(borderLayout2);
	  btnAdd.setText("Add");
	  jPanel2.setLayout(gridBagLayout1);
	  btnRemove.setText("Remove");
	  jScrollPane1.setOpaque(false);
	  this.setToolTipText("");
	  this.add(jPanel1, BorderLayout.NORTH);
	  jPanel1.add(jLabel1, BorderLayout.CENTER);
	  this.add(jScrollPane1, BorderLayout.CENTER);
	  this.add(jPanel2, BorderLayout.EAST);
	  jPanel2.add(btnAdd,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 5, 0, 5), 24, 0));
	  jPanel2.add(btnRemove, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 219, 5), 0, 0));
	  jScrollPane1.getViewport().add(jTable1, null);
	  jTable1.setModel(propertiesModel);
	  jScrollPane1.setOpaque(true);
	  jTable1.getSelectionModel().addListSelectionListener(
		new javax.swing.event.ListSelectionListener() {
		   public void valueChanged(javax.swing.event.ListSelectionEvent ev) {
			  updateControlState();
		   }
		 });

   }

   class AddAction extends AbstractAction {
   public void actionPerformed(ActionEvent e) {
	  propertiesModel.addRow();
   }
   }

   class RemoveAction extends AbstractAction {
	   public void actionPerformed(ActionEvent e) {
		   int selectedRow = jTable1.getSelectedRow();
		   propertiesModel.removeRow(selectedRow);
		   propertiesModel.fireTableDataChanged();
		   if ( selectedRow < jTable1.getRowCount() ) { jTable1.getSelectionModel().setSelectionInterval(selectedRow, selectedRow); }
		   updateControlState();
	   }
   }

   public void updateControlState() {
	 btnRemove.setEnabled(jTable1.getSelectedRow()>-1);
   }

   public void setServerProperties(HashMap hm) {
	  propertiesModel = new ConnPropertiesTableModel(hm, serverType);
	  jTable1.setModel(propertiesModel);
   }


}
