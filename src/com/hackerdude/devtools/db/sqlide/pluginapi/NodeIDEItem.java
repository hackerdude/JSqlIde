package com.hackerdude.devtools.db.sqlide.pluginapi;

import com.hackerdude.devtools.db.sqlide.dataaccess.*;

/**
 * This is a common superclass for all the objects that actually
 * represent an object on the database connection, as opposed to a
 * category.
 */

public abstract class NodeIDEItem extends NodeIDEBase {

	public NodeIDEItem(String name, DatabaseProcess db ) { super(name, db); }

}