package com.hackerdude.apps.sqlide.nodes;

import com.hackerdude.apps.sqlide.intf.SQLIDEDBInterface;
import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.pluginapi.*;
import java.sql.*;
import java.util.*;

/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
 * @author David Martinez
 * @version 1.0
 */

public class CategoryDbUsersNode extends NodeIDECategory
 {

	public CategoryDbUsersNode(DatabaseProcess db) {
		super(db.getCatalogTitle(), db);
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
		return "<HTML><P>Users per "+db.getCatalogTitle();
	}


}
