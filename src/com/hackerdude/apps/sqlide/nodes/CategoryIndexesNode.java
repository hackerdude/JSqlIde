package com.hackerdude.apps.sqlide.nodes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDECategory;

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
		Connection conn = null;
		try {
			conn = databaseProcess.getConnection();
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
				if ( indexName != null ) add(new ItemIndexNode(indexName, catalogName, schemaName, tableName, columnName, unique,  databaseProcess ));
			}
			rs.close();
			databaseProcess.returnConnection(conn);
		} catch ( SQLException exc ) {
		exc.printStackTrace();
		databaseProcess.returnConnection(conn);
		}

	}
	public String getInfo() {
		return "<HTML><P>Indices";
	}
}
