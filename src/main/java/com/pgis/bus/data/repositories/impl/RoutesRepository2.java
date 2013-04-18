package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

import org.postgis.LineString;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgresql.util.PGInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.geo.GeoObjectsFactory;
import com.pgis.bus.data.models.RouteGeoData;
import com.pgis.bus.data.models.RoutePart;
import com.pgis.bus.data.models.factory.StringValueModelFactory;
import com.pgis.bus.data.models.factory.geom.PolyLineModelFactory;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.RouteRelation;
import com.pgis.bus.data.orm.RouteWay;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.ScheduleGroup;
import com.pgis.bus.data.orm.ScheduleGroupDay;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.orm.Timetable;
import com.pgis.bus.data.repositories.IRoutesRepository;
import com.pgis.bus.data.repositories.IStationsRepository;
import com.pgis.bus.data.repositories.IStringValuesRepository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.opts.LoadDirectRouteOptions;
import com.pgis.bus.data.repositories.opts.LoadRouteOptions;
import com.pgis.bus.data.repositories.opts.LoadRouteRelationOptions;
import com.pgis.bus.data.repositories.opts.UpdateRouteOptions;
import com.pgis.bus.net.models.StringValueModel;
import com.pgis.bus.net.models.TimeIntervalModel;
import com.pgis.bus.net.models.route.RouteInfoModel;
import com.pgis.bus.net.models.route.RouteTypeModel;
import com.pgis.bus.net.models.route.RoutesListModel;
import com.pgis.bus.net.orm.DayEnum;

public class RoutesRepository2 extends Repository implements IRoutesModelRepository {
	private static final Logger log = LoggerFactory.getLogger(RoutesRepository.class);

	public RoutesRepository(IDBConnectionManager connManager) {
		super(connManager);
	}

	public RoutesRepository(Connection c, boolean isClosed, boolean isCommited) {
		super();
		this.connection = c;
		this.isClosed = isClosed;
		this.isCommited = isCommited;
	}

	@Override
	public Collection<RouteGeoData> getGeoDataByRoutePart(RoutePart routePart, String lang_id)
			throws RepositoryException {
		Connection c = super.getConnection();
		Collection<RouteGeoData> relations = null;

		try {
			String query = "SELECT" + " position_index 		as index," + " value          		as station_name,"
					+ " geometry(location)  as station_location," + " geometry(geom)      as relation_geom"
					+ " FROM bus.route_relations"
					+ " JOIN bus.stations ON bus.route_relations.station_b_id = bus.stations.id"
					+ " JOIN bus.string_values ON bus.string_values.key_id = bus.stations.name_key"
					+ " WHERE rway_id = ? AND position_index >= ? AND position_index <= ?"
					+ " AND lang_id = bus.lang_enum(?) ORDER BY position_index; ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, routePart.getRouteWayID());
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
					stationLocation = (PGgeometry) key.getObject("station_location");

				PGgeometry relationGeom = null;
				if (key.getObject("relation_geom") != null) {
					relationGeom = (PGgeometry) key.getObject("relation_geom");
				}
				RouteGeoData data = new RouteGeoData();
				data.setIndex(index);
				if (relationGeom != null)
					data.setRelationGeom((LineString) relationGeom.getGeometry());
				if (stationLocation != null)
					data.setStationLocation((Point) stationLocation.getGeometry());
				data.setStationName(stationName);

				relations.add(data);
			}
		} catch (SQLException e) {
			relations = null;
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return relations;
	}

