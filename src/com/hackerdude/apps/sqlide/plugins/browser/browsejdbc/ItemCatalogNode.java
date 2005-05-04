package com.hackerdude.apps.sqlide.plugins.browser.browsejdbc;

import java.sql.SQLException;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEItem;

public class ItemCatalogNode extends NodeIDEItem {

	String catalogName;
	String catalogInfo;

	public ItemCatalogNode(String containerName, DatabaseProcess proc) throws SQLException {
		super(containerName, proc);
		catalogName = containerName;
		catalogInfo = "<HTML><P><B>"+CategoryCatalogsNode.determineCatalogTerm(databaseProcess)+" name</B>: "+catalogName;
	};

	public void readChildren() {
		add( new CategoryTableNode(null, catalogName, databaseProcess));
	}

	public boolean canHaveChildren() { return true; }

	public String getInfo() {
		return catalogInfo;
	}

}