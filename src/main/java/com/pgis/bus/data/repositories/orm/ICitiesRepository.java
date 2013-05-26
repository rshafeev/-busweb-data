package com.pgis.bus.data.repositories.orm;

import java.sql.SQLException;
import java.util.Collection;

import com.pgis.bus.data.exp.RepositoryException;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.type.LangEnum;

public interface ICitiesRepository {
	City get(int id) throws RepositoryException;

	void insert(City city) throws RepositoryException;

	void remove(int city_id) throws RepositoryException;

	void update(City city) throws RepositoryException;

	Collection<City> getAll() throws RepositoryException;

	City getByKey(String key) throws RepositoryException;

	City getByName(LangEnum langID, String name) throws RepositoryException;

}
