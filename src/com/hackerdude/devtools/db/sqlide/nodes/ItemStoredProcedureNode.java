package com.hackerdude.devtools.db.sqlide.nodes;

import com.hackerdude.devtools.db.sqlide.pluginapi.*;
import com.hackerdude.devtools.db.sqlide.dataaccess.*;


/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
 * @author David Martinez
 * @version 1.0
 */

public class ItemStoredProcedureNode extends NodeIDEItem {

	private String remarks;

	public ItemStoredProcedureNode(String name, DatabaseProcess proc) {
		super(name, proc);
	}

	public boolean canHaveChildren() {
		return false;
	}

	public void readChildren() {}


	public String getInfo() {
		return "<HTML><P>"+this.itemName+"<BR>"+remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getRemarks() {
		return remarks;
	}


}