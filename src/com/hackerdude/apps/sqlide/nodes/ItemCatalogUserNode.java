package com.hackerdude.apps.sqlide.nodes;

import com.hackerdude.apps.sqlide.dataaccess.*;

/*
 * Catalog User Node.
 *  Copyright (C) 1999 by David Martinez (davidmartinez@home.net)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
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
 * Please read hacking.html for overview information if you want
 * to modify.
 *
 * @author David Martinez <david@hackerdude.com>
 * @version $Id$
*/


/**
 * This class represents a single user for a specific catalog.
 */
public class ItemCatalogUserNode extends ItemUserNode {

	String catalogName;

	public ItemCatalogUserNode(String catalog, String userName, String hostName, DatabaseProcess db) {
		super(userName, hostName, db);
		this.catalogName = catalog;
	}

	public String toString() {
		return "("+catalogName+") "+userName+"@"+hostName;
	}

	public String getInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("<HTML><B><I>User:</I> ");
		if ( userName.equals(ALL_USER) ) sb.append("(all users)");
		else sb.append(userName);
		sb.append("</B>");
		sb.append("<P><B></I>Host:</I> ");
		if ( hostName.equals(ALL_HOST) ) sb.append("(all Hosts)");
		else sb.append( hostName );
		return sb.toString();
	}

}
