package com.hackerdude.devtools.db.sqlide.dataaccess;

import java.io.*;
import org.xml.sax.SAXException;

/**
 * A class that creates new Database Specs.
 */
public class ConnectionConfigFactory {

	private static ConnectionConfigDOMParser parser = new ConnectionConfigDOMParser();

	/**
	 * Creates a default database spec.
	 */
	public static ConnectionConfig createConnectionConfig() {
		InputStream is = ConnectionConfigFactory.class.getResourceAsStream("default.db.xml");
		ConnectionConfig result = null;
		try {
			result = parser.parseDatabaseSpec(is);
		} catch ( Exception exc ) {
			exc.printStackTrace();
		}
		return result;
	}

	public static ConnectionConfig createConnectionConfig(InputStream is) throws SAXException, IOException {
		ConnectionConfig result = parser.parseDatabaseSpec(is);
		return result;
	}

	public static ConnectionConfig createConnectionConfig(String fileName) throws IOException {
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

	public static void saveConnectionConfig(ConnectionConfig config) {
		parser.save(config);
	}

}