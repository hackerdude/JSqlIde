package com.hackerdude.apps.sqlide.plugins.browser.browsejdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDECategory;

/**
 * A node for the Catalogs category.
 * 
 * @author David Martinez
 * @version 1.0
 */
public class CategoryCatalogsNode extends NodeIDECategory {

	String catalogTerm;

	public CategoryCatalogsNode(DatabaseProcess proc) throws SQLException {
		super(determineCatalogTerm(proc), proc);
		catalogTerm = determineCatalogTerm(proc);
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
			try {
				add(new ItemCatalogNode(null, db));
			}
			catch (Exception ex) {
			}
		}


	}
	public static String determineCatalogTerm(DatabaseProcess proc) throws SQLException {
		Connection conn = null;
		try {
			conn = proc.getConnection();
			return conn.getMetaData().getCatalogTerm();
		}
		finally {
			proc.returnConnection(conn);
		}

	}

	public boolean canHaveChildren() { return true; }

	public String getInfo() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<HTML><P><B>");
		try {
			stringBuffer.append(determineCatalogTerm(db));
		}
		catch (Exception ex) {
		}
		return stringBuffer.toString();
	}



}