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
package com.hackerdude.devtools.db.sqlide.dataaccess;

import com.hackerdude.devtools.db.sqlide.intf.SQLIDEDBInterface;
import com.hackerdude.lib.*;
import com.hackerdude.lib.dataaccess.ConnectionPool;
import com.hackerdude.devtools.db.sqlide.components.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.plaf.basic.*;
import java.util.*;
import java.lang.Object.*;
import java.io.*;
import java.sql.*;

/**
 * A Database Process is the db access backend for all the sqlIDE
 * functions. Just instantiate one of these (using the DatabaseSpec
 * db profile you want to use for the connection) and call its methods!
 * @version $Id$
 */
public class DatabaseProcess {

	public String currentCatalog;
	public String userName;
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

	private String passWord;
	private String serverCatalogName;
	private String urlform;

	private ConnectionConfig databaseSpec;

	public TableModel getTableModel() {
		return lastResultTable;
	}

	public QueryResults getLastQueryResults() {
		return lastQueryResults;
	}

	/**
	 * Constructor
	 * @param svr The database Specification to use for the connection..
	 */
	public DatabaseProcess(ConnectionConfig svr) {

		databaseSpec = svr;
		currentCatalog  = databaseSpec.getDefaultCatalog();
		userName   = databaseSpec.getUserName();
		serverCatalogName = databaseSpec.getDbIntfClassName();

		// This tries to load the class and put it in the
		// dbInterface member. Once this is done, the
		// Application's UI components should be able to
		// call the methods to find out stuff about the
		// class.

		if ( serverCatalogName == null || serverCatalogName.equals("null") ) {
			System.err.println("[DatabaseProcess] Warning: DB Interface Class for "+databaseSpec.getPoliteName()+" not specified");
		} else {
			String fullyQualName = "com.hackerdude.devtools.db.sqlide.dbspecific."+serverCatalogName;
//			System.out.println("[DatabaseProcess] Loading class "+fullyQualName);
			try {
				Class interfaceClass = Class.forName(fullyQualName);
				dbInterface = (SQLIDEDBInterface)interfaceClass.newInstance();
			} catch ( IllegalAccessException exc) {
			} catch ( InstantiationException exc) {
			} catch( java.lang.ClassNotFoundException e ) {
				dbInterface = null;
			}
		}

			lastQuery = "";
			passWord = null;

	}


	public Connection getConnection() {
			if ( ! doConnect() ) return null;
			Connection conn = null;
			try {
				conn = pool.getConnection();
				changeCatalog(conn);
				return conn;
			} catch( SQLException sqle ) {
				sqle.printStackTrace();
				JOptionPane.showMessageDialog(null, sqle, "SQL Error when getting Connection", JOptionPane.ERROR_MESSAGE);
			}
			return conn;
	}

	public SQLIDEDBInterface getDBInterface() {
		if ( ! doConnect() ) return null;
		else return dbInterface;
	}

	public void returnConnection( Connection conn ) {
		getPool().releaseConnection(conn);
	}

	/**
	 * This method returns a vector of different databases using JDBC's
	 * getCatalogs.
	 * @returns An arraylist Strings containing the available databases.
	 */
	public ArrayList getCatalogs() throws SQLException {
			ArrayList al = new ArrayList();
			if ( !  doConnect() ) return al;
			Connection conn = null;
			try {
				conn=getPool().getConnection();
				ResultSet rs = conn.getMetaData().getCatalogs();
				while(rs.next()) { al.add( rs.getString(1) ); }
			} finally { getPool().releaseConnection(conn); };
			return(al);
	}

	public Vector getSQLTypes( String database ) {

			Vector types = new Vector();
			if ( ! doConnect() )  return types;
			try {
					Connection conn = getPool().getConnection();
					changeCatalog(conn);
					try {
							ResultSet rs = conn.getMetaData().getTypeInfo();
							while(rs.next()) { types.add( rs.getString(1) ); }
					} finally { getPool().releaseConnection(conn); }
			} catch ( SQLException sqle ) {
					JOptionPane.showMessageDialog(null, sqle,"SQL Error while getting types",JOptionPane.ERROR_MESSAGE);
			}
			return(types);
	}

	/**
	 * Gets the tables that the datbase contains.
	 */
	public Vector getTablesIn( String schemaName, String catalogName ) {
			Vector tbls = new Vector();
			Connection conn = getConnection();
			try {
					ResultSet rs = conn.getMetaData().getTables( catalogName, schemaName, null, null );
					while(rs.next()) { tbls.add( rs.getString(3) );}
			} catch( SQLException sqle ) {
					sqle.printStackTrace();
					JOptionPane.showMessageDialog(null, sqle,"SQL Error while getting tables",JOptionPane.ERROR_MESSAGE);
			} finally { returnConnection(conn); }
			return(tbls);
	}


	public String getCatalogTitle() {
			String s = "Catalog";
			if ( ! doConnect() ) return s;
			try {
					Connection conn = getPool().getConnection();
					try {
							s = conn.getMetaData().getCatalogTerm();
					} finally { getPool().releaseConnection(conn); }

			} catch ( SQLException exc ) {}
			return s;
	}

