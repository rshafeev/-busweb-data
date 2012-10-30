package com.pgis.bus.data.repositories;

import java.util.Collection;

import com.pgis.bus.data.helpers.LoadDirectRouteOptions;
import com.pgis.bus.data.helpers.LoadRouteOptions;
import com.pgis.bus.data.models.RouteGeoData;
import com.pgis.bus.data.models.RoutePart;
import com.pgis.bus.data.orm.DirectRoute;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.RouteRelation;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.ScheduleGroup;
import com.pgis.bus.data.orm.ScheduleGroupDay;
import com.pgis.bus.data.orm.Timetable;

public interface IRoutesRepository {
	Collection<RouteGeoData> getGeoDataByRoutePart(RoutePart routePart,
			String lang_id) throws RepositoryException;

	Collection<Route> getRoutes(String routeTypeID, int city_id,
			LoadRouteOptions opts) throws RepositoryException;

	Route getRoute(int route_id, LoadRouteOptions opts)
			throws RepositoryException;

	DirectRoute getDirectRoute(int route_id, boolean directType,
			LoadDirectRouteOptions opts) throws RepositoryException;

	Schedule getSchedule(int route_id) throws RepositoryException;

	Collection<ScheduleGroup> getScheduleGroups(int schedule_id)
			throws RepositoryException;

	Collection<ScheduleGroupDay> getScheduleGroupDays(int schedule_group_id)
			throws RepositoryException;

	Collection<Timetable> getTimeTables(int schedule_group_id)
			throws RepositoryException;

	Collection<RouteRelation> getRouteRelationsWithStationData(
			int direct_route_id) throws RepositoryException;

}
