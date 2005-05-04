package com.hackerdude.apps.sqlide.nodes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.intf.SQLIDEDBInterface;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDECategory;

/**
 * General users node.
 */
public class CategoryGeneralUsersNode extends NodeIDECategory {

	public CategoryGeneralUsersNode(DatabaseProcess dbProcess) {
		super("General", dbProcess);
	}
	public void readChildren() {
		try {
			SQLIDEDBInterface intf = databaseProcess.getDBInterface();
			Connection conn = null;
			try {
				conn = databaseProcess.getConnection();
				ResultSet rs = intf.getUserList(conn);
				while ( rs.next() ) {
					String userName = rs.getString("UserName");
					String hostName = rs.getString("HostName");
					if ( hostName == null || hostName.equals("") ) hostName= ItemUserNode.ALL_HOST;
					if ( userName == null || userName.equals("") ) userName= ItemUserNode.ALL_USER;

					ItemUserNode userNode = new ItemUserNode(userName, hostName, databaseProcess);
					add(userNode);
				}
			} finally {
				databaseProcess.returnConnection(conn);
			}

		} catch ( SQLException exc ) {
			exc.printStackTrace();
		}
	}
	public String getInfo() {
		return "<HTML><P>General Users";
	}
}
