/*
 *  ProgramConfig - The Configuration class for the
 *  sqlDE program.
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
 * Id: $Id$
 *
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

import com.hackerdude.apps.sqlide.wizards.NewServerWizard;
import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.xml.hostconfig.*;
import com.hackerdude.apps.sqlide.xml.*;

/**
 * The Program Configuration object. This object's job is to maintain
 * the entire configuration information for the sqlIDE program, including
 * Icons, database access profiles, editor and general configuration.
 * @author David Martinez
 * @version $Id$
 */
public class ProgramConfig extends Observable {

	public final static String VERSION_NUMBER = determineVersion();

	// Some important basic properties
	public static final String PROP_LOOK_AND_FEEL      = "general.ui.lookandfeel";
	public static final String PROP_SAVE_DIRECTORY    = "general.ui.persistence.savedir";
	public static final String PROP_LOOK_AND_FEEL_CLASS = "general.ui.lookandfeel.class";
	public static final String PROP_SQL_FONT_SIZE     = "general.panel.isql.fontsize";
	public static final String PROP_SQL_FONT_NAME     = "general.panel.isql.fontname";
	public static final String PROP_SQL_TABLE_VIEW    = "general.panel.isql.tableview";
	public static final String PROP_ISQL_BY_DEFAULT      = "general.panel.isql.default";

	public static final String PROP_RESULTSET_FONT_SIZE = "general.resultset.font.size";
	public static final String PROP_RESULTSET_FONT_NAME = "general.resultset.font.name";

	public static final String propertiesFile = "sqlIDE.properties"; // Main Properties file
	static String defaultdbPropsFile;  // Default DB properties file

	protected Properties userinterface;  // The user interface properties.
	protected Properties panels;         // The panel registry.

	protected boolean iSQLbyDefault; // Open an interactive SQL by default?

	protected String saveDirectory = System.getProperty("user.home")+File.separator+"sqlide";

	protected SqlideHostConfig defaultDatabaseSpec;
	protected ArrayList SqlideHostConfigList;

	private static ProgramConfig instance;

	/**
	 * returns the current look and feel as a string.
	 * @return A string specifying the look-and-feel.
	 */
	public String getUILookandFeel() {  return(userinterface.getProperty(PROP_LOOK_AND_FEEL)); }

	/**
	 * Returns the current look and feel class as a string.
	 * @return The class name that implements the current look-and-feel.
	 */
	public String getUILookandFeelClass() { return(userinterface.getProperty(PROP_LOOK_AND_FEEL_CLASS)); }

	/**
	 * Change the look and feel. This method receives a look and feel
	 * string, NOT the class. It automatically finds out what class
	 * the string refers to and applies that.
	 * @param lookandFeel The Look and Feel.
	 */
	public void setUILookandFeel( String lookandFeel ) {
		String lookandFeelClass = null;
		LookAndFeelInfo[] theLooks = UIManager.getInstalledLookAndFeels();
		for ( int lf=0; lf<theLooks.length; lf++ )
			if ( theLooks[lf].getName().equals(lookandFeel) )
			lookandFeelClass = theLooks[lf].getClassName();

		if ( lookandFeelClass != null ) {
			userinterface.setProperty(PROP_LOOK_AND_FEEL, lookandFeel);
			userinterface.setProperty(PROP_LOOK_AND_FEEL_CLASS, lookandFeelClass);
		}
		notifyObservers();
	}

	public void setResultSetFont(Font newFont) {
		String fontName = newFont.getName();
		int fontSize = newFont.getSize();
		userinterface.setProperty(PROP_RESULTSET_FONT_NAME, fontName);
		userinterface.setProperty(PROP_RESULTSET_FONT_SIZE, new Integer(fontSize).toString() );
		notifyObservers();
	}

	public void setSQLFont(Font newFont) {
		String fontName = newFont.getName();
		int fontSize = newFont.getSize();
		userinterface.setProperty(PROP_SQL_FONT_NAME, fontName);
		userinterface.setProperty(PROP_SQL_FONT_SIZE, new Integer(fontSize).toString() );
		notifyObservers();
	}

	public Font getResultSetFont() {
		String fontName = userinterface.getProperty(PROP_RESULTSET_FONT_NAME, "Monospaced");
		int fontSize = 10;
		try {
			fontSize = Integer.parseInt(userinterface.getProperty(PROP_RESULTSET_FONT_SIZE));
		}
		catch (Throwable ex) {}
		Font result = new Font(fontName, Font.PLAIN, fontSize);
		return result;
	}

