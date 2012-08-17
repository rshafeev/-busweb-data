package com.pgis.bus.data;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnectionFactory {

	static private IDBConnectionManager dbConnectionManager;

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
	public synchronized static Connection getConnection() {
		return dbConnectionManager.getConnection();
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
		dbConnectionManager.free();
	}
}
