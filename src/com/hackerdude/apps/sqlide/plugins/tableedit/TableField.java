package com.hackerdude.apps.sqlide.plugins.tableedit;

/**
 *  A simple field item class.
 */
class TableField implements Cloneable {

	public String fieldName;
	public TableFieldType fieldType;
	public Integer fieldLen;
	public Boolean canBeNull;
	public Boolean primaryKey;
	public boolean readOnly = false;

	public TableField() {
		fieldName = "";
		fieldType = null;
		fieldLen = null;
		canBeNull = new Boolean(false);
		primaryKey = new Boolean(false);
	}

	public Object clone()  {
		TableField field = new TableField();
		field.fieldName = fieldName;
		field.fieldType = fieldType;
		field.fieldLen = fieldLen;
		field.canBeNull = canBeNull;
		field.primaryKey = primaryKey;
		field.readOnly = readOnly;
		return field;
	}

	public String toString() {
		return fieldName;
	}

	public int hashCode() {
		return fieldName.hashCode();
	}

	public boolean equals(Object obj) {
		if ( obj == null ) return false;
		if ( ! (obj instanceof TableField)) return false;
		TableField candidate = (TableField)obj;
		return candidate.fieldName.equals(fieldName);
	}

}