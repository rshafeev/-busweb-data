package com.pgis.bus.data.service.impl;

import java.sql.Connection;
import java.sql.SQLException;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.exp.RepositoryException;
import com.pgis.bus.data.service.IDataService;

public class DataService implements IDataService {
	protected ServiceConnectionManager connectionManager = null;

	public DataService(IConnectionManager rootConnectionManager) throws SQLException {
		if (rootConnectionManager == null)
			throw new NullPointerException();
		this.connectionManager = new ServiceConnectionManager(rootConnectionManager);

	}

	@Override
	public void commit() throws RepositoryException {
		Connection c = this.connectionManager.getExternConnection();
		try {
			if (c != null) {
				c.commit();
			}
		} catch (SQLException sqx) {
		}

	}

	@Override
	public void rollback() {
		try {
			Connection c = this.connectionManager.getExternConnection();
			if (c != null) {
				c.rollback();
			}
		} catch (SQLException sqx) {
		} finally {

		}

	}

	@Override
	public void dispose() {
		this.connectionManager.disposeExternConnection();
	}
}
