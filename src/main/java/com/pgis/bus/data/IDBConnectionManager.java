package com.pgis.bus.data;

import java.sql.Connection;

public interface IDBConnectionManager {
	
	Connection getConnection();
	void closeConnection(Connection c);
	void free();
	javax.sql.DataSource getSource();
}
