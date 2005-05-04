package com.hackerdude.apps.sqlide.pluginapi;
import javax.swing.Action;

/**
 * Implementing this interface allows you to act as a
 * Node Context provider.
 *
 * <P>A Node context provider is an interface that is asked the possible actions
 * for a set of selected browser nodes every time the user right-clicks the mouse
 * with those nodes selected.
 *
 * <P>Once the interface returns these actions, they get added to the context
 * menu. This allows several plugins to implement functionality to the same
 * nodes.
 */
public interface IDENodeContextPluginIF extends IDEPluginIF {

	/**
	 * Implement this method to return a series of actions that 
	 * should apply to this selection of nodes.
	 *  
	 * @param selectedNodes The nodes that are selected.
	 * @return The actions that should show up on a context menu.
	 */
	public Action[] getActionsFor(NodeIDEBase[] selectedNodes);


}
