/*
*
*   DatabaseProcess.java - a SQL Ide Process.
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
* Id      : $Id$
 */
package com.hackerdude.apps.sqlide.dataaccess;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import com.hackerdude.apps.sqlide.SqlIdeApplication;
import com.hackerdude.apps.sqlide.components.CachedResultSetTableModel;
import com.hackerdude.apps.sqlide.components.ScrollableResultSetTableModel;
import com.hackerdude.apps.sqlide.intf.SQLIDEDBInterface;
import com.hackerdude.apps.sqlide.xml.HostConfigFactory;
import com.hackerdude.apps.sqlide.xml.hostconfig.ConnectionProperties;
import com.hackerdude.apps.sqlide.xml.hostconfig.Property;
import com.hackerdude.apps.sqlide.xml.hostconfig.SqlideHostConfig;
import com.hackerdude.lib.dataaccess.ConnectionFactory;
import com.hackerdude.lib.dataaccess.ConnectionPool;

/**
 * A Database Process is the db access backend for all the sqlIDE
 * functions. Just instantiate one of these (using the DatabaseSpec
 * db profile you want to use for the connection) and call its methods!
 *
 * 
 * @author David Martinez
 * @version 1.0
 */
public class DatabaseProcess implements CredentialsProvider.CredentialsVerifier, ConnectionFactory {

	public String currentCatalog;
	public Vector dbList;

	public String lastQuery;
	private TableModel lastResultTable;

	private Connection lastConnection;
	private QueryResults lastQueryResults;
	private PreparedStatement lastDatabaseCall;
	private ConnectionPool pool;
	private Properties connProps;

	private Driver currentDriver;

	public SQLIDEDBInterface dbInterface;

	private String serverCatalogName;
	private String urlform;

	private Map properCredentials = null;

	private SqlideHostConfig hostConfiguration;

	public CredentialsProvider credentialsProvider;

	public TableModel getTableModel() {
		return lastResultTable;
	}

	public QueryResults getLastQueryResults() {
		return lastQueryResults;
	}

	protected DatabaseProcess(SqlideHostConfig hostConfiguration, CredentialsProvider credentialsProvider) {
		this.hostConfiguration = hostConfiguration;
		this.credentialsProvider = credentialsProvider;
	}


	public Connection getConnection() throws SQLException {
		if ( ! doConnect() ) return null;
		Connection conn = null;
		try {
			conn = pool.getConnection();
			changeCatalog(conn);
			return conn;
		} catch( SQLException sqle ) {
			sqle.printStackTrace();
			JOptionPane.showMessageDialog(SqlIdeApplication.getFrame(), sqle, "SQL Error when getting Connection", JOptionPane.ERROR_MESSAGE);
		}
		return conn;
	}

	public SQLIDEDBInterface getDBInterface() throws SQLException {
		if ( ! doConnect() ) return null;
		else return dbInterface;
	}

	public void returnConnection( Connection conn ) {
		getPool().releaseConnection(conn);
	}

	/**
	 * This method returns a vector of different databases using JDBC's
	 * getCatalogs.
	 * @return An arraylist Strings containing the available databases.
	 * @throws SQLException If there is a database problem.
	 */
	public ArrayList getCatalogs() throws SQLException {
		ArrayList al = new ArrayList();
		if ( !  doConnect() ) return al;
		Connection conn = null;
		try {
			conn=getPool().getConnection();
			ResultSet rs = conn.getMetaData().getCatalogs();
			while(rs.next()) { al.add( rs.getString(1) ); }
		} finally {
			getPool().releaseConnection(conn);
		}
		return(al);
	}

	public Vector getSQLTypes( String database ) {

		Vector types = new Vector();
		try {
			if ( ! doConnect() )  return types;
			Connection conn = getPool().getConnection();
			changeCatalog(conn);
			try {
				ResultSet rs = conn.getMetaData().getTypeInfo();
				while(rs.next()) { types.add( rs.getString(1) ); }
				} finally { getPool().releaseConnection(conn); }
		} catch ( SQLException sqle ) {
			JOptionPane.showMessageDialog(SqlIdeApplication.getFrame(), sqle,"SQL Error while getting types",JOptionPane.ERROR_MESSAGE);
		}
		return(types);
	}

