/*

	 NO WARRANTY! See the GPL license for details.

 */
package com.hackerdude.apps.sqlide.intf;
import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;

import java.sql.*;

/**
 * Database Interface.
 */
public interface SQLIDEDBInterface  {

	boolean supportsUserManagement();

	boolean supportsTriggers();

	boolean supportsRules();

	/**
	 *
	 * The result set returned by this must be as follows:
	 * <PRE>
	 * COLUMN        TYPE Size
	 * -----------------------
	 * HostName      CHAR 60
	 * UserName      CHAR 16
	 * Password      CHAR 16
	 * CanSelect     CHAR 1
	 * CanInsert     CHAR 1
	 * CanUpdate     CHAR 1
	 * CanDelete     CHAR 1
	 * CanCreate     CHAR 1
	 * CanDrop       CHAR 1
	 * CanReload     CHAR 1
	 * CanShutdown   CHAR 1
	 * CanGrant      CHAR 1
	 * CanIndex      CHAR 1
	 * CanAlter      CHAR 1
	 * </PRE>
	 */
	public ResultSet getUserList(Connection connection) throws SQLException;

	/**
	 * Returns the catalog Privileges
	 * <PRE>
	 * COLUMN        TYPE SIZE
	 * ------------------------
	 * HostName      CHAR 60
	 * Catalog       CHAR 64
	 * UserName      CHAR 16
	 * CanSelect     CHAR 1
	 * CanInsert     CHAR 1
	 * CanUpdate     CHAR 1
	 * CanDelete     CHAR 1
	 * CanCreate     CHAR 1
	 * CanDrop       CHAR 1
	 * CanGrant      CHAR 1
	 * CanIndex      CHAR 1
	 * CanAlter      CHAR 1
	 * </PRE>
	 */
	public ResultSet getCatalogPrivs(Connection conn) throws SQLException;

	/**
	 * Return the Table Privileges
	 * <PRE>
	 * COLUMN        TYPE SIZE
	 * ------------------------
	 * HostName      CHAR 60
	 * Catalog       CHAR 64
	 * UserName      CHAR 16
	 * TableNaem     CHAR 64
	 * Grantor       CHAR 77
	 * CanSelect     CHAR 1
	 * CanInsert     CHAR 1
	 * CanUpdate     CHAR 1
	 * CanDelete     CHAR 1
	 * CanCreate     CHAR 1
	 * CanDrop       CHAR 1
	 * CanGrant      CHAR 1
	 * CanIndex      CHAR 1
	 * CanAlter      CHAR 1
	 * </PRE>
	 */
	public ResultSet getTablePrivs(Connection conn) throws SQLException;

	public ResultSet getPerHostPrivs(Connection conn) throws SQLException;

	/**
	 * Returns the priviledges per-column
	 * <PRE>
	 * COLUMN        TYPE SIZE
	 * ------------------------
	 * HostName      CHAR 60
	 * Catalog       CHAR 64
	 * UserName      CHAR 16
	 * TableName     CHAR 64
	 * ColumnName    CHAR 64
	 * </PRE>
	 */
	public ResultSet getPerColumnPrivs(Connection conn) throws SQLException;

}


