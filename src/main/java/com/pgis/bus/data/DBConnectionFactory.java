package com.pgis.bus.data;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnectionFactory {
	private static javax.sql.DataSource source = null;
	private static final Logger log = LoggerFactory
			.getLogger(DBConnectionFactory.class);
	private static String dataSourceName = "";

	public synchronized static void init(javax.sql.DataSource source2) {
		DBConnectionFactory.source = source2;
	}

	public synchronized static void init(String dataSourceName) {
		try {
			DBConnectionFactory.dataSourceName = dataSourceName;
			String prefix = "java:/comp/env";
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup(prefix);
			source = (javax.sql.DataSource ) envContext.lookup(dataSourceName);

			log.debug("DB-pool created: ok");

		} catch (NamingException e) {
			log.debug("DB-pool created: false");
			log.debug(e.toString(true));
		}
	}

	public synchronized static Connection getConnection() {

		if (source == null) {
			init(DBConnectionFactory.dataSourceName);
			if (source == null)
				return null;
		}
		int iter = 0;
		do {
			try {
				Connection c = DBConnectionFactory.source.getConnection();
				if (c != null)
					return c;

			} catch (SQLException e) {
				log.debug(e.toString());
			}
		} while (iter < 3);
		return null;

	}

	public static void closeConnection(Connection c) {
		try {
			if (c != null)
				c.close();
		} catch (SQLException e) {
			log.debug(e.toString());
		}
	}

	public synchronized static void close() {
		if (DBConnectionFactory.source != null) {
			if (DBConnectionFactory.source instanceof org.apache.tomcat.jdbc.pool.DataSource)
				((org.apache.tomcat.jdbc.pool.DataSource) DBConnectionFactory.source).close();
			DBConnectionFactory.source = null;
			log.debug("destroy DB-pool: ok");
		}
	}
}
