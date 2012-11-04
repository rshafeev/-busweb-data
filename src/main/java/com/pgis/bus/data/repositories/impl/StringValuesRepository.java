package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.repositories.IStringValuesRepository;
import com.pgis.bus.data.repositories.RepositoryException;

public class StringValuesRepository extends Repository implements
		IStringValuesRepository {
	private static final Logger log = LoggerFactory
			.getLogger(StringValuesRepository.class);

	public StringValuesRepository() {
		super();
	}

	public StringValuesRepository(Connection c, boolean isClosed,
			boolean isCommited) {
		super();
		this.connection = c;
		this.isClosed = isClosed;
		this.isCommited = isCommited;
	}

	@Override
	public Collection<StringValue> getStringValues(int string_key)
			throws RepositoryException {
		ArrayList<StringValue> values = null;

		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();

		try {
			// Statement sql = (Statement) conn.createStatement();
			String query = "select * from bus.string_values where key_id = ? ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, string_key);
			ResultSet key = ps.executeQuery();
			values = new ArrayList<StringValue>();
			while (key.next()) {
				StringValue stringValue = new StringValue();
				stringValue.id = key.getInt("id");
				stringValue.key_id = string_key;
				stringValue.lang_id = key.getString("lang_id");
				stringValue.value = key.getString("value");
				values.add(stringValue);
			}
		} catch (SQLException e) {

			log.error("can not read database", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}

		return values;
	}

	@Override
	public HashMap<String, StringValue> getStringValuesToHashMap(int string_key)
			throws RepositoryException {

		HashMap<String, StringValue> map = new HashMap<String, StringValue>();
		Collection<StringValue> arr = getStringValues(string_key);
		Iterator<StringValue> i = arr.iterator();
		while (i.hasNext()) {
			StringValue value = i.next();
			map.put(value.lang_id, value);

		}
		return map;
	}

	@Override
	public void deleteStringValues(int string_key) throws RepositoryException {

		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		try {
			// Statement sql = (Statement) conn.createStatement();
			String query = "DELETE FROM bus.string_values WHERE key_id = ? ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, string_key);
			ps.execute();
			if (isCommited)
				c.commit();
		} catch (SQLException e) {
			try {
				log.error("deleteStringValues() exception: ", e);
				c.rollback();
				throw new RepositoryException(
						RepositoryException.err_enum.c_transaction_err);
			} catch (SQLException sqx) {
				throw new RepositoryException(
						RepositoryException.err_enum.c_rollback_err);
			}
		} finally {
			if (this.isClosed)
				DBConnectionFactory.closeConnection(c);
		}

	}

	@Override
	public int insertStringValue(StringValue value) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		try {
			String query = "INSERT INTO bus.string_values (key_id,lang_id,value) VALUES(?,lang_enum(?),?) RETURNING id;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, value.key_id);
			ps.setString(2, value.lang_id);
			ps.setString(3, value.value);
			ResultSet key = ps.executeQuery();
			int id = -1;
			if (key.next()) {
				id = key.getInt(1);
			}
			if (isCommited)
				c.commit();
			return id;
		} catch (SQLException e) {
			try {
				log.error("insertStringValue() exception: ", e);
				c.rollback();
				throw new RepositoryException(
						RepositoryException.err_enum.c_transaction_err);
			} catch (SQLException sqx) {
				throw new RepositoryException(
						RepositoryException.err_enum.c_rollback_err);
			}
		} finally {
			if (this.isClosed)
				DBConnectionFactory.closeConnection(c);
		}

	}

	@Override
	public void updateStringValues(int string_key,
			Collection<StringValue> values) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();

		try {
			StringValuesRepository repository = new StringValuesRepository(c,
					false, false);
			repository.deleteStringValues(string_key);
			Iterator<StringValue> i = values.iterator();
			while (i.hasNext()) {
				StringValue value = i.next();
				// Если значение новое, то key_id может быть пустым
				value.key_id = string_key;
				repository.insertStringValue(value);
			}
			if (isCommited)
				c.commit();

		} catch (SQLException | RepositoryException e) {
			try {
				log.error("updateStringValues() exception: ", e);
				c.rollback();
				throw new RepositoryException(
						RepositoryException.err_enum.c_transaction_err);
			} catch (SQLException sqx) {
				throw new RepositoryException(
						RepositoryException.err_enum.c_rollback_err);
			}
		} finally {
			if (this.isClosed)
				DBConnectionFactory.closeConnection(c);
		}

	}

}
