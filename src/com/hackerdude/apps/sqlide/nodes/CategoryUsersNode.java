package com.hackerdude.apps.sqlide.nodes;
import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDECategory;

/**
 * Users category node.
 */
public class CategoryUsersNode extends NodeIDECategory {

	public CategoryUsersNode(DatabaseProcess dbProcess) {
		super("Users", dbProcess);
	}

	public void readChildren() {

		CategoryGeneralUsersNode generalNode = new CategoryGeneralUsersNode(databaseProcess);
		CategoryDbUsersNode dbUsersNode = new CategoryDbUsersNode(databaseProcess);
		add(generalNode);
		add(dbUsersNode);

	}

	public String getInfo() {
		return "<HTML><P>"+itemName;
	}
}
