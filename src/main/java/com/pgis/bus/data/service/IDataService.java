package com.pgis.bus.data.service;

import java.sql.SQLException;

public interface IDataService {
	/**
	 * Выполняет commit соединения
	 * 
	 * @throws SQLException
	 */
	void commit() throws SQLException;

	/**
	 * Выполняет rallback соединения
	 * 
	 * @throws SQLException
	 */
	void rollback();

	/**
	 * Закрывает текущее соединение и освобождает ресурсы объекта
	 */
	void dispose();
}