	public Font getSQLFont() {
		String fontName = userinterface.getProperty(PROP_SQL_FONT_NAME, "Monospaced");
		int fontSize = 10;
		try {
			fontSize = Integer.parseInt(userinterface.getProperty(PROP_SQL_FONT_SIZE));
		}
		catch (Throwable ex) {}
		Font result = new Font(fontName, Font.PLAIN, fontSize);
		return result;
	}

	/**
	 * Returns true if and only if it has been specified to use
	 * a "Table View" by default on the interactive SQL results.
	 * @return True if we want to specify a table view.
	 */
	public Boolean getSQLUseTableView() {
		return(new Boolean(userinterface.getProperty(PROP_SQL_TABLE_VIEW).equals("true") ));
	};

	/**
	 * Call this method to decide if you want to use a table view
	 * (JTable) when you first open the interactive SQL.
	 * @param useTable True if you want sqlIDE to use a table view in
	 * subsequent calls.
	 */
	public void setSQLUseTableView( Boolean useTable ) {
		userinterface.setProperty("iSQL_Use_Table_View", useTable.toString() );
		notifyObservers();
	};

	/**
	 * Constructor
	 */
	private ProgramConfig() {

		// First, find out or create
		Properties sys = System.getProperties();
		File propsDir = new File(getUserProfilePath());
		propsDir.mkdirs();

		userinterface = new Properties();
		try {
			String propsFile = getUserProfilePath()+propertiesFile;
			FileInputStream fis = new FileInputStream(propsFile);
			userinterface.load(fis);
		} catch( FileNotFoundException exc) {
			System.err.println("File not found... Using defaults");
			setDefaults(userinterface);
			saveDefaults(userinterface);
		} catch( IOException exc) {
			System.err.println("File I/O error reading config file... Using defaults");
			setDefaults(userinterface);
		}

//		defaultdbPropsFile = userinterface.getProperty(SqlideHostConfig.PROP_DEFAULT_CATALOG)+SqlideHostConfig.PROP_DB_CONFIG_SUFFIX;
		iSQLbyDefault = userinterface.getProperty(PROP_ISQL_BY_DEFAULT).equals(new String("yes"));

		readSqlideHostConfigs();

	}

	/**
	 * Sets the defaults
	 * @param userinterface The Properties item we need to fill out.
	 */
	public void setDefaults(Properties userinterface) {
		userinterface.setProperty(PROP_LOOK_AND_FEEL, "metal");
		userinterface.setProperty(PROP_LOOK_AND_FEEL_CLASS, "javax.swing.plaf.metal.MetalLookAndFeel");
		userinterface.setProperty(PROP_ISQL_BY_DEFAULT, "yes");
		userinterface.setProperty(PROP_SQL_TABLE_VIEW, "true");
		userinterface.setProperty(PROP_SQL_FONT_SIZE, "12");
		userinterface.setProperty(PROP_SQL_FONT_NAME, "MonoSpaced");
//		userinterface.setProperty(SqlideHostConfig.PROP_DEFAULT_CATALOG, "default");

		File saveDir = new File(saveDirectory);
		if ( ! saveDir.exists() ) saveDir.mkdir();
		userinterface.setProperty(PROP_SAVE_DIRECTORY, saveDir.toString());
		notifyObservers(userinterface);

	}

	public void removeSqlideHostConfig(SqlideHostConfig spec) {
		SqlideHostConfigList.remove(spec);
	}

	public void addSqlideHostConfig(SqlideHostConfig spec) {
		SqlideHostConfigList.add(spec);
		setChanged();
		notifyObservers(spec);
	}

