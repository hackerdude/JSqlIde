package com.hackerdude.devtools.db.sqlide.servertypes;

import java.util.HashMap;

/**
 * A server type for PostgreSQL servers.
 */
public class PostgreSQLServerType extends ServerType {

	  public HashMap getProperties() {
		HashMap hm = new HashMap(89);
		hm.put("password", _ASK_);
		hm.put("HOSTNAME","localhost");
		hm.put("USERNAME", System.getProperty("user.name"));
		return hm;
	  }
	  public String toString() { return "PostgreSQL JDBC Driver"; }
	  public String getURL() { return "jdbc:postgresql://localhost:5432/"+System.getProperty("user.name"); }
	  public String getClassName() { return "postgresql.Driver"; }
	  public String getServerTitle() { return "PostgreSQL on LocalHost"; }
	  public String getDefaultCatalog() { return "test"; }

}