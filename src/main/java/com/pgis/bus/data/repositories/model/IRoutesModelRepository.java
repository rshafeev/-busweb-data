package com.pgis.bus.data.repositories.model;

import java.sql.SQLException;
import java.util.Collection;

import com.pgis.bus.net.models.route.RouteModel;
import com.pgis.bus.net.models.route.RouteRelationModel;
import com.pgis.bus.net.models.route.RouteWayModel;
import com.pgis.bus.net.models.route.RoutesListModel;
import com.pgis.bus.net.models.route.ScheduleModel;

public interface IRoutesModelRepository {

	RouteModel get(int routeID) throws SQLException;

	ScheduleModel getSchedule(int routeWayID) throws SQLException;

	RouteWayModel getRouteWay(int routeID, boolean directType) throws SQLException;

	RoutesListModel getRoutesList(int cityID, String routeTypeID) throws SQLException;

	Collection<RouteRelationModel> getRouteRelations(int routeID, boolean directType, String langID)
			throws SQLException;

	Collection<RouteRelationModel> getRouteRelations(int routeID, boolean directType, String langID, int startInd,
			int finishInd) throws SQLException;

}
