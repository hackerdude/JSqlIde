/*
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
* $Id$
* Version: $Revision$
*
 */
package com.hackerdude.apps.sqlide.plugins.browser;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import com.hackerdude.apps.sqlide.ProgramConfig;
import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.dataaccess.DatabaseService;
import com.hackerdude.apps.sqlide.dataaccess.HostConfigRegistry;
import com.hackerdude.apps.sqlide.nodes.ItemServerNode;

/**
 * This class acts as the backend for the browser. Basically the browser will
 * handle all the UI and this will handle actually asking questions to the
 * database.
 *
 * 
 * @author David Martinez
 * @version 1.0
 */
public class BrowserModel implements Observer {

	DatabaseProcess[] processes;
	Vector servers;
	DefaultMutableTreeNode topNode;

	/**
	 * Constructor
	 *
	 * @param topNode The top node for the browser
	 */
	public BrowserModel(DefaultMutableTreeNode topNode) {
		servers         = new Vector();
		this.topNode = topNode;
		ProgramConfig.getInstance().addObserver(this);
		update(ProgramConfig.getInstance(), null);
	}


	public void update(Observable obs, Object theObj) {
		//System.out.println("Notified of change. ");
		if ( theObj != null ) System.out.println("[BrowserModel] Observable Object param is "+theObj.toString());
		fillBrowser();
		createDBNodes();
	}

	private void fillBrowser() {
		int serverCount   = HostConfigRegistry.getInstance().getConnectionCount();
		servers.clear();
		for ( int i=0; i<serverCount; i++) {
			/** @todo This should be in a database service. */
			DatabaseProcess dbProcess = DatabaseService.getInstance().getDatabaseProcess(HostConfigRegistry.getInstance().getSqlideHostConfig(i));
			servers.add( dbProcess );
		}
	}

	/**
	 * Creates the database spec nodes.
	 */
	public void createDBNodes() {
		topNode.removeAllChildren();
		DefaultMutableTreeNode server;
		DefaultMutableTreeNode theNode;
		DatabaseProcess dbProcess;
		int serverCount = HostConfigRegistry.getInstance().getConnectionCount();
		for ( int i=0; i<serverCount; i++) {
			server =  new ItemServerNode( HostConfigRegistry.getInstance().getSqlideHostConfig(i) );
			topNode.add(server);
		}
	}

}