package com.pgis.bus.data.repositories.orm.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.exp.RepositoryException;
import com.pgis.bus.data.exp.RepositoryException.err_enum;
import com.pgis.bus.data.repositories.IRepository;

public class Repository implements IRepository, IRepositoryConnection {
	private static final Logger log = LoggerFactory.getLogger(Repository.class);
	protected IConnectionManager connManager = null;
	protected Connection transactConnection = null;
	protected boolean userOnlyExternConnection = false;
	private Connection repExternConnection = null;

	protected Repository(IConnectionManager connManager) {
		this.connManager = connManager;
	}

	protected void handeThrowble(Exception e) throws RepositoryException {
		log.error("Repository exception: ", e);
		if (e instanceof RepositoryException)
			throw (RepositoryException) e;
		else
			throw new RepositoryException(e);
	}

	protected Connection getConnection() throws RepositoryException {
		try {
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
					throw new RepositoryException(RepositoryException.err_enum.connection_invalid);
				}
				return mangerExternConnection;
			}
			// Если можно использовать только внешние подключения, то выбрасываем исключение
			if (userOnlyExternConnection == true)
				throw new RepositoryException(RepositoryException.err_enum.connection_invalid);

			if (transactConnection == null || transactConnection.isClosed() == true) {
				transactConnection = connManager.getConnection();
			}
			if (transactConnection == null)
				throw new RepositoryException(RepositoryException.err_enum.connection_invalid);
			return transactConnection;
		} catch (Exception e) {
			throw new RepositoryException(RepositoryException.err_enum.connection_invalid);
		}
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
	public void commit() throws RepositoryException {
		if (transactConnection == null)
			return;
		try {
			connManager.commit(transactConnection);
		} catch (Exception e) {
			connManager.closeConnection(transactConnection);
			this.transactConnection = null;
			throw new RepositoryException(RepositoryException.err_enum.commit);
		}

	}

	@Override
	public void rollback() throws RepositoryException {
		if (transactConnection == null)
			return;
		try {
			connManager.rollback(transactConnection);
		} catch (SQLException e) {
			connManager.closeConnection(transactConnection);
			this.transactConnection = null;
			throw new RepositoryException(RepositoryException.err_enum.rollback);
		}
	}

	@Override
	public void setConnectionManager(IConnectionManager connManager) {
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
