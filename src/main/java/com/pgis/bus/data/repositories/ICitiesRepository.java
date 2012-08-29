package com.pgis.bus.data.repositories;

import java.util.Collection;
import com.pgis.bus.data.orm.City;


public interface ICitiesRepository extends IRepository {

	Collection<City> getAllCities() throws RepositoryException;

	City getCityByID(int id) throws RepositoryException;

	City getCityByName(String lang_name, String name)
			throws RepositoryException;

	/**
	 * 
	 * @param city
	 * @return new city id
	 * @throws RepositoryException
	 */
	City insertCity(City city) throws RepositoryException;

	int updateCityNameByLang(int city_name_key, String lang_name)
			throws RepositoryException;

	int deleteCity(int city_id) throws RepositoryException;
	
	City updateCity(City updateCity) throws RepositoryException;

}
