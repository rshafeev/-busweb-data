package com.pgis.bus.data.service;

import java.sql.SQLException;

import com.pgis.bus.data.exp.RepositoryException;

public interface IDataService {
	/**
	 * Выполняет commit соединения
	 * 
	 * @throws RepositoryException
	 */
	void commit() throws RepositoryException;

	/**
	 * Выполняет rallback соединения
	 * 
	 */
	void rollback();

	/**
	 * Закрывает текущее соединение и освобождает ресурсы объекта
	 */
	void dispose();
}
