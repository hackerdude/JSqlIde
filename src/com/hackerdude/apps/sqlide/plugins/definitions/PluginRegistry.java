package com.hackerdude.apps.sqlide.plugins.definitions;

import com.hackerdude.apps.sqlide.pluginapi.*;
import java.util.*;
import java.io.*;
import javax.swing.Action;

/**
 * Plug-in Registry.
 *
 * <P>The plug-in registry is responsible for keeping a list of PluginDefinition
 * objects, and persisting it to a registry storage mechanism.
 */
public class PluginRegistry {

	private HashMap pluginsByName;
	private ArrayList plugins;
	private ArrayList visualPlugins;

	public static final String REGISTRY_NAME = "plugins.registry.properties";

	private static PluginRegistry instance = new PluginRegistry();

	private PluginRegistry() {
		/** @todo Get the panel information from a file, if available. */
		pluginsByName = new HashMap();
		plugins = new ArrayList();
		visualPlugins = new ArrayList();
		try {
			readRegistry();
		} catch ( IOException exc ) {
			System.out.println("Could not read registry!");
		}
	}


	public static PluginRegistry getInstance() {
	  return(instance);
	}


	public void readRegistry() throws java.io.IOException {

		InputStream registryIS = com.hackerdude.apps.sqlide.SqlIdeApplication.class.getResourceAsStream(REGISTRY_NAME);
		if ( registryIS == null ) throw new IllegalStateException("SQL-IDE is not installed correctly. Plugin registry not found.");
		ArrayList lines = new ArrayList();
		BufferedReader br  = new BufferedReader(new InputStreamReader(registryIS));
		String newLine = null;
		newLine = br.readLine();
		while ( newLine != null ) {
			if ( ! newLine.startsWith("#") ) {
				lines.add(newLine);
			}
			newLine = br.readLine();
		}
		String[] pluginClassNames = new String[lines.size()];
		pluginClassNames = (String[])lines.toArray(pluginClassNames);
		for ( int i = 0; i< pluginClassNames.length; i++ ) {
			try {
				PluginDefinition def = new PluginDefinition();
				//		def.politeName = pluginNames[i];
				String className  = pluginClassNames[i];
				IDEPluginIF plugin = (IDEPluginIF)Class.forName(className).newInstance();
				if ( plugin instanceof IDEVisualPluginIF ) {
					visualPlugins.add(plugin);
				}
				def.pluginInstance = plugin;
				String politeName = plugin.getPluginName();
				def.panelKey = plugin.getPluginName().charAt(0);
				def.panelMnemonic = plugin.getPluginName().charAt(0);
				pluginsByName.put(politeName, def);
				plugins.add(def);
			} catch ( Exception exc ) {
				System.out.println("Plugin class "+pluginClassNames[i]+" cannot be instantiated");
				exc.printStackTrace();
			}
		}
		Collections.sort(plugins);
	}


	/**
	 * This returns an array string with instances of all the visual plugins
	 * to get their metadata.
	 * <P>Note: These are not running plugins, nor are they intended to be.
	 */
	public IDEVisualPluginIF[] getAllVisualPlugins() {
		IDEVisualPluginIF[] thePlugins = new IDEVisualPluginIF[visualPlugins.size()];
		thePlugins = (IDEVisualPluginIF[])visualPlugins.toArray(thePlugins);
		return thePlugins;
	}


	/**
	 * This returns an array with all the recommended shorts.
	 */
	public char[] getAllRecommendedShorts() {
		char[] theShorts = new char[visualPlugins.size()];
		Iterator iter = visualPlugins.iterator();
		int i = 0;
		while ( iter.hasNext() ) {
		  theShorts[i] = ((PluginDefinition)iter.next()).panelMnemonic;
		  i++;
		}
		return theShorts;
	};

	/**
	 * This returns an array with all the recommended keys.
	 */
	public char[] getAllRecommendedKeys() {
		char[] theKeys = new char[plugins.size()];
		Iterator iter = visualPlugins.iterator();
		int i = 0;
		while ( iter.hasNext() ) {
		  theKeys[i] = ((PluginDefinition)iter.next()).panelKey;
		  i++;
		}
		return theKeys;
	};

	public int getPanelIndex( String politeName ) {
	  PluginDefinition def = (PluginDefinition)pluginsByName.get(politeName);
	  if ( def == null ) { return -1; };
	  return(plugins.indexOf(def));
	}


	public PluginDefinition[] getAllPluginDefinitions() {
		PluginDefinition[] definitions = new PluginDefinition[plugins.size()];
		definitions = (PluginDefinition[])plugins.toArray(definitions);
		return definitions;
	}

	public PluginDefinition[] getAllNodeContextPluginDefinitions() {
		ArrayList result = new ArrayList();
		Iterator iter = plugins.iterator();
		while ( iter.hasNext() ) {
			PluginDefinition def = (PluginDefinition)iter.next();
			if ( def.pluginInstance instanceof IDENodeContextPluginIF ) {
				result.add(def);
			}
		}
		PluginDefinition[] definitions = new PluginDefinition[result.size()];
		definitions = (PluginDefinition[])result.toArray(definitions);
		return definitions;
	}


	public PluginDefinition[] getAllBrowserExtensionPluginDefinitions() {
		ArrayList result = new ArrayList();
		Iterator iter = plugins.iterator();
		while ( iter.hasNext() ) {
			PluginDefinition def = (PluginDefinition)iter.next();
			if ( def.pluginInstance instanceof BrowserExtensionPluginIF ) {
				result.add(def);
			}
		}
		PluginDefinition[] definitions = new PluginDefinition[result.size()];
		definitions = (PluginDefinition[])result.toArray(definitions);
		return definitions;
	}


	public PluginDefinition[] getAllVisualPluginDefinitions() {
		ArrayList result = new ArrayList();
		Iterator iter = plugins.iterator();
		while ( iter.hasNext() ) {
			PluginDefinition def = (PluginDefinition)iter.next();
			if ( def.pluginInstance instanceof IDEVisualPluginIF ) {
				result.add(def);
			}
		}
		PluginDefinition[] definitions = new PluginDefinition[result.size()];
		definitions = (PluginDefinition[])result.toArray(definitions);
		return definitions;
	}


	public PluginDefinition[] getAllNonVisualPluginDefinitions() {
		ArrayList result = new ArrayList();
		Iterator iter = plugins.iterator();
		while ( iter.hasNext() ) {
			PluginDefinition def = (PluginDefinition)iter.next();
			if ( def.pluginInstance instanceof IDENonVisualPluginIF ) {
				result.add(def);
			}
		}
		PluginDefinition[] definitions = new PluginDefinition[result.size()];
		definitions = (PluginDefinition[])result.toArray(definitions);
		return definitions;
	}

	public PluginDefinition getPlugin(String name) {
		return (PluginDefinition)pluginsByName.get(name);
	}


	public PluginDefinition getPluginByClassName(String className) {
		PluginDefinition result = null;
		Iterator iter = plugins.iterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			PluginDefinition plugin = (PluginDefinition)item;
			if (plugin.pluginInstance.getClass().getName().equals(className) ) {
				result = plugin;
				break;
			}
		}
		return result;
	}

	/**
	 * Returns the number of panels
	 */
	public int getPanelCount() {
		return( plugins.size());
	};

}
