package com.hackerdude.lib.dataaccess;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Classes implementing this interface can create connections
 * @author David Martinez
 * @version 1.0
 */
public interface ConnectionFactory {
	public Connection createConnection() throws SQLException;
}