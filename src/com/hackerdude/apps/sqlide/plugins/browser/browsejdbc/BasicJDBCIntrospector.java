package com.hackerdude.apps.sqlide.plugins.browser.browsejdbc;

import com.hackerdude.apps.sqlide.pluginapi.*;
import com.hackerdude.apps.sqlide.ProgramIcons;
import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import javax.swing.Icon;

/**
 * The Basic JDBC Introspector implements the base browser functionality,
 * including Catalogs, schemas, tables and columns.
 * <P>Basically anything about the database you can find out through straight
 * JDBC calls can be added here.
 *
 * @copyright (C) 1998-2002 Hackerdude (David Martinez). All Rights Reserved.
 * @author David Martinez
 * @version 1.0
 */
public class BasicJDBCIntrospector implements BrowserExtensionPluginIF {

	/**
	 * Creates a basic introspector
	 */
	public BasicJDBCIntrospector() {

	}

	public void requestAddSubNodes(NodeIDEBase parentNode) {
		DatabaseProcess db = parentNode.getDatabaseProcess();
		if ( !(parentNode instanceof com.hackerdude.apps.sqlide.nodes.ItemServerNode) ) return;
		System.out.println("Adding items for subnode of class "+parentNode.getClass().getName());

		/**
		 * Note: I'm assuming that if the JDBC name for the item does
		 * not exist, the node should not be there because that item type
		 * is not supported (for catalogs & schemas).
		 */
		NodeIDEBase theNode = new CategoryCatalogsNode(db);
		if ( theNode.toString() != null && ! theNode.toString().equals("") ) parentNode.add( theNode );

		theNode = new CategorySchemaNode(db); //  devicesNodes.elementAt(i);
		if ( theNode.toString() != null && ! theNode.toString().equals("") ) parentNode.add( theNode );


	}

	public void initPlugin() {}

	public String getPluginName() { return "Basic JDBC Introspector"; }

	public String getPluginVersion() { return "0.0";}

	public void freePlugin() {}

	public String getPluginShortName() { return "JDBC Introspector"; }

	public Icon getPluginIcon() {
		return ProgramIcons.getInstance().findIcon("images/DataQuery.gif");
	}

}
