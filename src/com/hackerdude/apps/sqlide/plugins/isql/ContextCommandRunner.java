package com.hackerdude.apps.sqlide.plugins.isql;

import com.hackerdude.apps.sqlide.pluginapi.*;
import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.*;
import com.hackerdude.apps.sqlide.plugins.browser.browsejdbc.*;

/**
 * This non-visual node context plugin feeds to the interactive SQL commands
 * depending on the type of object that is selected.
 *
 * @copyright (C) 1998-2002 Hackerdude (David Martinez). All Rights Reserved.
 * @author David Martinez
 * @version 1.0
 */
public class ContextCommandRunner implements IDENodeContextPluginIF {

	Properties tableContextCommands = new Properties();
	Properties columnContextCommands = new Properties();
	Map iconNames = new HashMap();

	public final static String ITEM_TABLE_PREFIX="ItemTableNode";
	public final static String ITEM_COLUMN_PREFIX = "ItemTableColumnNode";

	public final static String ICON_SUFFIX = ".icon";


    public ContextCommandRunner() {
    }


	class ActionCatalogChanger extends AbstractAction {
		String catalogName;
		PluginInteractiveSQL interactiveSQL;

		public ActionCatalogChanger(PluginInteractiveSQL interactiveSQL, String label, String catalogName, Icon icon) {
				super(label, icon);
				this.catalogName = catalogName;
				this.interactiveSQL = interactiveSQL;
		}

		public void actionPerformed(ActionEvent ae) {
			interactiveSQL.selectCatalog(catalogName);
		}
	}


	public static Action createCustomCommandTyper(String name, String statement, PluginInteractiveSQL interactiveSQL) {
		Icon icon = ProgramIcons.getInstance().getUsersIcon();
		return new ActionCommandTyper(name, statement, icon, interactiveSQL);
	}

	public static class ActionCommandTyper extends AbstractAction {

		PluginInteractiveSQL interactiveSQL;
		String sqlStatement;

		public ActionCommandTyper(String statement, Icon icon, PluginInteractiveSQL interactiveSQL) {
			super(statement, icon);
			this.sqlStatement = statement;
			this.interactiveSQL = interactiveSQL;
		}

		public ActionCommandTyper(String name, String statement, Icon icon, PluginInteractiveSQL interactiveSQL) {
			super(name, icon);
			this.sqlStatement = statement;
			this.interactiveSQL = interactiveSQL;
		}

		public void actionPerformed(ActionEvent ae) {
			String statement = ae.getActionCommand();
			interactiveSQL.setQueryText(sqlStatement);
			interactiveSQL.grabFocus();
		}
	}

	public Action[] getActionsFor(NodeIDEBase[] nodes) {

		IDEVisualPluginIF activePlugin = SqlIdeApplication.getInstance().getRightPanel();

		if ( activePlugin == null ) return null;
		if ( ! (activePlugin instanceof PluginInteractiveSQL) ) { return null; }
		PluginInteractiveSQL isqlPlugin = (PluginInteractiveSQL)activePlugin;

		DatabaseProcess databaseProcess = activePlugin.getDatabaseProcess();
		if ( nodes.length != 1 ) return new Action [0];
		NodeIDEBase node = nodes[0];
		ArrayList al = new ArrayList();

		String tableName = determineTableName(node);
		String columnName = null;

		if ( ! ( node.getDatabaseProcess() == databaseProcess ) )  { return null; }

		if ( node instanceof ItemCatalogNode ) {
			ActionCatalogChanger changer = new ActionCatalogChanger(isqlPlugin, "Change to "+node.toString(), node.toString(), ProgramIcons.getInstance().getDatabaseIcon());
			al.add(changer);
		}

		if ( node instanceof ItemTableNode ) {
			addTableContextCommandActions(al, tableName, isqlPlugin );
		}

		if ( node instanceof ItemTableColumnNode ) {
			ItemTableColumnNode columnItem = (ItemTableColumnNode)node;
			columnName = columnItem.getColumnName();
			addColumnContextCommandActions(al, tableName, columnName, isqlPlugin);
		}

		Action[] actions = new Action[al.size()];
		actions = (Action[])al.toArray(actions);
		return actions;
	}

