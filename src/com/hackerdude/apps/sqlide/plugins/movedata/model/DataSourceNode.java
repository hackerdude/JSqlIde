package com.hackerdude.apps.sqlide.plugins.movedata.model;

import java.util.Properties;
import org.w3c.dom.*;
import org.apache.xerces.dom.*;

/**
 * Represents a Query Statement (anything that returns a resultset, really)
 */
public class DataSourceNode extends AbstractScriptNode {

	private String catalog;
	private String tableName;
	private String selectStatement;
	private String whereClause; // Optional Where Clause

	public DataSourceNode() {
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if ( tableName == null ) {
			sb.append("Source Statement: ");
			if ( selectStatement != null ) {
				sb.append(selectStatement.substring(0, 25));
				if ( selectStatement.length()> 25) sb.append("...");
			}
		} else {
			sb.append("Source Table ").append(tableName);
			if ( whereClause != null ) { sb.append(" (WHERE)"); }
		}
		return sb.toString();
	}

	public String getNodeName() { return "SOURCE"; }

	public Node getNode(Document doc) {
		ElementImpl element = new ElementImpl((DocumentImpl)doc, getNodeName());
		if ( catalog != null ) element.setAttribute("catalog", catalog);
		if ( whereClause != null ) element.setAttribute("whereclause", whereClause);
		if ( tableName != null ) element.setAttribute("tablename", tableName);
		if ( selectStatement != null ) element.setAttribute("selectstatement", selectStatement);
		Node result = element;
		return result;
	}

	public void setCatalog(String catalogName) { catalog = catalogName; }

	public String getCatalog() { return catalog; }

	public void setTableName(String tableName) { this.tableName = tableName; }

	public String getTableName() { return tableName; }

	public void setWhereClause(String whereClause) { this.whereClause = whereClause; }

	public void setSelectStatement(String selectStatement) { this.selectStatement = selectStatement; }

	public String getSelectStatement() { return selectStatement; }



}
