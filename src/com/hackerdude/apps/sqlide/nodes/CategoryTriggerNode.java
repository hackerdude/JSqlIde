package com.hackerdude.apps.sqlide.nodes;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDECategory;

/**
 * Triggers category node
 */
public class CategoryTriggerNode extends NodeIDECategory {
	public CategoryTriggerNode(DatabaseProcess proc) {
		super("Triggers", proc);
	};
	public void readChildren() {}

	public boolean canHaveChildren() {
		return true;
	}

	public String getInfo() {
		return "<HTML><P>" + itemName;
	}

}