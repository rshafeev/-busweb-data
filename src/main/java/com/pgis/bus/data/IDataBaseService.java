package com.pgis.bus.data;

import java.util.Collection;

import org.postgis.Point;

import com.pgis.bus.data.helpers.LoadRouteOptions;
import com.pgis.bus.data.models.RouteGeoData;
import com.pgis.bus.data.models.RoutePart;
import com.pgis.bus.data.orm.*;
import com.pgis.bus.data.orm.type.Path_t;
import com.pgis.bus.data.repositories.IStationsRepository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.net.request.FindPathsOptions;

public interface IDataBaseService {

	IStationsRepository Stations();

	Station getStation(int id);

	Station getStationByName(String name, Language lang);

	Collection<String> getRouteTypesForCity(int cityID)
			throws RepositoryException;

	Collection<Route> getRoutes(String routeTypeID, int city_id, String lang_id)
			throws RepositoryException;

	Collection<City> getAllCities() throws RepositoryException;

	Collection<Language> getAllLanguages() throws RepositoryException;

	Collection<Station> getStationsByCity(int city_id)
			throws RepositoryException;

	Collection<Station> getStationsByBox(int city_id, Point p1, Point p2)
			throws RepositoryException;

	City getCityByName(String lang_id, String value) throws RepositoryException;

	Collection<Path_t> getShortestPaths(FindPathsOptions options)
			throws RepositoryException;

	Collection<RouteGeoData> getGeoDataByRoutePart(RoutePart routePart,
			String lang_id) throws RepositoryException;

	City getCityByKey(String key) throws RepositoryException;
	City getCityByID(int id) throws RepositoryException;
	
	Collection<Route> getRoutes(String route_type_id, int city_id,
			LoadRouteOptions opts) throws RepositoryException;

	Route getRoute(Integer routeID, LoadRouteOptions opts)
			throws RepositoryException;
}
