/*

  ideServer_MSQL.java - a MySQL implementation of
  the ideServer class.

 */
package com.hackerdude.apps.sqlide.dbspecific;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.intf.SQLIDEDBInterface;

public class Database_MSQL implements SQLIDEDBInterface {

	public DatabaseProcess parentProcess;
	public boolean supportsUserManagement() { return true; }

	public boolean supportsTriggers() {
		/**@todo: Implement this com.hackerdude.apps.sqlide.intf.SQLIDEDBInterface method*/
		throw new java.lang.UnsupportedOperationException("Method supportsTriggers() not yet implemented.");
	}
	public boolean supportsRules() { return false; }

	public ResultSet getUserList(Connection connection) throws SQLException {
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(
			"SELECT Host as HostName, User as UserName, Password, "+
			"Select_priv as CanSelect, Insert_priv as CanInsert, Update_priv as CanUpdate,"+
			"Delete_priv as CanDelete, Create_priv as CanCreate, Drop_priv as CanDrop, "+
			"Reload_priv as CanReload, Shutdown_priv as CanShutdown, Grant_priv as CanGrant,"+
			"Alter_priv as CanAlter FROM mysql.user"

		);
		return rs;
	}

	public ResultSet getCatalogPrivs(Connection connection) throws SQLException {
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(
			"SELECT Host as HostName, Db as Catalog, User as UserName, "+
			"Select_priv as CanSelect, Insert_priv as CanInsert, Update_priv as CanUpdate,"+
			"Delete_priv as CanDelete, Create_priv as CanCreate, Drop_priv as CanDrop, "+
			"Grant_priv as CanGrant, Index_priv as CanIndex, "+
			"Alter_priv as CanAlter FROM mysql.db ORDER BY Db"

		);
		return rs;
	}

	public ResultSet getTablePrivs(Connection connection) {
		/**@todo: Implement this com.hackerdude.apps.sqlide.intf.SQLIDEDBInterface method*/
		throw new java.lang.UnsupportedOperationException("Method getTablePrivs() not yet implemented.");
	}
	public ResultSet etPerHostPrivs(Connection connection) {
		/**@todo: Implement this com.hackerdude.apps.sqlide.intf.SQLIDEDBInterface method*/
		throw new java.lang.UnsupportedOperationException("Method getPerHostPrivs() not yet implemented.");
	}
	public ResultSet getPerColumnPrivs(Connection connection) {
		/**@todo: Implement this com.hackerdude.apps.sqlide.intf.SQLIDEDBInterface method*/
		throw new java.lang.UnsupportedOperationException("Method getPerColumnPrivs() not yet implemented.");
	}

	public ResultSet getPerHostPrivs(Connection connection) {
		throw new java.lang.UnsupportedOperationException("Method getPerHostPrivs() not yet implemented.");
	}

	public void setParentProcess(DatabaseProcess proc) {
		parentProcess = proc;
	}
}