	@Override
	public RouteWay getRouteWay(int route_id, boolean directType, LoadDirectRouteOptions opts)
			throws RepositoryException {
		Connection c = super.getConnection();
		RouteWay directRoute = null;

		try {
			int direct_typeDB = directType ? 1 : 0;
			String query = "SELECT * from bus.route_ways WHERE " + "route_id = ? AND " + "direct = pg_catalog.bit(?);";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, route_id);
			ps.setString(2, Integer.toString(direct_typeDB));
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				int id = key.getInt("id");
				directRoute = new RouteWay();
				directRoute.setId(id);
				directRoute.setDirect(directType);
				directRoute.setRouteID(route_id);
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		if (directRoute == null)
			return directRoute;
		if (opts.isLoadScheduleData()) {
			directRoute.setSchedule(getSchedule(directRoute.getId()));
		}
		if (opts.isLoadRouteRelationsData()) {
			directRoute.setRouteRelations(getRouteRelations(directRoute.getId(), opts.getLoadRouteRelationOptions()));
		}
		return directRoute;
	}

	private Collection<RouteRelation> getRouteRelations(int routeWayID,
			LoadRouteRelationOptions loadRouteRelationOptions) throws RepositoryException {

		Connection c = super.getConnection();
		Collection<RouteRelation> relations = null;

		try {

			String query = "SELECT id,station_a_id,station_b_id,position_index,distance,"
					+ "ev_time, geometry(geom) as geom FROM "
					+ "bus.route_relations WHERE rway_id = ? ORDER BY position_index;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, routeWayID);
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
				relation.setRouteWayID(routeWayID);
				relation.setStationAId(station_a_id);
				relation.setStationBId(station_b_id);
				relation.setPosition_index(position_index);
				relation.setDistance(distance);
				relation.setEv_time(ev_time);
				if (geom != null)
					relation.setGeom(PolyLineModelFactory.createModel(geom.getGeometry()));
				relations.add(relation);
			}

			if (loadRouteRelationOptions.isLoadStationsData()) {
				IStationsModelRepository stationsRepository = new StationsRepository(c, false, false);
				for (RouteRelation relation : relations) {
					int station_id = relation.getStationBId();
					Station stationB = stationsRepository.getStation(station_id);
					relation.setStationB(stationB);
				}
			}
			// return getRouteRelationsWithStationData(direct_route_id);
		} catch (Exception e) {
			log.error("db exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}

		return relations;
	}

	@Override
	public Schedule getSchedule(int routeWayID) throws RepositoryException {
		Connection c = super.getConnection();
		Schedule schedule = null;

		try {

			String query = "SELECT * from bus.schedule WHERE rway_id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, routeWayID);
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				int id = key.getInt("id");
				schedule = new Schedule();
				schedule.setDirectRouteId(routeWayID);
				schedule.setId(id);
				schedule.setScheduleGroups(getScheduleGroups(schedule.getId()));
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}

		return schedule;
	}

	@Override
	public Collection<Timetable> getTimeTables(int schedule_group_id) throws RepositoryException {
		Connection c = super.getConnection();
		Collection<Timetable> timeTables = null;

		try {
			String query = "SELECT * from bus.timetable WHERE schedule_group_id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, schedule_group_id);
			ResultSet key = ps.executeQuery();
			timeTables = new ArrayList<Timetable>();
			while (key.next()) {
				int id = key.getInt("id");

				Time time_A = (Time) key.getObject("time_A");
				Time time_B = (Time) key.getObject("time_B");
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
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}

		return timeTables;
	}

	@Override
	public Collection<ScheduleGroupDay> getScheduleGroupDays(int schedule_group_id) throws RepositoryException {
		Connection c = super.getConnection();
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
				day.setScheduleGroupID(schedule_group_id);
				day.setDayID(day_id);
				days.add(day);
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}

		return days;
	}

	@Override
	public Collection<ScheduleGroup> getScheduleGroups(int schedule_id) throws RepositoryException {
		Connection c = super.getConnection();
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
				group.setScheduleID(schedule_id);
				group.setDays(getScheduleGroupDays(id));
				group.setTimetables(getTimeTables(id));
				groups.add(group);
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}

		return groups;
	}

