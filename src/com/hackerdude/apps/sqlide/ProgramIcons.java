/*
 *  ProgramIcons - The Icon collection for the
 *  sqlDE program. Lets me keep all the grunt icon code
 *  in the same place, and change clipart quickly.
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
 */
package com.hackerdude.apps.sqlide;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.UIManager.*;
import java.util.*;
import java.lang.Object.*;
import java.io.*;

/**
 * The Icon collection for the sqlDE program.
 * @version $Id$
 */
public class ProgramIcons {

	protected static String prop_treeusers        = "icon_Users";
	protected static String prop_treedatabase     = "icon_Database";
	protected static String prop_treedevices      = "icon_Devices";
	protected static String prop_treeroot         = "icon_Root";
	protected static String prop_treeserver       = "icon_Server";
	protected static String prop_treelogins       = "icon_Logins";
	protected static String prop_treestoredprocs  = "icon_Stored_Procs";
	protected static String prop_treetriggers     = "icon_Triggers";
	protected static String prop_Icon_Go          = "icon_Go";
	protected static String prop_Icon_Stop        = "icon_Stop";
	protected static String prop_Icon_Application = "icon_Application";
	protected static String prop_Icon_Collapse    = "icon_Collapse";
	protected static String prop_Icon_Expand      = "icon_Expand";

	protected ArrayList sqlIDEIcons;
	protected ArrayList sqlIDEIconNames;
	protected Properties props;
	protected Properties propsDefaults;

	protected static ProgramIcons instance;

	private ProgramIcons() {

	  Properties propitems = ProgramConfig.getInstance().userinterface;
	  this.props = propitems;

	  sqlIDEIcons = new ArrayList();
	  sqlIDEIconNames = new ArrayList();
	  propsDefaults = new Properties();

	  propsDefaults.setProperty( prop_treeusers,    "images/Users.gif" );
	  propsDefaults.setProperty( prop_treedatabase, "images/database.gif");
//      propsDefaults.setProperty( prop_treedatabase, "images/devices.gif");
//      propsDefaults.setProperty( prop_treedatabase, "images/database.gif" );
	  propsDefaults.setProperty( prop_treedevices      , "images/devices.gif" );
	  propsDefaults.setProperty( prop_treeroot         , "images/World2.gif" );
	  propsDefaults.setProperty( prop_treeserver       , "images/Workstation.gif" );
	  propsDefaults.setProperty( prop_treelogins       , "images/Users.gif" );
	  propsDefaults.setProperty( prop_treestoredprocs  , "images/storedproc.gif" );
	  propsDefaults.setProperty( prop_treetriggers     , "images/storedproc.gif" );
	  propsDefaults.setProperty( prop_Icon_Go          , "images/go.gif" );
	  propsDefaults.setProperty( prop_Icon_Stop        , "images/stop.gif" );
	  propsDefaults.setProperty( prop_Icon_Application , "images/database.gif" );
	  propsDefaults.setProperty( prop_Icon_Collapse    , "images/minus.gif" );
	  propsDefaults.setProperty( prop_Icon_Expand      , "images/plus.gif" );

	}

	/**
	 * Returns the singleton instance of this class
	 */
	public static ProgramIcons getInstance() {
	 if ( instance == null ) { instance = new ProgramIcons(); }
	  return(instance);
	}

	/**
	 * This procedure tries to find an icon.
	 */
	public ImageIcon findIcon( String iconName ) {

	  ImageIcon currentIcon = null;
	  File fileCheck = new File(iconName);
	  if ( fileCheck.exists() ) {
		System.out.println("[ProgramIcons] Pulling an icon from "+iconName);
		currentIcon = new ImageIcon( iconName );
	  }
	  else {
		URL anURL = ProgramIcons.class.getResource("/com/hackerdude/"+iconName);
		if ( anURL == null ) {
		  System.out.println("[ProgramIcons] Could not find "+iconName+" as resource!");
		} else {
		  currentIcon = new ImageIcon( anURL );
		};
	  };
	  return(currentIcon);
	};

	/**
	 * Returns an ImageIcon by number.
	 */
	private ImageIcon getIcon( int index ) {
		ImageIcon r = null;
		if ( index > 0 ) {
		   r = (ImageIcon)sqlIDEIcons.get(index); }
		return(r);
	};

