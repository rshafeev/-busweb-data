package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.repositories.ICitiesRepository;
import com.pgis.bus.data.repositories.IStringValuesRepository;
import com.pgis.bus.data.repositories.RepositoryException;

public class CitiesRepository extends Repository implements ICitiesRepository {
	private static final Logger log = LoggerFactory
			.getLogger(CitiesRepository.class);

	public CitiesRepository() {
		super();
	}

	public CitiesRepository(Connection c, boolean isClosed, boolean isCommited) {
		super();
		this.connection = c;
		this.isClosed = isClosed;
		this.isCommited = isCommited;
	}

	@Override
	public Collection<City> getAllCities() throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		Collection<City> cities = null;

		try {
			String query = "select * from bus.cities";
			PreparedStatement ps = c.prepareStatement(query);
			ResultSet key = ps.executeQuery();
			cities = new ArrayList<City>();
			IStringValuesRepository stringValuesRepository = new StringValuesRepository();
			while (key.next()) {
				City city = new City();
				city.id = key.getInt("id");
				city.lat = key.getDouble("lat");
				city.lon = key.getDouble("lon");
				city.name_key = key.getInt("name_key");
				city.scale = key.getInt("scale");
				city.isShow = key.getBoolean("is_show");
				city.name = stringValuesRepository
						.getStringValuesToHashMap(city.name_key);
				cities.add(city);
			}
		} catch (SQLException e) {
			cities = null;
			log.error("can not read database", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}
		return cities;
	}

	@Override
	public City getCityByID(int id) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();

		try {
			String query = "select * from bus.cities where id = ?";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setDouble(1, id);
			ResultSet key = ps.executeQuery();
			IStringValuesRepository stringValuesRepository = new StringValuesRepository();
			if (key.next()) {
				City city = new City();
				city.id = key.getInt("id");
				city.lat = key.getDouble("lat");
				city.lon = key.getDouble("lon");
				city.name_key = key.getInt("name_key");
				city.scale = key.getInt("scale");
				city.isShow = key.getBoolean("is_show");
				city.name = stringValuesRepository
						.getStringValuesToHashMap(city.name_key);
				return city;
			}
		} catch (SQLException e) {
			log.error("can not read database", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}
		return null;
	}

	@Override
	public City getCityByName(String lang_name, String name)
			throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
			
		try {
			String query = "SELECT bus.cities.id as id,bus.cities.name_key as name_key,bus.cities.lat as lat, bus.cities.is_show as is_show, ";
			query += "bus.cities.lon as lon,bus.cities.scale as scale,bus.string_values.lang_id as lang_id,";
			query += "bus.string_values.value as value FROM bus.cities JOIN bus.string_keys ON ";
			query += "bus.cities.name_key = bus.string_keys.id JOIN bus.string_values ON ";
			query += "bus.string_keys.id = bus.string_values.key_id WHERE value=? and lang_id=lang_enum(?)";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, lang_name);

			ResultSet key = ps.executeQuery();
			IStringValuesRepository stringValuesRepository = new StringValuesRepository(
					c, false, false);
			if (key.next()) {
				City city = new City();
				city.id = key.getInt("id");
				city.lat = key.getDouble("lat");
				city.lon = key.getDouble("lon");
				city.name_key = key.getInt("name_key");
				city.scale = key.getInt("scale");
				city.isShow = key.getBoolean("is_show");
				city.name = stringValuesRepository
						.getStringValuesToHashMap(city.name_key);
				return city;
			}
		} catch (SQLException e) {
			log.error("can not read database", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (this.isClosed)
				DBConnectionFactory.closeConnection(c);
		}
		return null;
	}

	@Override
	public City insertCity(City city) throws RepositoryException {
		Connection c = this.connection;
		City responceCity = null;
		if (c == null)
			c = Repository.getConnection();
		try {
			c.setAutoCommit(false);
			String query = "INSERT INTO bus.cities (lat,lon,scale,is_show) VALUES(?,?,?,pg_catalog.bit(?)) RETURNING  id,name_key";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setDouble(1, city.lat);
			ps.setDouble(2, city.lon);
			ps.setInt(3, city.scale);
			ps.setString(4, Integer.toString(city.isShow? 1 : 0)); 
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				responceCity = city.clone();
				
				responceCity.id = key.getInt("id");
				responceCity.name_key = key.getInt("name_key");
			}
			IStringValuesRepository stringValuesRepository = new StringValuesRepository(
					c, false, false);
			Iterator<StringValue> i = responceCity.name.values().iterator();
			while (i.hasNext()) {
				StringValue s = i.next();
				s.key_id = responceCity.name_key;
				s.id = stringValuesRepository.insertStringValue(s);
			}
			if (this.isCommited)
				c.commit();
		} catch (SQLException e) {
			try {
				log.error("updateCity() exception: ", e);
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
		return responceCity;
	}

	@Override
	public int updateCityNameByLang(int city_name_key, String lang_name)
			throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		try {
			if (this.isCommited)
				c.commit();
		} catch (SQLException e) {
			try {
				log.error("updateCity() exception: ", e);
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
		return 0;
	}

	@Override
	public int deleteCity(int city_id) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		try {
			c.setAutoCommit(false);
			String query = "DELETE FROM bus.cities WHERE id=?;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, city_id);
			ps.execute();
			if (this.isCommited)
				c.commit();
		} catch (SQLException e) {
			try {
				log.error("updateCity() exception: ", e);
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
		return 0;
	}

	@Override
	public City updateCity(City updateCity) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		try {

			c.setAutoCommit(false);
			String query = "UPDATE bus.cities SET lat=?, lon=? , scale=?, name_key=?,is_show=pg_catalog.bit(?) where id=?";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setDouble(1, updateCity.lat);
			ps.setDouble(2, updateCity.lon);
			ps.setInt(3, updateCity.scale);
			ps.setInt(4, updateCity.name_key);
			ps.setString(5, Integer.toString(updateCity.isShow? 1 : 0)); 
			ps.setInt(6, updateCity.id);
			ps.execute();
			IStringValuesRepository stringValuesRepository = new StringValuesRepository(
					c, false, false);
			stringValuesRepository.updateStringValues(updateCity.name_key,
					updateCity.name.values());
			City responceCity = updateCity.clone();
			responceCity.name = stringValuesRepository
					.getStringValuesToHashMap(updateCity.name_key);
			if (this.isCommited)
				c.commit();
			return responceCity;

		} catch (SQLException e) {
			try {
				log.error("updateCity() exception: ", e);
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
