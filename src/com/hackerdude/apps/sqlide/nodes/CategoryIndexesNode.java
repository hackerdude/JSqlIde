package com.hackerdude.apps.sqlide.nodes;

import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.pluginapi.*;
import java.sql.*;

/**
 * Index category node.
 */
public class CategoryIndexesNode extends NodeIDECategory {

	String catalogName;
	private int iIndices = 0;
	private String tableName;
	String schemaName;

	public CategoryIndexesNode(String tableName, DatabaseProcess proc, String catalogName, String schemaName) {
		super("Indices", proc);
		this.catalogName = catalogName;
		this.tableName = tableName;
		this.schemaName = schemaName;
	}

	public void readChildren() {
		Connection conn = db.getConnection();
		try {
			ResultSet rs = conn.getMetaData().getIndexInfo(catalogName, schemaName, tableName, false, true);
			iIndices = 0;
			while ( rs.next() ) {
				String indexName = rs.getString("INDEX_NAME");
				//         String catalogName = rs.getString("TABLE_CAT");
				String columnName = rs.getString("COLUMN_NAME");
				String schemaName  = rs.getString("TABLE_SCHEM");
				String tableName   = rs.getString("TABLE_NAME");
				boolean unique     = true;
				try {
					unique = ! rs.getBoolean("NON_UNIQUE");
				} catch (Exception exc ) {}
				add(new ItemIndexNode(indexName, catalogName, schemaName, tableName, columnName, unique,  db ));
			}
			rs.close();
			db.returnConnection(conn);
		} catch ( SQLException exc ) {
		exc.printStackTrace();
		db.returnConnection(conn);
		}

	}
	public String getInfo() {
		return "<HTML><P>Indices";
	}
}
