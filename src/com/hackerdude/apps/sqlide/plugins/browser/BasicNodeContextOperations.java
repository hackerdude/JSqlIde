package com.hackerdude.apps.sqlide.plugins.browser;

import com.hackerdude.apps.sqlide.pluginapi.IDENodeContextPluginIF;
import javax.swing.Action;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEBase;
import javax.swing.Icon;
import com.hackerdude.apps.sqlide.ProgramIcons;
import com.hackerdude.apps.sqlide.nodes.ItemServerNode;
import javax.swing.*;
import java.awt.event.*;
import com.hackerdude.apps.sqlide.SqlIdeApplication;
import com.hackerdude.apps.sqlide.dialogs.DlgConnectionConfig;
import com.hackerdude.apps.sqlide.dataaccess.ConnectionConfig;

public class BasicNodeContextOperations implements IDENodeContextPluginIF {

    public BasicNodeContextOperations() {
    }
    public Action[] getActionsFor(NodeIDEBase[] selectedNodes) {
		if ( selectedNodes.length != 1 ) { return null; }
		if ( selectedNodes[0] instanceof ItemServerNode ) {
			Action[] result = { new ActionEditServer( (ItemServerNode)selectedNodes[0] )  };
			return result;
		}
		return null;
    }
    public void initPlugin() {

    }
    public String getPluginName() {
		return "Basic Node Operations";
    }
    public String getPluginVersion() { return "";
    }
    public void freePlugin() {
    }
    public String getPluginShortName() {
        return "Node Operations";
    }
    public Icon getPluginIcon() {
		return ProgramIcons.getInstance().getDevicesIcon();
    }


	/**
	 * Shows the database spec editor.
	 */
	class ActionEditServer extends AbstractAction {
		ItemServerNode itemServerNode;
		public ActionEditServer(ItemServerNode itemServerNode) {
			super("Configure "+itemServerNode.toString(), ProgramIcons.getInstance().findIcon("images/EditBook.gif"));
			this.itemServerNode = itemServerNode;
		}
		public void actionPerformed(ActionEvent ev) {
			ConnectionConfig spec = itemServerNode.getDatabaseProcess().getConnectionConfig();
			DlgConnectionConfig.showConfigurationDialog( SqlIdeApplication.getFrame(), spec );
		}
	}

}