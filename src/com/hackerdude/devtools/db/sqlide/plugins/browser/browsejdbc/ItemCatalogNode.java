
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


  public class ItemCatalogNode extends NodeIDEItem {

	 String catalogName;

	 public ItemCatalogNode(String containerName, DatabaseProcess proc) {
		super(containerName, proc);
		catalogName = containerName;
	};

	  public void readChildren() {
		add( new CategoryTableNode(null, catalogName, db));
	 }

	 public boolean canHaveChildren() { return true; }

	 public String getInfo() { return "<HTML><P><B>"+db.getCatalogTitle()+" name</B>:"+catalogName; }

  };
