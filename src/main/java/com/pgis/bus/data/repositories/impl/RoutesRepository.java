package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.postgis.Geometry;
import org.postgis.MultiLineString;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgresql.util.PGInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.helpers.LoadDirectRouteOptions;
import com.pgis.bus.data.helpers.LoadRouteOptions;
import com.pgis.bus.data.helpers.LoadRouteRelationOptions;
import com.pgis.bus.data.models.GsonLineString;
import com.pgis.bus.data.models.RouteGeoData;
import com.pgis.bus.data.models.RoutePart;
import com.pgis.bus.data.orm.DirectRoute;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.RouteRelation;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.ScheduleGroup;
import com.pgis.bus.data.orm.ScheduleGroupDay;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.orm.Timetable;
import com.pgis.bus.data.orm.type.DayEnum;
import com.pgis.bus.data.repositories.IRoutesRepository;
import com.pgis.bus.data.repositories.IStationsRepository;
import com.pgis.bus.data.repositories.IStringValuesRepository;
import com.pgis.bus.data.repositories.RepositoryException;

public class RoutesRepository extends Repository implements IRoutesRepository {
	private static final Logger log = LoggerFactory
			.getLogger(RoutesRepository.class);

	public RoutesRepository() {
		super();
	}

	public RoutesRepository(Connection c, boolean isClosed, boolean isCommited) {
		super();
		this.connection = c;
		this.isClosed = isClosed;
		this.isCommited = isCommited;
	}

	@Override
	public Collection<RouteGeoData> getGeoDataByRoutePart(RoutePart routePart,
			String lang_id) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		Collection<RouteGeoData> relations = null;

		try {
			String query = "SELECT"
					+ " position_index 		as index,"
					+ " value          		as station_name,"
					+ " geometry(location)  as station_location,"
					+ " geometry(geom)      as relation_geom"
					+ " FROM bus.route_relations"
					+ " JOIN bus.stations ON bus.route_relations.station_b_id = bus.stations.id"
					+ " JOIN bus.string_values ON bus.string_values.key_id = bus.stations.name_key"
					+ " WHERE direct_route_id = ? AND position_index >= ? AND position_index <= ?"
					+ " AND lang_id = lang_enum(?) ORDER BY position_index; ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, routePart.getDirectRouteID());
			ps.setInt(2, routePart.getIndexStart());
			ps.setInt(3, routePart.getIndexFinish());
			ps.setString(4, lang_id);
			ResultSet key = ps.executeQuery();
			relations = new ArrayList<RouteGeoData>();
			while (key.next()) {
				int index = key.getInt("index");
				String stationName = key.getString("station_name");

				PGgeometry stationLocation = null;
				if (key.getObject("station_location") != null)
					stationLocation = (PGgeometry) key
							.getObject("station_location");

				PGgeometry relationGeom = null;
				if (key.getObject("relation_geom") != null) {
					relationGeom = (PGgeometry) key.getObject("relation_geom");
				}
				RouteGeoData data = new RouteGeoData();
				data.setIndex(index);
				if (relationGeom != null)
					data.setRelationGeom((MultiLineString) relationGeom
							.getGeometry());
				if (stationLocation != null)
					data.setStationLocation((Point) stationLocation
							.getGeometry());
				data.setStationName(stationName);

				relations.add(data);
			}
		} catch (SQLException e) {
			relations = null;
			log.error("can not read database", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}
		return relations;
	}

	@Override
	public DirectRoute getDirectRoute(int route_id, boolean directType,
			LoadDirectRouteOptions opts) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		DirectRoute directRoute = null;