	@Override
	public Collection<Route> getRoutes(String routeTypeID, int city_id, LoadRouteOptions opts)
			throws RepositoryException {

		Connection c = super.getConnection();
		Collection<Route> routes = null;

		try {
			String query = "SELECT * from bus.routes WHERE " + "city_id = ? AND "
					+ "route_type_id = bus.route_type_enum(?);";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, city_id);
			ps.setString(2, routeTypeID);
			ResultSet key = ps.executeQuery();
			routes = new ArrayList<Route>();
			while (key.next()) {
				int id = key.getInt("id");
				int number_key = key.getInt("number_key");
				double cost = key.getDouble("cost");
				Route route = new Route();
				route.setId(id);
				route.setCost(cost);
				route.setNumberKey(number_key);
				route.setCityID(city_id);
				route.setRouteTypeID(routeTypeID);
				routes.add(route);
			}

			try {
				IStringValuesRepository stringValuesRepository = null;

				if (opts.isLoadRouteNamesData()) {
					stringValuesRepository = new StringValuesRepository(c, false, false);
				}

				for (Route route : routes) {
					if (opts.isLoadRouteNamesData()) {

						Collection<StringValue> number = stringValuesRepository.getStringValues(route.getNumberKey());
						route.setNumber(StringValueModelFactory.createModels(number));
					}
					if (opts.isLoadDirectRouteData()) {
						RouteWay dRoute = getRouteWay(route.getId(), true, opts.getDirectRouteOptions());
						RouteWay rRoute = getRouteWay(route.getId(), false, opts.getDirectRouteOptions());
						route.setDirectRouteWay(dRoute);
						route.setReverseRouteWay(rRoute);
					}

				}
			} catch (Exception e) {
				routes = null;
				log.error("can not read database", e);
				super.throwable(e, RepositoryException.err_enum.c_sql_err);
			}
		} catch (Exception e) {
			routes = null;
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return routes;
	}

