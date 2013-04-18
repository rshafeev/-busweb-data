package com.pgis.bus.data.repositories.orm.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.repositories.Repository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.orm.IStringValuesRepository;

public class StringValuesRepository extends Repository implements IStringValuesRepository {
	private static final Logger log = LoggerFactory.getLogger(StringValuesRepository.class);

	public StringValuesRepository(IDBConnectionManager connManager) {
		super(connManager);
	}

	public StringValuesRepository(IDBConnectionManager connManager, boolean isCommited) {
		super(connManager, isCommited);
	}

	protected StringValuesRepository(IDBConnectionManager connManager, Connection c, boolean isClosed,
			boolean isCommited) {
		super(connManager, isCommited);
		super.isClosed = isClosed;
		super.connection = c;
	}

	@Override
	public Collection<StringValue> get(int string_key) throws RepositoryException {
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
				stringValue.setId(key.getInt("id"));
				stringValue.setKeyID(string_key);
				stringValue.setLangID(key.getString("lang_id"));
				stringValue.setValue(key.getString("value"));
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
	public HashMap<String, StringValue> getToHashMap(int string_key) throws RepositoryException {
		HashMap<String, StringValue> map = new HashMap<String, StringValue>();
		Collection<StringValue> arr = get(string_key);
		for (StringValue s : arr) {
			map.put(s.getLangID(), s);
		}
		return map;
	}

	@Override
	public void remove(int string_key) throws RepositoryException {

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
	public void insert(StringValue value) throws RepositoryException {
		Connection c = super.getConnection();
		try {
			String query = "INSERT INTO bus.string_values (key_id,lang_id,value) VALUES(?,bus.lang_enum(?),?) RETURNING id;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, value.getKeyID());
			ps.setString(2, value.getLangID());
			ps.setString(3, value.getValue());
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				value.setId(key.getInt(1));
			}
			super.commit(c);
		} catch (SQLException e) {
			super.rollback(c);
			log.error("insertStringValue() exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
	}

	@Override
	public void update(int string_key, Collection<StringValue> values) throws RepositoryException {
		Connection c = super.getConnection();
		try {
			StringValuesRepository rep = new StringValuesRepository(super.connManager, c, false, false);
			rep.remove(string_key);
			for (StringValue v : values) {
				// Если значение новое, то key_id может быть пустым
				v.setKeyID(string_key);
				rep.insert(v);
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
