package com.hackerdude.apps.sqlide.plugins.pgsql.storedproc;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.hackerdude.apps.sqlide.ProgramIcons;
import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEItem;


/**
 * Node for stored procedures.
 */
public class ItemStoredProcedureNode extends NodeIDEItem {

	private String remarks;
	private ArrayList argumentTypes;
	private String[] argumentTypeNames;
	private String name;

	public ItemStoredProcedureNode(String name, DatabaseProcess proc) {
		super(name, proc);
		this.name = name;
	}

	public ItemStoredProcedureNode(String name, ArrayList argumentTypes, String[] argumentTypeNames, DatabaseProcess proc) {
		super(name, proc);
		this.argumentTypes = argumentTypes;
	}

	public boolean canHaveChildren() {
		return false;
	}

	public void readChildren() {}


	public String getInfo() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<HTML><P>");
		buffer.append(itemName);
		if ( argumentTypes!= null ) buffer.append("(").append(argumentTypes.size()).append(" params)");
		if ( remarks!=null ) buffer.append("<BR>"+remarks);
		return buffer.toString();

	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getRemarks() {
		return remarks;
	}


	public ImageIcon getIcon() {
		return ProgramIcons.getInstance().findIcon("images/storedproc.gif");
	}

	public String getName() { return name; }
}
