package com.hackerdude.apps.sqlide.plugins.browser.browsejdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDECategory;

/**
 * A node for a schema category
 *
 * @copyright (C) 1998-2002 Hackerdude (David Martinez). All Rights Reserved.
 * @author David Martinez
 * @version 1.0
 */
public class CategorySchemaNode extends NodeIDECategory {

	String schemaTerm;

   public CategorySchemaNode(DatabaseProcess proc) throws SQLException {
	   super(determineCatalogTerm(proc), proc);
	   schemaTerm = determineCatalogTerm(proc);
   }

   public void readChildren() {
		DefaultMutableTreeNode dbItem = null;
		try {
			Vector vc = db.getSchemas();
			for(int i=0;i<vc.size();i++) {
				dbItem = new ItemSchemaNode(vc.elementAt(i).toString(), db);
				add(dbItem);
			}
		} catch ( Throwable exc ) {
			add(new ItemSchemaNode(null, db));
		}


   }

   public boolean canHaveChildren() { return true; }

   public String getInfo() {
	   return "<HTML><P>"+schemaTerm;
   }

   public String getSchemaTerm() {
	   return schemaTerm;
   }

   public static String determineCatalogTerm(DatabaseProcess proc) throws SQLException {
	   Connection conn = null;
	   try {
		   conn = proc.getConnection();
		   return conn.getMetaData().getSchemaTerm();
	   }
	   finally {
		   proc.returnConnection(conn);
	   }

   }
}