	/**
	 * Saves the configuration to a file.
	 */
	public void saveConfiguration() {
		saveDefaults(userinterface);
		try {
			saveSqlideHostConfigs();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Saves the defaults (TODO: Why is this here?)
	 */
	public void saveDefaults(Properties userinterface) {
		try {
			FileOutputStream fos = new FileOutputStream(getUserProfilePath()+propertiesFile);
			userinterface.store(fos, "User Interface");
		} catch( FileNotFoundException exc) {
			System.err.println("File not found while saving !?!?");
		} catch( IOException exc) {
			System.err.println("File I/O error writing config file - "+exc.toString());

		}
	}

	/**
	 * This function will always return the current sqlide release number.
	 */
	public static String getVersionNumber() {
		return(VERSION_NUMBER);
	};

	/**
	 * This function will return a hidden directory based on the
	 * location of the user's profile. If the directory does not exist
	 * it will be created. This way each user can have his or her own
	 * sqlIDE configuration.
	 * @return A string with the path for the configuration files.
	 */
	public static String getUserProfilePath() {
		String propsPath;
		propsPath = System.getProperty("user.home")+File.separator+".sqlide"+File.separator;
		return(propsPath);
	}

	/**
	 * Get the Database Configuration Name.
	 * @param index The number of database configuration we want to retrieve.
	 * @return The "Polite Name" of the database configuration item
	 */
	public String getDbConfigName( int index ) {
		return( ((SqlideHostConfig)SqlideHostConfigList.get(index)).getName());
	}

	/**
	 * Get the DB Configuration index for a specific name
	 * Not implemented Yet.
	 */
	public int getDbConfigIndex( String name ) { return(1); };  // TODO: Implement

	/**
	 * Get the default database specification.
	 * @return A DatabaseSpec object with the database specification that
	 * will be used for the default server.
	 */
	public SqlideHostConfig getDefaultDatabaseSpec() { return(defaultDatabaseSpec); };

	public void setDefaultDatabaseSpec(SqlideHostConfig config) { defaultDatabaseSpec = config; };

	/**
	 * Get a database spec by number
	 * @param index The number of database spec we want to retrieve
	 * @return A reference to the DatabaseSpec object with order in index.
	 */
	public SqlideHostConfig getSqlideHostConfig( int index ) { return((SqlideHostConfig)SqlideHostConfigList.get(index)); }

	/**
	 * Returns the number of database specs.
	 */
	public int getConnectionCount() { return SqlideHostConfigList.size(); }


	/**
	 * Read all the database configurations for this user.
	 *
	 */
	public synchronized void readSqlideHostConfigs() {

		File findFiles = new File(getUserProfilePath());
		String[] dbPropFileNames = findFiles.list(new FileSuffixChecker(HostConfigFactory.PROP_DB_CONFIG_SUFFIX));
		String fileName;
		SqlideHostConfig dbSpec;

		SqlideHostConfigList = new ArrayList();

		for ( int i=0; i<dbPropFileNames.length; i++) {

			fileName = getUserProfilePath()+dbPropFileNames[i];
			try {
				dbSpec = HostConfigFactory.createHostConfig(fileName);
				SqlideHostConfigList.add( dbSpec );
				if ( dbPropFileNames[i].equals( defaultdbPropsFile ) ) {
					defaultDatabaseSpec = dbSpec;
				}
			} catch ( IOException exc ) {
				exc.printStackTrace();
				JOptionPane.showMessageDialog(null, "Could not parse Database spec "+fileName+" - "+exc.toString(), "Could not parse "+fileName, JOptionPane.ERROR_MESSAGE);
			}
		}
		notifyObservers();

	}

	/**
	 * Save all the database specifications.
	 */
	public void saveSqlideHostConfigs() throws IOException {
		for (int i=0; i<SqlideHostConfigList.size(); i++) {
			SqlideHostConfig currentSpec = ((SqlideHostConfig)SqlideHostConfigList.get(i));
			HostConfigFactory.saveSqlideHostConfig(currentSpec);
		}
	}

	/**
	 * Returns the singleton instance of this configuration object.
	 * @return The singleton instance
	 */
	public static synchronized ProgramConfig getInstance() {
	  if ( instance == null ) { instance = new ProgramConfig(); }
	  return instance;
	}

	/**
	 * Changes the save directory, used to offer as default for saving/loading
	 * user data.
	 * @param newSaveDirectory The new save directory.
	 */
	public void setSaveDirectory(String newSaveDirectory) {
		saveDirectory = newSaveDirectory;
		notifyObservers(saveDirectory);
	}

	/**
	 * Returns the current save directory. Use this directory to offer as
	 * default for saving/loading user data.
	 * @return The save directory.
	 */
	public String getSaveDirectory() {
		return saveDirectory;
	}

	/*
	 * fileSuffixChecker a fileName filter that checks for a
	 * specific file suffix
	 */
	private class FileSuffixChecker implements FilenameFilter {
		String matching;
		public FileSuffixChecker( String suffix ) {
			matching = suffix;
		}
		public boolean accept( File dir, String name) {
			return( name.endsWith(matching) );
		}
	}


	/**
	 * Determines the current version of SQL-IDE
	 * @return A string with the current version.
	 */
	public static String determineVersion() {
		final String CONST_VERSION = "jsqlide.version";
		String result = "Could not determine version";
		try {
			InputStream is = ProgramConfig.class.getResourceAsStream("/sqlide.properties");
			if ( is == null ) return result;
			Properties props = new Properties();
			props.load(is);
			result = props.getProperty(CONST_VERSION,result);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}


}
