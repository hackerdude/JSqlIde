
/**
 * Title:        JSqlIde<p>
 * Description:  A Java SQL Integrated Development Environment
 * <p>
 * Copyright:    Copyright (c) David Martinez<p>
 * Company:      <p>
 * @author David Martinez
 * @version 1.0
 */
package com.hackerdude.devtools.db.sqlide.nodes;
import com.hackerdude.devtools.db.sqlide.dataaccess.*;
import com.hackerdude.devtools.db.sqlide.pluginapi.*;
import javax.swing.tree.*;
import java.util.*;

/**
 * This node represents a specific Server Connection.
 */
public class ItemServerNode extends NodeIDEBase {

	DatabaseSpec spec;

	public ItemServerNode(DatabaseSpec spec) {
		super(spec.getPoliteName(), new DatabaseProcess(spec));
		this.spec = spec;
	}

	public void readChildren() {
	// Code moved to a plugin. See JDBCIntrospector plugin.
	}

	public boolean canHaveChildren() { return true; }

	public String getInfo() {
		StringBuffer info = new StringBuffer();
		info.append("<HTML><P><B>UserName:</B> "+spec.getUserName());
		info.append("<P><B>Driver:</B> ").append(spec.getDriverClassName());
		return info.toString();
	}

}