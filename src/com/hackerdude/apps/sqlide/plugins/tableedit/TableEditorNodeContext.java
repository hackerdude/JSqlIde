package com.hackerdude.apps.sqlide.plugins.tableedit;

import javax.swing.Action;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEBase;
import javax.swing.Icon;
import com.hackerdude.apps.sqlide.pluginapi.IDENodeContextPluginIF;
import com.hackerdude.apps.sqlide.ProgramIcons;
import javax.swing.*;
import java.awt.event.*;
import com.hackerdude.apps.sqlide.plugins.browser.browsejdbc.*;
import com.hackerdude.apps.sqlide.*;
import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import java.sql.*;

public class TableEditorNodeContext
	  implements IDENodeContextPluginIF {
	public TableEditorNodeContext() {
	}

	public Action[] getActionsFor(NodeIDEBase[] selectedNodes) {

		if ( (selectedNodes != null) && (selectedNodes.length == 1)) {
			if (! (selectedNodes[0] instanceof ItemTableNode))
				return NULL_ACTIONS;
			ItemTableNode node = (ItemTableNode) selectedNodes[0];
			EditTableAction editTable = new EditTableAction(node);
			Action[] ACTION = {editTable};
			return ACTION;
		}
		else {
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
			}
			catch (SQLException ex) {
				JOptionPane.showMessageDialog(SqlIdeApplication.getFrame(), "Error while reading columns: " + ex.toString(), "Could not read columns", JOptionPane.ERROR_MESSAGE);
			}
			SqlIdeApplication.getInstance().setRightPanel(tableEditor);
		}
	}

}