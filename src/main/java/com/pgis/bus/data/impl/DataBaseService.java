package com.pgis.bus.data.impl;

import java.sql.Connection;
import java.util.Collection;

import org.postgis.Point;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.IDataBaseService;
import com.pgis.bus.data.models.FindWaysOptions;
import com.pgis.bus.data.models.RouteGeoData;
import com.pgis.bus.data.models.RoutePart;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Language;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.WayElem;
import com.pgis.bus.data.repositories.ICitiesRepository;
import com.pgis.bus.data.repositories.IMainRepository;
import com.pgis.bus.data.repositories.IRoutesRepository;
import com.pgis.bus.data.repositories.IStationsRepository;
import com.pgis.bus.data.repositories.IUsersRepository;
import com.pgis.bus.data.repositories.IWaysRepository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.impl.CitiesRepository;
import com.pgis.bus.data.repositories.impl.MainRepository;
import com.pgis.bus.data.repositories.impl.RoutesRepository;
import com.pgis.bus.data.repositories.impl.StationsRepository;
import com.pgis.bus.data.repositories.impl.UsersRepository;
import com.pgis.bus.data.repositories.impl.WaysRepository;

public class DataBaseService implements IDataBaseService {
	protected IUsersRepository usersRepotitory;
	protected ICitiesRepository citiesRepotitory;
	protected IMainRepository mainRepository;
	protected IStationsRepository stationsRepository;
	protected IWaysRepository waysRepository;
	protected IRoutesRepository routesRepository;
	protected IDBConnectionManager connectionManager;
	private void init(IDBConnectionManager connectionManager) {
		usersRepotitory = new UsersRepository(connectionManager);
		citiesRepotitory = new CitiesRepository(connectionManager);
		mainRepository = new MainRepository(connectionManager);
		stationsRepository = new StationsRepository(connectionManager);
		waysRepository = new WaysRepository(connectionManager);
		routesRepository = new RoutesRepository(connectionManager);
		
	}

	protected Connection getConnection() throws DataBaseServiceException {

		Connection conn = connectionManager.getConnection();
		if (conn == null)
			throw new DataBaseServiceException(
					DataBaseServiceException.err_enum.c_connect_to_db_err);
		return conn;
	}

	public DataBaseService(IDBConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
		init(connectionManager);
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
	public Collection<Station> getStationsByCity(int city_id)
			throws RepositoryException {
		return stationsRepository.getStationsByCity(city_id);
	}

	@Override
	public Collection<WayElem> getShortestWays(FindWaysOptions options)
			throws RepositoryException {
		return waysRepository.getShortestWays(options);
	}

	@Override
	public Collection<RouteGeoData> getGeoDataByRoutePart(RoutePart routePart,
			String lang_id) throws RepositoryException {
		return routesRepository.getGeoDataByRoutePart(routePart, lang_id);
	}

	@Override
	public Collection<Station> getStationsByBox(int city_id, Point p1, Point p2)
			throws RepositoryException {
		return stationsRepository.getStationsByBox(city_id, p1, p2);
	}

	@Override
	public City getCityByKey(String key) throws RepositoryException {

		return citiesRepotitory.getCityByKey(key);
	}

	@Override
	public Collection<Route> getRoutes(String routeTypeID, int city_id,
			String lang_id) throws RepositoryException {
		return routesRepository.getRoutes(routeTypeID, city_id, lang_id);
	}

	@Override
	public Collection<String> getRouteTypesForCity(int cityID)
			throws RepositoryException {
		return this.citiesRepotitory.getRouteTypesForCity(cityID);
	}

	@Override
	public City getCityByID(int id) throws RepositoryException {
		
		return this.citiesRepotitory.getCityByID(id);
	}

}
