package com.pgis.bus.data;

import java.util.Collection;

import com.pgis.bus.data.helpers.LoadRouteOptions;
import com.pgis.bus.data.helpers.UpdateRouteOptions;
import com.pgis.bus.data.impl.DataBaseServiceException;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.ImportObject;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.User;
import com.pgis.bus.data.repositories.RepositoryException;

public interface IAdminDataBaseService extends IDataBaseService {

	User getUser(int id) throws RepositoryException;

	User getUserByName(String name) throws DataBaseServiceException;

	Authenticate_enum authenticate(String userRole, String userName,
			String userPassword) throws RepositoryException;

	City updateCity(City updateCity) throws RepositoryException;

	City insertCity(City newCity) throws RepositoryException;

	void deleteCity(int city_id) throws RepositoryException;

	Station insertStation(Station station) throws RepositoryException;

	Station updateStation(Station station) throws RepositoryException;

	void deleteStation(int station_id) throws RepositoryException;

	Collection<Route> getRoutes(String route_type_id, int city_id,
			LoadRouteOptions opts) throws RepositoryException;

	Route getRoute(Integer routeID, LoadRouteOptions opts)
			throws RepositoryException;

	void insertRoute(Route newRoute) throws RepositoryException;

	void removeRoute(int routeID) throws RepositoryException;

	void updateRoute(Route updateRoute, UpdateRouteOptions opts)
			throws RepositoryException;

	void insertImportObject(ImportObject importObject)throws RepositoryException;
}