	/**
	 * Returns an ImageIcon by Name.
	 */
	public ImageIcon getIconByName( String iconName ) {
		ImageIcon theResult = null;
		// It might have been instantiated already...
		theResult = getIcon(getIconNumber(iconName));
		// If the icon is null, try to find it.
		if (theResult == null ) {
		  theResult = findIcon( props.getProperty( iconName, propsDefaults.getProperty( iconName )  ));

		};
		return(theResult);
	};

	/**
	 * Returns the current Icon number of an Icon Name.
	 */
	public synchronized int getIconNumber( String iconName ) {
	  String nameIteration;
	  int i=-1, result = -1;
	  for (Iterator it=sqlIDEIconNames.iterator(); it.hasNext(); ) {
		 nameIteration = (String)it.next();
		 i++;
		 if ( nameIteration.equals(iconName) ) {
		   result = i;
		   break;
		 }
	  }
	  return(result);
	};

	/**
	 * Returns the name of the icon.
	 */
	public String getIconName( int index ) { return( (String)sqlIDEIconNames.get(index) ); };

	// Getters for some important icons (for runtime operation).
	/**
	 * The icon for the "Go!"/"Execute" buttons in the application.
	 */
	public ImageIcon getGoIcon() { return(getIconByName(prop_Icon_Go)); }

	/**
	 * The Icon for the "Stop"/"Abort" buttons in the application.
	 */
	public ImageIcon getStopIcon() { return(getIconByName(prop_Icon_Stop)); }

	/**
	 * The Application icon.
	 */
	public ImageIcon getAppIcon() { return(getIconByName(prop_Icon_Application)); }

	/**
	 * The users icon
	 */
	public ImageIcon getUsersIcon() { return(getIconByName(prop_treeusers)); }


	/**
	 * The database Icon
	 */
	public ImageIcon getDatabaseIcon () { return(getIconByName(prop_treedatabase)); }

	/**
	 * The "Physical devices" icon.
	 */
	public ImageIcon getDevicesIcon() { return(getIconByName(prop_treedevices)); }

	/**
	 * The root icon (for the root of the browser)
	 */
	public ImageIcon getRootIcon() { return(getIconByName(prop_treeroot)); }

	/**
	 * The server icon
	 */
	public ImageIcon getServerIcon() { return(getIconByName(prop_treeserver)); }


	/**
	 * The Logins icon
	 */
	public ImageIcon getLoginsIcon() { return(getIconByName(prop_treelogins)); }


	/**
	 * Icon for stored procedures
	 */
	public ImageIcon getStoredProcIcon() { return(getIconByName(prop_treestoredprocs)); }


	/**
	 * Icon for triggers.
	 */
	public ImageIcon getTriggerIcon() { return(getIconByName(prop_treetriggers)); }


	/**&
	 * Icon for tree expansion
	 */
	 public ImageIcon getExpandIcon() { return(getIconByName(prop_Icon_Collapse)); }

	 /**
	  * Icon for tree collapse
	  */
	 public ImageIcon getCollapseIcon() { return(getIconByName(prop_Icon_Expand)); }

}

/*

  $Log$
  Revision 1.2  2003/01/27 19:44:45  davidmartinez
  New tasks menu. Miscellaneous fixes.

  Revision 1.1  2002/08/21 21:24:12  davidmartinez
  Refactoring - moved application from devtools.... to apps

  Revision 1.1.1.1  2001/09/07 02:50:59  davidmartinez
  Initial Checkin of the Alpha tree

  Revision 1.2  2000/05/08 22:24:15  david
  Tweaks so that Jar Packaging will work. Also new-user settings bug fixes.

  Revision 1.1.1.1  2000/04/27 22:20:49  david
  Initial Import

  Revision 1.8  1999/12/15 09:10:18  david
  Lots of fixes, now Database specs can be added and deleted; changed
  expand/collapse icons switched.

  Revision 1.7  1999/12/09 11:33:32  david
  Fixed problems with images, optimized makefile.

  Revision 1.6  1999/11/20 01:09:10  david
  Added changes so it will work correctly with JBuilder. It will also load
  the images correctly when part of a JAR file.

  Revision 1.5  1999/10/31 20:22:55  david
  Modified some parts of the program so they work inside a package.
  About box now shows GPL in JAR

  Revision 1.4  1999/10/25 15:20:38  david
  Moved files to a flat JSqlIde package, in preparation for the
  makefile and package organization.

  Revision 1.3  1999/10/19 00:56:43  david
  .

  Revision 1.2  1999/10/19 00:53:44  david
  Added Log


 */
