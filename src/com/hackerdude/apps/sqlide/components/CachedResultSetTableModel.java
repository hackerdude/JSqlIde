package com.hackerdude.apps.sqlide.components;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.hackerdude.apps.sqlide.dataaccess.QueryResults;

/**
 * A cached Resultset table model for forward-only resultsets.
 *
 * <P>A cached resultset table model reads the resultset in small increments.
 * As the user scrolls down, rows are added.
 *
 * <P>This is useful for showing forward-only result sets. It requires
 * potentially as much memory as the resultset itself.
 */
public class CachedResultSetTableModel extends DefaultTableModel {

	int columnCount;
	int rowCount;

	String[] columnNames;
	Class[] columnClasses;
	QueryResults queryResults;
	boolean readingDone = false;

	/**
	 * The maximum amount of rows to fetch every time more rows are needed.
	 */
	public static final int BUFFER_INCREMENT = 200;

	public CachedResultSetTableModel(QueryResults queryResults, int initialRows) {
		this.queryResults = queryResults;
		try {
			this.columnNames = queryResults.getColumnNames();
			this.columnClasses = queryResults.getColumnClasses();
			columnCount = columnNames.length;
			getMoreRows(BUFFER_INCREMENT);
		} catch ( SQLException exc ) {
			JOptionPane.showMessageDialog(null, "SQL Error while retrieving query results:"+exc.toString(), "Query Results Table", JOptionPane.ERROR_MESSAGE);
		}
	}

	public Class getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}

	public String getColumnName(int columnIndex)  {
		return columnNames[columnIndex];
	}

	public int getRowCount() { return rowCount; }

	public int getColumnCount() { return columnCount; }

	private void getMoreRows(int maxRows) throws SQLException {
		int rowsRead = 0;
		ResultSet resultSet = queryResults.getResultSet();
		boolean done = !resultSet.next();
		while ( !done ) {
			rowsRead++;
			//
			Vector newRow = new Vector();
			for ( int i=0; i<columnCount; i++) {
				try {
					newRow.add(resultSet.getObject(i+1));
				} catch ( Throwable exc ) { newRow.add(null); }
			}
			if ( rowsRead >= maxRows ) break;
			done = !resultSet.next();
			addRow(newRow);
		}
		rowCount = rowCount + rowsRead;
		readingDone = done;
	}


}
