package com.hackerdude.devtools.db.sqlide.components;

import com.hackerdude.devtools.db.sqlide.dataaccess.QueryResults;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.*;
import java.sql.*;

/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
 * @author David Martinez
 * @version 1.0
 */

public class ScrollableResultSetTableModel extends AbstractTableModel {

	QueryResults queryResults;
	int columnCount;
	int rowCount;
	String[] columnNames;
	Class[]  getColumnClasses;
	ResultSet resultSet;

	public ScrollableResultSetTableModel(QueryResults queryResults) {
		this.queryResults = queryResults;
		resultSet = queryResults.getResultSet();
		columnNames = queryResults.getColumnNames();
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

	public Object getValueAt(int row, int column) {
		Object result = null;

		try {
			resultSet.absolute(row+1);
			result = resultSet.getObject(column+1);
			if ( result == null ) result = resultSet.getString(column+1);
		} catch (SQLException exc) {
			exc.printStackTrace();
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