package com.pgis.bus.data.impl;

import java.util.Collection;

import com.pgis.bus.data.Authenticate_enum;
import com.pgis.bus.data.IAdminDataBaseService;
import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.helpers.LoadImportObjectOptions;
import com.pgis.bus.data.helpers.LoadRouteOptions;
import com.pgis.bus.data.helpers.UpdateRouteOptions;
import com.pgis.bus.data.models.ImportRouteModel;
import com.pgis.bus.data.orm.*;
import com.pgis.bus.data.repositories.IimportRepository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.impl.ImportRepository;

public class AdminDataBaseService extends DataBaseService implements
		IAdminDataBaseService {

	IimportRepository importRepository = null;

	public AdminDataBaseService(IDBConnectionManager connectionManager) {
		super(connectionManager);
		importRepository = new ImportRepository(connectionManager);
	}

	@Override
	public User getUser(int id) throws RepositoryException {
		return usersRepotitory.getUser(id);
	}

	@Override
	public User getUserByName(String name) {
		return null;
	}

	@Override
	public Authenticate_enum authenticate(String userRole, String userName,
			String userPassword) throws RepositoryException {
		return usersRepotitory.authenticate(userRole, userName, userPassword);
	}

	@Override
	public City updateCity(City updateCity) throws RepositoryException {
		return citiesRepotitory.updateCity(updateCity);
	}

	@Override
	public City insertCity(City newCity) throws RepositoryException {
		return citiesRepotitory.insertCity(newCity);
	}

	@Override
	public void deleteCity(int city_id) throws RepositoryException {
		this.citiesRepotitory.deleteCity(city_id);

	}

	@Override
	public Station insertStation(Station station) throws RepositoryException {
		return this.stationsRepository.insertStation(station);
	}

	@Override
	public Station updateStation(Station station) throws RepositoryException {
		return this.stationsRepository.updateStation(station);
	}

	@Override
	public void deleteStation(int station_id) throws RepositoryException {
		this.stationsRepository.deleteStation(station_id);

	}

	@Override
	public Collection<Route> getRoutes(String route_type_id, int city_id,
			LoadRouteOptions opts) throws RepositoryException {
		return routesRepository.getRoutes(route_type_id, city_id, opts);
	}

	@Override
	public Route getRoute(Integer routeID, LoadRouteOptions opts)
			throws RepositoryException {
		return routesRepository.getRoute(routeID, opts);
	}

	@Override
	public void insertRoute(Route newRoute) throws RepositoryException {
		routesRepository.insertRoute(newRoute);
	}

	@Override
	public void removeRoute(int routeID) throws RepositoryException {
		routesRepository.removeRoute(routeID);

	}

	@Override
	public void updateRoute(Route updateRoute, UpdateRouteOptions opts)
			throws RepositoryException {
		routesRepository.updateRoute(updateRoute, opts);
	}

	@Override
	public void insertImportObject(ImportObject importObject)
			throws RepositoryException {
		importRepository.insertObject(importObject);
	}

	@Override
	public ImportObject getImportObject(String cityKey, String routeType,
			String number) throws RepositoryException {
		return importRepository.getObject(cityKey, routeType, number);
	}

	@Override
	public void updateObjectByID(ImportObject importObject)
			throws RepositoryException {
		importRepository.updateObjectByID(importObject);
	}

	@Override
	public ImportRouteModel getRouteModelForObj(int objID)
			throws RepositoryException {
		return importRepository.getRouteModelForObj(objID);
	}

	@Override
	public void removeImportObject(int ID) throws RepositoryException {
		importRepository.removeObject(ID);
	}

	@Override
	public Collection<ImportObject> getImportObjects(String cityKey,
			String routeType, LoadImportObjectOptions opts)
			throws RepositoryException {
		return importRepository.getObjects(cityKey, routeType, opts);
	}

}
