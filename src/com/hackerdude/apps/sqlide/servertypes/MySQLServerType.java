package com.hackerdude.apps.sqlide.servertypes;

import java.util.HashMap;

/**
 * This is the MySQL Server Type. This class
 * sets all the defaults that a new server
 * needs to set. It's useful for the New Connection
 * Wizard to know what to set by default.
 */
public class MySQLServerType extends ServerType {
	  public HashMap getProperties() {
		HashMap hm = new HashMap(89);
		hm.put("password", _ASK_);
		hm.put("username", System.getProperty("user.name"));
		return hm;
	  }
	  public String toString() { return "MM MySQL Server Driver"; }
	  public String getURL() { return "jdbc:mysql://localhost:3306/mysql?user="+System.getProperty("user.name"); }
	  public String getClassName() { return "org.gjt.mm.mysql.Driver"; }
	  public String getServerTitle() { return "MySQL on Localhost"; }
	  public String getDefaultCatalog() { return "mysql"; }

	  public boolean supportsSQLDotNotation() { return false; }

}
