package com.pgis.bus.data.service.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.PoolConnectionManager;

public class ServiceConnectionManager implements IConnectionManager {
	private final Logger log = LoggerFactory.getLogger(ServiceConnectionManager.class);
	private Connection externConnection = null;
	private boolean disposedExternConnection = false;
	private IConnectionManager rootConnectionManager = null;

	public ServiceConnectionManager(IConnectionManager rootConnectionManager) {
		this.rootConnectionManager = rootConnectionManager;
	}

	@Override
	public Connection getConnection() {
		return rootConnectionManager.getConnection();
	}

	@Override
	public void dispose() {
		rootConnectionManager.dispose();
		this.disposeExternConnection();
	}

	@Override
	public void closeConnection(Connection c) {
		rootConnectionManager.closeConnection(c);
	}

	@Override
	public void commit(Connection transactConnection) throws SQLException {
		rootConnectionManager.commit(transactConnection);

	}

	@Override
	public void rollback(Connection transactConnection) throws SQLException {
		rootConnectionManager.rollback(transactConnection);

	}

	@Override
	public Connection getExternConnection() {
		if (disposedExternConnection == true)
			return null;
		boolean isClosed = true;
		try {
			if (externConnection != null)
				isClosed = externConnection.isClosed();
		} catch (SQLException e) {
		}
		if (isClosed == true)
			this.externConnection = this.getConnection();
		return externConnection;
	}

	public void closeExternConnection() {
		this.rootConnectionManager.closeConnection(externConnection);
		this.externConnection = null;
	}

	public void disposeExternConnection() {
		disposedExternConnection = true;
		this.rootConnectionManager.closeConnection(externConnection);
		this.externConnection = null;
	}

}
