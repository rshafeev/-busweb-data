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

		Connection c = super.getConnection();

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
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}

		return values;
	}

	@Override
	public HashMap<String, StringValue> getStringValuesToHashMap(int string_key)
			throws RepositoryException {

		HashMap<String, StringValue> map = new HashMap<String, StringValue>();
		Collection<StringValue> arr = getStringValues(string_key);
		for (StringValue s : arr) {
			map.put(s.lang_id, s);
		}
		return map;
	}

	@Override
	public void deleteStringValues(int string_key) throws RepositoryException {

		Connection c = super.getConnection();
		try {
			// Statement sql = (Statement) conn.createStatement();
			String query = "DELETE FROM bus.string_values WHERE key_id = ? ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, string_key);
			ps.execute();
			super.commit(c);
		} catch (SQLException e) {
			super.rollback(c);
			log.error("deleteStringValues() exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}

	}

	@Override
	public int insertStringValue(StringValue value) throws RepositoryException {
		Connection c = super.getConnection();
		int id = -1;
		try {
			String query = "INSERT INTO bus.string_values (key_id,lang_id,value) VALUES(?,bus.lang_enum(?),?) RETURNING id;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, value.key_id);
			ps.setString(2, value.lang_id);
			ps.setString(3, value.value);
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				id = key.getInt(1);
			}
			super.commit(c);
		} catch (SQLException e) {
			super.rollback(c);
			log.error("insertStringValue() exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return id;
	}

	@Override
	public void updateStringValues(int string_key,
			Collection<StringValue> values) throws RepositoryException {
		Connection c = super.getConnection();

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
			super.commit(c);

		} catch (SQLException | RepositoryException e) {
			super.rollback(c);
			log.error("updateStringValues() exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}

	}

}
