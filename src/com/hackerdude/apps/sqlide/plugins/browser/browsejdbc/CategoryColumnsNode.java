package com.hackerdude.apps.sqlide.plugins.browser.browsejdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDECategory;

/**
 * A node for category columns.
 * 
 * @author David Martinez
 * @version 1.0
 */
public class CategoryColumnsNode extends NodeIDECategory {

	String catalogName;
	private int iColumnCount;
	String tableName;

	public CategoryColumnsNode(String tableName, DatabaseProcess proc, String catalogName) {
		super("Columns", proc);
		this.catalogName = catalogName;
		this.tableName = tableName;
	}

	public void readChildren() {
		Connection conn = null;
		try {
			conn = databaseProcess.getConnection();
			ResultSet rs = conn.getMetaData().getColumns(catalogName, null, tableName, null);
			iColumnCount = 0;
			while ( rs.next() ) {
				iColumnCount++;
				String fieldName  = rs.getString("COLUMN_NAME");
				String typeName = rs.getString("TYPE_NAME");
				int columnSize   = -999;
				try {
					columnSize = rs.getInt("COLUMN_SIZE");
				} catch ( Exception exc ) { }
				int iNullable     = -999;
				try {
					iNullable = rs.getInt("NULLABLE");
				} catch ( Exception exc ) { }
				boolean nullable  = ( iNullable == DatabaseMetaData.columnNullable );
				add(new ItemTableColumnNode(catalogName, tableName, fieldName, typeName, columnSize, nullable, databaseProcess ));
			}
			rs.close();
			databaseProcess.returnConnection(conn);
		} catch ( SQLException exc ) {
			exc.printStackTrace();
			databaseProcess.returnConnection(conn);
		}
	}
	public String getInfo() {
		return "<HTML><P><B>"+iColumnCount+"</B> columns for <B>"+tableName+"</B>";
	}
}
