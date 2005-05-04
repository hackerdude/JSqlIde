package com.hackerdude.apps.sqlide.plugins.browser;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import com.hackerdude.apps.sqlide.ProgramIcons;
import com.hackerdude.apps.sqlide.SqlIdeApplication;
import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.dialogs.DlgConnectionConfig;
import com.hackerdude.apps.sqlide.nodes.ItemServerNode;
import com.hackerdude.apps.sqlide.pluginapi.IDENodeContextPluginIF;
import com.hackerdude.apps.sqlide.pluginapi.IDEVisualPluginIF;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEBase;
import com.hackerdude.apps.sqlide.plugins.isql.ContextCommandRunner;
import com.hackerdude.apps.sqlide.plugins.isql.PluginInteractiveSQL;
import com.hackerdude.apps.sqlide.xml.HostConfigFactory;
import com.hackerdude.apps.sqlide.xml.hostconfig.BrowserPlugin;
import com.hackerdude.apps.sqlide.xml.hostconfig.NamedQueries;
import com.hackerdude.apps.sqlide.xml.hostconfig.Property;
import com.hackerdude.apps.sqlide.xml.hostconfig.SqlideHostConfig;

/**
 * This plugin returns all the basic Node Context Operations. It is the lowest
 * level node context plugin.
 */
public class BasicNodeContextOperations implements IDENodeContextPluginIF {

	public BasicNodeContextOperations() {
	}

	/**
	 * Returns the actions for the selected nodes. 
	 *  
	 * @param selectedNodes The nodes currently selected. 
	 * @return An array of actions that will be appropriate for the selected nodes. 
	 */
	public Action[] getActionsFor(NodeIDEBase[] selectedNodes) {
		if (selectedNodes.length != 1) { return NULL_ACTIONS; }
		ArrayList actionsList = new ArrayList();

		if (selectedNodes[0] instanceof ItemServerNode) {
			Action newISqlWindow = new ActionISQLWindow(
					(ItemServerNode) selectedNodes[0]);
			actionsList.add(newISqlWindow);
			Action editServerAction = new ActionEditServer(
					(ItemServerNode) selectedNodes[0]);
			actionsList.add(editServerAction);
		}
		if (selectedNodes[0] instanceof NodeIDEBase) {
			IDEVisualPluginIF rightComponent = SqlIdeApplication.getInstance()
					.getRightPanel();
			NodeIDEBase node = selectedNodes[0];
			if ((rightComponent != null)
					&& (rightComponent.getClass() == PluginInteractiveSQL.class)) {
				PluginInteractiveSQL iSQL = (PluginInteractiveSQL) rightComponent;
				String queryText = iSQL.getQueryText();
				if ((queryText != null) && (!queryText.equals(""))) {
					Action addQueryAction = new ActionAddQuery(node);
					actionsList.add(addQueryAction);
				}
				addNamedQueries(node.getDatabaseProcess().getHostConfiguration(),
						actionsList, iSQL);

			}
			if ( rightComponent == null &&  ! ( selectedNodes[0] instanceof ItemServerNode) ) {
				DatabaseProcess databaseProcess = selectedNodes[0].getDatabaseProcess();
				if ( databaseProcess != null ) {
				 Action iSQLWindowAction = new ActionISQLWindow("Open ISQL on "+databaseProcess.toString(), databaseProcess);
				 actionsList.add(iSQLWindowAction);
				}
			}
		}
		Action[] result = new Action[actionsList.size()];
		result = (Action[]) actionsList.toArray(result);
		return result;
	}

	public void initPlugin() {

	}

	public String getPluginName() {
		return "Basic Node Operations";
	}

	public String getPluginVersion() {
		return "";
	}

	public void freePlugin() {
	}

	public String getPluginShortName() {
		return "Node Operations";
	}

	public Icon getPluginIcon() {
		return ProgramIcons.getInstance().getDevicesIcon();
	}

	/**
	 * Shows the database spec editor.
	 */
	class ActionEditServer extends AbstractAction {

		ItemServerNode itemServerNode;

