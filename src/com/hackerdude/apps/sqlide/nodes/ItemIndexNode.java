
/**
 * Title:        JSqlIde<p>
 * Description:  A Java SQL Integrated Development Environment
 * <p>
 * Copyright:    Copyright (c) David Martinez<p>
 * Company:      <p>
 * @author David Martinez
 * @version 1.0
 */
package com.hackerdude.apps.sqlide.nodes;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEItem;

public class ItemIndexNode  extends NodeIDEItem {

	 String indexName;
	 String columnName;
	 String catalogName;
	 String tableName;
	 String schemaName;
	 boolean unique;

	 public ItemIndexNode(String indexName, String catalog, String schema, String table, String columnName, boolean unique, DatabaseProcess proc) {
		  super(indexName, proc);
		  this.columnName = columnName;
		  this.indexName = indexName;
		  this.catalogName = catalog;
		  this.schemaName  = schema;
		  this.tableName   = table;
		  this.unique      = unique;

	 }

	 public void readChildren() {}

	 public boolean canHaveChildren() { return false; }

	 public String getInfo() { return "<HTML><P>Index: "+itemName+"<P>Column: "+columnName; }

  };
