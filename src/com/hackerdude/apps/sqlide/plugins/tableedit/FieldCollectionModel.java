package com.hackerdude.apps.sqlide.plugins.tableedit;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.IDEVisualPluginIF;

/**
 * A TableModel for editing the fields in a JTable.
 * @copyright (C) 1998-2002 Hackerdude (David Martinez). All Rights Reserved.
 * @author David Martinez
 * @version 1.0
 */
class FieldCollectionModel extends AbstractTableModel {

	public static final String[] COLUMN_NAMES = {
		  "Field Name", "Type", "Length", "Null", "PK"};
	public static final Class[] COLUMN_CLASSES = {
		  String.class, TableFieldType.class, Integer.class, Boolean.class, Boolean.class};

	public static final int COL_FIELDNAME = 0;
	public static final int COL_TYPE = 1;
	public static final int COL_LENGTH = 2;
	public static final int COL_NULLALLOWED = 3;
	public static final int COL_PK = 4;

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

	public FieldCollectionModel(ArrayList fields, IDEVisualPluginIF panel) { //, DatabaseProcess ideProcess) {
		super();
		cbDataTypeCombo = new JDataTypeCombo();
		this.fields = fields;
		this.panel = panel;
		classFldType = new TableFieldType("");
		classFldInt = new Integer(0);
		classFldBool = new Boolean(false);
		classFldString = new String();

		TableField itm = new TableField();
		fields.add(itm);

	}

	public void setTableName(String value) {
		tableName = value;
	}

	public String getTableName() {
		return (tableName);
	}

	public void deleteField(int row) {
		if (fields.size() > row && row > -1) {
			fields.remove(row);
			fireTableRowsDeleted(row, row);
		}
	}

	public void refreshTypes(JComboBox cbDatabases) {
		cbDataTypeCombo.refreshTypes(cbDatabases, panel.getDatabaseProcess());
		cbDataTypeCombo.setSelectedIndex(0);
	}

	public int getRowCount() {
		return (fields.size());
	}

	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	public boolean isCellEditable(int row, int col) {
		TableField itm = (TableField)fields.get(row);
		return ! itm.readOnly;
	}

	public Class getColumnClass(int index) {
		Class theResult;
		theResult = COLUMN_CLASSES[index];
		return (theResult);
	}

	public void setValueAt(Object aValue, int row, int column) {
		TableField itm = (TableField) fields.get(row);
		String value = null;
		Integer iValue = null;
		Boolean bValue = null;
		if (aValue instanceof Integer) {
			iValue = (Integer) aValue;
		} else if (aValue instanceof Boolean) {
			bValue = (Boolean) aValue;
		} else {
			value = (String) aValue;
			if (column == 2)
				try {
					iValue = new Integer(value);
				} catch (NumberFormatException exc) {}
			;
		}
		switch (column) {
			case COL_FIELDNAME:
				itm.fieldName = value; break;
			case COL_TYPE:
				itm.fieldType = new TableFieldType(value); break;
			case COL_LENGTH:
				itm.fieldLen = iValue; break;
			case COL_NULLALLOWED:
				itm.canBeNull = bValue; break;
			case COL_PK:
				itm.primaryKey = bValue; break;
		}
	}

