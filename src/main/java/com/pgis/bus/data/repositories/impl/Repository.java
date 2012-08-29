package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.SQLException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.repositories.IRepository;
import com.pgis.bus.data.repositories.RepositoryException;

public class Repository implements IRepository {
	private static final Logger log = LoggerFactory.getLogger(Repository.class);
	protected Connection connection;
	protected boolean isClosed;
	protected boolean isCommited;

	public Repository() {
		connection = null;
		isClosed = true;
		isCommited = true;
	}

	public static Connection getConnection() throws RepositoryException {
		Connection conn = DBConnectionFactory.getConnection();
		try {
			if (conn == null) {
				throw new RepositoryException(
						RepositoryException.err_enum.c_connection_invalid);
			}
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			log.error("getConnection() exception: ", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_connection_invalid);
		}

		return conn;
	}
}
