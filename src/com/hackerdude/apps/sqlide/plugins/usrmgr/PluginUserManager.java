package com.hackerdude.apps.sqlide.plugins.usrmgr;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.*;
import java.awt.*;
import javax.swing.*;
import com.hackerdude.apps.sqlide.ProgramIcons;
import com.hackerdude.apps.sqlide.nodes.*;
import com.hackerdude.apps.sqlide.plugins.browser.browsejdbc.*;

/**
 * A User Manager Panel for PostgreSQL
 *
 * @copyright (C) 1998-2002 Hackerdude (David Martinez). All Rights Reserved.
 * @author David Martinez
 * @version 1.0
 */
public class PluginUserManager implements BrowserExtensionPluginIF  {



  public void requestAddSubNodes(NodeIDEBase parentNode) {
	  if ( parentNode instanceof ItemServerNode ) {
		  ItemServerNode serverNode = (ItemServerNode)parentNode;
		  parentNode.add(new UsersCategoryNode("Users", serverNode.getDatabaseProcess()));
	  }
  }

  public void initPlugin() {

  }
  public String getPluginName() { return "PostgreSQL User Manager";   }

  public String getPluginVersion() { return "0";
  }
  public void freePlugin() {
  }

  public String getPluginShortName() {
    return "User Mgr";
  }
  public Icon getPluginIcon() {
	  return ProgramIcons.getInstance().getUsersIcon();
  }

  class UsersCategoryNode extends NodeIDECategory {
	  public UsersCategoryNode(String name, DatabaseProcess proc) {
		  super(name, proc);
	  }
	  public void readChildren() {}

	  public String getInfo() { return "PostgreSQL Users"; }


  }

}
