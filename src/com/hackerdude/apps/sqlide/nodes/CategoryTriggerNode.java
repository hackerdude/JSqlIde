
/**
 * Title:        JSqlIde<p>
 * Description:  A Java SQL Integrated Development Environment
 * <p>
 * Copyright:    Copyright (c) David Martinez<p>
 * Company:      <p>
 * @author David Martinez
 * @version 1.0
 */
package com.hackerdude.apps.sqlide.nodes;
import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.pluginapi.*;

  public class CategoryTriggerNode extends NodeIDECategory {
	 public CategoryTriggerNode(DatabaseProcess proc) { super("Triggers", proc); };
	 public void readChildren() {}

	 public boolean canHaveChildren() { return true; }

	 public String getInfo() { return "<HTML><P>"+itemName; }

  };