	/**
	 * Gets the tables that the database contains.
	 *
	 * @param schemaName The name of the schema.
	 * @param catalogName The name of the catalog
	 * @return A vector with the table names.
	 */
	public Vector getTablesIn( String schemaName, String catalogName ) {
		Vector tbls = new Vector();
		Connection conn = null;
		try {
			conn = getConnection();
			ResultSet rs = conn.getMetaData().getTables( catalogName, schemaName, null, null );
			while(rs.next()) {
				String tableSchema = rs.getString(2);
				String tableType = rs.getString(4);
				if ( (tableType !=null ) && ( ! tableType.equals("INDEX") ) ) {
					String tableName = rs.getString(3);
					if ( ! tbls.contains(tableName) ) tbls.add(tableName);
				}
			}
		} catch( SQLException sqle ) {
			sqle.printStackTrace();
			JOptionPane.showMessageDialog(SqlIdeApplication.getFrame(), sqle,"SQL Error while getting tables",JOptionPane.ERROR_MESSAGE);
		} finally {
			returnConnection(conn);
		}
		return(tbls);
	}


	public Vector getSchemas() throws SQLException {
		Vector v = new Vector();
		if ( ! doConnect() ) return v;
		Connection conn = null;
		//changeCatalog(database);
		try {
			conn = getPool().getConnection();
			changeCatalog(conn);
			ResultSet rs = conn.getMetaData().getSchemas();
			while(rs.next()) { v.add( rs.getString(1) );}
			} finally { getPool().releaseConnection(conn); }
			return v;
	}

	/**
	 * Returns a vector with the stored procedure names.
	 * @param schema The name of the database schema.
	 * @return A vector with the stored procedures as strings.
	 */
	public Vector getStoredProcedures(String schema) {
		Vector v = new Vector();
		try {
			if ( ! doConnect() ) return v;
			Connection conn = getPool().getConnection();
			changeCatalog(conn);
			try {
				ResultSet rs = conn.getMetaData().getProcedures( currentCatalog, schema, null );
				while(rs.next()) { v.add( rs.getString(3) );}
			} finally {
				getPool().releaseConnection(conn);
			}
		} catch( SQLException sqle ) {
			sqle.printStackTrace();
			JOptionPane.showMessageDialog(SqlIdeApplication.getFrame(), sqle,
					"SQL Error when getting procedures", JOptionPane.ERROR_MESSAGE);
		}
		return v;
	}

	/**
	 * This method allows the user to change the database context.
	 *
	 * @param catalogName The name of the new catalog.
	 */
	public void changeCatalog(String catalogName) {
		currentCatalog = catalogName;
	}

	/**
	 * This method actually changes the current catalog to the last catalog
	 * specified with changeCatalog.
	 * @param conn The connection to set the catalog on.
	 */
	private void changeCatalog( Connection conn ) {
		try {
			if ( currentCatalog != null && !currentCatalog.equals("") ) conn.setCatalog(currentCatalog);
		} catch( SQLException sqle ) {
			sqle.printStackTrace();
			JOptionPane.showMessageDialog(SqlIdeApplication.getFrame(), sqle,
					"SQL Error while changing DB", JOptionPane.ERROR_MESSAGE);
		}
	}


	/**
	 * Call this method to run any query string. The results will be processed
	 * and placed on the tablemodel for this object.
	 * @param queryString The string of the query you want to run.
	 * @param updatableResultSet Should it be updatable?
	 * @param asUpdate Run as update?
	 * @return QueryResults The results of executing this query.
	 * @throws SQLException if a SQL error ocurrs
	 */
	public QueryResults runQuery(String queryString, boolean asUpdate, boolean updatableResultSet) throws SQLException {

		lastResultTable = null;
		if ( lastQueryResults != null ) {
			if ( lastQueryResults.getResultSet()!=null ) lastQueryResults.getResultSet().close();
			lastQueryResults = null;
		}
		if ( lastDatabaseCall != null ) { lastDatabaseCall.close(); lastDatabaseCall= null;}
		if ( lastConnection != null ) { returnConnection(lastConnection); lastConnection = null; }
		if ( !  doConnect() ) return null;
		lastQuery = queryString;
		if ( lastConnection == null ) lastConnection = getPool().getConnection();
		changeCatalog(lastConnection);
		if ( updatableResultSet ) lastConnection.setAutoCommit(false);
		int concurrency = (updatableResultSet?ResultSet.CONCUR_UPDATABLE:ResultSet.CONCUR_READ_ONLY);
		int rtype        = ResultSet.TYPE_SCROLL_INSENSITIVE;

		if ( asUpdate == true ) {
			lastDatabaseCall = lastConnection.prepareStatement(queryString);
			_executeUpdate();
			return lastQueryResults;
		}

		try {
			// Try a scrollable cursor
			lastDatabaseCall = lastConnection.prepareStatement(queryString,rtype, concurrency);
			_executeQuery(rtype);
		} catch ( Throwable exc ) {
			System.out.println("[DatabaseProcess.runQuery] This driver does not support scrollable cursors... Using forward_only and CachedResultSetTableModel.");
			rtype       = ResultSet.TYPE_FORWARD_ONLY;
			lastDatabaseCall = lastConnection.prepareStatement(queryString, rtype, concurrency);
			if ( exc.toString().toLowerCase().indexOf("no results") == -1 ) {
				_executeQuery(rtype);
			}
		}
//		if ( rtype == ResultSet.TYPE_SCROLL_INSENSITIVE ) {
//			System.out.println("[DatabaseProcess.runQuery] Cool.. Scrollable resultset returned. Using ScrollableResultSetTableModel");
//		} else {
//			System.out.println("[DatabaseProcess.runQuery] Scrollable resultset not available... Using CachedResultSetTableModel");
//		}
		return lastQueryResults;

	}

