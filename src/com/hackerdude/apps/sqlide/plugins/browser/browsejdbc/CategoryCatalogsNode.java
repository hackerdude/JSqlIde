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
import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.pluginapi.*;
import java.util.*;
import javax.swing.tree.*;
import java.sql.*;

public class CategoryCatalogsNode extends NodeIDECategory {

	 public CategoryCatalogsNode(DatabaseProcess proc) {
		super(getNodeName(proc), proc);
	  }

	 public void readChildren() {

		try {
			ArrayList al = db.getCatalogs();
			DefaultMutableTreeNode dbItem = null;

			for(int i=0;i<al.size();i++) {
			   dbItem = new ItemCatalogNode(al.get(i).toString(), db);
			   add(dbItem);
			}

		} catch ( SQLException exc ) {
			add(new ItemCatalogNode(null, db));
		}


	 }

	 public static String getNodeName(DatabaseProcess proc) {
		String nodeName = proc.getCatalogTitle();
		if ( nodeName == null || nodeName.equals("") ) nodeName = "Catalogs";
		return nodeName;
	 }
	 public boolean canHaveChildren() { return true; }

	 public String getInfo() { return "<HTML><P><B>"+db.getCatalogTitle(); }

}
