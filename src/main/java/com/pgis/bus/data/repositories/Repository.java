package com.pgis.bus.data.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.orm.Language;
import com.pgis.bus.data.orm.User;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.impl.CitiesRepository;

public class Repository {
	private static final Logger log = LoggerFactory.getLogger(Repository.class);

	public static Connection getConnection() throws RepositoryException {
		Connection conn = DBConnectionFactory.getConnection();
		if (conn == null) {
			throw new RepositoryException(
					RepositoryException.err_enum.c_connection_invalid);
		}
		return conn;
	}

	public HashMap<String, String> getStringValues(int string_key)
			throws RepositoryException {
		HashMap<String, String> values = null;

		Connection conn = Repository.getConnection();
		User user = null;
		try {
			// Statement sql = (Statement) conn.createStatement();
			String query = "select * from bus.string_values where key_id = ? ";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, string_key);
			ResultSet key = ps.executeQuery();
			values = new HashMap<String, String>();
			while (key.next()) {
				String lang_id, value;
				lang_id = key.getString("lang_id");
				value = key.getString("value");
				values.put(lang_id, value);
			}
		} catch (SQLException e) {
			user = null;
			log.error("can not read database", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			DBConnectionFactory.closeConnection(conn);
		}

		return values;
	}

}