	public String getSchemaTitle() {
			String s = "Schema";
			if ( ! doConnect() ) return s;
			try {
				Connection conn = getPool().getConnection();
				try {
						s = conn.getMetaData().getSchemaTerm();
				} finally { getPool().releaseConnection(conn); }

			} catch ( SQLException exc ) {}
			return s;

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

	public Vector getStoredProcedures(String schema) {
		Vector v = new Vector();
		if ( ! doConnect() ) return v;
		try {
			Connection conn = getPool().getConnection();
			changeCatalog(conn);
			try {
				ResultSet rs = conn.getMetaData().getProcedures( currentCatalog, schema, null );
				while(rs.next()) { v.add( rs.getString(3) );}
			} finally { getPool().releaseConnection(conn); }
		} catch( SQLException sqle ) {
				sqle.printStackTrace();
				JOptionPane.showMessageDialog(null, sqle,
								"SQL Error when getting procedures",
								JOptionPane.ERROR_MESSAGE);
		}
		return v;
	}

	/**
	 * This method allows the user to change the database context.
	 */
	public void changeCatalog(String catalogName) {
		currentCatalog = catalogName;
	}

	/**
	 * This method actually changes the database. Call before every call to set the context correctly.
	 */
	private void changeCatalog( Connection conn ) {
		try {
			if ( currentCatalog != null && !currentCatalog.equals("") ) conn.setCatalog(currentCatalog);
		} catch( SQLException sqle ) {
			sqle.printStackTrace();
			JOptionPane.showMessageDialog(null, sqle,
							"SQL Error while changing DB",
							JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 *
	 */
	public void processResults( Connection conn, ResultSet resultSet, int rtype ) throws SQLException {

	//	if (

/**			int n;
			String theResult = new String();
			SQLWarning thewarning;
			StringBuffer sb = new StringBuffer(32678);  // 38K Buffer
			Vector RSData = new Vector(), Columns = new Vector();
			String AString = new String();
			ResultSetMetaData meta = rs.getMetaData();
			if (meta == null) return;

			// How many columns were returned?
			int numColumns = meta.getColumnCount();
			for ( n = 1; n<=numColumns; n++ ) {
					AString = meta.getColumnName(n);
					sb.append(AString);
					sb.append('\t');
					Columns.add(AString);
			}
			sb.append('\n');
			// Loop through the result set and get data
			while(rs.next()) {
					Vector row = new Vector();
					for (n = 1; n<=numColumns; n++) {
							AString = rs.getString(n);
							row.add( AString );
							if ( ! rs.wasNull() ) {
									sb.append(AString);
							}
							if (n < numColumns) sb.append('\t');
					} // for
					RSData.add(row);
					sb.append('\n');
			}
			thewarning = rs.getWarnings();
			if ( thewarning != null ) {
					if ( thewarning.equals(null) ) {
							sb.append(thewarning);
							Vector warning = new Vector();
							RSData.add( warning );
							thewarning = thewarning.getNextWarning();
					}}

			lastResult = sb;
			lastResultTable = new DefaultTableModel( RSData, Columns );\
			*/


	} // processResults


	/**
	 * Call this method to run any query string. The results will be processed
	 * and placed on the tablemodel for this object.
	 * @param queryString The string of the query you want to run.
	 * @param StatisticsIO Turn on I/O statistics?  (not implemented yet)
	 * @param Statisticstime Turn on Time Statistics? (not implemented yet)
	 */
	public void runQuery(String queryString, boolean asUpdate, boolean Statisticsio, boolean Statisticstime) throws SQLException {

		lastResultTable = null;
		if ( lastQueryResults != null ) { lastQueryResults.getResultSet().close(); lastQueryResults = null; }
		if ( lastDatabaseCall != null ) { lastDatabaseCall.close(); lastDatabaseCall= null;}
		if ( lastConnection != null ) { returnConnection(lastConnection); lastConnection = null; }
		if ( !  doConnect() ) return;
		lastQuery = queryString;
		if ( lastConnection == null ) lastConnection = getPool().getConnection();
			changeCatalog(lastConnection);
			int concurrency = ResultSet.CONCUR_READ_ONLY;
			int rtype        = ResultSet.TYPE_SCROLL_INSENSITIVE;
			try {
				lastDatabaseCall = lastConnection.prepareStatement(queryString,rtype, concurrency);
			} catch ( Throwable exc ) {
				System.out.println("[DatabaseProcess.runQuery] This driver does not support scrollable cursors... Using forward_only and CachedResultSetTableModel.");
				rtype       = ResultSet.TYPE_FORWARD_ONLY;
				lastDatabaseCall = lastConnection.prepareStatement(queryString);
			}
			if ( rtype == ResultSet.TYPE_SCROLL_INSENSITIVE ) {
				System.out.println("[DatabaseProcess.runQuery] Cool.. Scrollable resultset returned. Using ScrollableResultSetTableModel");
			} else {
				System.out.println("[DatabaseProcess.runQuery] Scrollable resultset not available... Using CachedResultSetTableModel");
			}
			if ( asUpdate == true )
				lastDatabaseCall.executeUpdate();
			else {
				ResultSet rs = lastDatabaseCall.executeQuery();
				lastQueryResults = new QueryResults(rs);
				if ( rtype == ResultSet.TYPE_FORWARD_ONLY ) {
					CachedResultSetTableModel model = new CachedResultSetTableModel(lastQueryResults, rtype);
					lastResultTable = model;
				} else if ( rtype == ResultSet.TYPE_SCROLL_INSENSITIVE || rtype == ResultSet.TYPE_SCROLL_SENSITIVE ) {
					ScrollableResultSetTableModel model = new ScrollableResultSetTableModel(lastQueryResults);
					lastResultTable = model;
				}

				int updateCount = 0;
				try {
					updateCount = lastDatabaseCall.getUpdateCount();
				} catch (SQLException exc) {}
				if ( updateCount > 0 ) {
						//lastResult.append("\nRows Affected: ").append(Integer.toString(updateCount));
				}
			}

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
				Class theClass = databaseSpec.resolveDriverClass();
				currentDriver = (Driver)theClass.newInstance();
				DriverManager.registerDriver( currentDriver );
			} catch(Exception exc) {
				exc.printStackTrace();
				JOptionPane.showMessageDialog(null, exc, "The Driver was Loaded, but had a Problem",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}


	/**
	 * Connects to the server.
	 */
	public synchronized boolean doConnect() {
		String theurl = new String();
		boolean TryAgain = true;
		Connection conn = null;
		Connection poolConn = null;

		try {
			if ( pool != null ) poolConn = pool.getConnection();
		} catch ( SQLException exc ) {
				doDisconnect();
		}
		if ( poolConn !=null) { return( true ); } // We are connected.

		// Otherwise, try to start a new connection
		try {
			while (TryAgain) {
				TryAgain = showLoginBox();
				if (TryAgain) {
					theurl = databaseSpec.getURL();
					loadDriver();
					connProps = new Properties(databaseSpec.getProperties());

					/** @hack This is still not right...  */
					connProps.setProperty("user", userName);
					connProps.setProperty("password", passWord);
					conn = currentDriver.connect(theurl, connProps );

					// TODO: Create the maxconnections here. Later I'll have to refactor this entire class so the it automatically grows new connections.
					if ( conn !=null ) {
						getPool().addConnection(conn);
						TryAgain = false;
					}
				}  // if tryagain
			} // while
		} catch(SQLWarning exc) {
				JOptionPane.showMessageDialog(null, getMessageFromWarnings(exc), "SQL Warning",
								JOptionPane.ERROR_MESSAGE);
		} catch(SQLException exc) {
				JOptionPane.showMessageDialog(null, getMessageFromWarnings(exc), "SQL Exception",
								JOptionPane.ERROR_MESSAGE);
		} catch(Throwable exc) {
				JOptionPane.showMessageDialog(null, exc.toString(), "Java Exception",
								JOptionPane.ERROR_MESSAGE);
		}
		if ( conn == null ) { return(false); } else { return(true); };
	}

	/**
	 * Shows the Login Box.
	 * @return <code>true</code> if the login was successful.
	 */
	public boolean showLoginBox() {

		boolean bresult = false;

		Object[]      message = new Object[5];
		JTextField name = new JTextField(userName);
		JPasswordField password = new JPasswordField();

		JOptionPane pane = new JOptionPane();

		pane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		// TODO: How can I set a default button if I'm not the one
		// defining the buttons?
		// pane.setInitialSelectionValue( JOptionPane.OK_OPTION );
		message[0] = databaseSpec.getPoliteName()+" ("+databaseSpec.getHostName()+")"
				+"\nDatabase: "+currentCatalog;
		message[1] = "User Name:";
		message[2] = name;
		message[3] = "Password: ";
		message[4] = password;
		pane.setMessage(message);

		JDialog dialog = pane.createDialog(null, "Database Login");
		dialog.show();

		Object theValue = pane.getValue();
		int theintValue;

		theintValue = JOptionPane.CANCEL_OPTION;

		if (theValue instanceof Integer)
				theintValue = ((Integer)theValue).intValue();

		if ( theintValue == JOptionPane.OK_OPTION ) {
				bresult = true;
		}

		if (bresult) {
			userName = name.getText();
			passWord = new String(password.getPassword());
		}
		return(bresult);
	}

	public String toString() {
		if ( databaseSpec.getPoliteName() == null ) return "Unknown Server";
			return(databaseSpec.getPoliteName());
	}

	public ConnectionConfig getSpec() { return databaseSpec; }

   /**
	* Get a connection pool for this Process
	*/
   public synchronized ConnectionPool getPool() {
	  if ( pool == null ) {
		 pool = new ConnectionPool(databaseSpec.getPoliteName(), connProps);
	  }
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

}
