package com.hackerdude.apps.sqlide.dataaccess;

import com.hackerdude.apps.sqlide.xml.hostconfig.*;

/**
 * Database Service. It Database Process creation. Eventually it will also
 * handle credentials and such.
 *
 */
public class DatabaseService {

	private static DatabaseService instance = null;

    private DatabaseService() {
    }

	public static synchronized DatabaseService getInstance() {
		if ( instance == null ) instance = new DatabaseService();
		return instance;
	}

	/**
	 * Creates or returns an existing database spec.
	 * @param hostConfig
	 * @return
	 */
	public DatabaseProcess getDatabaseProcess(SqlideHostConfig hostConfig) {
		DatabaseProcess result = new DatabaseProcess( hostConfig );
		return result;
	}


}