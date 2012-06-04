package com.pgis.bus.data.repositories;

import java.sql.Connection;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.repositories.RepositoryException;
public class Repository {

	public static Connection getConnection() throws RepositoryException {
		Connection conn = DBConnectionFactory.getConnection();
		if (conn == null) {
			throw new RepositoryException(
					RepositoryException.err_enum.c_connection_invalid);
		}
		return conn;
	}

}
