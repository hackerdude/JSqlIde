package com.hackerdude.apps.sqlide.plugins.tableedit;

/**
 *  A simple field item class.
 */
class TableField {

public String fieldName;
public TableFieldType fieldType;
public Integer fieldLen;
public Boolean canBeNull;
public Boolean primaryKey;

public TableField() {
	fieldName = "";
	fieldType = null;
	fieldLen  = null;
	canBeNull = new Boolean(false);
	primaryKey = new Boolean(false);
}


}
