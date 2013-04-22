package com.pgis.bus.data;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionManager {

	Connection getConnection();

	/**
	 * Возвращает внешнее подключение. Объект, который имеет доступ к данному объекту не должен закрывать его. За
	 * закрытие данного подлючеия отвечает другой объект.
	 */
	Connection getExternConnection();

	void closeConnection(Connection c);

	void commit(Connection transactConnection) throws SQLException;

	void rollback(Connection transactConnection) throws SQLException;

	void dispose();

}
