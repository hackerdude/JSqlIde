package com.hackerdude.devtools.db.sqlide.nodes;
import com.hackerdude.devtools.db.sqlide.dataaccess.*;
import com.hackerdude.devtools.db.sqlide.intf.SQLIDEDBInterface;
import com.hackerdude.devtools.db.sqlide.pluginapi.*;

/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
 * @author David Martinez
 * @version 1.0
 */

public class CategoryUsersNode extends NodeIDECategory {

	public CategoryUsersNode(DatabaseProcess dbProcess) {
		super("Users", dbProcess);
	}

	public void readChildren() {

		CategoryGeneralUsersNode generalNode = new CategoryGeneralUsersNode(db);
		CategoryDbUsersNode dbUsersNode = new CategoryDbUsersNode(db);
		add(generalNode);
		add(dbUsersNode);

	}

	public String getInfo() {
		return "<HTML><P>"+itemName;
	}
}