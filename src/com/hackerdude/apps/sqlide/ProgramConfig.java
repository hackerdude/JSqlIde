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

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * The Program Configuration object. This object's job is to maintain
 * the entire configuration information for the sqlIDE program, including
 * Icons, database access profiles, editor and general configuration.
 * @author David Martinez
 * @version $Id$
 */
public class ProgramConfig extends Observable {

	public final static String VERSION_NUMBER = determineVersion();

	public static final String PROP_FONT_FOR_EDITOR    = "editor";
	public static final String PROP_FONT_FOR_RESULTSET = "resultset";
	public static final String PROP_FONT_FOR_BROWSER   = "browser";

	public static final String[] FONT_NAMES = { PROP_FONT_FOR_EDITOR, PROP_FONT_FOR_RESULTSET, PROP_FONT_FOR_BROWSER };

	// Some important basic properties
	public static final String PROP_LOOK_AND_FEEL      = "general.ui.lookandfeel";
	public static final String PROP_SAVE_DIRECTORY    = "general.ui.persistence.savedir";
	public static final String PROP_LOOK_AND_FEEL_CLASS = "general.ui.lookandfeel.class";
	public static final String PROP_FONT_SIZE     = "general.fontsize.";
	public static final String PROP_FONT_NAME     = "general.fontname.";
	public static final String PROP_SQL_TABLE_VIEW    = "general.panel.isql.tableview";
	public static final String PROP_ISQL_BY_DEFAULT      = "general.panel.isql.default";

	public static final String propertiesFile = "sqlIDE.properties"; // Main Properties file

	protected Properties userinterface;  // The user interface properties.
	protected Properties panels;         // The panel registry.

	protected boolean iSQLbyDefault; // Open an interactive SQL by default?

	protected String saveDirectory = System.getProperty("user.home")+File.separator+"sqlide";

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
		setFont(PROP_FONT_FOR_RESULTSET, newFont);
	}

	public void setFont(String fontDefinitionName, Font newFont) {
		String fontName = newFont.getName();
		int fontSize = newFont.getSize();
		userinterface.setProperty(PROP_FONT_NAME+fontDefinitionName, fontName);
		userinterface.setProperty(PROP_FONT_SIZE+fontDefinitionName, Integer.toString(fontSize));
		notifyObservers();
	}

	public Font getFont(String fontDefinitionName) {
		return getFont(userinterface, fontDefinitionName);
	}

	public static Font getFont(Properties userInterface, String fontDefinitionName) {
		String fontName = userInterface.getProperty(PROP_FONT_NAME+fontDefinitionName, "Monospaced");
		int fontSize = 10;
		try {
			fontSize = Integer.parseInt(userInterface.getProperty(PROP_FONT_SIZE+fontDefinitionName));
		}
		catch (Throwable ex) {}
		Font result = new Font(fontName, Font.PLAIN, fontSize);
		return result;
	}

	public void setSQLFont(Font newFont) {
		setFont(PROP_FONT_FOR_EDITOR, newFont);
	}


	public Font getResultSetFont() {
		return getFont(userinterface, PROP_FONT_FOR_RESULTSET);
	}

	public Font getSQLFont() {
		return getFont(userinterface, PROP_FONT_FOR_EDITOR);
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

		iSQLbyDefault = userinterface.getProperty(PROP_ISQL_BY_DEFAULT).equals(new String("yes"));

//		readSqlideHostConfigs();

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

		File saveDir = new File(saveDirectory);
		if ( ! saveDir.exists() ) saveDir.mkdir();
		userinterface.setProperty(PROP_SAVE_DIRECTORY, saveDir.toString());
		notifyObservers(userinterface);

	}

	/**
	 * Saves the configuration to a file.
	 */
	public void saveConfiguration() {
		saveDefaults(userinterface);
		try {
			//TODO: Save the host configs.
//			saveSqlideHostConfigs();
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

	public Properties getUserInterfaceProperties() {
		return userinterface;
	}

}
