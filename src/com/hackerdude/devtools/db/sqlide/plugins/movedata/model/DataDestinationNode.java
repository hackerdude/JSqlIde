package com.hackerdude.devtools.db.sqlide.plugins.movedata.model;

import org.apache.xerces.dom.*;
import org.w3c.dom.*;

/**
 * A destination node defines a partial or total destination for a data set.
 */
public class DataDestinationNode extends AbstractScriptNode {

	private String catalog;
	private boolean overwrite;
	private boolean create;
	private String tableName;

	public String getNodeName() { return "DESTINATION"; }
	public void setCatalog(String newCatalog) {
		catalog = newCatalog;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setOverwrite(boolean newOverwrite) {
		overwrite = newOverwrite;
	}
	public boolean isOverwrite() {
		return overwrite;
	}
	public void setCreate(boolean newCreate) {
		create = newCreate;
	}
	public boolean isCreate() {
		return create;
	}
	public void setTableName(String newTableName) {
		tableName = newTableName;
	}
	public String getTableName() {
		return tableName;
	}

	public Node getNode(Document doc) {
		ElementImpl element = new ElementImpl((DocumentImpl)doc, getNodeName());
		if ( catalog != null ) element.setAttribute("catalog", catalog);
		if ( tableName != null ) element.setAttribute("tablename", tableName);
		element.setAttribute("overwrite", new Boolean(overwrite).toString());
		element.setAttribute("create", new Boolean(create).toString());
		return element;
	}

}