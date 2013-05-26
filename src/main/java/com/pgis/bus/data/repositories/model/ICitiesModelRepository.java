package com.pgis.bus.data.repositories.model;

import java.sql.SQLException;
import java.util.Collection;

import com.pgis.bus.data.exp.RepositoryException;
import com.pgis.bus.data.repositories.IRepository;
import com.pgis.bus.net.models.city.CityModel;
import com.pgis.bus.net.models.route.RouteTypeModel;

public interface ICitiesModelRepository extends IRepository {
	CityModel get(int id) throws RepositoryException;

	Collection<CityModel> getAll() throws RepositoryException;

	CityModel getByKey(String key) throws RepositoryException;

	Collection<RouteTypeModel> getRouteTypesByCity(int cityID) throws RepositoryException;

}
