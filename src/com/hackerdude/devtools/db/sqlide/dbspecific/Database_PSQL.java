/*

  ideServer_PSQL.java - a postgresql implementation of
  the ideServer class.

 */
package com.hackerdude.devtools.db.sqlide.dbspecific;

import com.hackerdude.devtools.db.sqlide.intf.SQLIDEDBInterface;

import java.sql.*;

public class Database_PSQL implements SQLIDEDBInterface  {
    public boolean supportsUserManagement() {
        /**@todo: Implement this com.hackerdude.devtools.db.sqlide.intf.SQLIDEDBInterface method*/
        throw new java.lang.UnsupportedOperationException("Method supportsUserManagement() not yet implemented.");
    }
    public boolean supportsTriggers() {
        /**@todo: Implement this com.hackerdude.devtools.db.sqlide.intf.SQLIDEDBInterface method*/
        throw new java.lang.UnsupportedOperationException("Method supportsTriggers() not yet implemented.");
    }
    public boolean supportsRules() {
        /**@todo: Implement this com.hackerdude.devtools.db.sqlide.intf.SQLIDEDBInterface method*/
        throw new java.lang.UnsupportedOperationException("Method supportsRules() not yet implemented.");
    }
    public ResultSet getUserList(Connection conn) {
        /**@todo: Implement this com.hackerdude.devtools.db.sqlide.intf.SQLIDEDBInterface method*/
        throw new java.lang.UnsupportedOperationException("Method getUserList() not yet implemented.");
    }
    public ResultSet getCatalogPrivs(Connection conn) {
        /**@todo: Implement this com.hackerdude.devtools.db.sqlide.intf.SQLIDEDBInterface method*/
        throw new java.lang.UnsupportedOperationException("Method getCatalogPrivs() not yet implemented.");
    }
    public ResultSet getTablePrivs(Connection conn) {
        /**@todo: Implement this com.hackerdude.devtools.db.sqlide.intf.SQLIDEDBInterface method*/
        throw new java.lang.UnsupportedOperationException("Method getTablePrivs() not yet implemented.");
    }
    public ResultSet getPerHostPrivs(Connection conn) {
        /**@todo: Implement this com.hackerdude.devtools.db.sqlide.intf.SQLIDEDBInterface method*/
        throw new java.lang.UnsupportedOperationException("Method getPerHostPrivs() not yet implemented.");
    }
    public ResultSet getPerColumnPrivs(Connection conn) {
        /**@todo: Implement this com.hackerdude.devtools.db.sqlide.intf.SQLIDEDBInterface method*/
        throw new java.lang.UnsupportedOperationException("Method getPerColumnPrivs() not yet implemented.");
    }

}