package com.hackerdude.apps.sqlide.plugins.browser.browsejdbc;

import java.sql.*;

import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.pluginapi.*;

public class ItemCatalogNode extends NodeIDEItem {

	String catalogName;
	String catalogInfo;

	public ItemCatalogNode(String containerName, DatabaseProcess proc) throws SQLException {
		super(containerName, proc);
		catalogName = containerName;
		catalogInfo = "<HTML><P><B>"+CategoryCatalogsNode.determineCatalogTerm(db)+" name</B>: "+catalogName;
	};

	public void readChildren() {
		add( new CategoryTableNode(null, catalogName, db));
	}

	public boolean canHaveChildren() { return true; }

	public String getInfo() {
		return catalogInfo;
	}

}