package com.pgis.bus.data;

import java.util.ArrayList;
import java.util.Collection;

import com.pgis.bus.data.orm.*;
import com.pgis.bus.data.repositories.RepositoryException;

public interface IDataBaseService {

	Station getStation(int id);
	Station getStationByName(String name,Language lang);
	
	Collection<City> getAllCities()throws RepositoryException;
	Collection<Language> getAllLanguages() throws RepositoryException;
}
