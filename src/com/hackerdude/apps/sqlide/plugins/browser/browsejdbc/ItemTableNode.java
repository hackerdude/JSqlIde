/**
 * Title:        JSqlIde<p>
 * Description:  A Java SQL Integrated Development Environment
 * <p>
 * Copyright:    Copyright (c) David Martinez<p>
 * Company:      <p>
 * @author David Martinez
 * @version 1.0
 */
package com.hackerdude.apps.sqlide.plugins.browser.browsejdbc;

import com.hackerdude.apps.sqlide.pluginapi.*;
import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.nodes.CategoryIndexesNode;
import java.sql.*;
import javax.swing.tree.*;

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