package com.hackerdude.devtools.db.sqlide.servertypes;

import java.util.*;

public class MSSQLServerType extends ServerType {

	  public HashMap getProperties() {
		HashMap hm = new HashMap(89);
		return hm;
	  }
	  public String toString() { return "MS SQL Server 2000"; }
	  public String getURL() { return "jdbc:microsoft:sqlserver://localhost:1433"; }
	  public String getClassName() { return "com.microsoft.jdbc.sqlserver.SQLServerDriver"; }
	  public String getServerTitle() { return "MS SQL Server on Localhost"; }
	  public String getDefaultCatalog() { return "master"; }

}