package com.hackerdude.devtools.db.sqlide.servertypes;

import java.util.HashMap;

/**
 * SQLIDE Server Type for Sybase
 */
public class SybaseServerType extends ServerType {

	  public HashMap getProperties() {
		HashMap hm = new HashMap(89);
		hm.put("password", _ASK_);
		hm.put("user", System.getProperty("user.name"));
		return hm;
	  }
	  public String toString() { return "Sybase ASE"; }
	  public String getURL() { return "jdbc:sybase:Tds:hostname:4100/master"; }
	  public String getClassName() { return "com.sybase.jdbc.SybDriver"; }
	  public String getServerTitle() { return "Sybase ASE on hostname"; }
	  public String getDefaultCatalog() { return "master"; }

}