package com.hackerdude.apps.sqlide.plugins.browser.browsejdbc;

import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.pluginapi.*;
//import com.hackerdude.apps.sqlide.plugins.pgsql.storedproc.CategoryStoredProcedureNode;

/**
 * A Schema Item
 * @copyright (C) 1998-2002 Hackerdude (David Martinez). All Rights Reserved.
 * @author David Martinez
 * @version 1.0
 */
public class ItemSchemaNode extends NodeIDEItem {

	String contName;
	public ItemSchemaNode(String containerName, DatabaseProcess proc) {
		super(containerName, proc);
		contName = containerName;
	}

	public void readChildren() {
		add( new CategoryTableNode(contName, contName, db));
//		add( new CategoryStoredProcedureNode(null, contName, db));
	}

	public boolean canHaveChildren() { return true; }

	public String getInfo() { return "<HTML><P>Schema:"+contName; }


}