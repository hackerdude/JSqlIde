/*
 *
 * DatabaseSpec.java - the Database Specification
 * Configuration.
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
 * Id:       $Id$
 *
 */
package com.hackerdude.devtools.db.sqlide.dataaccess;

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
import java.sql.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import com.hackerdude.devtools.db.sqlide.ProgramConfig;

/**
 * This class represents the connection Configuration.
 */
public class ConnectionConfig {


	public static final String _ASK_ = "____<ASK>____";

	public static final String prop_db_defaultdb     = "db.defaultdb";
	public static final String prop_db_configsuffix  = ".db.xml";

	protected Document xmlDatabaseSpec;

	public static final String DEFAULT_DBSPEC = "default";
	public static final String DBSPEC_SUFFIX = ".db.xml";

	public static final String DEFAULT_DBSPEC_FILENAME = ProgramConfig.getPropsPath()+DEFAULT_DBSPEC+DBSPEC_SUFFIX;

	protected Properties connProperties = new Properties();

	protected String fileName;
	protected String politeName;
	protected String userName;
	protected String password;
	protected String defaultCatalog;
	protected String hostName;
	protected int maxConnections;
	protected String urlString;
	protected String driverClassName;
	protected String[] jarFiles;
	protected String dbIntfClassName = "Database_MSQL";
	protected boolean jdbcCompliant;
	protected String userMessage;

	public ConnectionConfig() {
	   this(DEFAULT_DBSPEC_FILENAME);
	}

	public void setFileName(String newValue) { fileName  = newValue; }

	/**
	 * This constructor creates a new DatabaseSpec, defining the filename.
	 * @param fileName The specification file to load.
	 */
	public ConnectionConfig( String fileName ) {
	  jdbcCompliant = false;
	  this.fileName = fileName;
	}

	public String getUserMessage() { return userMessage; }

	public void setUserName(String newValue) { userName = newValue; }
	public void setHostName(String newValue) { hostName = newValue; }
	public void setDefaultCatalog(String newValue) { defaultCatalog = newValue; }

	public Properties getProperties() {
	  return connProperties;
	}

	/**
	 * Retrieves the file name of the driver jar file.
	 */
	public String[] getJarFileNames() { return jarFiles; };

	/**
	 * Sets the file name of the driver jar file.
	 */
	public void setJarFileName( String[] jarFiles ) { this.jarFiles = jarFiles; }

	/**
	 * Retrieves the file name of the properties file.
	 */
	public String getFileName() { return fileName; };

	// Getters and setters for the database configuration.

	/**
	 * Returns the driver (JDBC) class name.
	 */
	public String getDriverClassName() {
		return(driverClassName);
	}


	/**
	 * Changes the driver name.
	 */
	public void setDriverClassName( String aValue ) {
	   driverClassName = aValue;
	   driverDiscovery();
	}

	private void driverDiscovery() {
	   try {

		  Class drv = resolveDriverClass();
		  Driver d = (Driver)drv.newInstance();
		  jdbcCompliant = d.jdbcCompliant();
		  String driverVersion = "V."+d.getMajorVersion()+"."+d.getMinorVersion();

		  if ( jdbcCompliant ) {
			 userMessage = "JDBC Compliant Driver. Driver "+driverVersion;
		  } else {
			 userMessage = "Warning: This driver is not fully JDBC compliant. Driver "+driverVersion;
		  }
	   } catch ( ClassNotFoundException exc ) {
		  userMessage = "Warning: class "+driverClassName+" not found. Cannot do driver discovery";
	   } catch ( Exception exc2 ) {
		  userMessage = "Warning: class "+driverClassName+" cannot be instantiated. Cannot do driver discovery";
	   }
	}

	public boolean isJdbcCompliant() {
	  return jdbcCompliant;
	}

	public Map getConnectionProperties() {
	  return connProperties;
	}

	public void setConnectionProperties(Map inProperties) {
	   connProperties = new Properties();
	   Iterator it = inProperties.keySet().iterator();
	   while ( it.hasNext() ) {
		  Object obj = it.next();
		  connProperties.setProperty(obj.toString(), inProperties.get(obj).toString());
	   }
	}

	/**
	 * Returns the URL
	 */
	public String getURL() { return urlString; }

	/**
	 * Changes the URL
	 */
	public void   setURL( String aValue )  { urlString = aValue; }

	/**
	 * Returns the polite name (the name that will show up in
	 * the browser) of this database profile.
	 */
	public String getPoliteName() { return politeName; }

	/**
	 * This determines the number of connections that will be ready
	 * for access simultaneously by threads.
	 */
	public int getConnections() { return(maxConnections); }

	/**
	 *This changes the number of connections that will be readied
	 * for access simultaneously by threads.
	 */
	public void setConnections(int numConnections) { maxConnections = numConnections; }


	/**
	 * Changes the polite name (the name that will show up on the browser)
	 * for this database profile.
	 */
	public void   setPoliteName(String aValue) { politeName = aValue; };

	/**
	 * Returns the default user name (which will show up in the login prompt)
	 * for this database profile.
	 */
	public String getDefaultUserName() { return userName; }

	/**
	 * Changes the default user name (which will show up in the login prompt)
	 * for this database profile.
	 */
	public void   setDefaultUserName(String aValue) { userName = aValue; }

	/**
	 * Returns the interface class (internal class that helps us deal with
	 * different databases - kind of like a plug in) for this database spec.
	 */
	public String getDbIntfClassName() { return dbIntfClassName; };
	/**
	 * Changes the interface class (internal class that helps us deal with
	 * different databases - kind of like a plug in) for this database spec.
	 */
	public void setDbIntfClassName(String aValue) { dbIntfClassName = aValue; };

   public String toString() {
	  return(getPoliteName());
   };


	public static void main(String[] args) {
	   ConnectionConfig spec = new ConnectionConfig("asample.xml");

	}

	public String getUserName() {
		return userName;
	}

	public String getDefaultCatalog() {
		return defaultCatalog;
	}

	public String getHostName() { return hostName; }


	public Class resolveDriverClass() throws ClassNotFoundException, MalformedURLException {
		Class theClass;
		if ( (jarFiles == null) || ( jarFiles.length ==0 ) ) {
			theClass = Class.forName(driverClassName);
		} else {
			ArrayList al = new ArrayList();
			for ( int i=0; i<jarFiles.length; i++ ) {
				String jarFile = jarFiles[i];
				File file = new File(jarFile);
				URL url = file.toURL();
				al.add(url);
			}
			URL[] urls = new URL[al.size()];
			urls = (URL[])al.toArray(urls);
			URLClassLoader urlClassLoader = new URLClassLoader(urls);
			theClass = urlClassLoader.loadClass(driverClassName);
			System.out.println("[DatabaseProcess] Loaded driver class "+driverClassName);
		}
		System.out.println("[DatabaseProcess] Loaded driver class "+driverClassName);
		return theClass;
	}

}