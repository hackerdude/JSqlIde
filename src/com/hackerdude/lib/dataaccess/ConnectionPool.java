/**
 * Title:        Code Repository Project<p>
 * Description:  The code repository is a web application
 * that keeps hints and tips in a hierarchical fashion.
 * The user can search the repository in several different ways,
 * and the repository can contain any data, along with a
 * description of the data. Most of the time the data will be code.
 * Eventually the web application should be able to do syntax
 * highlighting on-the-fly when showing the code.
 * <p>
 * Copyright:    Copyright (c) David Martinez<p>
 * Company:      <p>
 * @author David Martinez
 * @version 1.0
 */
package com.hackerdude.lib.dataaccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;


/**
 * The ConnectionPool is basically a class that manages simultaneous connections across multiple
 * threads
 *
 * This is done by keeping a busy flag that is raised when you call {@link getConnection} and lowered
 * when you call {@link releaseConnection}.
 *
 * {@link getConnection} <B>will return a null if all the connections are busy</B> and the maximum connections
 * value has been exceeded. The calling program should know enough to check for this and return
 * a "Server too busy" message or something similar.
 */
public class ConnectionPool {

  private Vector pooledConnections = new Vector();
  private int maxConnections = 10;
  private ConnectionFactory connectionFactory;

  public String poolName;

  /**
  * Return the maximum number of connections this class is configured
  * to support.
  */
  public int getMaxConnections() { return (maxConnections); }
  /**
  * Changes the maximum number of connections this class is configured
  * to support.
  */
  public synchronized void setMaxConnections( int max ) { maxConnections = max; }

  /** When creating a connection pool you can give it a name. In the future
   * I might create a final list of connection pools for an application. For now
   * simply give a name to each connection pool you want to use.
   */
  public ConnectionPool(String name, ConnectionFactory factory) {
	poolName = name;
	this.connectionFactory = factory;
  }

  /** Use this methods to add connections to the list manually.
   * The pooler will also be able to generate connections
   * automatically, both as needed and at the class creation time.
   */
  public synchronized PooledConnection addConnection(Connection c) {
	PooledConnection result = new PooledConnection(c);
	pooledConnections.add(result);
	return(result);
  }

  /** Every time you want a connection, call this function instead of creating one.
   * This is the magic function that will find you an unused connection from the
   * pool and give it to you. When you're done, make sure you release the
   * connection.
   */
  public synchronized Connection getConnection() throws SQLException {
	Enumeration e = pooledConnections.elements();
	PooledConnection p = null;
	int elements = 0;
	while ( e.hasMoreElements() ) {
	  elements++;
	  p = (PooledConnection)e.nextElement();
	  if ( ! p.isBusy ) {
		p.isBusy = true;
		break;
	  }
	}
	// Create more connections as needed, until maxConnections hit.
    if ( p == null && elements < maxConnections ) {
      Connection c = connectionFactory.createConnection();
      p = addConnection( c );
    }
	if ( p == null ) return null;

	return p.connection;
  }

  /**
   * This finds out if a connection is in the pool and releases it.
   * it returns null if the system was able to release it.
   */
  public synchronized Connection releaseConnection(Connection c) {
	if ( c == null ) return null;
	Enumeration e = pooledConnections.elements();
	PooledConnection p = null;
	while ( e.hasMoreElements() ) {
	  p = (PooledConnection)e.nextElement();
	  if ( p.connection == c ) {
		p.isBusy = false;
		break;
	  }
	}
	return ( null );

  }

  public synchronized void disconectAll() {
	Enumeration e = pooledConnections.elements();
	PooledConnection p = null;
	while ( e.hasMoreElements() ) {
		  p = (PooledConnection)e.nextElement();
		  if ( p.connection != null ) {
			try {
				p.connection.close();
			} catch ( SQLException exc ) {}
		  }
	}
	pooledConnections.clear();
  }

  public Enumeration elements() {
	return pooledConnections.elements();
  }

  /**
   * This <I>private</I> class is what keeps the busy information for each
   * connection. It could be extended.
   */
  private class PooledConnection {
	public boolean isBusy;

	public Connection connection;

	public PooledConnection(Connection c) {
	  isBusy = false;
	  connection = c;
	}


  }
}