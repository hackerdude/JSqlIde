package com.hackerdude.devtools.db.sqlide.dataaccess;

import java.io.*;
import org.xml.sax.SAXException;

/**
 * A class that creates new Database Specs.
 */
public class DatabaseSpecFactory {

	private static DatabaseSpecDOMParser parser = new DatabaseSpecDOMParser();

	/**
	 * Creates a default database spec.
	 */
	public static ConnectionConfig createDatabaseSpec() {
		InputStream is = DatabaseSpecFactory.class.getResourceAsStream("default.db.xml");
		ConnectionConfig result = null;
		try {
			result = parser.parseDatabaseSpec(is);
		} catch ( Exception exc ) {
			exc.printStackTrace();
		}
		return result;
	}

	public static ConnectionConfig createDatabaseSpec(InputStream is) throws SAXException, IOException {
		ConnectionConfig result = parser.parseDatabaseSpec(is);
		return result;
	}

	public static ConnectionConfig createDatabaseSpec(String fileName) throws IOException {
		ConnectionConfig result = null;
		try {
			FileInputStream is = new FileInputStream(fileName);
			result = parser.parseDatabaseSpec(is);
			result.setFileName(fileName);
		} catch ( Throwable exc ) {
			exc.printStackTrace();
			throw new IOException(exc.toString());
		}
		return result;
	}

	public static void saveDatabaseSpec(ConnectionConfig spec) {
		parser.save(spec);
	}

}