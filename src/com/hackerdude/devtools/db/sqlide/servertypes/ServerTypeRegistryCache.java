package com.hackerdude.devtools.db.sqlide.servertypes;

import com.hackerdude.devtools.db.sqlide.*;
import com.hackerdude.lib.ClassPathIntrospector;
import java.util.*;
import java.io.*;


/**
 * A Registry cache for server type entries.
 * @author David Martinez (david@hackerdude.com)
 * @version 1.0
 */
public class ServerTypeRegistryCache {

	static final String cacheFile = "servertypes.cache";
	static final String COMMENT = "#";
	private Collection serverTypes;
	private ClassPathIntrospector cip = new ClassPathIntrospector();

	private static ServerTypeRegistryCache instance;

	private ServerTypeRegistryCache() {
		initializeCache();
	}

	public static synchronized ServerTypeRegistryCache getInstance() {
		if ( instance == null ) {
			instance = new ServerTypeRegistryCache();
		}
		return instance;
	}

	/**
	 * This method uses the introspector to figure out all the classes that can
	 * be used as server type entries for sqlide. It may take a little while.
	 */
	public synchronized void refreshCache() {
		ClassPathIntrospector cip = new ClassPathIntrospector();
		serverTypes = cip.getClassNamesFor(ServerType.class, ServerType.class.getPackage());
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
	private void initializeCache() {
		File cache = getCacheFile();
		if ( cache.exists() ) {
			try {
				loadCache(cache);
			} catch ( IOException exc ) {
				refreshCache();
			}
		} else {
			refreshCache();
			try {
				saveCache(cache);
			} catch ( IOException exc ) {
				System.out.println("[ServerTypeRegistryCache] Warning: Could not save Server type cache file to "+cache.toString());
			}
		}
	}

	/**
	 * Loads the cache from a file instance.
	 */
	private void loadCache(File cache) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(cache));
		serverTypes = new ArrayList();
		String theLine = br.readLine();
		while ( theLine != null ) {
			if ( ! theLine.equals("") && !theLine.startsWith(COMMENT) ) serverTypes.add(theLine);
			theLine = br.readLine();
		}

	}

	/**
	 * Saves the existing cache to a file instance.
	 */
	private void saveCache(File cache) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(cache));
		bw.write("# Server Types Registry Cache.\n");
		bw.write("#   This cache is automatically Generated. To refresh, simply remove this file.\n");
		Iterator it = serverTypes.iterator();
		while (it.hasNext() ) {
			bw.write((String)it.next());
			bw.write('\n');
		}
		bw.close();
	}

	public File getCacheFile() {
		String cacheFileName = ProgramConfig.getPropsPath()+File.separator+cacheFile;
		File cache = new File(cacheFileName);
		return cache;
	}

}