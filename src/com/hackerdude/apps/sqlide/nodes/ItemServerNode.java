package com.hackerdude.apps.sqlide.nodes;
import com.hackerdude.apps.sqlide.dataaccess.DatabaseService;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEBase;
import com.hackerdude.apps.sqlide.xml.hostconfig.SqlideHostConfig;

/**
 * This node represents a specific Server Connection.
 */
public class ItemServerNode extends NodeIDEBase {

	SqlideHostConfig spec;

	public ItemServerNode(SqlideHostConfig spec) {
		super(spec.getName(), DatabaseService.getInstance().getDatabaseProcess(spec));
		this.spec = spec;
	}

	public void readChildren() {
	// Code moved to a plugin. See JDBCIntrospector plugin.
	}

	public boolean canHaveChildren() { return true; }

	public String getInfo() {
		StringBuffer info = new StringBuffer();
		info.append("<HTML><P><B>UserName:</B> "+spec.getJdbc().getUserName());
		info.append("<P><B>Driver:</B> ").append(spec.getJdbc().getDriver());
		return info.toString();
	}

}
