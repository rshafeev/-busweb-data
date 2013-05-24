package com.pgis.bus.data.repositories;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.impl.TransactConnectionException;
import com.pgis.bus.data.repositories.RepositoryException.err_enum;

public class Repository implements IRepository, IRepositoryConnection {
	private static final Logger log = LoggerFactory.getLogger(Repository.class);
	protected IConnectionManager connManager = null;
	protected Connection transactConnection = null;
	protected boolean userOnlyExternConnection = false;
	private Connection repExternConnection = null;

	protected Repository(IConnectionManager connManager) {
		this.connManager = connManager;
	}

	protected void throwable(Exception e, err_enum err) throws RepositoryException {
		if (e instanceof RepositoryException)
			throw (RepositoryException) e;
		else
			throw new RepositoryException(err);
	}

	protected void throwable(Exception e, err_enum err, String text) throws RepositoryException {
		if (e instanceof RepositoryException)
			throw (RepositoryException) e;
		else
			throw new RepositoryException(err, text);
	}

	protected Connection getConnection() throws SQLException {
		// Если задано репозиторию внешнее подключение, используем его
		if (repExternConnection != null)
			return repExternConnection;
		// Если задано внешнее подключение менеджеру подключений, используем его
		Connection mangerExternConnection = this.connManager.getExternConnection();
		if (mangerExternConnection != null) {
			if (transactConnection != null) {
				this.connManager.closeConnection(transactConnection);
				this.transactConnection = null;
			}
			if (mangerExternConnection.isClosed() == true) {
				throw new TransactConnectionException(TransactConnectionException.err_enum.c_received_connect);
			}
			return mangerExternConnection;
		}
		// Если можно использовать только внешние подключения, то выбрасываем исключение
		if (userOnlyExternConnection == true)
			throw new TransactConnectionException(TransactConnectionException.err_enum.c_received_connect);

		if (transactConnection == null || transactConnection.isClosed() == true) {
			transactConnection = connManager.getConnection();
		}
		if (transactConnection == null)
			throw new TransactConnectionException(TransactConnectionException.err_enum.c_received_connect);
		return transactConnection;
	}

	@Override
	public void dispose() {
		try {
			connManager.closeConnection(transactConnection);
			this.transactConnection = null;
		} catch (Exception e) {
		}

	}

	@Override
	public void commit() throws SQLException {
		if (transactConnection == null)
			return;
		try {
			connManager.commit(transactConnection);
		} catch (SQLException e) {
			connManager.closeConnection(transactConnection);
			this.transactConnection = null;
			throw new SQLException(e);
		}

	}

	@Override
	public void rollback() throws SQLException {
		if (transactConnection == null)
			return;
		try {
			connManager.rollback(transactConnection);
		} catch (SQLException e) {
			connManager.closeConnection(transactConnection);
			this.transactConnection = null;
			throw new SQLException(e);
		}
	}

	@Override
	public void setConnectionManager(IConnectionManager connManager) throws SQLException {
		this.connManager.closeConnection(transactConnection);
		this.transactConnection = null;
		this.connManager = connManager;

	}

	@Override
	public void useOnlyExternConnection(boolean val) {
		this.userOnlyExternConnection = val;

	}

	@Override
	public void setRepositoryExternConnection(Connection c) {
		this.repExternConnection = c;
	}

}
