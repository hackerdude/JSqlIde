package com.hackerdude.apps.sqlide.plugins.tableedit;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import com.hackerdude.apps.sqlide.ProgramIcons;
import com.hackerdude.apps.sqlide.SqlIdeApplication;
import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.IDENodeContextPluginIF;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEBase;
import com.hackerdude.apps.sqlide.plugins.browser.browsejdbc.CategoryTableNode;
import com.hackerdude.apps.sqlide.plugins.browser.browsejdbc.ItemTableNode;

public class TableEditorNodeContext
	  implements IDENodeContextPluginIF {

	public TableEditorNodeContext() {
	}

	public Action[] getActionsFor(NodeIDEBase[] selectedNodes) {

		if ( (selectedNodes != null) && (selectedNodes.length == 1)) {
			if (selectedNodes[0] instanceof CategoryTableNode) {
				Action[] ACTIONS = { new CreateTableAction(selectedNodes[0])};
				return ACTIONS;
			}
			if (! (selectedNodes[0] instanceof ItemTableNode))
				return NULL_ACTIONS;
			ItemTableNode node = (ItemTableNode) selectedNodes[0];
			EditTableAction editTable = new EditTableAction(node);
			CreateTableAction createTable = new CreateTableAction(node);
			Action[] ACTION = { editTable, createTable };
			return ACTION;
		} else {
			return NULL_ACTIONS;
		}

	}

	public void initPlugin() {
	}

	public String getPluginName() {
		return "Table Editor Node Operations";
	}

	public String getPluginVersion() {
		return "0.0";
	}

	public void freePlugin() {

	}

	public String getPluginShortName() {
		return "Table Editor Node Context";
	}

	public Icon getPluginIcon() {
		return ProgramIcons.getInstance().getDevicesIcon();
	}

	class CreateTableAction extends AbstractAction {
		NodeIDEBase tables;
		public CreateTableAction(NodeIDEBase aNode) {
			super("Create new table", ProgramIcons.getInstance().findIcon("images/NewColumn.gif"));
			tables = aNode;
		}

		public void actionPerformed(ActionEvent evt) {
			PluginTableEditor tableEditor = new PluginTableEditor();
			tableEditor.initPlugin();
			DatabaseProcess proc = tables.getDatabaseProcess();
			tableEditor.setDatabaseProcess(proc);
			SqlIdeApplication.getInstance().setRightPanel(tableEditor);

		}
	}

	class EditTableAction extends AbstractAction {

		ItemTableNode tableNode;

		public EditTableAction(ItemTableNode tableNode) {
			super("Edit Table " + tableNode.toString(), ProgramIcons.getInstance().findIcon("images/NewColumn.gif"));
			this.tableNode = tableNode;
		}

		public void actionPerformed(ActionEvent evt) {
			PluginTableEditor tableEditor = new PluginTableEditor();
			tableEditor.initPlugin();
			DatabaseProcess proc = tableNode.getDatabaseProcess();
			tableEditor.setDatabaseProcess(proc);
			try {
				tableEditor.setTableNode(tableNode);
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(SqlIdeApplication.getFrame(), "Error while reading columns: " + ex.toString(), "Could not read columns", JOptionPane.ERROR_MESSAGE);
			}
			SqlIdeApplication.getInstance().setRightPanel(tableEditor);
		}
	}

}