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
	public static DatabaseSpec createDatabaseSpec() {
		InputStream is = DatabaseSpecFactory.class.getResourceAsStream("default.db.xml");
		DatabaseSpec result = null;
		try {
			result = parser.parseDatabaseSpec(is);
		} catch ( Exception exc ) {
			exc.printStackTrace();
		}
		return result;
	}

	public static DatabaseSpec createDatabaseSpec(InputStream is) throws SAXException, IOException {
		DatabaseSpec result = parser.parseDatabaseSpec(is);
		return result;
	}

	public static DatabaseSpec createDatabaseSpec(String fileName) throws IOException {
		DatabaseSpec result = null;
		try {
			FileInputStream is = new FileInputStream(fileName);
			result = parser.parseDatabaseSpec(is);
			result.setFileName(fileName);
		} catch ( SAXException exc ) {
			exc.printStackTrace();
			throw new IOException(exc.toString());
		}
		return result;
	}

	public static void saveDatabaseSpec(DatabaseSpec spec) {
		parser.save(spec);
	}

}