package com.hackerdude.devtools.db.sqlide.plugins.definitions;

import com.hackerdude.devtools.db.sqlide.pluginapi.IDEPluginIF;
/**
 * SQLIDE Panel Definition
 *
 * <P>This class encapsulates the description of a plugin.
 *
 *
 * <P>A plugin is defined as <B>a visual piece of functionality that can be standardized
 * and placed as a container component on on the SQLIDE right-hand panel, and that
 * acts relation to a DatabaseProcess.</B>
 *
 * <P>In the near future, we will extend this definition to include non-visual
 * actions that can "become" available depending on the Browser's context.
 */
public class PluginDefinition {

   public IDEPluginIF pluginInstance;
   public char panelMnemonic;
   public char panelKey;

}