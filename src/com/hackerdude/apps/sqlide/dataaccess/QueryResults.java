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
