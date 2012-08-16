package test.com.pgis.data;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IDBConnectionManager;

public class TestDBConnectionManager implements IDBConnectionManager {
	private javax.sql.DataSource source = null;
	private final Logger log = LoggerFactory
			.getLogger(TestDBConnectionManager.class);
	private String dataSourceName = "";

	public TestDBConnectionManager(javax.sql.DataSource source2) {
		this.source = source2;
		
	}

	/**
	 * Инициализирует фабрику подключений к БД
	 * 
	 * @param dataSourceName
	 *            - JNDI имя пула подключений (например: myPool/jdbc)
	 */
	public TestDBConnectionManager(String dataSourceName) {
		try {
			this.dataSourceName = dataSourceName;
			String prefix = "java:/comp/env";
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup(prefix);
			source = (javax.sql.DataSource) envContext.lookup(dataSourceName);
			log.debug("DB-pool created: ok");

		} catch (NamingException e) {
			log.debug("DB-pool created: false");
			log.debug(e.toString(true));
		}
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
			if (c != null){
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
		if (this.source != null) {
			if (this.source instanceof org.apache.tomcat.jdbc.pool.DataSource)
				((org.apache.tomcat.jdbc.pool.DataSource) this.source).close();
			this.source = null;
			log.debug("destroy DB-pool: ok");
		}
	}
}
