package com.pgis.bus.data.repositories.orm;

import java.util.Collection;

import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.repositories.IRepository;
import com.pgis.bus.data.repositories.RepositoryException;

public interface ICitiesRepository extends IRepository {
	City get(int id) throws RepositoryException;

	void insert(City city) throws RepositoryException;

	void remove(int city_id) throws RepositoryException;

	void update(City city) throws RepositoryException;

	Collection<City> getAll() throws RepositoryException;

	City getByKey(String key) throws RepositoryException;

	City getByName(String langID, String name) throws RepositoryException;

}
