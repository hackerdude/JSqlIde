
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

import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.pluginapi.*;
import javax.swing.tree.*;
import javax.swing.*;
import java.util.*;
import java.sql.*;


public class CategoryStoredProcedureNode extends NodeIDECategory {
	 String schemaName;
	 String catalogName;
	 public CategoryStoredProcedureNode(String catalogName, String schema, DatabaseProcess proc) {
		super("Stored Procedures", proc);
		this.catalogName = catalogName;
		schemaName = schema;
	  }
	 public void readChildren() {

		ItemStoredProcedureNode dbItem = null;

/*		Vector vc = db.getStoredProcedures(schemaName);
		for(int i=0;i<vc.size();i++) {
		   dbItem = new ItemStoredProcedureNode(vc.elementAt(i).toString(), db);
		   add(dbItem);
		}*/

		try {
			Connection conn = db.getPool().getConnection();
//			db.changeCatalog(conn);
			try {
				ResultSet rs = conn.getMetaData().getProcedures(catalogName, schemaName, null );
				while(rs.next()) {

					dbItem = new ItemStoredProcedureNode(rs.getString("PROCEDURE_NAME"),  db);
					String remarks = "No remarks";
					try {
						remarks = rs.getString("REMARKS");
					} catch ( Throwable th ) {}
					dbItem.setRemarks(remarks);
					add( dbItem );

				}
			} finally { db.getPool().releaseConnection(conn); }
		} catch( SQLException sqle ) {
				sqle.printStackTrace();
				JOptionPane.showMessageDialog(null, sqle,
								"SQL Error when getting procedures",
								JOptionPane.ERROR_MESSAGE);
		}

	 }

	 public String getInfo() { return "<HTML>Procedures"; }

}
