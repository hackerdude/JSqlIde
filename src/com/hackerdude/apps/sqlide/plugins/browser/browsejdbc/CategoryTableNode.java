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

import javax.swing.tree.*;
import java.util.*;
import com.hackerdude.apps.sqlide.pluginapi.*;
import com.hackerdude.apps.sqlide.dataaccess.*;

/**
 * A category with tables inside of it.
 *
 * @copyright (C) 1998-2002 Hackerdude (David Martinez). All Rights Reserved.
 * @author David Martinez
 * @version 1.0
 */
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

}

