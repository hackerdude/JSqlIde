package com.hackerdude.apps.sqlide.pluginapi;

import com.hackerdude.apps.sqlide.dataaccess.*;

/**
 * This is a common superclass for all the objects that actually
 * represent an object on the database connection, as opposed to a
 * category.
 */

public abstract class NodeIDEItem extends NodeIDEBase {

	public NodeIDEItem(String name, DatabaseProcess db ) { super(name, db); }

}
