package com.hackerdude.apps.sqlide;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Action;

import com.hackerdude.apps.sqlide.pluginapi.BrowserExtensionPluginIF;
import com.hackerdude.apps.sqlide.pluginapi.IDENodeContextPluginIF;
import com.hackerdude.apps.sqlide.pluginapi.IDENonVisualPluginIF;
import com.hackerdude.apps.sqlide.pluginapi.IDEPluginIF;
import com.hackerdude.apps.sqlide.pluginapi.IDEVisualPluginIF;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEBase;

/**
 * This class encapsulates the currently running plugins.
 */
public class RunningPlugins {

	ArrayList pluginInstances;

	ArrayList nonVisualPlugins;
	ArrayList visualPlugins;
	ArrayList nodeContextPlugins;
	ArrayList browserExtensionPlugins;

	public RunningPlugins() {
		pluginInstances = new ArrayList();
		visualPlugins   = new ArrayList();
		nodeContextPlugins = new ArrayList();
		nonVisualPlugins = new ArrayList();
		browserExtensionPlugins = new ArrayList();
	}


	public synchronized void refreshPlugins() {
		Iterator iPanels = pluginInstances.iterator();
		while ( iPanels.hasNext() ) {
				IDEPluginIF current =  (IDEPluginIF)iPanels.next();
				/** @todo Reimplement */
				// current.refreshPanel();
		}

	}

	/**
	 * Adds a new plugin to the list of running plugins, and start the plugin.
	 */
	public void startPlugin(IDEPluginIF plugin) {
		plugin.initPlugin();
		pluginInstances.add(plugin);
		if ( plugin instanceof  IDENonVisualPluginIF) nonVisualPlugins.add(plugin);
		if ( plugin instanceof  IDEVisualPluginIF) visualPlugins.add(plugin);
		if ( plugin instanceof  IDENodeContextPluginIF ) nodeContextPlugins.add(plugin);
		if ( plugin instanceof  BrowserExtensionPluginIF ) browserExtensionPlugins.add(plugin);
	}

	/**
	 * Removes a plugin from the list of running plugins, and stop the plugin
	 */
	public void endPlugin(IDEPluginIF plugin) {
		plugin.freePlugin();
		pluginInstances.remove(plugin);
		if ( plugin instanceof  IDENonVisualPluginIF) nonVisualPlugins.remove(plugin);
		if ( plugin instanceof  IDEVisualPluginIF) visualPlugins.remove(plugin);
		if ( plugin instanceof  IDENodeContextPluginIF ) nodeContextPlugins.remove(plugin);
		if ( plugin instanceof  BrowserExtensionPluginIF ) browserExtensionPlugins.remove(plugin);
	}

	public synchronized void terminatePlugins() {
		Iterator iPanels = pluginInstances.iterator();
		while ( iPanels.hasNext() ) {
				IDEPluginIF current =  (IDEPluginIF)iPanels.next();
				current.freePlugin();
		}
		pluginInstances.clear();
		nonVisualPlugins.clear();
		nodeContextPlugins.clear();

	}

	/**
	 * This function receives, for all the running NodeContextPlugins,
	 * the possible actions for the specified node. The SQLIDE main class
	 * calls this method to get the possible actions from all plugins and
	 * put them in a context menu.
	 */
	public synchronized Action[] getActionsFor(NodeIDEBase[] selectedNodes) {
		ArrayList allActions = new ArrayList();
		Iterator iContextPlugins = nodeContextPlugins.iterator();
		while (iContextPlugins.hasNext() ) {
			IDENodeContextPluginIF currentContextPlugin = (IDENodeContextPluginIF)iContextPlugins.next();
			Action[] actions = currentContextPlugin.getActionsFor(selectedNodes);
			if ( actions != null ) {
				for ( int i=0; i<actions.length; i++ ) allActions.add(actions[i]);
			}
		}
		Action[] result = new Action[allActions.size()];
		result = (Action[])allActions.toArray(result);
		return result;
	}

	/**
	 * This method asks all the running browser extension plugins
	 * to add themselves to the specified
	 */
	public void requestAddSubNodes(NodeIDEBase parentNode) {
		Iterator iPanels = browserExtensionPlugins.iterator();
		while ( iPanels.hasNext() ) {
			BrowserExtensionPluginIF current =  (BrowserExtensionPluginIF)iPanels.next();
			current.requestAddSubNodes(parentNode);
		}
	}

	/**
	 * This function returns all the running node context plugins.
	 * @return The running node context plugins.
	 */
	public IDENodeContextPluginIF[] getNodeContextPlugins() {
		IDENodeContextPluginIF[] nodePlugins = new IDENodeContextPluginIF[nodeContextPlugins.size()];
		nodePlugins = (IDENodeContextPluginIF[]) nodeContextPlugins.toArray(nodePlugins);
		return nodePlugins;
	}

	public BrowserExtensionPluginIF[] getBrowserExtensionPlugins() {
		BrowserExtensionPluginIF[] plugins = new BrowserExtensionPluginIF[browserExtensionPlugins.size()];
		plugins = (BrowserExtensionPluginIF[]) browserExtensionPlugins.toArray(plugins);
		return plugins;
	}

	public IDEPluginIF[] getPluginInstances() {
		IDEPluginIF[] plugins = new IDEPluginIF[pluginInstances.size()];
		plugins = (IDEPluginIF[]) pluginInstances.toArray(plugins);
		return plugins;
	}

}
