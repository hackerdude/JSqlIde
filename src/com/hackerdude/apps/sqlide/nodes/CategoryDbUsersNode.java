package com.hackerdude.apps.sqlide.nodes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDECategory;

/**
 * Node for database users.
 */
public class CategoryDbUsersNode extends NodeIDECategory
 {

	public CategoryDbUsersNode(DatabaseProcess db) {
		super("Users", db);
	}

	public void readChildren() {

		Connection conn = null;
		try {
			try {
				conn = db.getConnection();
				ResultSet result = db.getDBInterface().getCatalogPrivs(conn);
				while ( result.next() ) {
					String catalogName = result.getString("Catalog");
					String hostName    = result.getString("HostName");
					String userName    = result.getString("UserName");
					if ( hostName == null || hostName.equals("") ) hostName = ItemUserNode.ALL_HOST;
					if ( userName == null || userName.equals("") ) userName = ItemUserNode.ALL_USER;
					ItemCatalogUserNode userNode = new ItemCatalogUserNode(catalogName, userName, hostName,db);
					add(userNode);
				}


			} finally {
				db.returnConnection(conn);
			}
		} catch ( SQLException exc ) {  }

	}

	public String getInfo() {
		return "<HTML><P>Users";
	}


}
