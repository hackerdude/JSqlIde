/*
 *   IDEPanelInterface.java - An interface specification
 *   for embedding Panels into sqlIDE.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Revision: $Revision$
 * Id      : $Id$
 *
 */
package com.hackerdude.devtools.db.sqlide.intf;

import com.hackerdude.devtools.db.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.devtools.db.sqlide.ProgramConfig;
import com.hackerdude.devtools.db.sqlide.intf.SQLIDEDBInterface;
import com.hackerdude.devtools.db.sqlide.pluginapi.NodeIDEBase;
import com.hackerdude.lib.JARClassLoader;
import javax.swing.*;

/**
 * This interface specifies the functionality a class must implement
 * to be used as a SQLIDE Visual Plugin.
 *
 * @deprecated Use pluginapi.IDE* interfaces instead.
 * @author David Martinez
 *
 */
public interface SqlIdePluginIF extends SqlIdePluginBaseIF {

	/**
	 * Changes the DatabaseProcess this panel will use to
	 * communicate with the database server. Some panels
	 * (i.e. the ideBrowser) don't do anything with this.
	 *  @param proc The database process that will be attached to this panel.
	 */
	public void setDatabaseProcess(DatabaseProcess proc);

	/**
	 * Returns the current DatabaseProcess for this panel.
	 */
	public DatabaseProcess getDatabaseProcess();

	/**
	 * Hides the menus for this panel.
	 *
	 */
	public void setVisibleMenus( boolean value, JMenu menu );

	/**
	 * Show an about box for this panel.
	 */
	public void showAboutBox();


	/**
	 * Returns all the available actions for this panel.
	 */
	public Action[] getAvailableActions();

	/**
	 * Returns a JPopupMenu adequate for the browserNode submitted. This
	 * is used so that the IDE Browser can
	 */
	public Action[] getActionsFor(NodeIDEBase node);

	/**
	 * This will be called whenever the user needs to switch to this IDE window.
	 * Each panel should call receiveFocus() on the most logical default
	 * component for that particular panel
	 */
	public void requestIDEFocus();


}

