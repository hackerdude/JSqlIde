package com.hackerdude.devtools.db.sqlide.servertypes;

import java.util.HashMap;

/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
 * @author David Martinez
 * @version 1.0
 */

public class InterClientServerType extends ServerType {

	public InterClientServerType() {
	}
	public HashMap getProperties() {
		HashMap result = new HashMap();
		return result;
	}
	public String getServerTitle() {
		return "InterClient Interbase Driverr";
	}
	public String getDefaultCatalog() {
		return "";
	}
	public String getClassName() {
		return "interbase.interclient.Driver";
	}
	public String getURL() {
		return "jdbc:interbase://server/c:/database-dir/databasename.gdb";
	}

	public boolean supportsSQLDotNotation() { return true; }

	public String toString() {
		return "Interbase Server Driver";
	}
}