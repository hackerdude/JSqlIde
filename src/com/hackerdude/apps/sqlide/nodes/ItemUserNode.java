package com.hackerdude.apps.sqlide.nodes;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEItem;

/**
 * Users node.
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
