package com.hackerdude.apps.sqlide.dialogs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.hackerdude.apps.sqlide.xml.HostConfigFactory;
import com.hackerdude.apps.sqlide.xml.hostconfig.SqlideHostConfig;

/**
 * Panel that allows the user to specify the connection parameters.
 * @author David Martinez
 * @version 1.0
 */
public class ConnectionParametersPanel extends JPanel {

	public final Action ACTION_NEW_CONNECTION = new NewConnectionAction();
	public final Action ACTION_DELETE_CONNECTION = new DeleteConnectionAction();

	ConnPropertiesTableModel connModel = null;
	SqlideHostConfig SqlideHostConfig;

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

	public void setSqlideHostConfig(SqlideHostConfig databaseSpec) {
		this.SqlideHostConfig = databaseSpec;
	}

	public void readFromModel() {
		connModel = new ConnPropertiesTableModel(HostConfigFactory.connectionPropertiesToMap(SqlideHostConfig.getJdbc().getConnectionProperties()), "Parameter");
		tblConnectionParams.setModel(connModel);
	}


	/**
	 * TODO: Apply the data model for connection Properties
	 */
	public void applyToModel() {
		SqlideHostConfig.getJdbc().setConnectionProperties(HostConfigFactory.mapToConnectionProperties(connModel.getProperties()));
	}

}
