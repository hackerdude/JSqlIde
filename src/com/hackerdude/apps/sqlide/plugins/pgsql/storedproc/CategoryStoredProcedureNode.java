/**
 * Title:        JSqlIde<p>
 * Description:  A Java SQL Integrated Development Environment
 * <p>
 * Copyright:    Copyright (c) David Martinez<p>
 * Company:      <p>
 * @author David Martinez
 * @version 1.0
 */
package com.hackerdude.apps.sqlide.plugins.pgsql.storedproc;

import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.pluginapi.*;
import javax.swing.tree.*;
import javax.swing.*;
import java.util.*;
import java.sql.*;
import com.hackerdude.apps.sqlide.*;


/**
 * Node for the stored procedure category.
 */
public class CategoryStoredProcedureNode extends NodeIDECategory {
	 String schemaName;
	 String catalogName;

	 public CategoryStoredProcedureNode(String catalogName, String schema, DatabaseProcess proc) {
		super("Stored Procedures", proc);
		this.catalogName = catalogName;
		schemaName = schema;
	  }

	 public void readChildren() {
	 }

	public String getCatalogName() { return catalogName; }

	public String getSchemaName() { return schemaName; }

	 public String getInfo() { return "<HTML>Procedures"; }


	 public ImageIcon getIcon() { return ProgramIcons.getInstance().getStoredProcIcon(); }
}
