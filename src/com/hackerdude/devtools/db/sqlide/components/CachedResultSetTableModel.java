package com.hackerdude.devtools.db.sqlide.components;

import javax.swing.table.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
import java.util.*;
import com.hackerdude.devtools.db.sqlide.dataaccess.QueryResults;

/**
 * A cached Resultset table model.
 */
public class CachedResultSetTableModel extends DefaultTableModel {

	int columnCount;
	int rowCount;

	String[] columnNames;
	Class[] columnClasses;
	QueryResults queryResults;
	boolean readingDone = false;

	public static final int bufferIncrements = 200;

	public CachedResultSetTableModel(QueryResults queryResults, int initialRows) {
		this.queryResults = queryResults;
		try {
			this.columnNames = queryResults.getColumnNames();
			this.columnClasses = queryResults.getColumnClasses();
			columnCount = columnNames.length;
			getMoreRows(bufferIncrements);
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