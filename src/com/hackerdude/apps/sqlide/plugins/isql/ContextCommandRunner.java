package com.hackerdude.apps.sqlide.plugins.isql;

import com.hackerdude.apps.sqlide.pluginapi.*;
import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import com.hackerdude.apps.sqlide.plugins.browser.browsejdbc.*;

/**
 * <p>Title: JSqlIde</p>
 * <p>Description: A Java SQL Integrated Development Environment
 * <p>Copyright: Copyright (c) David Martinez</p>
 * <p>Company: </p>
 * @author David Martinez
 * @version 1.0
 */

public class ContextCommandRunner implements IDENodeContextPluginIF {

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


	class ActionCommandTyper extends AbstractAction {

		PluginInteractiveSQL interactiveSQL;

		public ActionCommandTyper(String statement, Icon icon, PluginInteractiveSQL interactiveSQL) {
			super(statement, icon);
			this.interactiveSQL = interactiveSQL;
		}

		public void actionPerformed(ActionEvent ae) {
			String statement = ae.getActionCommand();
			interactiveSQL.setQueryText(statement);
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
		if ( node.getDatabaseProcess() == databaseProcess ) {

			if ( node instanceof ItemTableNode ) {
				ItemTableNode tableItem = (ItemTableNode)node;
				String objectName;
				if ( databaseProcess.getConnectionConfig().isSupportsDotNotation() ) {
					objectName = tableItem.getCatalogName()+"."+tableItem.toString();
				} else {
					objectName = tableItem.toString();
				}
				if ( objectName.indexOf(" ") > -1 ) objectName = "\""+objectName+"\"";

				String statement = "SELECT * FROM "+objectName;
				ActionCommandTyper typer = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/Sheet.gif"), isqlPlugin);
				al.add(typer);

				statement = "SELECT COUNT(*) FROM "+objectName;
				ActionCommandTyper countTyper = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/Gauge.gif"), isqlPlugin);
				al.add(countTyper);

				statement = "INSERT INTO "+objectName+" VALUES ";
				ActionCommandTyper ins = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/NewRow.gif"), isqlPlugin);
				al.add(ins);

				statement = "DELETE FROM "+objectName+" WHERE ";
				ActionCommandTyper del = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/DeleteRow.gif"), isqlPlugin);
				al.add(del);

			}
			if ( node instanceof ItemCatalogNode ) {
				ActionCatalogChanger changer = new ActionCatalogChanger(isqlPlugin, "Change to "+node.toString(), node.toString(), ProgramIcons.getInstance().getDatabaseIcon());
				al.add(changer);
			}


			if ( node instanceof ItemTableColumnNode ) {
				ItemTableColumnNode columnItem = (ItemTableColumnNode)node;
				String objectName;

				if ( columnItem.getCatalogName() == null || columnItem.getCatalogName().equals("") ) {
					objectName = columnItem.getTableName();
				}
				else objectName = columnItem.getCatalogName()+"."+columnItem.getTableName();

				//				String objectName = columnItem.getCatalogName()+"."+columnItem.getTableName();
				String columnName = columnItem.getColumnName();

				String statement;
				ActionCommandTyper typer;

				statement = "SELECT MIN("+columnName+") FROM "+objectName;
				typer = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/minus.gif"),isqlPlugin);
				al.add(typer);

				statement = "SELECT MAX("+columnName+") FROM "+objectName;
				typer = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/plus.gif"), isqlPlugin);
				al.add(typer);

				statement = "SELECT COUNT("+columnName+") , MIN("+columnName+") , MAX("+columnName+")  FROM "+objectName;
				typer = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/List.gif"), isqlPlugin);
				al.add(typer);


				statement = "SELECT "+columnName+" FROM "+objectName+"  GROUP BY "+columnName;
				typer = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/Column.gif"),isqlPlugin);
				al.add(typer);

				statement = "SELECT * FROM "+objectName+" WHERE "+columnName;
				typer = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/Binocular.gif"), isqlPlugin);
				al.add(typer);

				statement = "SELECT * FROM "+objectName+" ORDER BY "+columnName;
				typer = new ActionCommandTyper(statement, ProgramIcons.getInstance().findIcon("images/Sheet.gif"), isqlPlugin);
				al.add(typer);


			}
		}

		Action[] actions = new Action[al.size()];
		actions = (Action[])al.toArray(actions);
		return actions;
	}

    public void initPlugin() {

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
}