package com.hackerdude.apps.sqlide.dataaccess;

import java.sql.*;
import java.util.*;

/**
 * An encapsulation of the query results so that JDBC 1.0 drivers can remain
 * compatible with their 2.0 counterparts.
 */

public class QueryResults {

	private ResultSet results;
	private ResultSetMetaData metaData;

	private ArrayList columnNames;
	private ArrayList columnTypes;
	private int[] columnSQLTypes;
	private int[] columnSizes;

	private int columnCount;

	private int rowsAffected;

	public QueryResults(ResultSet results) throws SQLException {
		columnNames = new ArrayList();
		columnTypes = new ArrayList();
		this.results = results;
		this.metaData = results.getMetaData();
		readColumnNames();
	}

	/**
	 * Returns the underlying resultset for this object.
	 * @return the underlying resultset for this object.
	 */
	public ResultSet getResultSet() { return results; }

	/**
	 * Returns the metadata
	 * @return the query metadata.
	 */
	public ResultSetMetaData getMetaData() { return metaData; }

	/**
	 * Returns the number of rows affected.
	 * @return The number of rows affected.
	 */
	public int getRowsAffected() { return rowsAffected; }

	/**
	 * Returns the sizes of the columns
	 * @return an array with the column sizes.
	 */
	public int[] getColumnSizes() { return columnSizes; }

	public String[] getColumnNames() {
		String[] result = new String[columnNames.size()];
		result = (String[])columnNames.toArray(result);
		return result;
	}


	/**
	 * Returns an array of java classes that contain the column types.
	 * @return
	 */
	public Class[] getColumnClasses() {
		Class[] result = new Class[columnTypes.size()];
		result = (Class[])columnTypes.toArray(result);
		return result;
	}

	/**
	 * Reads the column names and types, adding them to the arrayList.
	 */
	private void readColumnNames() throws SQLException {
		columnCount = metaData.getColumnCount();
		columnSizes = new int[columnCount];
		columnSQLTypes = new int[columnCount];
		for ( int i=0; i<columnCount; i++ ) {
			int colIndex = i+1;
			String columnName = metaData.getColumnLabel(colIndex);
			columnNames.add(columnName);
			int sqlType = metaData.getColumnType(colIndex);
			columnSQLTypes[i] = sqlType;
			try {
				String columnClassName = metaData.getColumnClassName(colIndex);
				Class theClass =Class.forName(columnClassName);
				columnTypes.add(theClass);
			} catch ( Throwable exc ) {
				if ( sqlType == java.sql.Types.CLOB ) {
					columnTypes.add(java.sql.Clob.class);
				} else {
					columnTypes.add(String.class);
				}
			}

			int displaySize = 20;
			try {
				columnSizes[i] = metaData.getColumnDisplaySize(colIndex);
			} catch ( Throwable exc ) {	}
			columnSizes[i] = displaySize;
		}
	}

	/**
	 * Returns the number of columns
	 * @return The number of columns.
	 */
	public int getColumnCount() { return columnCount; }

	/**
	 * Returns the name of the columnn specified by its index
	 * @param i The index of the column
	 * @return The name of the column
	 */
	public String getColumnName(int i) {
		return (String)columnNames.get(i);
	}

	/**
	 * Returns the size of the column.
	 * @param i The column index
	 * @return The size of the column with index i
	 */
	public int getColumnSize(int i) {
		return columnSizes[i];
	}

}
