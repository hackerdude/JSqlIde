package com.hackerdude.devtools.db.sqlide.pluginapi;

import com.hackerdude.devtools.db.sqlide.dataaccess.*;

/**
 * This is the base class for all the SQLIDE Nodes that refer to a
 * category instead of a specific object. These are only used by
 * the Browser Panel to categorize things in the tree.
 */

public abstract class NodeIDECategory extends NodeIDEBase {

	public NodeIDECategory(String name, DatabaseProcess db ) { super(name, db); }

	public boolean canHaveChildren() { return true; }

}