		try {
			int direct_typeDB = directType ? 1 : 0;
			String query = "SELECT * from bus.direct_routes WHERE "
					+ "route_id = ? AND " + "direct = pg_catalog.bit(?);";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, route_id);
			ps.setString(2, Integer.toString(direct_typeDB));
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				int id = key.getInt("id");
				directRoute = new DirectRoute();
				directRoute.setId(id);
				directRoute.setDirect(directType);
				directRoute.setRoute_id(route_id);
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}

		if (opts.isLoadScheduleData()) {
			directRoute.setSchedule(getSchedule(directRoute.getId()));
		}
		if (opts.isLoadRouteRelationsData()) {
			directRoute.setRoute_relations(getRouteRelations(
					directRoute.getId(), opts.getLoadRouteRelationOptions()));
		}
		return directRoute;
	}

	private Collection<RouteRelation> getRouteRelations(int direct_route_id,
			LoadRouteRelationOptions loadRouteRelationOptions)
			throws RepositoryException {
		
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		Collection<RouteRelation> relations = null;

		try {

			String query = "SELECT id,station_a_id,station_b_id,position_index,distance,"
					+ "ev_time, geometry(geom) as geom FROM "
					+ "bus.route_relations WHERE direct_route_id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, direct_route_id);
			ResultSet key = ps.executeQuery();
			relations = new ArrayList<RouteRelation>();
			while (key.next()) {
				int id = key.getInt("id");
				Integer station_a_id = key.getInt("station_a_id");
				Integer station_b_id = key.getInt("station_b_id");
				int position_index = key.getInt("position_index");
				double distance = key.getDouble("distance");
				PGInterval ev_time = (PGInterval) key.getObject("ev_time");

				PGgeometry geom = null;
				if (key.getObject("geom") != null) {
					geom = (PGgeometry) key.getObject("geom");
				}

				RouteRelation relation = new RouteRelation();
				relation.setId(id);
				relation.setDirect_route_id(direct_route_id);
				relation.setStation_a_id(station_a_id);
				relation.setStation_b_id(station_b_id);
				relation.setPosition_index(position_index);
				relation.setDistance(distance);
				relation.setEv_time(ev_time);
				if (geom != null)
					relation.setGeom(new GsonLineString(geom.getGeometry()));

				relations.add(relation);
			}
			
			if (loadRouteRelationOptions.isLoadStationsData()){
				IStationsRepository stationsRepository = new StationsRepository();
				for(RouteRelation relation : relations){
					int station_id = relation.getStation_b_id();
					Station stationB = stationsRepository.getStation(station_id);
					relation.setStationB(stationB);
				}
			}
				//return getRouteRelationsWithStationData(direct_route_id);
		} catch (Exception e) {
			log.error("db exception: ", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}

		return relations;
	}

	@Override
	public Collection<RouteRelation> getRouteRelationsWithStationData(
			int direct_route_id) throws RepositoryException {

		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		Collection<RouteRelation> relations = null;

		try {

			String query = "SELECT id,station_a_id,station_b_id,position_index,distance,"
					+ "ev_time, geometry(geom) as geom FROM "
					+ "bus.route_relations WHERE direct_route_id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, direct_route_id);
			ResultSet key = ps.executeQuery();
			relations = new ArrayList<RouteRelation>();
			while (key.next()) {
				int id = key.getInt("id");
				Integer station_a_id = key.getInt("station_a_id");
				Integer station_b_id = key.getInt("station_b_id");
				int position_index = key.getInt("position_index");
				double distance = key.getDouble("distance");
				PGInterval ev_time = (PGInterval) key.getObject("ev_time");

				PGgeometry geom = null;
				if (key.getObject("geom") != null) {
					geom = (PGgeometry) key.getObject("geom");
				}

				RouteRelation relation = new RouteRelation();
				relation.setId(id);
				relation.setDirect_route_id(direct_route_id);
				relation.setStation_a_id(station_a_id);
				relation.setStation_b_id(station_b_id);
				relation.setPosition_index(position_index);
				relation.setDistance(distance);
				relation.setEv_time(ev_time);
				if (geom != null)
					relation.setGeom(new GsonLineString(geom.getGeometry()));

				relations.add(relation);
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}

		return relations;
	}

	@Override
	public Schedule getSchedule(int direct_route_id) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		Schedule schedule = null;

		try {

			String query = "SELECT * from bus.schedule WHERE direct_route_id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, direct_route_id);
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				int id = key.getInt("id");
				schedule = new Schedule();
				schedule.setDirect_route_id(direct_route_id);
				schedule.setId(id);
				schedule.setScheduleGroups(getScheduleGroups(schedule.getId()));
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}

		return schedule;
	}

	@Override
	public Collection<Timetable> getTimeTables(int schedule_group_id)
			throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		Collection<Timetable> timeTables = null;

		try {
			String query = "SELECT * from bus.timetable WHERE schedule_group_id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, schedule_group_id);
			ResultSet key = ps.executeQuery();
			timeTables = new ArrayList<Timetable>();
			while (key.next()) {
				int id = key.getInt("id");
				Time time_A = key.getTime("time_A");
				Time time_B = key.getTime("time_A");
				PGInterval frequency = (PGInterval) key.getObject("frequency");

				Timetable timetable = new Timetable();
				timetable.setId(id);
				timetable.setSchedule_group_id(schedule_group_id);
				timetable.setTime_A(time_A);
				timetable.setTime_B(time_B);
				timetable.setFrequancy(frequency);

				timeTables.add(timetable);
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}

		return timeTables;
	}

	@Override
	public Collection<ScheduleGroupDay> getScheduleGroupDays(
			int schedule_group_id) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		Collection<ScheduleGroupDay> days = null;

		try {

			String query = "SELECT * FROM bus.schedule_group_days WHERE schedule_group_id = ?;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, schedule_group_id);
			ResultSet key = ps.executeQuery();
			days = new ArrayList<ScheduleGroupDay>();
			while (key.next()) {
				int id = key.getInt("id");
				DayEnum day_id = DayEnum.valueOf(key.getString("day_id"));
				ScheduleGroupDay day = new ScheduleGroupDay();
				day.setId(id);
				day.setSchedule_group_id(schedule_group_id);
				day.setDay_id(day_id);
				days.add(day);
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}

		return days;
	}

	@Override
	public Collection<ScheduleGroup> getScheduleGroups(int schedule_id)
			throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		Collection<ScheduleGroup> groups = null;

		try {

			String query = "SELECT * from bus.schedule_groups WHERE schedule_id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, schedule_id);
			ResultSet key = ps.executeQuery();
			groups = new ArrayList<ScheduleGroup>();
			while (key.next()) {
				int id = key.getInt("id");
				ScheduleGroup group = new ScheduleGroup();
				group.setId(id);
				group.setSchedule_id(schedule_id);
				group.setDays(getScheduleGroupDays(id));
				group.setTimetables(getTimeTables(id));
				groups.add(group);
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}

		return groups;
	}

	@Override
	public Collection<Route> getRoutes(String routeTypeID, int city_id,
			LoadRouteOptions opts) throws RepositoryException {

		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		Collection<Route> routes = null;

		try {
			String query = "SELECT * from bus.routes WHERE "
					+ "city_id = ? AND "
					+ "route_type_id = bus.route_type_enum(?);";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, city_id);
			ps.setString(2, routeTypeID);
			ResultSet key = ps.executeQuery();
			routes = new ArrayList<Route>();
			while (key.next()) {
				int id = key.getInt("id");
				String number = key.getString("number");
				int name_key = key.getInt("name_key");
				double cost = key.getDouble("cost");

				Route route = new Route();
				route.setId(id);
				route.setCost(cost);
				route.setName_key(name_key);
				route.setNumber(number);
				route.setCity_id(city_id);
				route.setRoute_type_id(routeTypeID);
				routes.add(route);
			}
		} catch (Exception e) {
			routes = null;
			log.error("can not read database", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}
		try {
			IStringValuesRepository stringValuesRepository = null;

			if (opts.isLoadRouteNamesData()) {
				stringValuesRepository = new StringValuesRepository();
			}

			for (Route route : routes) {
				if (opts.isLoadRouteNamesData()) {
					HashMap<String, StringValue> name = stringValuesRepository
							.getStringValuesToHashMap(route.getName_key());
					route.setName(name);
				}
				if (opts.isLoadDirectRouteData()) {
					DirectRoute dRoute = getDirectRoute(route.getId(), true,
							opts.getDirectRouteOptions());
					DirectRoute rRoute = getDirectRoute(route.getId(), false,
							opts.getDirectRouteOptions());
					route.setDirectRouteWay(dRoute);
					route.setReverseRouteWay(rRoute);
				}

			}
		} catch (Exception e) {
			routes = null;
			log.error("can not read database", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		}
		return routes;
	}

	@Override
	public Route getRoute(int route_id, LoadRouteOptions opts)
			throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

}
