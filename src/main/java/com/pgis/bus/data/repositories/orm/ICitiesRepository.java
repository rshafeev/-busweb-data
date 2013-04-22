package com.pgis.bus.data.repositories.orm;

import java.sql.SQLException;
import java.util.Collection;

import com.pgis.bus.data.orm.City;

public interface ICitiesRepository {
	City get(int id) throws SQLException;

	void insert(City city) throws SQLException;

	void remove(int city_id) throws SQLException;

	void update(City city) throws SQLException;

	Collection<City> getAll() throws SQLException;

	City getByKey(String key) throws SQLException;

	City getByName(String langID, String name) throws SQLException;

}
