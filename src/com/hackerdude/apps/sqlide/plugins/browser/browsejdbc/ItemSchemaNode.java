package com.hackerdude.apps.sqlide.plugins.browser.browsejdbc;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEItem;
//import com.hackerdude.apps.sqlide.plugins.pgsql.storedproc.CategoryStoredProcedureNode;

/**
 * A Schema Item
 * 
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