	@Override
	public Route getRoute(int route_id, LoadRouteOptions opts) throws RepositoryException {
		Connection c = super.getConnection();
		Route route = null;

		try {
			String query = "SELECT * from bus.routes WHERE id = ?; ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, route_id);
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				int id = key.getInt("id");
				int city_id = key.getInt("city_id");
				String routeTypeID = key.getString("route_type_id");
				int number_key = key.getInt("number_key");
				double cost = key.getDouble("cost");

				route = new Route();
				route.setId(id);
				route.setCost(cost);
				route.setNumberKey(number_key);
				route.setCityID(city_id);
				route.setRouteTypeID(routeTypeID);

			}
			if (route == null)
				throw new Exception("not found");
			RoutesRepository rep = new RoutesRepository(c, false, false);
			if (opts.isLoadRouteNamesData()) {
				IStringValuesRepository stringValuesRepository = new StringValuesRepository(c, false, false);
				Collection<StringValue> number = stringValuesRepository.getStringValues(route.getNumberKey());
				route.setNumber(StringValueModelFactory.createModels(number));

			}
			if (opts.isLoadDirectRouteData()) {
				RouteWay dRoute = rep.getRouteWay(route.getId(), true, opts.getDirectRouteOptions());
				RouteWay rRoute = rep.getRouteWay(route.getId(), false, opts.getDirectRouteOptions());
				route.setDirectRouteWay(dRoute);
				route.setReverseRouteWay(rRoute);
			}

		} catch (Exception e) {
			route = null;
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
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
			throw new RepositoryException(RepositoryException.err_enum.c_id_notFind);
		}

	}

	private void updateSchedule(Schedule s, Connection c) throws Exception {
		this.removeSchedule(s.getId(), c);
		this.insertSchedule(s, c);
	}

	private void insertScheduleGroupDay(ScheduleGroupDay d, Connection c) throws Exception {
		String query = "INSERT INTO bus.schedule_group_days (schedule_group_id,day_id) "
				+ "VALUES(?,bus.day_enum(?)) RETURNING id;";
		PreparedStatement ps = c.prepareStatement(query);
		ps.setInt(1, d.getScheduleGroupID());
		ps.setString(2, d.getDayID().name());
		ResultSet key = ps.executeQuery();
		if (key.next()) {
			int id = key.getInt("id");
			d.setId(id);

		} else {
			throw new RepositoryException("not found new id and name_key");
		}
	}

	private void insertScheduleGroup(ScheduleGroup scheduleGroup, Connection c) throws Exception {
		String query = "INSERT INTO bus.schedule_groups (schedule_id) " + "VALUES(?) RETURNING id;";
		PreparedStatement ps = c.prepareStatement(query);
		ps.setInt(1, scheduleGroup.getScheduleID());
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

	private void insertSchedule(Schedule schedule, Connection c) throws Exception {
		String query = "INSERT INTO bus.schedule (rway_id) " + "VALUES(?) RETURNING id;";
		PreparedStatement ps = c.prepareStatement(query);
		ps.setInt(1, schedule.getDirectRouteId());
		ResultSet key = ps.executeQuery();
		if (key.next()) {
			int id = key.getInt("id");
			schedule.setId(id);

		} else {

			throw new RepositoryException(RepositoryException.err_enum.c_id_notFind);
		}
		for (ScheduleGroup g : schedule.getScheduleGroups()) {
			insertScheduleGroup(g, c);

		}
	}

	private void removeSchedule(int scheduleID, Connection c) throws Exception {
		String query = "DELETE FROM bus.schedule WHERE id = ?";
		PreparedStatement ps = c.prepareStatement(query);
		ps.setInt(1, scheduleID);
		ps.execute();

	}

	private void insertDirectRoute(RouteWay rWay, Connection c) throws Exception {

		String query = "INSERT INTO bus.routes_ways (route_id,direct) " + "VALUES(?,pg_catalog.bit(?)) RETURNING id; ";
		PreparedStatement ps = c.prepareStatement(query);

		ps.setInt(1, rWay.getRouteID());
		rWay.updateIDs();
		ps.setString(2, Integer.toString(rWay.isDirect() ? 1 : 0));
		ResultSet key = ps.executeQuery();
		if (key.next()) {
			int id = key.getInt("id");
			rWay.setId(id);
		} else {
			throw new RepositoryException(RepositoryException.err_enum.c_insertDirectRoute);
		}
		insertSchedule(rWay.getSchedule(), c);
		for (RouteRelation r : rWay.getRouteRelations()) {
			insertRouteRelation(r, c);
		}

	}

	private void deleteRouteRelations(int routeWayID, Connection c) throws Exception {
		String query = "DELETE FROM bus.route_relations WHERE rway_id = ?; ";
		PreparedStatement ps = c.prepareStatement(query);
		ps.setInt(1, routeWayID);
		ps.execute();
	}

	private void insertRouteRelation(RouteRelation r, Connection c) throws Exception {
		r.optimizePoints();
		String query = "SELECT * from bus.insert_route_relation(?,?,?,?,?); ";
		PreparedStatement ps = c.prepareStatement(query);

		// System.out.println
		ps.setInt(1, r.getRouteWayID());
		ps.setInt(2, r.getStationAId());
		ps.setInt(3, r.getStationBId());
		ps.setInt(4, r.getPosition_index());

		PGgeometry geom = null;
		if (r.getGeom() != null) {
			log.debug(r.getGeom().toString());
			geom = new PGgeometry(GeoObjectsFactory.createLine(r.getGeom()));
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

	private void insertStationsForRoute(Route route, Connection c) throws Exception {
		// insert stations from directWay
		IStationsModelRepository stationsRepository = new StationsRepository(c, false, false);
		for (RouteRelation r : route.getDirectRouteWay().getRouteRelations()) {
			Station s = r.getStationB();
			s.setCityID(route.getCityID());
			Integer id = s.getId();
			if (id == null || id <= 0) {
				s = stationsRepository.insertStation(s);
				r.setStationBId(s.getId());
				r.setStationB(s);

				for (RouteRelation rr : route.getDirectRouteWay().getRouteRelations()) {
					if (rr.getStationB().getId() == id)
						rr.setStationB(s);
				}
				if (route.getReverseRouteWay() != null) {
					for (RouteRelation rr : route.getReverseRouteWay().getRouteRelations()) {
						if (rr.getStationB().getId() == id)
							rr.setStationB(s);
					}
				}
			} else {
				r.setStationB(s);
			}

		}
		if (route.getReverseRouteWay() == null) {
			route.setReverseRouteWay(RouteWay.createReverseByDirect(route.getDirectRouteWay()));
		} else {

			// insert stations from reverseWay
			for (RouteRelation r : route.getReverseRouteWay().getRouteRelations()) {
				Station s = r.getStationB();
				s.setCityID(route.getCityID());
				int id = s.getId();
				if (id <= 0) {
					s = stationsRepository.insertStation(s);
					r.setStationBId(s.getId());
					r.setStationB(s);
				} else {
					r.setStationB(s);
				}
			}
		}

	}

	@Override
	public void insert(Route route) throws RepositoryException {
		Connection c = super.getConnection();
		try {
			// validate
			if (route.getDirectRouteWay() == null) {
				throw new RepositoryException(RepositoryException.err_enum.c_route_data);
			}
			if (route.getReverseRouteWay() != null) {
				throw new RepositoryException(RepositoryException.err_enum.c_route_data);
			}
			this.insertStationsForRoute(route, c);
			// route.updateIDs();
			String query = "INSERT INTO bus.routes (city_id,cost,route_type_id) "
					+ "VALUES(?,?,bus.route_type_enum(?)) RETURNING id,number_key; ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, route.getCityID());
			ps.setDouble(2, route.getCost());
			ps.setString(3, route.getRouteTypeID());
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				int id = key.getInt("id");
				int number_key = key.getInt("number_key");
				route.setId(id);
				route.setNumberKey(number_key);
			} else {
				throw new Exception("not found new id and name_key");
			}

			insertDirectRoute(route.getDirectRouteWay(), c);
			insertDirectRoute(route.getReverseRouteWay(), c);
			if (route.getNumber() != null) {
				IStringValuesRepository svRep = new StringValuesRepository(c, false, false);
				for (StringValueModel v : route.getNumber()) {
					svRep.insert(new StringValue(route.getNumberKey(), v));
				}
			}
			super.commit(c);
		} catch (Exception e) {
			log.error("insertRoute() exception: ", e);
			super.rollback(c);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
	}

	@Override
	public void remove(int routeID) throws RepositoryException {
		Connection c = super.getConnection();
		try {
			String query = "DELETE FROM bus.routes WHERE id=?;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, routeID);
			ps.execute();
			super.commit(c);
		} catch (SQLException e) {
			log.error("removeRoute() exception: ", e);
			super.rollback(c);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
	}

	@Override
	public void update(Route updateRoute, UpdateRouteOptions opts) throws RepositoryException {
		Connection c = super.getConnection();
		try {

			if (opts.isUpdateMainInfo()) {
				String query = "UPDATE bus.routes SET city_id = ?, cost = ?, "
						+ "route_type_id = bus.route_type_enum(?) WHERE id = ? ;";
				PreparedStatement ps = c.prepareStatement(query);
				ps.setInt(1, updateRoute.getCityID());
				ps.setDouble(2, updateRoute.getCost());
				ps.setString(3, updateRoute.getRouteTypeID());
				ps.setInt(4, updateRoute.getId());
				ps.execute();

				IStringValuesRepository svRep = new StringValuesRepository(c, false, false);
				svRep.update(updateRoute.getNumberKey(),
						StringValue.createCollection(updateRoute.getNumberKey(), updateRoute.getNumber()));

			}
			if (opts.isUpdateSchedule()) {
				// updateRoute.updateIDs();
				if (updateRoute.getDirectRouteWay() != null && updateRoute.getDirectRouteWay().getSchedule() != null) {
					Schedule s1 = updateRoute.getDirectRouteWay().getSchedule();
					s1.setDirectRouteId(updateRoute.getDirectRouteWay().getId());
					updateSchedule(s1, c);
				}
				if (updateRoute.getReverseRouteWay() != null && updateRoute.getReverseRouteWay().getSchedule() != null) {
					Schedule s2 = updateRoute.getReverseRouteWay().getSchedule();
					s2.setDirectRouteId(updateRoute.getReverseRouteWay().getId());
					updateSchedule(s2, c);
				}
			}

			if (opts.isUpdateRouteRelations()) {
				this.insertStationsForRoute(updateRoute, c);
				this.deleteRouteRelations(updateRoute.getDirectRouteWay().getId(), c);
				this.deleteRouteRelations(updateRoute.getReverseRouteWay().getId(), c);
				updateRoute.getDirectRouteWay().setId(updateRoute.getDirectRouteWay().getId());
				updateRoute.getReverseRouteWay().setId(updateRoute.getReverseRouteWay().getId());

				for (RouteRelation r : updateRoute.getDirectRouteWay().getRouteRelations()) {
					System.out.println(r.toString());
					insertRouteRelation(r, c);
				}
				for (RouteRelation r : updateRoute.getReverseRouteWay().getRouteRelations()) {
					System.out.println(r.toString());
					insertRouteRelation(r, c);
				}
			}
			super.commit(c);
		} catch (Exception e) {
			log.error("updateRoute() exception: ", e);
			super.rollback(c);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
	}

	@Override
	public RoutesListModel getRoutesList(int cityID, String routeTypeID, String langID) throws RepositoryException {
		Connection c = super.getConnection();
		Collection<RouteInfoModel> routesList = null;

		try {
			String query = "select EXTRACT(EPOCH FROM time_a) as time_a, " + " EXTRACT(EPOCH FROM time_b) as time_b,"
					+ " EXTRACT(EPOCH FROM freq) as interval, "
					+ "id, cost, number, start_station, finish_station from bus.view_list_routes "
					+ "where city_id = ? and route_type_id = bus.route_type_enum(?) and lang_id = bus.lang_enum(?);";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, cityID);
			ps.setString(2, routeTypeID);
			ps.setString(3, langID);
			ResultSet key = ps.executeQuery();
			routesList = new ArrayList<RouteInfoModel>();

			while (key.next()) {
				int id = key.getInt("id");
				String number = key.getString("number");
				double cost = key.getDouble("cost");
				TimeIntervalModel startWork = new TimeIntervalModel(key.getDouble("time_a"));
				TimeIntervalModel finishWork = new TimeIntervalModel(key.getDouble("time_b"));
				TimeIntervalModel interval = new TimeIntervalModel(key.getDouble("interval"));
				String startStation = key.getString("start_station");
				String finishStation = key.getString("finish_station");

				RouteInfoModel routeInfoModel = new RouteInfoModel();
				routeInfoModel.setCost(cost);
				routeInfoModel.setId(id);
				routeInfoModel.setNumber(number);
				routeInfoModel.setMinInterval(interval);
				routeInfoModel.setMaxInterval(interval);
				routeInfoModel.setStartWork(startWork);
				routeInfoModel.setFinishWork(finishWork);
				routeInfoModel.setStartStation(startStation);
				routeInfoModel.setFinishStation(finishStation);
				routesList.add(routeInfoModel);

			}
			RoutesListModel model = new RoutesListModel(routesList);
			model.setCityID(cityID);
			model.setRouteType(new RouteTypeModel(routeTypeID));
			return model;
		} catch (Exception e) {
			log.error("can not read routes list", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return null;
	}

}
