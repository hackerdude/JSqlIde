package com.hackerdude.apps.sqlide.plugins.browser.browsejdbc;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEItem;

/**
 * A Table Column
 * @copyright (C) 1998-2002 Hackerdude (David Martinez). All Rights Reserved.
 * @author David Martinez
 * @version 1.0
 */
public class ItemTableColumnNode extends NodeIDEItem {
	private int size;
	private String typeName;
	private String catalogName;
	private String tableName;

	private boolean nullable;

	public ItemTableColumnNode(String catalogName, String tableName, String columnName, String typeName, int size, boolean nullable, DatabaseProcess proc) {
		super(columnName, proc);
		this.typeName = typeName;
		this.size = size;
		this.nullable = nullable;
		this.catalogName = catalogName;
		this.tableName = tableName;
	};


	public void readChildren() {
	}

	public String toString() {
		return itemName+" "+typeName+"("+size+")";
	}


	public String getCatalogName() { return catalogName; }

	public String getTableName() { return tableName; }

	public String getColumnName() { return itemName; }

	public String getColumnType() { return typeName; }

	public boolean canHaveChildren() { return false; }

	public int getColumnSize() { return size; }

	public String getInfo() {

		StringBuffer info = new StringBuffer("<HTML><P><B>Type:</B>"+typeName);
		info.append(" (").append(size).append(")");
		info.append("<P><B>Allows Null:</B> ");
		if ( nullable ) info.append("yes"); else
		info.append("no");
		return info.toString();
	}


}