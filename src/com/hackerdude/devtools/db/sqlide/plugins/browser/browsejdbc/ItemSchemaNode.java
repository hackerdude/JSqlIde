
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
import com.hackerdude.devtools.db.sqlide.dataaccess.*;
import com.hackerdude.devtools.db.sqlide.pluginapi.*;
import com.hackerdude.devtools.db.sqlide.nodes.CategoryStoredProcedureNode;

  public class ItemSchemaNode extends NodeIDEItem {

	 String contName;
	 public ItemSchemaNode(String containerName, DatabaseProcess proc) {
		super(containerName, proc);
		contName = containerName;
	}

	  public void readChildren() {
		add( new CategoryTableNode(contName, contName, db));
		add( new CategoryStoredProcedureNode(null, contName, db));
	 }

	 public boolean canHaveChildren() { return true; }

	 public String getInfo() { return "<HTML><P>Schema:"+contName; }

  };