	private void _executeQuery(int rtype) throws SQLException {
		long currentMS = System.currentTimeMillis();
		ResultSet rs = lastDatabaseCall.executeQuery();
		long elapsedMS = calculateElapsedTime(currentMS);
		lastQueryResults = new QueryResults(rs, elapsedMS);
		if ( rtype == ResultSet.TYPE_FORWARD_ONLY ) {
			CachedResultSetTableModel model = new CachedResultSetTableModel(lastQueryResults, 1000);
			lastResultTable = model;
		} else if ( rtype == ResultSet.TYPE_SCROLL_INSENSITIVE || rtype == ResultSet.TYPE_SCROLL_SENSITIVE ) {
			ScrollableResultSetTableModel model = new ScrollableResultSetTableModel(lastQueryResults);
			lastResultTable = model;
		}
	}

	private long calculateElapsedTime(long currentMS) {
		long finalMS = System.currentTimeMillis();
		long elapsedMS = finalMS - currentMS;
		return elapsedMS;
	}


	private void _executeUpdate() throws SQLException {
		long currentMS = System.currentTimeMillis();
		lastDatabaseCall.executeUpdate();
		long elapsedMS = calculateElapsedTime(currentMS);
		lastQueryResults = new QueryResults(null, elapsedMS);
		int updateCount = 0;
		try {
			updateCount = lastDatabaseCall.getUpdateCount();
			lastQueryResults.setRowsAffected(updateCount);
		} catch (SQLException exc) {}
	}


	/**
	 * This call no longer makes any sense. Maybe we can iterate down all
	 * the connections and get it to release.
	 */
	public synchronized void doDisconnect() {
		pool.disconectAll();
	}

