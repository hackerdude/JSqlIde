package com.hackerdude.apps.sqlide.dataaccess;

import com.hackerdude.apps.sqlide.xml.hostconfig.SqlideHostConfig;
import java.util.ArrayList;
import java.util.Observable;
import java.io.*;
import com.hackerdude.apps.sqlide.*;
import com.hackerdude.apps.sqlide.xml.*;

/**
 *
 * A Host configuration registry for SQL-IDE.
 *
 * Because it is a service in a standalone application, it is a singleton.
 *
 */
public class HostConfigRegistry extends Observable {

	protected SqlideHostConfig defaultDatabaseSpec;

	protected ArrayList SqlideHostConfigList;

	private static HostConfigRegistry instance = null;

	static String defaultdbPropsFile;  // Default DB properties file

	private HostConfigRegistry() {
		readSqlideHostConfigs();
	}

	public synchronized static HostConfigRegistry getInstance() {
		if ( instance == null ) instance = new HostConfigRegistry();
		return instance;
	}

	/**
	 * Get the default database specification.
	 * @return A DatabaseSpec object with the database specification that
	 * will be used for the default server.
	 */
	public SqlideHostConfig getDefaultHostConfig() { return(defaultDatabaseSpec); };

	public void setDefaultDatabaseSpec(SqlideHostConfig config) { defaultDatabaseSpec = config; };

	/**
	 * Get a database spec by number
	 * @param index The number of database spec we want to retrieve
	 * @return A reference to the DatabaseSpec object with order in index.
	 */
	public SqlideHostConfig getSqlideHostConfig( int index ) { return((SqlideHostConfig)SqlideHostConfigList.get(index)); }


	public void removeSqlideHostConfig(SqlideHostConfig spec) {
		SqlideHostConfigList.remove(spec);
	}

	public void addSqlideHostConfig(SqlideHostConfig spec) {
		SqlideHostConfigList.add(spec);
		setChanged();
		notifyObservers(spec);
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
	 * Returns the number of database specs.
	 */
	public int getConnectionCount() { return SqlideHostConfigList.size(); }



	/**
	 * Read all the database configurations for this user.
	 *
	 */
	public synchronized void readSqlideHostConfigs() {

		File findFiles = new File(ProgramConfig.getInstance().getUserProfilePath());
		String[] dbPropFileNames = findFiles.list(new FileSuffixChecker(HostConfigFactory.PROP_DB_CONFIG_SUFFIX));
		String fileName;
		SqlideHostConfig dbSpec;

		SqlideHostConfigList = new ArrayList();

		for ( int i=0; i<dbPropFileNames.length; i++) {

			fileName = ProgramConfig.getInstance().getUserProfilePath()+dbPropFileNames[i];
			try {
				dbSpec = HostConfigFactory.createHostConfig(fileName);
				// Just in case they renamed it from the outside.
				dbSpec.setFileName(fileName);
				SqlideHostConfigList.add( dbSpec );
				if ( fileName.equals( defaultdbPropsFile ) ) {
					defaultDatabaseSpec = dbSpec;
				}
			} catch ( IOException exc ) {
				System.out.println("Error "+exc.toString()+" parsing "+fileName+"... Will not add to available configs list");
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


}