package com.hackerdude.apps.sqlide.pluginapi;

/**
 * The browser Extension plugin is a type of plugin that allows the
 * implementer to add nodes to the browser. This is useful for implementing
 * functionality for browsing objects.
 *
 * <P>The built-in implementation of a browser extension is the
 * BasicJDBCIntrospector plugin.
 *
 * TODO: BasicJDBCIntrospectorPlugin - It takes the typical schema, objects and
 * tables common to all databases and obtainable with getMetaData() calls on the
 * connection and adds those node elements and groups to the server node.
 */
public interface BrowserExtensionPluginIF extends IDEPluginIF {

	/**
	 * Every time a node with children is about to expand, all browser
	 * extension plugins will receive notification via this method.
	 * Implement this method using instanceof to add whatever type of plugin
	 * you deem necessary.ss
	 *
	 * <P>Note: If you don't compare the parentNode using instanceof, your
	 * plugin will show up as a child on every expandable node.
	 */
	public void requestAddSubNodes(NodeIDEBase parentNode);

}
