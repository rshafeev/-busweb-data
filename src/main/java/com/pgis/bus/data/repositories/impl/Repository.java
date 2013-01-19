package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.repositories.IRepository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.RepositoryException.err_enum;

public class Repository implements IRepository {
	private static final Logger log = LoggerFactory.getLogger(Repository.class);
	protected IDBConnectionManager connManager;
	protected Connection connection;
	protected boolean isClosed;
	protected boolean isCommited;

	protected Repository() {
		this.connManager = connManager;
		connection = null;
		isClosed = true;
		isCommited = true;
	}
	
	public Repository(IDBConnectionManager connManager) {
		this.connManager = connManager;
		connection = null;
		isClosed = true;
		isCommited = true;
	}

	protected void throwable(Exception e, err_enum err)
			throws RepositoryException {
		if (e instanceof RepositoryException)
			throw (RepositoryException) e;
		else
			throw new RepositoryException(err);
	}

	protected void throwable(Exception e, err_enum err, String text)
			throws RepositoryException {
		if (e instanceof RepositoryException)
			throw (RepositoryException) e;
		else
			throw new RepositoryException(err, text);
	}

	protected void rollback(Connection c) throws RepositoryException {
		try {
			if (c != null && !c.isClosed())
				c.rollback();
			throw new RepositoryException(
					RepositoryException.err_enum.c_transaction_err);
		} catch (SQLException sqx) {
		}
	}

	protected void commit(Connection c) throws SQLException {
		if (c != null && this.isCommited)
			c.commit();
	}

	protected Connection getConnection() throws RepositoryException {
		if (connection != null)
			return connection;
		Connection conn = connManager.getConnection();
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

	protected void closeConnection(Connection c) {
		if (isClosed && c != null)
			connManager.closeConnection(c);
	}

}