	public Object getValueAt(int row, int column) {
		Object result = null;
		TableField itm = (TableField) fields.get(row);
		switch (column) {
			case COL_FIELDNAME:
				result = itm.fieldName; break;
			case COL_TYPE:
				result = itm.fieldType; break;
			case COL_LENGTH:
				result = itm.fieldLen; break;
			case COL_NULLALLOWED:
				result = itm.canBeNull; break;
			case COL_PK:
				result = itm.primaryKey; break;
		}
		return (result);
	}

	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}

	/* ********************* Inner Classes ************************ */

	/**
	 * Will write the SQL statement for creating the table.
	 */
	public String writeCreateStatement(String tableName) {
		StringBuffer result = new StringBuffer("CREATE TABLE ");
		result.append(tableName);
		result.append(" (\n");
		TableField tf;
		Iterator en = fields.iterator();
		boolean began = false;
		while (en.hasNext()) {
			if (began) {
				result.append(",\n");
			}
			tf = (TableField) en.next();
			if (tf.fieldName != null && !tf.fieldName.equals("")) {
				String tableFieldDef = getFieldDefinitionFor(tf);
				result.append(tableFieldDef);
				began = true;
			}

		}
		result.append(")");
		return result.toString();

	}

	public String getFieldDefinitionFor(TableField tf) {
		StringBuffer result = new StringBuffer();
		result.append(tf.fieldName);
		result.append(" ");
		result.append(tf.fieldType.fieldType);
		if (tf.fieldLen != null && tf.fieldLen.intValue() > 0) {
			result.append("(");
			result.append(tf.fieldLen.toString());
			result.append(")");
		}
		if (tf.canBeNull != null && tf.canBeNull.booleanValue()) {
			result.append(" NULL");
		} else {
			result.append(" NOT NULL");
		}
		if (tf.primaryKey != null && tf.primaryKey.booleanValue()) {
			result.append(" PRIMARY KEY");
		}
		return result.toString();
	}

	/**
	 * Writes the alter table statement.
	 * @return Alter table Statement to be written.
	 */
	public String writeAlterStatement(String tableName, ArrayList originalFields) {
		StringBuffer result = new StringBuffer();
		result.append("ALTER TABLE "+tableName).append('\n');
		appendFieldsInsertedList(result, originalFields);
		appendFieldsRemovedList(result, originalFields);
		return result.toString();
	}

	public int appendFieldsRemovedList(StringBuffer buffer, ArrayList originalFields) {
		int appended = 0;
		ArrayList fieldsRemoved  = calculateFieldsRemoved(originalFields);
		Iterator removedIterator = fieldsRemoved.iterator();
		while ( removedIterator.hasNext() ) {
			TableField field = (TableField)removedIterator.next();
			buffer.append(" DROP COLUMN ").append(field.fieldName).append("\n");
			appended++;
		}
		return appended;
	}

	public int appendFieldsInsertedList(StringBuffer buffer, ArrayList originalFields) {
		int appended = 0;
		ArrayList fieldsInserted = calculateFieldsAdded(originalFields);
		Iterator iterator = fieldsInserted.iterator();
		while ( iterator.hasNext() ) {
			TableField field = (TableField)iterator.next();
			String fieldDef = getFieldDefinitionFor(field);
			buffer.append(" ADD COLUMN ").append(fieldDef).append("\n");
			appended++;
		}
		return appended;
	}

	public synchronized ArrayList calculateFieldsRemoved(ArrayList originalFields) {
		ArrayList result = new ArrayList();
		Iterator iterator = originalFields.iterator();
		while ( iterator.hasNext() ) {
			TableField field = (TableField)iterator.next();
			if ( ! hasField(field) ) {
				result.add(field);
			}
		}
		return result;
	}

	public synchronized boolean hasField(TableField field) {
		return fields.indexOf(field) > -1;
	}

	public synchronized ArrayList calculateFieldsAdded(ArrayList originalFields) {
		ArrayList result = new ArrayList();
		Iterator iterator = fields.iterator();
		while ( iterator.hasNext() ) {
			TableField field = (TableField)iterator.next();
			if ( originalFields.indexOf(field) == -1 ) {
				result.add(field);
			}
		}
		return result;
	}

	public void setupDataTypeEditor(JTable table) {
		final TableEditDataTypeEditor dtEdit = new TableEditDataTypeEditor(cbDataTypeCombo);
		table.setDefaultEditor(TableFieldType.class, dtEdit);
	}

	class JDataTypeCombo extends JComboBox {
		public JDataTypeCombo() {
			super();
		}

		public void refreshTypes(JComboBox cbDatabases, DatabaseProcess ideprocess) {
			Vector types;
			String currentDB = (String) cbDatabases.getSelectedItem();
			if (currentDB == null) {
				currentDB = ideprocess.currentCatalog;
			}
			types = ideprocess.getSQLTypes(currentDB);
			if (types != null)
				classFldType = new TableFieldType( (String) types.get(0));

			Enumeration en = types.elements();
			while (en.hasMoreElements()) {
				addItem( (String) en.nextElement());
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

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

			Component result = super.getTableCellEditorComponent(table, value, isSelected, row, column);
			if (value instanceof TableFieldType) {
				TableFieldType selectedField = (TableFieldType) value;
				cbDataTypeCombo.setSelectedItem(selectedField.fieldType);

			}

			return result;
		}

	}

	public void insertField(TableField field) {
		fields.add(field);
		fireTableDataChanged();
	}

}