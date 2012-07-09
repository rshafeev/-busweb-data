package com.pgis.bus.data.repositories;

import java.util.ArrayList;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Language;

public interface ICitiesRepository {

	ArrayList<City> getAllCities() throws RepositoryException;
	City getCityByID(int id) throws RepositoryException;
	City getCityByName(String name, String lang_name) throws RepositoryException;
	
	/**
	 * 
	 * @param city
	 * @return new city id
	 * @throws RepositoryException
	 */
	int insertCity(City city) throws RepositoryException;
	int updateCityNameByLang(int city_name_key,String lang_name) throws RepositoryException;
	int deleteCity(int city_id) throws RepositoryException;
	
}
