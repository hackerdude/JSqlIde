package com.hackerdude.apps.sqlide.xml;

import com.hackerdude.apps.sqlide.xml.hostconfig.*;
import java.util.*;
import java.io.*;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.*;

/**
 * This class creates properly-initialized Host Configuration objects.
 */
public class HostConfigFactory {

	public static final String DEFAULT_DBSPEC = "default";
	public static final String PROP_DB_CONFIG_SUFFIX = ".db.xml";

	public static SqlideHostConfig createHostConfig() {
		SqlideHostConfig result = new SqlideHostConfig();
		result.setGeneral(new General());
		result.getGeneral().setSupportsDotNotation(true);
		result.setJdbc(new Jdbc());
		result.getJdbc().setUserName(System.getProperty("user.name"));
		result.setName("New Configuration");
		result.getJdbc().setClassPath(new ClassPath());
		result.getJdbc().setConnectionProperties(new ConnectionProperties());
		return result;
	}


	public static SqlideHostConfig createHostConfig(String fileName) throws IOException {
		FileReader reader = new FileReader(fileName);
		SqlideHostConfig config = null;
		try {
			config = SqlideHostConfig.unmarshal(reader);
		}
		catch (MarshalException ex) {
			throw new IOException(ex.toString());
		}catch (ValidationException ex) {
			throw new IOException(ex.toString());
		}
		return config;
	}

	public static ConnectionProperties mapToConnectionProperties(Map propsMap) {
		ConnectionProperties result = new ConnectionProperties();
		Iterator iter = propsMap.keySet().iterator();
		while ( iter.hasNext() ) {
			Object key = iter.next();
			Object value = propsMap.get(key);
			Property prop = new Property();
			prop.setName((String)key);
			prop.setValue((String)value);
		}
		return result;
	}



	public static Properties connectionPropertiesToMap(ConnectionProperties props) {
		Properties result = new Properties();
		for ( int i=0; i<props.getPropertyCount(); i++) {
			String key = props.getProperty(i).getName();
			String value = props.getProperty(i).getValue();
			result.put(key, value);
		}
		return result;

	}

	public static void saveSqlideHostConfig(SqlideHostConfig config) throws IOException {
		String fileName = config.getFileName();
		FileWriter writer = new FileWriter(fileName);
		try {
			config.marshal(writer);
		}
		catch (MarshalException ex) {
			throw new IOException(ex.toString());
		}catch (ValidationException ex) {
			throw new IOException(ex.toString());
		}

	}
}