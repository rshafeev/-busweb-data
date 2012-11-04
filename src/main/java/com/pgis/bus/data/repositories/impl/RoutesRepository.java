package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

import org.postgis.Geometry;
import org.postgis.LineString;
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
					data.setRelationGeom((LineString) relationGeom
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
				Object geomObj = key.getObject("geom");
				if (geomObj instanceof PGgeometry) {
					geom = (PGgeometry) geomObj;
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

			if (loadRouteRelationOptions.isLoadStationsData()) {
				IStationsRepository stationsRepository = new StationsRepository();
				for (RouteRelation relation : relations) {
					int station_id = relation.getStation_b_id();
					Station stationB = stationsRepository
							.getStation(station_id);
					relation.setStationB(stationB);
				}
			}
			// return getRouteRelationsWithStationData(direct_route_id);
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

					Collection<StringValue> name = stringValuesRepository
							.getStringValues(route.getName_key());
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
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		Route route = null;

		try {
			String query = "SELECT * from bus.routes WHERE id = ?; ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, route_id);
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				int id = key.getInt("id");
				String number = key.getString("number");
				int city_id = key.getInt("city_id");
				String routeTypeID = key.getString("route_type_id");
				int name_key = key.getInt("name_key");
				double cost = key.getDouble("cost");

				route = new Route();
				route.setId(id);
				route.setCost(cost);
				route.setName_key(name_key);
				route.setNumber(number);
				route.setCity_id(city_id);
				route.setRoute_type_id(routeTypeID);

			}
		} catch (Exception e) {
			route = null;
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

			if (route != null) {
				if (opts.isLoadRouteNamesData()) {

					Collection<StringValue> name = stringValuesRepository
							.getStringValues(route.getName_key());
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
			route = null;
			log.error("can not read database", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		}
		return route;
	}

	private void insertTimetable(Timetable t, Connection c) throws Exception {
		String query = "INSERT INTO bus.timetable (schedule_group_id,time_A,time_B,frequency) "
				+ "VALUES(?,?,?,?) RETURNING id;";
		PreparedStatement ps = c.prepareStatement(query);

		ps.setInt(1, t.getSchedule_group_id());
		ps.setTime(2, t.getTimeAObj());
		ps.setTime(3, t.getTimeBObj());
		ps.setObject(4, t.getFrequancyObj());
		ResultSet key = ps.executeQuery();
		if (key.next()) {
			int id = key.getInt("id");
			t.setId(id);
		} else {
			throw new Exception("not found new id and name_key");
		}

	}

	private void insertScheduleGroupDay(ScheduleGroupDay d, Connection c)
			throws Exception {
		String query = "INSERT INTO bus.schedule_group_days (schedule_group_id,day_id) "
				+ "VALUES(?,day_enum(?)) RETURNING id;";
		PreparedStatement ps = c.prepareStatement(query);
		ps.setInt(1, d.getSchedule_group_id());
		ps.setString(2, d.getDay_id().name());
		ResultSet key = ps.executeQuery();
		if (key.next()) {
			int id = key.getInt("id");
			d.setId(id);

		} else {
			throw new Exception("not found new id and name_key");
		}
	}

	private void insertScheduleGroup(ScheduleGroup scheduleGroup, Connection c)
			throws Exception {
		String query = "INSERT INTO bus.schedule_groups (schedule_id) "
				+ "VALUES(?) RETURNING id;";
		PreparedStatement ps = c.prepareStatement(query);
		ps.setInt(1, scheduleGroup.getSchedule_id());
		ResultSet key = ps.executeQuery();
		if (key.next()) {
			int id = key.getInt("id");
			scheduleGroup.setId(id);

		} else {
			throw new Exception("not found new id and name_key");
		}
		for (Timetable t : scheduleGroup.getTimetables()) {
			insertTimetable(t, c);
		}
		for (ScheduleGroupDay d : scheduleGroup.getDays()) {
			insertScheduleGroupDay(d, c);
		}
	}

	private void insertSchedule(Schedule schedule, Connection c)
			throws Exception {
		String query = "INSERT INTO bus.schedule (direct_route_id) "
				+ "VALUES(?) RETURNING id;";
		PreparedStatement ps = c.prepareStatement(query);
		ps.setInt(1, schedule.getDirect_route_id());
		ResultSet key = ps.executeQuery();
		if (key.next()) {
			int id = key.getInt("id");
			schedule.setId(id);

		} else {
			throw new Exception("not found new id and name_key");
		}
		for (ScheduleGroup g : schedule.getScheduleGroups()) {
			insertScheduleGroup(g, c);

		}
	}

	private void insertDirectRoute(DirectRoute directRoute, Connection c)
			throws Exception {

		String query = "INSERT INTO bus.direct_routes (route_id,direct) "
				+ "VALUES(?,pg_catalog.bit(?)) RETURNING id; ";
		PreparedStatement ps = c.prepareStatement(query);

		ps.setInt(1, directRoute.getRoute_id());
		ps.setString(2, Integer.toString(directRoute.isDirect() ? 1 : 0));
		ResultSet key = ps.executeQuery();
		if (key.next()) {
			int id = key.getInt("id");
			directRoute.setId(id);
		} else {
			throw new Exception("not found new id and name_key");
		}
		insertSchedule(directRoute.getSchedule(), c);
		for (RouteRelation r : directRoute.getRoute_relations()) {
			insertRouteRelation(r, c);
		}

	}

	private void insertRouteRelation(RouteRelation r, Connection c)
			throws Exception {
		String query = "SELECT * from bus.insert_route_relation(?,?,?,?,?); ";
		PreparedStatement ps = c.prepareStatement(query);

		ps.setInt(1, r.getDirect_route_id());
		ps.setInt(2, r.getStation_a_id());
		ps.setInt(3, r.getStation_b_id());
		ps.setInt(4, r.getPosition_index());
		PGgeometry geom = null;
		if(r.getGeom()!=null){
			geom = new PGgeometry(r.getGeom().toLineString());
		}
		ps.setObject(5, geom);
		ResultSet key = ps.executeQuery();
		if (key.next()) {
			int id = key.getInt(1);
			r.setId(id);
		} else {
			throw new Exception("not found new id and name_key");
		}

	}

	@Override
	public void insertRoute(Route route) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();

		try {
			String query = "INSERT INTO bus.routes (city_id,number,cost,route_type_id) "
					+ "VALUES(?,?,?,bus.route_type_enum(?)) RETURNING id,name_key; ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, route.getCity_id());
			ps.setString(2, route.getNumber());
			ps.setDouble(3, route.getCost());
			ps.setString(4, route.getRoute_type_id());
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				int id = key.getInt("id");
				int name_key = key.getInt("name_key");
				route.setId(id);
				route.setName_key(name_key);
			} else {
				throw new Exception("not found new id and name_key");
			}

			insertDirectRoute(route.getDirectRouteWay(), c);
			insertDirectRoute(route.getReverseRouteWay(), c);

			if (this.isCommited)
				c.commit();
		} catch (Exception e) {
			try {
				log.error("insertRoute() exception: ", e);
				c.rollback();
				throw new RepositoryException(
						RepositoryException.err_enum.c_transaction_err);
			} catch (SQLException sqx) {
				throw new RepositoryException(
						RepositoryException.err_enum.c_rollback_err);
			}
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}
	}

}
