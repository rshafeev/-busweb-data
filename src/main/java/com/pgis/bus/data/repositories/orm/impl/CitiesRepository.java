package com.pgis.bus.data.repositories.orm.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.exp.RepositoryException;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.orm.ICitiesRepository;

public class CitiesRepository extends Repository implements ICitiesRepository {
	private static final Logger log = LoggerFactory.getLogger(CitiesRepository.class);

	public CitiesRepository(IConnectionManager connManager) {
		super(connManager);
	}

	@Override
	public Collection<City> getAll() throws RepositoryException {

		Collection<City> cities = null;
		try {
			Connection c = super.getConnection();
			String query = "select * from bus.cities";
			PreparedStatement ps = c.prepareStatement(query);
			ResultSet key = ps.executeQuery();
			cities = new ArrayList<City>();
			while (key.next()) {
				int id = key.getInt("id");
				double lat = key.getDouble("lat");
				double lon = key.getDouble("lon");
				String cityKey = key.getString("key");
				int nameKey = key.getInt("name_key");
				int scale = key.getInt("scale");
				boolean isShow = key.getBoolean("is_show");
				City city = new City(super.connManager);
				city.setId(id);
				city.setNameKey(nameKey);
				city.setKey(cityKey);
				city.setLat(lat);
				city.setLon(lon);
				city.setScale(scale);
				city.setShow(isShow);
				cities.add(city);
			}
		} catch (Exception e) {
			cities = null;
			super.handeThrowble(e);
		}
		return cities;
	}

	@Override
	public City get(int id) throws RepositoryException {

		try {
			Connection c = super.getConnection();
			String query = "SELECT * FROM bus.cities WHERE id = ?";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				double lat = key.getDouble("lat");
				double lon = key.getDouble("lon");
				String cityKey = key.getString("key");
				int nameKey = key.getInt("name_key");
				int scale = key.getInt("scale");
				boolean isShow = key.getBoolean("is_show");
				City city = new City(super.connManager);
				city.setId(id);
				city.setNameKey(nameKey);
				city.setKey(cityKey);
				city.setLat(lat);
				city.setLon(lon);
				city.setScale(scale);
				city.setShow(isShow);
				return city;
			}
		} catch (Exception e) {
			super.handeThrowble(e);
		}
		return null;
	}

	@Override
	public City getByKey(String key) throws RepositoryException {

		try {
			Connection c = super.getConnection();
			String query = "select * from bus.cities where key = ?";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, key);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				double lat = rs.getDouble("lat");
				double lon = rs.getDouble("lon");
				int nameKey = rs.getInt("name_key");
				int scale = rs.getInt("scale");
				boolean isShow = rs.getBoolean("is_show");
				City city = new City(super.connManager);
				city.setId(id);
				city.setNameKey(nameKey);
				city.setKey(key);
				city.setLat(lat);
				city.setLon(lon);
				city.setScale(scale);
				city.setShow(isShow);
				return city;
			}
		} catch (Exception e) {
			super.handeThrowble(e);
		}
		return null;
	}

	@Override
	public City getByName(LangEnum langID, String name) throws RepositoryException {

		try {
			Connection c = super.getConnection();
			String query = "SELECT  bus.cities.key as key, bus.cities.id as id,bus.cities.name_key as name_key, "
					+ "bus.cities.lat as lat, bus.cities.is_show as is_show, "
					+ "bus.cities.lon as lon,bus.cities.scale as scale,bus.string_values.lang_id as lang_id, "
					+ "bus.string_values.value as value FROM bus.cities JOIN bus.string_keys ON "
					+ "bus.cities.name_key = bus.string_keys.id JOIN bus.string_values ON "
					+ "bus.string_keys.id = bus.string_values.key_id WHERE value=? and lang_id=bus.lang_enum(?)";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, langID.name());

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				double lat = rs.getDouble("lat");
				double lon = rs.getDouble("lon");
				int nameKey = rs.getInt("name_key");
				String cityKey = rs.getString("key");
				int scale = rs.getInt("scale");
				boolean isShow = rs.getBoolean("is_show");
				City city = new City(super.connManager);
				city.setId(id);
				city.setNameKey(nameKey);
				city.setKey(cityKey);
				city.setLat(lat);
				city.setLon(lon);
				city.setScale(scale);
				city.setShow(isShow);
				return city;
			}
		} catch (Exception e) {
			super.handeThrowble(e);
		}
		return null;
	}

	@Override
	public void insert(City city) throws RepositoryException {

		try {
			Connection c = super.getConnection();
			city.setConnManager(connManager);
			String query = "INSERT INTO bus.cities (key,lat,lon,scale,is_show) VALUES(?,?,?,?,?) RETURNING  id,name_key";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, city.getKey());
			ps.setDouble(2, city.getLat());
			ps.setDouble(3, city.getLon());
			ps.setInt(4, city.getScale());
			ps.setBoolean(5, city.isShow());
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				city.setId(key.getInt("id"));
				city.setNameKey(key.getInt("name_key"));

				StringValuesRepository strValuesRep = new StringValuesRepository(super.connManager);
				strValuesRep.setRepositoryExternConnection(c);
				for (StringValue v : city.getName().values()) {
					strValuesRep.insert(v);
				}

			}

		} catch (Exception e) {
			super.handeThrowble(e);
		}
	}

	@Override
	public void remove(int city_id) throws RepositoryException {

		try {
			Connection c = super.getConnection();
			String query = "DELETE FROM bus.cities WHERE id=?;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, city_id);
			ps.execute();
		} catch (Exception e) {
			super.handeThrowble(e);
		}
	}

	@Override
	public void update(City city) throws RepositoryException {

		try {
			Connection c = super.getConnection();
			String query = "UPDATE bus.cities SET lat=?, lon=? , scale=?, name_key=?,is_show=? where id=?";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setDouble(1, city.getLat());
			ps.setDouble(2, city.getLon());
			ps.setInt(3, city.getScale());
			ps.setInt(4, city.getNameKey());
			ps.setBoolean(5, city.isShow());
			ps.setInt(6, city.getId());
			ps.execute();
			city.setConnManager(connManager);

			StringValuesRepository stringValuesRepository = new StringValuesRepository(super.connManager);
			stringValuesRepository.setRepositoryExternConnection(c);
			stringValuesRepository.update(city.getNameKey(), city.getName().values());

		} catch (Exception e) {
			super.handeThrowble(e);
		} finally {
		}
	}

}
