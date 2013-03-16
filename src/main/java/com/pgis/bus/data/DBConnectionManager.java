package com.pgis.bus.data;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.postgresql.ds.PGPoolingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnectionManager implements IDBConnectionManager {
	private javax.sql.DataSource source = null;
	private final Logger log = LoggerFactory
			.getLogger(DBConnectionManager.class);

	public static PGPoolingDataSource createPGPoolingDataSource(
			String sourceName, String serverName, String dbName, String user,
			String password) {
		PGPoolingDataSource source = PGPoolingDataSource
				.getDataSource(sourceName);
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

	public DBConnectionManager(DataSource source) {
		this.source = source;
	}

	/**
	 * Извлечь подключение из пула Обратите внимание! После окончания работы с
	 * подключением, нужно обязательно вызвать функцию
	 * DBConnectionFactory::closeConnection(c)
	 * 
	 * @return Connection
	 */
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

	/**
	 * Положить подключение обратно в пул
	 * 
	 * @param c
	 *            - объект подключения
	 */
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

}
