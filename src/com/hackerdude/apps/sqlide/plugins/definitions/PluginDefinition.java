package com.hackerdude.apps.sqlide.plugins.definitions;

import com.hackerdude.apps.sqlide.pluginapi.IDEPluginIF;
/**
 * SQLIDE Panel Definition
 *
 * <P>This class encapsulates the description of a plugin.
 *
 * <P>A plugin is defined as <B>a visual piece of functionality that can be standardized
 * and placed as a container component on on the SQLIDE right-hand panel, and that
 * acts relation to a DatabaseProcess.</B>
 *
 * <P>In the near future, we will extend this definition to include non-visual
 * actions that can "become" available depending on the Browser's context.
 */
public class PluginDefinition implements Comparable {

	public IDEPluginIF pluginInstance;
	public char panelMnemonic;
	public char panelKey;


	public boolean equals(Object o) {
		if ( o == null || (!(o instanceof PluginDefinition) ) ) return false;
		PluginDefinition compareDef = (PluginDefinition)o;
		return pluginInstance.getClass().equals(compareDef.pluginInstance.getClass());
	}


	public int compareTo(Object o) {
		/** @todo Add error checking */
		PluginDefinition compareDef = (PluginDefinition)o;
		return pluginInstance.getPluginName().compareTo(compareDef.pluginInstance.getPluginName());
	}



}