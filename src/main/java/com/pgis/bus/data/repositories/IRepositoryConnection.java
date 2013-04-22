package com.pgis.bus.data.repositories;

import java.sql.Connection;
import java.sql.SQLException;

import com.pgis.bus.data.IConnectionManager;

public interface IRepositoryConnection {
	/**
	 * Выполняет сохранение всех изменений в репозитории. (В случае, если используется внешнее соединение, то commit не
	 * выполняется.)
	 * 
	 * @throws SQLException
	 */
	void commit() throws SQLException;

	void rollback() throws SQLException;

	void setConnectionManager(IConnectionManager connManager) throws SQLException;

	void setRepositoryExternConnection(Connection c);

	/**
	 * Установить использование только внешнего соединения, установленного функцией setRepositoryExternConnection() или
	 * полученного из менеджера соединений функцией getExternConnection()
	 * 
	 * @param val Истина - использовать только внешненее соединение, Ложь - как внешнее, так и соединение из менеджера
	 *            подключений
	 */
	void useOnlyExternConnection(boolean val);
}
