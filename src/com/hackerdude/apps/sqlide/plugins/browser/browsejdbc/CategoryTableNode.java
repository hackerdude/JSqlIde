package com.hackerdude.apps.sqlide.plugins.browser.browsejdbc;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDECategory;

/**
 * A category with tables inside of it.
 *
 * 
 * @author David Martinez
 * @version 1.0
 */
public class CategoryTableNode extends NodeIDECategory {

	String schemaName;
	String catalogName;

	public CategoryTableNode(String schemaName, String catalogName, DatabaseProcess proc) {
		super("Tables", proc);
		this.schemaName  = schemaName;
		this.catalogName = catalogName;
	}

	public void readChildren() {
		DefaultMutableTreeNode dbItem = null;
		Vector vc = databaseProcess.getTablesIn(schemaName, catalogName);
		for(int i=0;i<vc.size();i++) {
			dbItem = new ItemTableNode(catalogName, schemaName, vc.elementAt(i).toString(), databaseProcess);
			add(dbItem);
		}
	}

	public boolean canHaveChildren() { return true; }

	public String getInfo() { return "<HTML><P>Tables"; }

}

