package com.hackerdude.devtools.db.sqlide.plugins.tableedit;

import javax.swing.table.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import com.hackerdude.devtools.db.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.devtools.db.sqlide.pluginapi.*;

/**
 * A TableModel for editing the fields in a JTable.
 */
class FieldCollectionModel extends AbstractTableModel {

	String tableName;

	// These are just so getColumnClass can get the
	// class for the columns.
	TableFieldType classFldType;
	Integer classFldInt;
	Boolean classFldBool;
	String classFldString;
	ArrayList fields;
	JDataTypeCombo cbDataTypeCombo;
	IDEVisualPluginIF panel;

public FieldCollectionModel(ArrayList fields, IDEVisualPluginIF panel ) {//, DatabaseProcess ideProcess) {
	super();
	cbDataTypeCombo = new JDataTypeCombo();
	this.fields = fields;
	this.panel = panel;
	classFldType = new TableFieldType("");
	classFldInt  = new Integer(0);
	classFldBool = new Boolean(false);
	classFldString = new String();


	TableField itm = new TableField();
	fields.add( itm );
/*	cbDatabases.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
		refreshTypes(cbDatabases); }
	});*/

}

public void setTableName( String value ) { tableName = value; }
public String getTableName() { return(tableName); }

	public void deleteField( int row ) {
		if ( fields.size() > row && row > -1 ) {
		  fields.remove(row);
		  fireTableRowsDeleted(row, row);
		}
}


public void refreshTypes(JComboBox cbDatabases) {
cbDataTypeCombo.refreshTypes(cbDatabases,  panel.getDatabaseProcess());
}


public void insertField( int row ) {
	fields.add( new TableField() );
	fireTableDataChanged();

}

public int getRowCount() { return( fields.size() ); }

public int getColumnCount() { return(4); }

public boolean isCellEditable(int row, int col) { return true; }

public Class getColumnClass( int columnindex ) {
	Class theResult;
	theResult = classFldString.getClass();
	if ( columnindex ==1 ) theResult = classFldType.getClass();
	if ( columnindex ==2 ) theResult = classFldInt.getClass();
	if ( columnindex ==3 ) theResult = classFldBool.getClass();
	return(theResult);
}

public void setValueAt( Object aValue, int row, int column ) {
	TableField itm = (TableField)fields.get(row);
		String  value = null;
		Integer iValue = null;
		Boolean bValue = null;
		if ( aValue instanceof Integer ) { iValue = (Integer)aValue; }
		else if ( aValue instanceof Boolean ) { bValue = (Boolean)aValue; }
		else {
		  value = (String)aValue;
		  if ( column == 2 ) try { iValue = new Integer(value); } catch (NumberFormatException exc) {};
		}
	switch (column) {
	case 0: itm.fieldName = value; break;
	case 1: itm.fieldType = new TableFieldType(value); break;
	case 2: itm.fieldLen  = iValue; break;
	case 3: itm.canBeNull = bValue; break;
	}
}

public Object getValueAt( int row, int column ) {
	Object result = null;
	TableField itm = (TableField)fields.get(row);
	switch (column) {
	case 0: result = itm.fieldName; break;
	case 1: result = itm.fieldType; break;
	case 2: result = itm.fieldLen; break;
	case 3: result = itm.canBeNull; break;
	}
	return(result);
}

public String getColumnName(int column) {

	String theResult;
	theResult = "";
	switch( column ) {
	case 0 : theResult = "Field Name"; break;
	case 1 : theResult = "Type" ; break;
	case 2 : theResult = "Length" ;   break;
	case 3 : theResult = "Null?" ;   break;
	}
	return(theResult);
}
	/* ********************* Inner Classes ************************ */

   /**
	* This class will write
	* the SQL statement for creating the table.
	*/
   public String writeStatement(String tableName) {
	  StringBuffer result = new StringBuffer("CREATE TABLE ");
	  result.append(tableName);
	  result.append(" (\n");
	  TableField tf;
	  Iterator en = fields.iterator();
	  boolean began = false;
	  while ( en.hasNext() ) {
		 if ( began ) { result.append(",\n"); }
		 tf = (TableField)en.next();
		 if ( tf.fieldName != null && ! tf.fieldName.equals("") ) {
			result.append(tf.fieldName);
			result.append(" ");
			result.append(tf.fieldType.fieldType);
			if ( tf.fieldLen != null && tf.fieldLen.intValue() > 0 ) {
			  result.append("(");
			  result.append(tf.fieldLen.toString());
			  result.append(")");
			}
			if ( tf.canBeNull != null && tf.canBeNull.booleanValue() ) { result.append(" NULL");
			} else { result.append(" NOT NULL"); }
			began = true;
		 }

	  }
	  result.append(")");
	  return result.toString();

	}



	public void setupDataTypeEditor( JTable table ) {
		final TableEditDataTypeEditor dtEdit = new TableEditDataTypeEditor(cbDataTypeCombo);
		table.setDefaultEditor(TableFieldType.class, dtEdit);
	}

	class JDataTypeCombo extends JComboBox {
		public JDataTypeCombo() {
			super();
		}

		public void refreshTypes(JComboBox cbDatabases, DatabaseProcess ideprocess) {
			Vector types;
			String currentDB = (String)cbDatabases.getSelectedItem();
			if ( currentDB == null ) { currentDB = ideprocess.currentCatalog; }
			types = ideprocess.getSQLTypes(currentDB);

			Enumeration en = types.elements();
			while ( en.hasMoreElements() ) {
			  addItem((String)en.nextElement());
			}

		}
	}

	class TableEditDataTypeEditor extends DefaultCellEditor {

		public TableEditDataTypeEditor(JComboBox cb) {
			super(cb);
		}

		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}

	}

	public void insertField(TableField field) {
		fields.add( field );
		fireTableDataChanged();
	}

}