		public ActionEditServer(ItemServerNode itemServerNode) {
			super("Configure " + itemServerNode.toString(), ProgramIcons
					.getInstance().findIcon("images/EditBook.gif"));
			this.itemServerNode = itemServerNode;
		}

		public void actionPerformed(ActionEvent ev) {
			SqlideHostConfig spec = itemServerNode.getDatabaseProcess()
					.getHostConfiguration();
			DlgConnectionConfig.showConfigurationDialog(SqlIdeApplication.getFrame(),
					spec);
			SqlIdeApplication.getInstance().refreshPanels();
			try {
				HostConfigFactory.saveSqlideHostConfig(spec);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Could not save configuration "
						+ spec.getName() + ":\n" + "Could not write to "
						+ spec.getFileName() + ex.toString(), "Error while saving",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * Shows the database spec editor.
	 */
	class ActionISQLWindow extends AbstractAction {

		DatabaseProcess databaseProcess;

		public ActionISQLWindow(ItemServerNode itemServerNode) {
			this(itemServerNode.toString(), itemServerNode.getDatabaseProcess());
		}

		
		public ActionISQLWindow(String actionName, DatabaseProcess databaseProcess) {
			super(actionName, ProgramIcons.getInstance().findIcon("images/Sheet.gif"));
			this.databaseProcess = databaseProcess;
		}

		public void actionPerformed(ActionEvent ev) {
			PluginInteractiveSQL iSql = new PluginInteractiveSQL();
			iSql.setDatabaseProcess(databaseProcess);
			iSql.initPlugin();
			SqlIdeApplication.getInstance().setRightPanel(iSql);
		}
	}

	/**
	 * This action allows us to add a custom query to this connection's context
	 * menu.
	 */
	class ActionAddQuery extends AbstractAction {

		NodeIDEBase ideNode;

		public ActionAddQuery(NodeIDEBase ideNode) {
			super(determineAddQueryActionTitle(ideNode));
			this.ideNode = ideNode;
		}

		public void actionPerformed(ActionEvent ev) {
			DatabaseProcess proc = ideNode.getDatabaseProcess();
			SqlideHostConfig hostConfiguration = proc.getHostConfiguration();
			PluginInteractiveSQL iSql = (PluginInteractiveSQL) SqlIdeApplication
					.getInstance().getRightPanel();
			String queryValue = iSql.getQueryText();
			String message = "<HTML><P>Enter a simple name for query:\n"
					+ iSql.getQueryText();
			String queryName = JOptionPane.showInputDialog(SqlIdeApplication
					.getFrame(), message, "Name Query", JOptionPane.OK_CANCEL_OPTION);
			if (queryName == null) return;
			Property prop = new Property();
			prop.setName(queryName);
			prop.setValue(queryValue);
			if (hostConfiguration.getBrowserPlugin() == null) {
				hostConfiguration.setBrowserPlugin(new BrowserPlugin());
			}
			if (hostConfiguration.getBrowserPlugin().getNamedQueries() == null)
				hostConfiguration.getBrowserPlugin()
						.setNamedQueries(new NamedQueries());
			proc.getHostConfiguration().getBrowserPlugin().getNamedQueries()
					.addProperty(prop);
			try {
				HostConfigFactory.saveSqlideHostConfig(hostConfiguration);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public String determineAddQueryActionTitle(NodeIDEBase ideNode) {

		String result = "Add this query to "
				+ ideNode.getDatabaseProcess().toString();
		return result;
	}

	private void addNamedQueries(SqlideHostConfig config, ArrayList list,
			PluginInteractiveSQL iSQL) {
		if (config.getBrowserPlugin() == null) return;
		if (config.getBrowserPlugin().getNamedQueries() == null) return;
		Property[] properties = config.getBrowserPlugin().getNamedQueries()
				.getProperty();
		if (properties == null) return;
		for (int i = 0; i < properties.length; i++) {
			String name = properties[i].getName();
			String value = properties[i].getValue();
			Action action = ContextCommandRunner.createCustomCommandTyper(name,
					value, iSQL);
			list.add(action);
		}
	}

}