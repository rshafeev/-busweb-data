package com.pgis.bus.data.impl;

import java.sql.Connection;
import java.util.Collection;


import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.IDataBaseService;
import com.pgis.bus.data.models.FindWaysOptions;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Language;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.WayElem;
import com.pgis.bus.data.repositories.ICitiesRepository;
import com.pgis.bus.data.repositories.IMainRepository;
import com.pgis.bus.data.repositories.IStationsRepository;
import com.pgis.bus.data.repositories.IUsersRepository;
import com.pgis.bus.data.repositories.IWaysRepository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.impl.CitiesRepository;
import com.pgis.bus.data.repositories.impl.MainRepository;
import com.pgis.bus.data.repositories.impl.StationsRepository;
import com.pgis.bus.data.repositories.impl.UsersRepository;
import com.pgis.bus.data.repositories.impl.WaysRepository;

public class DataBaseService implements IDataBaseService {
	protected IUsersRepository usersRepotitory;
	protected ICitiesRepository citiesRepotitory;
	protected IMainRepository mainRepository;
	protected IStationsRepository stationsRepository;
	protected IWaysRepository waysRepository;
	
	private void init() {
		usersRepotitory = new UsersRepository();
		citiesRepotitory = new CitiesRepository();
		mainRepository = new MainRepository();
		stationsRepository = new StationsRepository();
		waysRepository = new WaysRepository();
	}

	protected Connection getConnection() throws DataBaseServiceException {

		Connection conn = DBConnectionFactory.getConnection();
		if (conn == null)
			throw new DataBaseServiceException(
					DataBaseServiceException.err_enum.c_connect_to_db_err);
		return conn;
	}

	public DataBaseService() {
		init();
	}


	@Override
	public Collection<City> getAllCities() throws RepositoryException {
		return citiesRepotitory.getAllCities();
	}
	
	@Override
	public City getCityByName(String lang_id, String value)
			throws RepositoryException {
		return citiesRepotitory.getCityByName(lang_id, value);
	}
	
	@Override
	public Collection<Language> getAllLanguages() throws RepositoryException {
		return mainRepository.getAllLanguages();
	}

	@Override
	public Station getStation(int id) {
		return null;
	}

	@Override
	public Station getStationByName(String name, Language lang) {
		return null;
	}

	@Override
	public Collection<Station> getStationsByCityAndTransport(int city_id,
			String transportType) throws RepositoryException {
		return stationsRepository.getStationsByCityAndTransport(city_id, transportType);
	}

	@Override
	public Collection<WayElem> getShortestWays(FindWaysOptions options)
			throws RepositoryException {
		return waysRepository.getShortestWays(options);
	}






}
