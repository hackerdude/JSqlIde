package com.hackerdude.apps.sqlide.components;

import com.hackerdude.apps.sqlide.dataaccess.QueryResults;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.*;
import java.sql.*;

/**
 * Table Model for a Scrollable ResultSet.
 *
 * <P>This table model uses a JDBC 2.0 resultset of type TYPE_SCROLL*.
 *
 * @author David Martinez <A HREF="mailto:david@hackerdude.com">david@hackerdude.com</A>
 * @version 1.0
 */
public class ScrollableResultSetTableModel extends AbstractTableModel {

	QueryResults queryResults;
	int columnCount;
	int rowCount;
	String[] columnNames;
	Class[]  columnClasses;
	ResultSet resultSet;

	public ScrollableResultSetTableModel(QueryResults queryResults) {
		this.queryResults = queryResults;
		resultSet = queryResults.getResultSet();
		columnNames = queryResults.getColumnNames();
		columnClasses = queryResults.getColumnClasses();
		// Since this is a scrollable cursor it's okay to go to the bottom right away.
		try {
			resultSet.last();
			rowCount = resultSet.getRow();
		} catch (SQLException exc) {
			exc.printStackTrace();
			rowCount = 0;
		}
	}

	public int getColumnCount() {
			return columnCount;
	}

	public Class getColumnClass(int col) {
		return columnClasses[col];
	}

	public Object getValueAt(int row, int column) {
		Object result = null;

		try {
			if ( resultSet == null ) return null;
			resultSet.absolute(row+1);
			result = resultSet.getObject(column+1);
			if ( result == null ) result = resultSet.getString(column+1);
		} catch (SQLException exc) {
			return "SQLException";
		}
		return result;
	}

	public int getRowCount() {
		return rowCount;
	}

	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

}
