package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.impl.DataBaseService;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.User;
import com.pgis.bus.data.repositories.ICitiesRepository;
import com.pgis.bus.data.repositories.Repository;
import com.pgis.bus.data.repositories.RepositoryException;

public class CitiesRepository extends Repository implements ICitiesRepository {
	private static final Logger log = LoggerFactory
			.getLogger(CitiesRepository.class);

	@Override
	public ArrayList<City> getAllCities() throws RepositoryException {
		
			Connection conn = Repository.getConnection();
			ArrayList<City> cities = null;

			try {
				String query = "select * from bus.cities";
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet key = ps.executeQuery();
				cities = new ArrayList<City>();
				while (key.next()) {
					City city = new City();
					city.id = key.getInt("id");
					city.lat = key.getDouble("lat");
					city.lon = key.getDouble("lon");
					city.name_key = key.getInt("name_key");
					city.name = getStringValues(city.name_key);
					cities.add(city);
				}
			} catch (SQLException e) {
				cities = null;
				log.error("can not read database", e);
				throw new RepositoryException(
						RepositoryException.err_enum.c_sql_err);
			} finally {
				DBConnectionFactory.closeConnection(conn);
			}
			return cities;
	}

	@Override
	public City getCityByID(int id) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public City getCityByName(String name, String lang_name)
			throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertCity(City city) throws RepositoryException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateCityNameByLang(int city_name_key, String lang_name)
			throws RepositoryException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteCity(int city_id) throws RepositoryException {
		// TODO Auto-generated method stub
		return 0;
	}

}
