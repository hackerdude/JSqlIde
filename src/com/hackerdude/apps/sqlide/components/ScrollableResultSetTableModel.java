package com.hackerdude.apps.sqlide.components;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import com.hackerdude.apps.sqlide.dataaccess.QueryResults;

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
	int rowCount;
	String[] columnNames;
	Class[]  columnClasses;
	ResultSet resultSet;

	ArrayList insertBuffers = new ArrayList();

	public ScrollableResultSetTableModel(QueryResults queryResults) {
		this.queryResults = queryResults;
		resultSet = queryResults.getResultSet();
		columnNames = queryResults.getColumnNames();
		columnClasses = queryResults.getColumnClasses();
		setRowCountFromResultSet();
	}

	private void setRowCountFromResultSet() {
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
			return columnNames.length;
	}

	public Class getColumnClass(int col) {
		return columnClasses[col];
	}

	public void setValueAt(Object obj, int row, int column) {
		if ( row < rowCount ) {
			try {
				if (resultSet == null)
					return;
				resultSet.absolute(row + 1);
				resultSet.updateString(column + 1, obj.toString());
				resultSet.updateRow();
			} catch (SQLException exc) {
				exc.printStackTrace();
			}
		} else {
			setInsertBufferValueAt(obj, row-rowCount, column);
		}
	}

	public void setInsertBufferValueAt(Object obj, int row, int column) {
		RowInserting currentRow = (RowInserting)insertBuffers.get(row);
		currentRow.values[column] = obj;
	}

	public Object getInsertBufferValueAt(int row, int column) {
		RowInserting currentRow = (RowInserting)insertBuffers.get(row);
		return currentRow.values[column];
	}

	public Object getValueAt(int row, int column) {
		Object result = null;
		if ( row < rowCount )  {
			try {
				if (resultSet == null)
					return null;
				resultSet.absolute(row + 1);
				result = resultSet.getObject(column + 1);
				if (result == null)
					result = resultSet.getString(column + 1);
			} catch (SQLException exc) {
				resultSet = null;
				return "SQLException";
			}
		} else {
			return getInsertBufferValueAt(row-rowCount, column);
		}
		return result;
	}

	public int getRowCount() {
		return rowCount+insertBuffers.size();
	}

	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	public boolean isCellEditable(int row, int column) {
		if ( resultSet == null ) return false;
		try {
			boolean isResultSetEditable = (resultSet.getConcurrency() == ResultSet.CONCUR_UPDATABLE);
			return isResultSetEditable;
		}
		catch (SQLException ex) {
			return false;
		}
	}
	public void prepareInsertRow() {
		RowInserting newRow = new RowInserting();
		newRow.values = new Object[columnNames.length];
		insertBuffers.add(newRow);
		fireTableRowsInserted(rowCount, rowCount+1);
	}

	private void insertRow(RowInserting row) throws SQLException {
		rowCount++;
		resultSet.moveToInsertRow(); // moves cursor to the insert row
		for ( int i=0; i<row.values.length; i++ ) {
			resultSet.updateObject(i+1, row.values[i]);
		}
		resultSet.insertRow();
//		if ( ! wasEmpty ) resultSet.moveToCurrentRow();
	}


	class RowInserting {
		Object[] values;

	}

	public synchronized void saveInsertions() throws SQLException {
//		boolean wasEmpty = rowCount == 0;
		Iterator iterator = insertBuffers.iterator();
		while (  iterator.hasNext() ) {
			RowInserting row = (RowInserting)iterator.next();
			insertRow(row);// , wasEmpty);
		}
	}

	public void deleteRow(int row) throws SQLException {
		if ( row < rowCount )  {
			resultSet.absolute(row + 1);
			resultSet.deleteRow();
		} else {
			insertBuffers.remove(row-rowCount);
		}
		rowCount--;
		fireTableDataChanged();
	}

	public synchronized void rollBack() throws SQLException {
		insertBuffers.clear();
		resultSet.getStatement().getConnection().rollback();
		resultSet = null;
		rowCount = 0;
		fireTableDataChanged();
	}


}
