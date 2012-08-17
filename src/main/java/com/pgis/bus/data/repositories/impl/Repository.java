package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Language;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.orm.User;
import com.pgis.bus.data.repositories.IRepository;
import com.pgis.bus.data.repositories.RepositoryException;

public class Repository implements IRepository {
	private static final Logger log = LoggerFactory.getLogger(Repository.class);
	protected Connection connection;
	protected boolean isClosed;
	protected boolean isCommited;

	protected static Connection getConnection() throws RepositoryException {
		Connection conn = DBConnectionFactory.getConnection();
		try {
			if (conn == null) {
				throw new RepositoryException(
						RepositoryException.err_enum.c_connection_invalid);
			}
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			log.error("getConnection() exception: ", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_connection_invalid);
		}

		return conn;
	}
}
