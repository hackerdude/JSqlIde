
/**
 * Title:        JSqlIde<p>
 * Description:  A Java SQL Integrated Development Environment
 * <p>
 * Copyright:    Copyright (c) David Martinez<p>
 * Company:      <p>
 * @author David Martinez
 * @version 1.0
 */
package com.hackerdude.apps.sqlide.plugins.browser.browsejdbc;
import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.pluginapi.*;

import java.sql.*;

public class ItemCatalogNode extends NodeIDEItem {

	String catalogName;
	String catalogInfo;

	public ItemCatalogNode(String containerName, DatabaseProcess proc) throws SQLException {
		super(containerName, proc);
		catalogName = containerName;
		catalogInfo = "<HTML><P><B>"+CategoryCatalogsNode.determineCatalogTerm(db)+" name</B>: "+catalogName;
	};

	public void readChildren() {
		add( new CategoryTableNode(null, catalogName, db));
	}

	public boolean canHaveChildren() { return true; }

	public String getInfo() {
		return catalogInfo;
	}

}