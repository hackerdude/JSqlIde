package com.hackerdude.apps.sqlide.plugins.browser;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.hackerdude.apps.sqlide.*;
import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.dialogs.*;
import com.hackerdude.apps.sqlide.nodes.*;
import com.hackerdude.apps.sqlide.pluginapi.*;
import com.hackerdude.apps.sqlide.plugins.isql.*;
import com.hackerdude.apps.sqlide.xml.hostconfig.*;
import com.hackerdude.apps.sqlide.xml.*;

/**
 * This plugin returns all the basic Node Context Operations. It is the
 * lowest level node context plugin.
 */
public class BasicNodeContextOperations implements IDENodeContextPluginIF {

    public BasicNodeContextOperations() {
    }

    public Action[] getActionsFor(NodeIDEBase[] selectedNodes) {
		if ( selectedNodes.length != 1 ) { return null; }
		ArrayList actionsList = new ArrayList();

		if ( selectedNodes[0] instanceof ItemServerNode ) {
			Action editServerAction = new ActionEditServer( (ItemServerNode)selectedNodes[0] );
			actionsList.add(editServerAction);
		}
		if ( selectedNodes[0] instanceof NodeIDEBase ) {
			IDEVisualPluginIF rightComponent = SqlIdeApplication.getInstance().getRightPanel();
			NodeIDEBase node = selectedNodes[0];
			if ((rightComponent != null) && (rightComponent.getClass() == PluginInteractiveSQL.class) ) {
				PluginInteractiveSQL iSQL = (PluginInteractiveSQL)rightComponent;
				String queryText = iSQL.getQueryText();
				if ( (queryText != null) &&(!queryText.equals("")) ) {
					Action addQueryAction = new ActionAddQuery(node);
					actionsList.add(addQueryAction);
				}
				addNamedQueries(node.getDatabaseProcess().getHostConfiguration(), actionsList, iSQL);

			}
		}
		Action[] result = new Action[actionsList.size()];
		result = (Action[])actionsList.toArray(result);
		return result;
    }

    public void initPlugin() {

    }
    public String getPluginName() {
		return "Basic Node Operations";
    }
    public String getPluginVersion() { return "";
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
			super("Configure "+itemServerNode.toString(), ProgramIcons.getInstance().findIcon("images/EditBook.gif"));
			this.itemServerNode = itemServerNode;
		}
		public void actionPerformed(ActionEvent ev) {
			SqlideHostConfig spec = itemServerNode.getDatabaseProcess().getHostConfiguration();
			DlgConnectionConfig.showConfigurationDialog( SqlIdeApplication.getFrame(), spec );
		}
	}

	/**
	 * This action allows us to add a custom query to this connection's
	 * context menu.
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
			PluginInteractiveSQL iSql = (PluginInteractiveSQL)SqlIdeApplication.getInstance().getRightPanel();
			String queryValue = iSql.getQueryText();
			String message = "<HTML><P>Enter a simple name for query:\n"+iSql.getQueryText();
			String queryName = JOptionPane.showInputDialog(SqlIdeApplication.getInstance().getFrame(), message, "Name Query", JOptionPane.OK_CANCEL_OPTION);
			if ( queryName == null ) return;
			Property prop = new Property();
			prop.setName(queryName);
			prop.setValue(queryValue);
			if ( hostConfiguration.getBrowserPlugin() == null ) {
				hostConfiguration.setBrowserPlugin(new BrowserPlugin());
			}
			if ( hostConfiguration.getBrowserPlugin().getNamedQueries() == null ) hostConfiguration.getBrowserPlugin().setNamedQueries(new NamedQueries());
			proc.getHostConfiguration().getBrowserPlugin().getNamedQueries().addProperty(prop);
			try {
				HostConfigFactory.saveSqlideHostConfig(hostConfiguration);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public  String determineAddQueryActionTitle(NodeIDEBase ideNode) {

		String result = "Add this query to "+ideNode.getDatabaseProcess().toString();
		return result;
	}

	private void addNamedQueries(SqlideHostConfig config, ArrayList list, PluginInteractiveSQL iSQL) {
		if ( config.getBrowserPlugin() == null )  return;
		if ( config.getBrowserPlugin().getNamedQueries() == null ) return;
		Property[] properties = config.getBrowserPlugin().getNamedQueries().getProperty();
		if ( properties == null ) return;
		for ( int i=0; i<properties.length; i++ ) {
			String name = properties[i].getName();
			String value = properties[i].getValue();
			Action action = ContextCommandRunner.createCustomCommandTyper(name, value, iSQL);
			list.add(action);
		}
	}

}