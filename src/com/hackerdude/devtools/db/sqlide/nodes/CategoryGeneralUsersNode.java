package com.hackerdude.devtools.db.sqlide.nodes;

import com.hackerdude.devtools.db.sqlide.intf.SQLIDEDBInterface;
import com.hackerdude.devtools.db.sqlide.dataaccess.*;
import com.hackerdude.devtools.db.sqlide.pluginapi.*;
import java.sql.*;

/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
 * @author David Martinez
 * @version 1.0
 */

public class CategoryGeneralUsersNode extends NodeIDECategory {

	public CategoryGeneralUsersNode(DatabaseProcess dbProcess) {
		super("General", dbProcess);
	}
	public void readChildren() {
		try {
			SQLIDEDBInterface intf = db.getDBInterface();
			Connection conn = null;
			try {
				conn = db.getConnection();
				ResultSet rs = intf.getUserList(conn);
				while ( rs.next() ) {
					String userName = rs.getString("UserName");
					String hostName = rs.getString("HostName");
					if ( hostName == null || hostName.equals("") ) hostName= ItemUserNode.ALL_HOST;
					if ( userName == null || userName.equals("") ) userName= ItemUserNode.ALL_USER;

					ItemUserNode userNode = new ItemUserNode(userName, hostName, db);
					add(userNode);
				}
			} finally {
				db.returnConnection(conn);
			}

		} catch ( SQLException exc ) {
			exc.printStackTrace();
		}
	}
	public String getInfo() {
		return "<HTML><P>General Users";
	}
}