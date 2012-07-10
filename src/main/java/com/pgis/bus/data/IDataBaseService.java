package com.pgis.bus.data;

import java.util.ArrayList;

import com.pgis.bus.data.orm.*;
import com.pgis.bus.data.repositories.RepositoryException;

public interface IDataBaseService {

	Station getStation(int id);
	Station getStationByName(String name,Language lang);
	
	ArrayList<City> getAllCities()throws RepositoryException;
}
