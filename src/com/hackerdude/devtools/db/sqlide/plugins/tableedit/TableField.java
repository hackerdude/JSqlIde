package com.hackerdude.devtools.db.sqlide.plugins.tableedit;

/**
 *  A simple field item class.
 */
class TableField {

public String fieldName;
public TableFieldType fieldType;
public Integer fieldLen;
public Boolean canBeNull;

	/**
	 * Constructor. It adds a CHAR(10) by default.
	 */
public TableField() {
	fieldName = "";
		fieldType = new TableFieldType("CHAR");
	fieldLen  = new Integer(10);
	canBeNull = new Boolean(false);
}


}
