/*
 * MenuBroker.java - SQLIDE's menu generator class.
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
 * @author David Martinez <david@hackerdude.com>
 * @version $Id$
 *
 */
package com.hackerdude.lib;

import java.awt.event.*;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.KeyStroke;
import java.util.*;

/**
 * This is sqlIDE's very own menu generator class.
 * @author David Martinez <david@hackerdude.com>
 * @version $Revision$
 */
public class MenuBroker {

	/**
	 * This function assembles menu items starting with an array of
	 * strings, character shortcuts, Control-character accelerators,
	 * and Alt-character accelerators (Alt taking precedence). It also
	 * adds an actionlistener to the menu that it assembles.
	 *
	 * @param menu The menu we want to add these arrays to.
	 * @param menuItems An array of strings with the menu items.
	 * @param menuShorts An array of chars specifying the shortcuts to use.
	 * @param CtrlAccel An array of chars specifying what Control-<char> keys to use for keyboard shortcut. May be null.
	 * @param AltAccel An array of chars specifying what Alt-<char> keys to use for keyboard shortcut. May be null.
	 * @param listener An ActionListener to attach to these menu items.
	 *
	 * This function very probably will evolve into something cooler.
	 * Images of GTK+-style menu definitions cross through my mind. It also
	 * might move to a menu generator.
	 */
	public static void assembleMenu( JMenu menu, String[] menuItems, char[] menuShorts, char[] CtrlAccel, char[] AltAccel, ActionListener listener ) {

	menuFactory(menu, menuItems, menuShorts, CtrlAccel,
			AltAccel, listener, null);

	};

	/**
	 * This function is like assembleMenu, but it also adds the
	 * generated items to a factory.
	 *
	 * @param menu The menu we want to add these arrays to.
	 * @param menuItems An array of strings with the menu items.
	 * @param menuShorts An array of chars specifying the shortcuts to use.
	 * @param CtrlAccel An array of chars specifying what Control-<char> keys to use for keyboard shortcut. May be null.
	 * @param AltAccel An array of chars specifying what Alt-<char> keys to use for keyboard shortcut. May be null.
	 * @param listener An ActionListener to attach to these menu items.
	 * @param items A vector that will be filled with the menu items.
	 */
	public static void menuFactory( JMenu menu, String[] menuItems, char[] menuShorts, char[] CtrlAccel, char[] AltAccel, ActionListener listener, Vector items )
	{

	for ( int i=0; i<menuItems.length; i++ ) {

		if ( menuItems[i].equals("-") ) {
		menu.addSeparator();
			// items.add(mitem);
		} else {
		JMenuItem mitem = new JMenuItem(menuItems[i], menuShorts[i]);
		if ( CtrlAccel != null )
			mitem.setAccelerator( KeyStroke.getKeyStroke(CtrlAccel[i],
								 java.awt.Event.CTRL_MASK, false) );
		if ( AltAccel != null )
			mitem.setAccelerator( KeyStroke.getKeyStroke(AltAccel[i],
								 java.awt.Event.ALT_MASK, false) );

		if ( listener != null )
			mitem.addActionListener(listener);
		if ( items != null )
			items.add(mitem);
			menu.add(mitem);
		};
	};


	};

}

/*

  $Log$
  Revision 1.2  2001/09/07 03:15:33  davidmartinez
  Changed e-mail address.

  Revision 1.1.1.1  2001/09/07 02:50:50  davidmartinez
  Initial Checkin of the Alpha tree

  Revision 1.1.1.1  2000/04/27 10:57:07  david
  Initial Import

  Revision 1.5  1999/11/02 08:59:55  david
  Added support for menu handling for each plugin.

  Revision 1.4  1999/10/25 15:20:38  david
  Moved files to a flat JSqlIde package, in preparation for the
  makefile and package organization.

  Revision 1.3  1999/10/21 16:06:49  david
  Added event passing to PanelIDEInterface, and implemented (as an example)
  cut, copy and paste on InteractiveSQL through the general edit menu.

  Revision 1.2  1999/10/20 22:38:30  david
  Now the MenuBroker works, and it can also add separators. The Server Menu
  becomes the plugin-specific menu, and some initial menuing support within
  the isql panel is implemented.

  Revision 1.1  1999/10/20 21:41:18  david
  Initial Checkin. Moved sqlide's menu generator to here.


 */