	public synchronized void loadDriver() {
		if ( currentDriver == null ) {
			try {
				Class theClass = resolveDriverClass(hostConfiguration);
				currentDriver = (Driver)theClass.newInstance();
				DriverManager.registerDriver( currentDriver );
			} catch(Exception exc) {
				exc.printStackTrace();
				JOptionPane.showMessageDialog(SqlIdeApplication.getFrame(), exc, "The Driver was Loaded, but had a Problem",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}


	/**
	 * Connects to the server, showing a dialog box if necessary.
	 * @return true if the connection was succesful, false otherwise.
	 */
	public synchronized boolean doConnect() throws SQLException {
		Connection conn = null;
		Connection poolConn = null;

		try {
			if ( pool != null ) poolConn = pool.getConnection();
		} catch ( SQLException exc ) {
			doDisconnect();
		}

		if ( poolConn !=null ) { return( true ); } // We are connected.

		if ( credentialsProvider.areCredentialsAvailable(hostConfiguration, this) ) {
			properCredentials = credentialsProvider.getCredentials();
			if ( pool == null ) {
				pool = new ConnectionPool(hostConfiguration.getFileName(), this);
			}

			return true;
		} else {
			return false;
		}
	}

	private Properties getConfigProperties() {
		Properties result = new Properties();
		ConnectionProperties connectionProps = hostConfiguration.getJdbc().getConnectionProperties();
		Property[] props = connectionProps.getProperty();
		for ( int i=0; i<props.length; i++ ) {
			result.setProperty(props[i].getName(), props[i].getValue());
		}
		return result;
	}

	public String toString() {
		if ( hostConfiguration.getName() == null ) return "Unknown Server";
		return(hostConfiguration.getName());
	}

	public SqlideHostConfig getHostConfiguration() { return hostConfiguration; }

	/**
	 * Get a connection pool for this Process
	 * @return ConnectionPool the connection pool associated with this process.
	 */
	public synchronized ConnectionPool getPool() {
		return(pool);
	}

	public String getMessageFromWarnings(SQLException exception) {
		StringBuffer buffer = new StringBuffer();
		while ( exception != null ) {
			buffer.append(exception.getMessage());
			exception = exception.getNextException();
		}
		return buffer.toString();
	}

	public static Class resolveDriverClass(SqlideHostConfig configuration) throws ClassNotFoundException, MalformedURLException {
		Class theClass;

		String driverClassName = configuration.getJdbc().getDriver();

		if ( (configuration.getJdbc().getClassPath() == null || configuration.getJdbc().getClassPath().getPathelementCount() == 0 ) ) {
			theClass = Class.forName(driverClassName);
//			System.out.println("[DatabaseProcess] Loaded driver class "+driverClassName+" using base classloader.");
		} else {
			String[] classPath = configuration.getJdbc().getClassPath().getPathelement();
			ArrayList al = new ArrayList();
			for ( int i=0; i<classPath.length; i++ ) {
				String jarFile = classPath[i];
				File file = new File(jarFile);
				URL url = file.toURL();
				al.add(url);
			}
			URL[] urls = new URL[al.size()];
			urls = (URL[])al.toArray(urls);
			URLClassLoader urlClassLoader = new URLClassLoader(urls);
			theClass = urlClassLoader.loadClass(driverClassName);
//			System.out.println("[DatabaseProcess] Loaded driver class "+driverClassName);
		}
		return theClass;
	}

	public boolean equals(Object obj) {
		if ( ! ( obj instanceof DatabaseProcess ) ) return false;
		DatabaseProcess compareTo = (DatabaseProcess)obj;
		// Short Circuit Null possibilities.
		if ( compareTo.getHostConfiguration() == null ) return false;
		if ( compareTo.getHostConfiguration().getJdbc() == null ) return false;
		if ( getHostConfiguration() == null ) return false;
		if ( compareTo.getHostConfiguration().getJdbc() == null ) return false;

		// A database process is the same as another one if their URL and Driver class are the same.
		String compareToURL = compareTo.getHostConfiguration().getJdbc().getUrl();
		String compareDriverClass = compareTo.getHostConfiguration().getJdbc().getDriver();
		String url = getHostConfiguration().getJdbc().getUrl();
		String driverClass = compareTo.getHostConfiguration().getJdbc().getDriver();

		boolean theSame = ( url.equals(compareToURL) && driverClass.equals(compareDriverClass) );
		return theSame;

	}

	/**
	 * CredentialsProvider code calls this to make a test connection with
	 * the supplied credentials. The system returns true if it is possible
	 * to connect, or SQLException/false if it is not possible to connect.
	 * @param credentials
	 * @return True if we could make a test connection to the database.
	 * @throws SQLException If it is not possible to connect, or another error ocurrs
	 */
	public boolean areCredentialsCorrect(Map credentials) throws SQLException {

		boolean testOK = false;
		Connection conn = null;
		// Otherwise, try to start a new connection
		try {
			String theurl = hostConfiguration.getJdbc().getUrl();
			loadDriver();
			Properties testConnProps = new Properties(HostConfigFactory.connectionPropertiesToMap(hostConfiguration.getJdbc().getConnectionProperties()));
			String userName = (String) credentials.get(CredentialsProvider.KEY_USER_NAME);
			String password = (String) credentials.get(CredentialsProvider.KEY_PASSWORD);
			testConnProps.putAll(getConfigProperties());
			testConnProps.setProperty("user", userName);
			testConnProps.setProperty("password", password);
			conn = currentDriver.connect(theurl, testConnProps);
			testOK = true;
			conn.close();
		} finally {
			if ( conn != null ) try { conn.close(); } catch ( Throwable thr ) {}
		}
		return testOK;

	}

    public Connection createConnection() throws SQLException {
		String jdbcURL = hostConfiguration.getJdbc().getUrl();
		Properties config = new Properties();
		config.putAll(getConfigProperties());
		String userName = (String) properCredentials.get(CredentialsProvider.KEY_USER_NAME);
		String password = (String) properCredentials.get(CredentialsProvider.KEY_PASSWORD);
		config.setProperty("user", userName);
		config.setProperty("password", password);
		loadDriver();
		return currentDriver.connect(jdbcURL, config);
    }


}