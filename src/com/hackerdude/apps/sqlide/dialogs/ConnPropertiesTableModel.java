package com.hackerdude.apps.sqlide.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * Table Model for connection properties.
 * @author David Martinez
 * @version 1.0
 */
public class ConnPropertiesTableModel extends AbstractTableModel  {

		Vector colNames = new Vector();
		ArrayList dataTable = new ArrayList();

		public Map getProperties() {
			Properties props = new Properties();
			Iterator it = dataTable.iterator();
			while ( it.hasNext() ) {
				Vector row = (Vector)it.next();
				String name  = (String)row.get(0);
				String value = (String)row.get(1);
				props.setProperty(name, value);
			}
			return props;
		}

		public ConnPropertiesTableModel(Map nvPairs, String nameTitle) {
			super();
			colNames.add(nameTitle);
			colNames.add("value");
			colNames.add("Ask?");
			replaceData(nvPairs);
		}

		private void parseMap(Map hm) {

			Iterator i = hm.keySet().iterator();
			while ( i.hasNext() ) {
				Object key = i.next();
				Object value = hm.get(key);
				Vector row = new Vector();
				row.add(key);
				row.add(value);
				dataTable.add(row);
			}
		}

		public void replaceData(Map nvPairs) {
			dataTable.clear();
			parseMap(nvPairs);
		}

	public int getRowCount() {
		return dataTable.size();
	}
	public int getColumnCount() { return 3;
	}
	public String getColumnName(int columnIndex) {
		return (String)colNames.get(columnIndex);
	}
//	public Class getColumnClass(int columnIndex) {
//		if ( columnIndex == 2  ) return Boolean.class;
//		else return String.class;
//	}
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if ( columnIndex != 1 ) return true;

		Vector row = (Vector)dataTable.get(rowIndex);
		String value = (String)row.get(1);
//		if ( value.equals( SqlideHostConfig._ASK_) ) return false;
		return true;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Vector row = (Vector)dataTable.get(rowIndex);

		String value = null;
		if ( columnIndex == 2 ) {
			value = (String)row.get(1);
			/** @todo Reduce object creation by putting these in the actual vector. */
//			if ( value.equals(SqlideHostConfig._ASK_) ) { return new Boolean(true); }
//			else return new Boolean(false);
		} else
		value = (String)row.get(columnIndex);
//		if ( columnIndex == 1 && value.equals(SqlideHostConfig._ASK_) ) { value = ""; }
		return value;

	}


	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

		Vector row = (Vector)dataTable.get(rowIndex);

		if ( columnIndex == 2 ) {
			row.remove(1);
//			if ( ((Boolean)aValue).booleanValue() )
//				row.add(1, SqlideHostConfig._ASK_);
//			else row.add(1, "");
		} else {
			row.set(columnIndex, aValue);

		}


	}

	public void addRow() {
		Vector v = new Vector();
		v.add("untitled");
		v.add("value");
		dataTable.add(v);
		fireTableRowsInserted(dataTable.size()-1, dataTable.size()-1);
	}


	public void removeRow(int rowIndex) {
		dataTable.remove(rowIndex);
		fireTableRowsDeleted(rowIndex, rowIndex);
	}

}
