package com.hackerdude.apps.sqlide.servertypes;

import com.hackerdude.apps.sqlide.*;
import com.hackerdude.lib.ClassPathIntrospector;
import java.util.*;
import java.io.*;


/**
 * A Registry cache for server type entries.
 * @author David Martinez (david@hackerdude.com)
 * @version 1.0
 */
public class ServerTypeRegistryCache {

	static final String SERVERTYPES_PROPERTIES = "servertypes.list.properties";
	static final String COMMENT = "#";
	private Collection serverTypes;

	private static ServerTypeRegistryCache instance;

	private ServerTypeRegistryCache() {
		try {
			initializeCache();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static synchronized ServerTypeRegistryCache getInstance() {
		if ( instance == null ) {
			instance = new ServerTypeRegistryCache();
		}
		return instance;
	}

	/**
	 * Returns all the server types.
	 */
	public Collection getServerTypes() {
		return serverTypes;
	}

	/**
	 * Initializes the server types cache.
	 */
	private void initializeCache() throws IOException {
		InputStream inputStream = ServerTypeRegistryCache.class.getResourceAsStream(SERVERTYPES_PROPERTIES);
		loadCache(inputStream);
	}

	/**
	 * Loads the cache from a file instance.
	 */
	private void loadCache(InputStream is) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		serverTypes = new ArrayList();
		String theLine = br.readLine();
		while ( theLine != null ) {
			if ( ! theLine.equals("") && !theLine.startsWith(COMMENT) ) serverTypes.add(theLine);
			theLine = br.readLine();
		}

	}

}
