package com.hackerdude.devtools.db.sqlide.servertypes;

import java.util.HashMap;

/**
 *  A server type for Oracle Thin driver.
 */

public class OracleServerType extends ServerType {

	  public HashMap getProperties() {
		HashMap hm = new HashMap(89);
		hm.put("password", _ASK_);
		hm.put("dll", "ocijdbc8");
		hm.put("protocol", "thin");
		hm.put("host","localhost");
		hm.put("username", System.getProperty("user.name"));
		return hm;
	  }
	  public String toString() { return "Oracle 8i with THIN Driver"; }
	  public String getURL() { return "jdbc:oracle:thin:"+System.getProperty("user.name")+"/password@localhost:1521:database"; }
	  public String getClassName() { return "oracle.jdbc.driver.OracleDriver"; }
	  public String getServerTitle() { return "Oracle 8i on Localhost"; }
	  public String getDefaultCatalog() { return "mysql"; }

}