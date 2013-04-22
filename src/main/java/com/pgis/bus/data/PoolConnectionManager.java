package com.pgis.bus.data;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.postgresql.ds.PGPoolingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoolConnectionManager implements IConnectionManager {
	private DataSource source = null;
	private final Logger log = LoggerFactory.getLogger(PoolConnectionManager.class);

	public static PGPoolingDataSource createPGPoolingDataSource(String sourceName, String serverName, String dbName,
			String user, String password) {
		PGPoolingDataSource source = PGPoolingDataSource.getDataSource(sourceName);
		if (source != null)
			return source;
		source = new PGPoolingDataSource();
		source.setDataSourceName(sourceName);
		source.setDatabaseName(dbName);
		source.setUser(user);
		source.setPassword(password);
		source.setMaxConnections(100);
		source.setServerName(serverName);
		return source;
	}

	public PoolConnectionManager(PGPoolingDataSource source) {
		this.source = source;
	}

	public PoolConnectionManager(DataSource source) {
		this.source = source;
	}

	/**
	 * Извлечь подключение из пула Обратите внимание! После окончания работы с подключением, нужно обязательно вызвать
	 * функцию DBConnectionFactory::closeConnection(c)
	 * 
	 * @return Connection
	 */
	@Override
	public Connection getConnection() {

		if (source == null) {
			return null;
		}
		int iter = 0;
		do {
			try {
				Connection c = this.source.getConnection();
				if (c != null)
					return c;

			} catch (SQLException e) {
				log.debug(e.toString());
			}
		} while (iter < 3);
		return null;

	}

	@Override
	public Connection getExternConnection() {
		return null;
	}

	/**
	 * Положить подключение обратно в пул
	 * 
	 * @param c - объект подключения
	 */
	@Override
	public void closeConnection(Connection c) {
		try {
			if (c != null)
				c.close();
		} catch (SQLException e) {
			log.debug(e.toString());
		}
	}

	/**
	 * Очистить фабрику
	 */
	public void free() {
		log.debug("destroy DB-pool: ok");
	}

	public javax.sql.DataSource getSource() {
		return source;
	}

	@Override
	public void dispose() {
		if (this.source instanceof PGPoolingDataSource) {
			((PGPoolingDataSource) this.source).close();
		}

	}

	@Override
	public void commit(Connection transactConnection) throws SQLException {
		if (transactConnection != null) {
			transactConnection.commit();
		}
	}

	@Override
	public void rollback(Connection transactConnection) throws SQLException {
		if (transactConnection != null) {
			transactConnection.rollback();
		}
	}

}
