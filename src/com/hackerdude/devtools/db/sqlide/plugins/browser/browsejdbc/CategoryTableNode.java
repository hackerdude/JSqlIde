
/**
 * Title:        JSqlIde<p>
 * Description:  A Java SQL Integrated Development Environment
 * <p>
 * Copyright:    Copyright (c) David Martinez<p>
 * Company:      <p>
 * @author David Martinez
 * @version 1.0
 */
package com.hackerdude.devtools.db.sqlide.plugins.browser.browsejdbc;


import javax.swing.tree.*;
import java.util.*;
import com.hackerdude.devtools.db.sqlide.pluginapi.*;
import com.hackerdude.devtools.db.sqlide.dataaccess.*;

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
		Vector vc = db.getTablesIn(schemaName, catalogName);
		for(int i=0;i<vc.size();i++) {
		   dbItem = new ItemTableNode(catalogName, schemaName, vc.elementAt(i).toString(), db);
		   add(dbItem);
		}
	 }

	 public boolean canHaveChildren() { return true; }

	 public String getInfo() { return "<HTML><P>Tables"; }

  };

