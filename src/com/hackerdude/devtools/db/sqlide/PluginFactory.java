package com.hackerdude.devtools.db.sqlide;

import com.hackerdude.devtools.db.sqlide.pluginapi.*;
import com.hackerdude.devtools.db.sqlide.plugins.*;
import com.hackerdude.devtools.db.sqlide.plugins.isql.PluginInteractiveSQL;
import com.hackerdude.devtools.db.sqlide.plugins.definitions.*;

import javax.swing.JOptionPane;
import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.Enumeration;

/**
 * This class is responsible for dynamically creating plugins.
 *
 * <P>You can create a plugin in two ways:
 *
 *  <UL>
 *     <LI>By retrieving a PluginDefinition object from the registry
 *      <LI>By giving it the "polite name" so it finds the appropriate plugin
 *      definition and instantiates it.
 *  </UL>
 */
public class PluginFactory {

	public static IDEPluginIF createPlugin( PluginDefinition def ) {
		IDEPluginIF theResult = null;
		Class theClass;
		try {
			theResult = (IDEPluginIF)def.pluginInstance.getClass().newInstance();
		}
		catch( java.lang.InstantiationException e ) {
			theResult = null;
			System.err.println("Warning: Couldn't create plugin "+def.pluginInstance.getPluginName()+":\n"+e);
		}
		catch( java.lang.IllegalAccessException e ) {
			theResult = null;
			System.err.println("Warning: Illegal Access Exception when trying to instantiate class "+def.pluginInstance.getClass().getName());
		}
		catch ( Exception exc ) {
			exc.printStackTrace();
			JOptionPane.showMessageDialog(null, "This panel is not working.\nIf you are so inclined, you're welcome to debug it.\n\nSee the stack dump in stderr.\nAlso consult the BUGS and TODO files in the SQLIDE distribution.", "Not Implemented Yet", JOptionPane.ERROR_MESSAGE);

		}
		return(theResult);
	}

	public static IDEPluginIF createPlugin(String politeName) {
		IDEPluginIF theResult = null;
		PluginDefinition def = PluginRegistry.getInstance().getPlugin(politeName);
		if ( def != null ) { theResult = createPlugin(def); }
		if ( theResult == null ) {
			// HACK: Ugly hack so things work until I find out how to
			// implement my own ClassLoader or fix the not finding
			// panel problem.
			theResult = (IDEPluginIF)new PluginInteractiveSQL();
		}
	return(theResult);
	}


}


