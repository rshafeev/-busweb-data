package com.pgis.bus.data.repositories.orm;

import java.util.Collection;

import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.RouteRelation;
import com.pgis.bus.data.orm.RouteWay;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.repositories.RepositoryException;

public interface IRoutesRepository {

	Route get(int routeID) throws RepositoryException;

	Schedule getSchedule(int routeWayID) throws RepositoryException;

	RouteWay getRouteWay(int routeID, boolean directType) throws RepositoryException;

	void insert(Route route) throws RepositoryException;

	void remove(int routeID) throws RepositoryException;

	void update(Route route) throws RepositoryException;

	void update(Schedule schedule) throws RepositoryException;

	void update(RouteWay routeWay) throws RepositoryException;

	Collection<RouteRelation> getRouteWayRelations(int routeWayID) throws RepositoryException;

}
