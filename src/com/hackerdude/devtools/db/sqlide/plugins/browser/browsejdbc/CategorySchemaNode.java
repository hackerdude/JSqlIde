
/**
 * Title:        JSqlIde<p>
 * Description:  A Java SQL Integrated Development Environment
 * <p>
 * Copyright:    Copyright (c) David Martinez<p>
 * Company:      <p>
 * @author David Martinez
 * @version 1.0
 */
package com.hackerdude.devtools.db.sqlide.plugins.browser.browsejdbc;
import com.hackerdude.devtools.db.sqlide.dataaccess.*;
import com.hackerdude.devtools.db.sqlide.pluginapi.*;
import javax.swing.tree.*;
import java.util.*;
import java.sql.*;

public class CategorySchemaNode extends NodeIDECategory {

   public CategorySchemaNode(DatabaseProcess proc) { super(proc.getSchemaTitle(), proc); };

   public void readChildren() {
		DefaultMutableTreeNode dbItem = null;
		try {
			Vector vc = db.getSchemas();
			for(int i=0;i<vc.size();i++) {
				dbItem = new ItemSchemaNode(vc.elementAt(i).toString(), db);//vc.elementAt(i).toString());
				add(dbItem);
			}
		} catch ( Throwable exc ) {
			add(new ItemSchemaNode(null, db));
		}


   }

   public boolean canHaveChildren() { return true; }

   public String getInfo() { return "<HTML><P>Schemas"; }


};