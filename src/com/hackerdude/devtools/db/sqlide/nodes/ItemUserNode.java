package com.hackerdude.devtools.db.sqlide.nodes;

import com.hackerdude.devtools.db.sqlide.dataaccess.*;
import com.hackerdude.devtools.db.sqlide.intf.*;
import com.hackerdude.devtools.db.sqlide.pluginapi.*;

/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
 * @author David Martinez
 * @version 1.0
 */


public class ItemUserNode extends NodeIDEItem {

	String userName;
	String hostName;

	String infoString;

	public final static String ALL_USER = "<all>";
	public final static String ALL_HOST = "<all>";

	public final static String UNSPECIFIED_USER = "<unknown>";
	public final static String UNSPECIFIED_HOST = "<unknown>";

	public ItemUserNode(String userName, String hostName, DatabaseProcess db) {
		super(userName+"@"+hostName, db);
		this.userName = userName;
		this.hostName = hostName;
		infoString = "<HTML><P><B>UserName:</B> "+userName+"<P><B>HostName:</B> "+hostName;
	}

	public boolean canHaveChildren() {
		return false;
	}

	public void readChildren() {    }

	public String getInfo() {
		return infoString;
	}
}