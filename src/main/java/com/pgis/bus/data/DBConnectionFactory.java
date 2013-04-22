package com.pgis.bus.data;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnectionFactory {
	static private final Logger log = LoggerFactory.getLogger(DBConnectionFactory.class);

	static private IConnectionManager dbConnectionManager;

	/**
	 * Инициализирует фабрику подключений к БД
	 * 
	 * @param dataSourceName - JNDI имя пула подключений (например: myPool/jdbc)
	 */
	public synchronized static void init(String dataSourceName) {
		try {
			String prefix = "java:/comp/env";
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup(prefix);
			javax.sql.DataSource source = (javax.sql.DataSource) envContext.lookup(dataSourceName);
			dbConnectionManager = new PoolConnectionManager(source);
			log.debug("DataSource created: ok");

		} catch (NamingException e) {
			log.debug("DataSource created: false");
			log.debug(e.toString(true));
		}
	}

	public synchronized static void init(IConnectionManager dbConnectionManager) {
		DBConnectionFactory.dbConnectionManager = dbConnectionManager;
	}

	/**
	 * Извлечь подключение из пула Обратите внимание! После окончания работы с подключением, нужно обязательно вызвать
	 * функцию DBConnectionFactory::closeConnection(c)
	 * 
	 * @return Connection
	 */
	public synchronized static IConnectionManager getConnectionManager() {
		return dbConnectionManager;
	}

	/**
	 * Положить подключение обратно в пул
	 * 
	 * @param c объект подключения
	 * @throws SQLException
	 */
	public static void closeConnection(Connection c) throws SQLException {
		dbConnectionManager.closeConnection(c);
	}

	/**
	 * Очистить фабрику
	 */
	public synchronized static void dispose() {
		dbConnectionManager.dispose();

	}
}
