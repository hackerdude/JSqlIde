package com.hackerdude.apps.sqlide.plugins.definitions;

import com.hackerdude.apps.sqlide.pluginapi.IDEPluginIF;
/**
 * SQLIDE Panel Definition
 *
 * <P>This class encapsulates the description of a plugin.
 *
 * <P>A plugin is defined as <B>a piece of functionality that can be managed
 * and accessed by SQLIDE in a standard way.
 * </B>
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