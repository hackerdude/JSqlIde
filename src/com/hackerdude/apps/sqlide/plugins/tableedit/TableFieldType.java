package com.hackerdude.apps.sqlide.plugins.tableedit;

/**
 * A Field Type class (for the editor to drop down)
 */
class TableFieldType {
String fieldType;
	public TableFieldType( String Name ) {
	  fieldType = Name;
	}
	public String toString() {
	  return fieldType;
	}
}

