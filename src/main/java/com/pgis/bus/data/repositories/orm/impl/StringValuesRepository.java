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

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.exp.RepositoryException;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.orm.IStringValuesRepository;

public class StringValuesRepository extends Repository implements IStringValuesRepository {
	private static final Logger log = LoggerFactory.getLogger(StringValuesRepository.class);

	public StringValuesRepository(IConnectionManager connManager) {
		super(connManager);
	}

	@Override
	public Collection<StringValue> get(int string_key) throws RepositoryException {
		ArrayList<StringValue> values = null;

		try {
			Connection c = super.getConnection();
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
			super.handeThrowble(e);
		}

		return values;
	}

	@Override
	public HashMap<LangEnum, StringValue> getToHashMap(int string_key) throws RepositoryException {
		HashMap<LangEnum, StringValue> map = new HashMap<LangEnum, StringValue>();
		Collection<StringValue> arr = this.get(string_key);
		for (StringValue s : arr) {
			map.put(s.getLangID(), s);
		}
		return map;
	}

	@Override
	public void remove(int string_key) throws RepositoryException {

		try {
			Connection c = super.getConnection();
			String query = "DELETE FROM bus.string_values WHERE key_id = ? ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, string_key);
			ps.execute();
		} catch (SQLException e) {
			super.handeThrowble(e);
		}

	}

	@Override
	public void insert(StringValue value) throws RepositoryException {

		try {
			Connection c = super.getConnection();
			String query = "INSERT INTO bus.string_values (key_id,lang_id,value) VALUES(?,bus.lang_enum(?),?) RETURNING id;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, value.getKeyID());
			ps.setString(2, value.getLangID().name());
			ps.setString(3, value.getValue());
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				value.setId(key.getInt(1));
			}
		} catch (SQLException e) {
			super.handeThrowble(e);
		}
	}

	@Override
	public void update(int string_key, Collection<StringValue> values) throws RepositoryException {
		try {
			this.remove(string_key);
			for (StringValue v : values) {
				// Если значение новое, то key_id может быть пустым
				v.setKeyID(string_key);
				this.insert(v);
			}

		} catch (Exception e) {
			super.handeThrowble(e);
		}

	}
}
