package test.com.pgis.data;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.postgresql.ds.PGPoolingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.DBConnectionManager;
import com.pgis.bus.data.IDBConnectionManager;

public class TestDBConnectionManager implements IDBConnectionManager {
	private javax.sql.DataSource source = null;
	private final Logger log = LoggerFactory
			.getLogger(TestDBConnectionManager.class);
	
	public static IDBConnectionManager create(){
		PGPoolingDataSource source = DBConnectionManager.createPGPoolingDataSource("jdbc:postgresql",
				"localhost", "bus.test", "postgres", "postgres");
		IDBConnectionManager dbConnectionManager = new TestDBConnectionManager(source);
		return dbConnectionManager;
	}
	public TestDBConnectionManager(javax.sql.DataSource source2) {
		this.source = source2;
		
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
				c.setAutoCommit(false);
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
			if (c != null){
				c.rollback();
				c.close();
			}
		} catch (SQLException e) {
			log.debug(e.toString());
		}
	}

	/**
	 * Очистить фабрику
	 */
	public void free() {
		this.source = null;
		log.debug("destroy DB-pool: ok");
	}


	public javax.sql.DataSource getSource() {
		return source;
	}
	
	
}
