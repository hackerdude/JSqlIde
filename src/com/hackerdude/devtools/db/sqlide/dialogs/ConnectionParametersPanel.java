package com.hackerdude.devtools.db.sqlide.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.hackerdude.devtools.db.sqlide.dataaccess.ConnectionConfig;

public class ConnectionParametersPanel extends JPanel {

	public final Action ACTION_NEW_CONNECTION = new NewConnectionAction();
	public final Action ACTION_DELETE_CONNECTION = new DeleteConnectionAction();

	ConnPropertiesTableModel connModel = null;
	ConnectionConfig connectionConfig;

    private BorderLayout borderLayout1 = new BorderLayout();
    private JButton btnDeleteConnection = new JButton(ACTION_DELETE_CONNECTION);
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JPanel jPanel3 = new JPanel();
    private JPanel jPanel2 = new JPanel();
    private JTable tblConnectionParams = new JTable();
    private JButton btnNewConnection = new JButton(ACTION_NEW_CONNECTION);
    private JPanel pnlConnection = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JLabel lblConnection = new JLabel();
    private BorderLayout borderLayout8 = new BorderLayout();
    private BorderLayout borderLayout7 = new BorderLayout();

    public ConnectionParametersPanel() {
        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
        jPanel3.setLayout(gridBagLayout1);
        btnDeleteConnection.setText("Delete");
        btnDeleteConnection.setMnemonic('D');
        this.setLayout(borderLayout1);
        jPanel2.setLayout(borderLayout8);
        btnNewConnection.setMnemonic('N');
        btnNewConnection.setText("New");
        pnlConnection.setLayout(borderLayout7);
        lblConnection.setText("Connection Parameters");
        pnlConnection.add(lblConnection, BorderLayout.NORTH);
        pnlConnection.add(jPanel2, BorderLayout.SOUTH);
        pnlConnection.add(jPanel3, BorderLayout.EAST);
        jPanel3.add(btnNewConnection, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 12, 0));
        jPanel3.add(btnDeleteConnection, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 143, 5), 0, 0));
        pnlConnection.add(jScrollPane1, BorderLayout.CENTER);
        jScrollPane1.getViewport().add(tblConnectionParams, null);
        this.add(pnlConnection, BorderLayout.CENTER);
    }


	class NewConnectionAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			connModel.addRow();
		}
	}

	class DeleteConnectionAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			connModel.removeRow(tblConnectionParams.getSelectedRow());
		}
	}

	public void setConnectionConfig(ConnectionConfig databaseSpec) {
		this.connectionConfig = databaseSpec;
	}

	public void readFromModel() {
		connModel = new ConnPropertiesTableModel(connectionConfig.getConnectionProperties(), "Parameter");
		tblConnectionParams.setModel(connModel);
	}


	/**
	 * TODO: Apply the data model for connection Properties
	 */
	public void applyToModel() {
		connectionConfig.setConnectionProperties(connModel.getProperties());
	}

}