package com.hackerdude.apps.sqlide.dataaccess;

import java.util.Hashtable;

import com.hackerdude.apps.sqlide.xml.hostconfig.SqlideHostConfig;

/**
 * Database Service. It Database Process creation. Eventually it will also
 * handle credentials and such.
 *
 */
public class DatabaseService {

	private static DatabaseService instance = null;

	private Hashtable serviceList = new Hashtable();

	CredentialsProvider credentialsProvider;

    private DatabaseService() {
    }

	/**
	 * Singleton access method.
	 * @return The database service instance.
	 */
	public static synchronized DatabaseService getInstance() {
		if ( instance == null ) instance = new DatabaseService();
		return instance;
	}

	/**
	 * Creates or returns an existing database spec. It never constructs a
	 * spec for the same instance twice.
	 *
	 * @param hostConfig
	 * @return The specified database process.
	 */
	public synchronized DatabaseProcess getDatabaseProcess(SqlideHostConfig hostConfig) {

		String key = hostConfig.getFileName();
		DatabaseProcess process = (DatabaseProcess)serviceList.get(key);
		if ( process == null ) {
			process = new DatabaseProcess(hostConfig, credentialsProvider);
			serviceList.put(key, process);
		}
		return process;

	}


	/**
	 * Registers a credentials provider with the database service.
	 *
	 * @param provider The provider to register
	 * @see CredentialsProvider
	 */
	public void setCredentialsProvider(CredentialsProvider provider) {
		this.credentialsProvider = provider;
	}

}