    public void initPlugin() {
		try {
			readContextCommands();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

    }
	public String getPluginName() {
		return "Context ISQL commands";
    }
    public String getPluginVersion() {
		return "";
    }
    public void freePlugin() {
    }
    public String getPluginShortName() {
		return "commandrunner";
    }
    public Icon getPluginIcon() {
		return ProgramIcons.getInstance().getServerIcon();
    }

	private void readContextCommands() throws Exception {
		InputStream is = ContextCommandRunner.class.getResourceAsStream("context.commands.properties");
		Properties props = new Properties();
		props.load(is);
		Enumeration enum = props.keys();
		while ( enum.hasMoreElements() ) {
			String key = (String)enum.nextElement();
			String value = props.getProperty(key);
			if ( key.endsWith(ICON_SUFFIX) ) {
				Icon iconValue = ProgramIcons.getInstance().findIcon(value);
				iconNames.put(key, iconValue);
			}
			else {
				if ( key.startsWith(ITEM_TABLE_PREFIX) ) {
					tableContextCommands.put(key, value);
				} else if ( key.startsWith(ITEM_COLUMN_PREFIX) ) {
					columnContextCommands.put(key, value);
				} else {
					System.out.println("Warning: Key "+key+" not for column or table nodes");
				}
			}
		}
	}

	public void addColumnContextCommandActions(ArrayList destination, String tableName, String columnName, PluginInteractiveSQL interactiveSQL) {
		String[] PARAMS = { tableName, columnName };
		Set set = columnContextCommands.keySet();
		List aList = new ArrayList(set);
		Collections.sort(aList);
		String[] queryNames = new String[aList.size()];
		queryNames  = (String[])aList.toArray(queryNames);
		for ( int i=0; i<queryNames.length; i++ ) {
			String queryName = queryNames[i];
			String queryPattern = columnContextCommands.getProperty(queryName);
			Icon icon = (Icon)iconNames.get(queryName+ICON_SUFFIX);
			String query = MessageFormat.format(queryPattern, PARAMS);
			Action newAction = new ActionCommandTyper(query, icon, interactiveSQL);
			destination.add(newAction);
		}
	}

	public void addTableContextCommandActions(ArrayList destination, String tableName, PluginInteractiveSQL interactiveSQL) {
		String[] PARAMS = { tableName };
		Set set = tableContextCommands.keySet();
		List aList = new ArrayList(set);
		Collections.sort(aList);
		String[] queryNames = new String[aList.size()];
		queryNames  = (String[])aList.toArray(queryNames);
		for ( int i=0; i<queryNames.length; i++ ) {
			String queryName = queryNames[i];
			String queryPattern = tableContextCommands.getProperty(queryName);
			Icon icon = (Icon)iconNames.get(queryName+ICON_SUFFIX);
			String query = MessageFormat.format(queryPattern, PARAMS);
			Action newAction = new ActionCommandTyper(query, icon, interactiveSQL);
			destination.add(newAction);
		}
	}

	public String determineTableName(NodeIDEBase node) {
		String result;
		String table = "";
		String catalog = "";
		if ( node instanceof ItemTableNode ) {
			ItemTableNode tableItem = (ItemTableNode)node;
			table = tableItem.toString();
			catalog = tableItem.getCatalogName();
		} else if ( node instanceof ItemTableColumnNode ) {
			ItemTableColumnNode columnItem = (ItemTableColumnNode)node;
			table = columnItem.getTableName();
			catalog = columnItem.getCatalogName();
		}
		// If we support dot notation and there is a catalog, use the fully qualified name
		boolean isCatalogEmpty = catalog==null || catalog.equals("");
		if ( node.getDatabaseProcess().getHostConfiguration().getGeneral().getSupportsDotNotation() && ! isCatalogEmpty )
		{
			result = catalog+"."+table;

		} else {
			result = table;
		}
		// If it has spaces in the name, add quotes.
		if ( result.indexOf(" ") > -1 ) result = "\""+result+"\"";
		return result;

	}

}