package com.hackerdude.devtools.db.sqlide.dataaccess;

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

	public ResultSet getResultSet() { return results; }

	public ResultSetMetaData getMetaData() { return metaData; }

	public int getRowsAffected() { return rowsAffected; }


	public int[] getColumnSizes() { return columnSizes; }

	public String[] getColumnNames() {
		String[] result = new String[columnNames.size()];
		result = (String[])columnNames.toArray(result);
		return result;
	}


	public Class[] getColumnClasses() {
		Class[] result = new Class[columnNames.size()];
		result = (Class[])columnTypes.toArray(result);
		return result;
	}

	/**
	 * Reads the column names and types, adding them to the arrayList.
	 */
	private void readColumnNames() throws SQLException {
		columnCount = metaData.getColumnCount();
		columnSizes = new int[columnCount];
		for ( int i=0; i<columnCount; i++ ) {
			String columnName = metaData.getColumnLabel(i+1);
			columnNames.add(columnName);
			try {
				String columnClassName = metaData.getColumnClassName(i+1);
				columnTypes.add(Class.forName(columnClassName));
			} catch ( Throwable exc ) {
//				System.out.println("[QueryResults]  "+exc.toString()+" while trying to retrieve column class for "+columnName+". Will use String in its place.");
				columnTypes.add(String.class);
			}
			try {
				columnSizes[i] = metaData.getColumnDisplaySize(i+1);
			} catch ( Throwable exc ) {
				exc.printStackTrace();
				columnSizes[i] = 20;
			}
		}
	}


	public int getColumnCount() { return columnCount; }

	public String getColumnName(int i) {
		return (String)columnNames.get(i);
	}

	public int getColumnSize(int i) {
		return columnSizes[i];
	}

}