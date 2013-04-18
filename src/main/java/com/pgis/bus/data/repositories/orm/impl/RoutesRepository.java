package com.pgis.bus.data.repositories.orm.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

import org.postgis.LineString;
import org.postgis.PGgeometry;
import org.postgresql.util.PGInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.RouteRelation;
import com.pgis.bus.data.orm.RouteWay;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.ScheduleGroup;
import com.pgis.bus.data.orm.ScheduleGroupDay;
import com.pgis.bus.data.orm.Timetable;
import com.pgis.bus.data.repositories.Repository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.orm.IRoutesRepository;
import com.pgis.bus.net.orm.DayEnum;

public class RoutesRepository extends Repository implements IRoutesRepository {

	private static final Logger log = LoggerFactory.getLogger(RoutesRepository.class);

	public RoutesRepository(IDBConnectionManager connManager) {
		super(connManager);
	}

	public RoutesRepository(IDBConnectionManager connManager, boolean isCommited) {
		super(connManager, isCommited);
	}

	protected RoutesRepository(IDBConnectionManager connManager, Connection c, boolean isClosed, boolean isCommited) {
		super(connManager, isCommited);
		super.isClosed = isClosed;
		super.connection = c;
	}

	@Override
	public Route get(int routeID) throws RepositoryException {
		Connection c = super.getConnection();
		Route route = null;
		try {
			String query = "SELECT * from bus.routes WHERE id = ?; ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, routeID);
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

		} catch (Exception e) {
			route = null;
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return route;
	}

	private Collection<Timetable> getTimeTables(int scheduleGroupId) throws RepositoryException {
		Connection c = super.getConnection();
		Collection<Timetable> timeTables = null;

		try {
			String query = "SELECT * from bus.timetable WHERE schedule_group_id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, scheduleGroupId);
			ResultSet key = ps.executeQuery();
			timeTables = new ArrayList<Timetable>();
			while (key.next()) {
				int id = key.getInt("id");

				Time time_A = (Time) key.getObject("time_A");
				Time time_B = (Time) key.getObject("time_B");
				PGInterval frequency = (PGInterval) key.getObject("frequency");

				Timetable timetable = new Timetable();
				timetable.setId(id);
				timetable.setSchedule_group_id(scheduleGroupId);
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

	private Collection<ScheduleGroupDay> getScheduleGroupDays(int schedule_group_id) throws RepositoryException {
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

	private Collection<ScheduleGroup> getScheduleGroups(int scheduleID) throws RepositoryException {
		Connection c = super.getConnection();
		Collection<ScheduleGroup> groups = null;

		try {

			String query = "SELECT * from bus.schedule_groups WHERE schedule_id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, scheduleID);
			ResultSet key = ps.executeQuery();
			groups = new ArrayList<ScheduleGroup>();
			while (key.next()) {
				int id = key.getInt("id");
				ScheduleGroup group = new ScheduleGroup();
				group.setId(id);
				group.setScheduleID(scheduleID);
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
	public RouteWay getRouteWay(int routeID, boolean directType) throws RepositoryException {
		Connection c = super.getConnection();
		RouteWay routeWay = null;

		try {
			int direct_typeDB = directType ? 1 : 0;
			String query = "SELECT * from bus.route_ways WHERE " + "route_id = ? AND " + "direct = pg_catalog.bit(?);";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, routeID);
			ps.setString(2, Integer.toString(direct_typeDB));
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				int id = key.getInt("id");
				routeWay = new RouteWay();
				routeWay.setId(id);
				routeWay.setDirect(directType);
				routeWay.setRouteID(routeID);
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return routeWay;
	}

	@Override
	public void insert(Route route) throws RepositoryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(int routeID) throws RepositoryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Route route) throws RepositoryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Schedule schedule) throws RepositoryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(RouteWay routeWay) throws RepositoryException {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<RouteRelation> getRouteWayRelations(int routeWayID) throws RepositoryException {
		Connection c = super.getConnection();
		try {
			String query = "SELECT id,station_a_id,station_b_id,position_index,distance,"
					+ "ev_time, geometry(geom) as geom FROM "
					+ "bus.route_relations WHERE rway_id = ? ORDER BY position_index;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, routeWayID);
			ResultSet key = ps.executeQuery();
			Collection<RouteRelation> relations = new ArrayList<RouteRelation>();
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
				} else
					throw new SQLException("can not convert geom to org.pgis.LineString");
				RouteRelation relation = new RouteRelation();
				relation.setId(id);
				relation.setRouteWayID(routeWayID);
				relation.setStationAId(station_a_id);
				relation.setStationBId(station_b_id);
				relation.setPosition_index(position_index);
				relation.setDistance(distance);
				relation.setEv_time(ev_time);
				if (geom != null)
					relation.setGeom((LineString) geom.getGeometry());
				relations.add(relation);
			}
		} catch (Exception e) {
			log.error("db exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}

		return null;

	}

}
