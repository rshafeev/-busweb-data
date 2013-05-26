package com.pgis.bus.data.repositories.model;

import java.sql.SQLException;
import java.util.Collection;

import com.pgis.bus.data.exp.RepositoryException;
import com.pgis.bus.net.models.route.RouteModel;
import com.pgis.bus.net.models.route.RouteRelationModel;
import com.pgis.bus.net.models.route.RouteWayModel;
import com.pgis.bus.net.models.route.RoutesListModel;
import com.pgis.bus.net.models.route.ScheduleModel;

public interface IRoutesModelRepository {

	RouteModel get(int routeID) throws RepositoryException;

	ScheduleModel getSchedule(int routeWayID) throws RepositoryException;

	RouteWayModel getRouteWay(int routeID, boolean directType) throws RepositoryException;

	RoutesListModel getRoutesList(int cityID, String routeTypeID) throws RepositoryException;

	Collection<RouteRelationModel> getRouteRelations(int routeID, boolean directType, String langID)
			throws RepositoryException;

	Collection<RouteRelationModel> getRouteRelations(int routeID, boolean directType, String langID, int startInd,
			int finishInd) throws RepositoryException;

}
