package com.hackerdude.apps.sqlide.plugins.usrmgr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.hackerdude.apps.sqlide.ProgramIcons;
import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.nodes.ItemServerNode;
import com.hackerdude.apps.sqlide.pluginapi.BrowserExtensionPluginIF;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEBase;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDECategory;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEItem;

/**
 * A User Manager plugin for PostgreSQL. Displays available users, and lets you
 * add/remove users at will.
 *
 * 
 * @author David Martinez
 * @version 1.0
 */
public class PluginUserManager implements BrowserExtensionPluginIF {

	private static final String QUERY_USERNAME_PROP = "usernames.select";
	private static final String QUERY_USERNAME_GROUPS = "groups.select";

	private String getSupportedType(DatabaseProcess process) {
		String driverClass = process.getHostConfiguration().getJdbc().getDriver();
		String supportedType = UsrMgmtCommandFactory.getSupportedDriver(driverClass);
		return supportedType;
	}

	public void requestAddSubNodes(NodeIDEBase parentNode) {
		if ( getSupportedType(parentNode.getDatabaseProcess()) == null ) return;
		if ( parentNode instanceof ItemServerNode ) {
			ItemServerNode serverNode = (ItemServerNode) parentNode;
			parentNode.add(new UsersCategoryNode("Users", serverNode.getDatabaseProcess()));
		}
		if ( parentNode instanceof UsersCategoryNode ) {
			UsersCategoryNode usersNode = (UsersCategoryNode)parentNode;
			addUsers(usersNode);
		}

	}

	public void addUsers(UsersCategoryNode usersNode) {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			conn = usersNode.getDatabaseProcess().getConnection();
			String queryText = UsrMgmtCommandFactory.getCommandsFor(getSupportedType(usersNode.getDatabaseProcess())).getProperty(QUERY_USERNAME_PROP);
			preparedStatement = conn.prepareStatement(queryText);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String userName = resultSet.getString(1);
				UserNode node = new UserNode(userName, usersNode.getDatabaseProcess());
				usersNode.add(node);
			}
		} catch ( SQLException exc ) {
			exc.printStackTrace();
		} finally {
			if ( resultSet != null ) try { resultSet.close(); } catch ( Throwable thr) {}
			if ( preparedStatement != null ) try { preparedStatement.close(); } catch ( Throwable thr) {}
			if ( conn != null ) try { usersNode.getDatabaseProcess().returnConnection(conn); } catch ( Throwable thr) {}
		}
	}

	public void initPlugin() {

	}

	public String getPluginName() {
		return "User Manager";
	}

	public String getPluginVersion() {
		return "0";
	}

	public void freePlugin() {
	}

	public String getPluginShortName() {
		return "User Mgr";
	}

	public Icon getPluginIcon() {
		return ProgramIcons.getInstance().getUsersIcon();
	}

	class UsersCategoryNode extends NodeIDECategory {
		public UsersCategoryNode(String name, DatabaseProcess proc) {
			super(name, proc);
		}

		public void readChildren() {}

		public String getInfo() {
			return "PostgreSQL Users";
		}

		public ImageIcon getIcon() {
			return ProgramIcons.getInstance().findIcon("images/Users.gif");
		}
	}

	class UserNode extends NodeIDEItem {
		public UserNode(String userName, DatabaseProcess proc) {
			super(userName, proc);

		}
		public boolean canHaveChildren() {
			return false;
		}
		public void readChildren() {}
		public String getInfo() { return "User: "+toString(); }

		public ImageIcon getIcon() { return ProgramIcons.getInstance().findIcon("images/User.gif"); }
	}
}