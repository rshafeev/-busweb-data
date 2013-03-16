package com.pgis.bus.data;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.DBConnectionManager;
import com.pgis.bus.data.IDBConnectionManager;

public class DBConnectionFactory {
	static private final Logger log = LoggerFactory
			.getLogger(DBConnectionFactory.class);

	static private IDBConnectionManager dbConnectionManager;
	/**
	 * Инициализирует фабрику подключений к БД
	 * 
	 * @param dataSourceName
	 *            - JNDI имя пула подключений (например: myPool/jdbc)
	 */
	public synchronized static void init(String dataSourceName) {
		try {
			String prefix = "java:/comp/env";
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup(prefix);
			javax.sql.DataSource source = (javax.sql.DataSource) envContext.lookup(dataSourceName);
			dbConnectionManager = new DBConnectionManager(source);
			log.debug("DataSource created: ok");

		} catch (NamingException e) {
			log.debug("DataSource created: false");
			log.debug(e.toString(true));
		}
	}
	public synchronized static void init(
			IDBConnectionManager dbConnectionManager) {
		DBConnectionFactory.dbConnectionManager = dbConnectionManager;
	}

	/**
	 * Извлечь подключение из пула Обратите внимание! После окончания работы с
	 * подключением, нужно обязательно вызвать функцию
	 * DBConnectionFactory::closeConnection(c)
	 * 
	 * @return Connection
	 */
	public synchronized static IDBConnectionManager getConnectionManager() {
		return dbConnectionManager;
	}

	/**
	 * Положить подключение обратно в пул
	 * 
	 * @param c
	 *            объект подключения
	 */
	public static void closeConnection(Connection c) {
		dbConnectionManager.closeConnection(c);
	}
	/**
	 * Очистить фабрику
	 */
	public synchronized static void free() {
		if (dbConnectionManager.getSource() instanceof org.apache.tomcat.jdbc.pool.DataSource)
			((org.apache.tomcat.jdbc.pool.DataSource) dbConnectionManager.getSource()).close();
		dbConnectionManager.free();
	}
}
