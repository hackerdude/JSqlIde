package com.hackerdude.apps.sqlide.plugins.browser.browsejdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.nodes.CategoryIndexesNode;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEItem;

/**
 * A table item.
 * @copyright (C) 1998-2002 Hackerdude (David Martinez). All Rights Reserved.
 * @author David Martinez
 * @version 1.0
 */
public class ItemTableNode extends NodeIDEItem {

	private int iIndexCount;

	private String catalogName = null;
	private String schemaName = null;

	public ItemTableNode(String catalogName, String schemaName, String name, DatabaseProcess proc) {
		super(name, proc);
		this.catalogName = catalogName;
		this.schemaName = schemaName;
	}

	public void readChildren() {
		add(new CategoryColumnsNode(itemName, db, catalogName));
		add(new CategoryIndexesNode(itemName, db, catalogName, schemaName));
	}

	public boolean canHaveChildren() {
		return true;
	}

	public String getInfo() {
		return "<HTML><P><B>Catalog: </B>" + catalogName + "<P><B>Table: </B>" + itemName;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public ResultSet readColumns(Connection conn) throws SQLException {
		return conn.getMetaData().getColumns(catalogName, schemaName, toString(), null);
	}

}