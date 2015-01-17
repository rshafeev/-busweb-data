package test.com.pgis.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.postgresql.ds.PGPoolingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.PoolConnectionManager;
import org.springframework.core.io.ClassPathResource;

public class TestDBConnectionManager implements IConnectionManager {
	private javax.sql.DataSource source = null;
	private final Logger log = LoggerFactory.getLogger(TestDBConnectionManager.class);
	private Connection connection = null;
	private int useConnections = 0;
	private static String db;
	private static String localhost;
	private static String dbname;
	private static String login;
	private static String password;

	public int getInitialConnections() {
		return useConnections;
	}

	public static IConnectionManager create() throws IOException {

		Properties props = new Properties();
		File settingsFile = new ClassPathResource("/dbsettings.properties").getFile();
		props.load(new FileInputStream(settingsFile));
		db = props.getProperty("db");
		localhost = props.getProperty("localhost");
		dbname = props.getProperty("dbname");
		login = props.getProperty("login");
		password = props.getProperty("password");
		PGPoolingDataSource source = PoolConnectionManager.createPGPoolingDataSource(db, localhost,
				dbname, login, password);
		IConnectionManager dbConnectionManager = new TestDBConnectionManager(source);
		return dbConnectionManager;
	}

	public TestDBConnectionManager(javax.sql.DataSource source2) {
		this.source = source2;

	}

	public void setSource(javax.sql.DataSource source) {
		this.source = source;
	}

	/**
	 * Извлечь подключение из пула Обратите внимание! После окончания работы с подключением, нужно обязательно вызвать
	 * функцию DBConnectionFactory::closeConnection(c)
	 * 
	 * @return Connection
	 */
	public Connection getConnection() {
		useConnections++;
		log.debug("create new connection");
		/*
		 * if (connection != null) return connection;
		 */
		if (source == null) {
			return null;
		}
		int iter = 0;
		do {
			try {
				connection = this.source.getConnection();

				connection.setAutoCommit(false);
				return connection;

			} catch (SQLException e) {
				log.debug(e.toString());
			}
		} while (iter < 3);
		return null;

	}

	/**
	 * Положить подключение обратно в пул
	 * 
	 * @param c - объект подключения
	 */
	public void closeConnection(Connection c) {
		log.debug("close connection");
		useConnections--;
	}

	/**
	 * Очистить фабрику
	 * 
	 * @throws SQLException
	 */
	public void free() {

	}

	public javax.sql.DataSource getSource() {
		return source;
	}

	@Override
	public void commit(Connection transactConnection) {

	}

	@Override
	public void rollback(Connection transactConnection) {
		try {
			if (connection != null) {
				connection.rollback();
			}
		} catch (SQLException e) {
			log.debug(e.toString());
		}
	}

	@Override
	public void dispose() {
		this.source = null;
		log.debug("destroy DB-pool: ok");
		if (useConnections > 0)
			log.error("Uses connections: {}", useConnections);
		try {
			if (connection != null) {
				connection.rollback();
				connection.close();
			}
		} catch (SQLException e) {
			log.debug(e.toString());
		}
	}

	@Override
	public Connection getExternConnection() {
		return null;
	}

}
