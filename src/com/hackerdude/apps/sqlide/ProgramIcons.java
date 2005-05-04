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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.ImageIcon;

/**
 * The Icon collection for the sqlDE program.
 * 
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
//	  propsDefaults.setProperty( prop_Icon_Collapse    , "images/minus.gif" );
//	  propsDefaults.setProperty( prop_Icon_Expand      , "images/plus.gif" );

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


}
