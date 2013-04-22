package com.pgis.bus.data.repositories.orm.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.postgis.LineString;
import org.postgis.PGgeometry;
import org.postgresql.util.PGInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.RouteRelation;
import com.pgis.bus.data.orm.RouteWay;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.repositories.Repository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.orm.IRoutesRepository;

public class RoutesRepository extends Repository implements IRoutesRepository {

	private static final Logger log = LoggerFactory.getLogger(RoutesRepository.class);

	public RoutesRepository(IConnectionManager connManager) {
		super(connManager);
	}

	@Override
	public Route get(int routeID) throws SQLException {
		Route route = null;
		try {
			Connection c = super.getConnection();
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
		}
		return route;
	}

	@Override
	public RouteWay getRouteWay(int routeID, boolean directType) throws SQLException {
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
		}
		return routeWay;
	}

	@Override
	public void insert(Route route) throws SQLException {
		Connection c = super.getConnection();
		try {
			// validate
			if (route.getDirectRouteWay() == null) {
				throw new RepositoryException(RepositoryException.err_enum.c_route_data);
			}
			if (route.getReverseRouteWay() != null) {
				throw new RepositoryException(RepositoryException.err_enum.c_route_data);
			}
			this.insertStationsForRoute(route);
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

			this.insertRouteWay(route.getDirectRouteWay());
			this.insertRouteWay(route.getReverseRouteWay());
			if (route.getNumber() != null) {
				StringValuesRepository svRep = new StringValuesRepository(this.connManager);
				svRep.setRepositoryExternConnection(c);
				for (StringValue v : route.getNumber()) {
					svRep.insert(v);
				}
			}
		} catch (Exception e) {
			log.error("insertRoute() exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}
	}

	private void insertRouteWay(RouteWay routeWay) throws SQLException {
		Connection c = super.getConnection();
		String query = "INSERT INTO bus.routes_ways (route_id,direct) " + "VALUES(?,pg_catalog.bit(?)) RETURNING id; ";
		PreparedStatement ps = c.prepareStatement(query);

		ps.setInt(1, routeWay.getRouteID());
		routeWay.updateIDs();
		ps.setString(2, Integer.toString(routeWay.isDirect() ? 1 : 0));
		ResultSet key = ps.executeQuery();
		if (key.next()) {
			int id = key.getInt("id");
			routeWay.setId(id);
		} else {
			throw new RepositoryException(RepositoryException.err_enum.c_insertDirectRoute);
		}
		ScheduleRepository scheduleRep = new ScheduleRepository(this.connManager);
		scheduleRep.setRepositoryExternConnection(c);
		scheduleRep.insert(routeWay.getSchedule());
		for (RouteRelation r : routeWay.getRouteRelations()) {
			this.insertRouteRelation(r);
		}
	}

	private void insertRouteRelation(RouteRelation r) throws SQLException {
		Connection c = super.getConnection();
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
			geom = new PGgeometry(r.getGeom());
		}
		ps.setObject(5, geom);
		ResultSet key = ps.executeQuery();
		if (key.next()) {
			int id = key.getInt(1);
			r.setId(id);
		} else {
			throw new SQLException("not found new id and name_key");
		}

	}

	private void insertStationsForRoute(Route route) throws SQLException {
		// insert stations from directWay
		Connection c = super.getConnection();
		StationsRepository stationsRepository = new StationsRepository(this.connManager);
		stationsRepository.setRepositoryExternConnection(c);
		for (RouteRelation r : route.getDirectRouteWay().getRouteRelations()) {
			Station s = r.getStationB();
			s.setCityID(route.getCityID());
			Integer id = s.getId();
			if (id == null || id <= 0) {
				stationsRepository.insert(s);
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
					stationsRepository.insert(s);
					r.setStationBId(s.getId());
					r.setStationB(s);
				} else {
					r.setStationB(s);
				}
			}
		}
	}

	@Override
	public void remove(int routeID) throws SQLException {
		Connection c = super.getConnection();
		try {
			String query = "DELETE FROM bus.routes WHERE id=?;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, routeID);
			ps.execute();
		} catch (SQLException e) {
			log.error("removeRoute() exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}

	}

	@Override
	public void update(Route route) throws SQLException {
		if (route == null)
			return;
		Connection c = super.getConnection();
		try {
			String query = "UPDATE bus.routes SET city_id = ?, cost = ?, "
					+ "route_type_id = bus.route_type_enum(?) WHERE id = ? ;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, route.getCityID());
			ps.setDouble(2, route.getCost());
			ps.setString(3, route.getRouteTypeID());
			ps.setInt(4, route.getId());
			ps.execute();
			RoutesRepository rep = new RoutesRepository(this.connManager);
			rep.setRepositoryExternConnection(c);
			rep.updateNumber(route.getNumberKey(), route.getNumber());
			rep.update(route.getDirectRouteWay());
			rep.update(route.getReverseRouteWay());
		} catch (Exception e) {
			log.error("updateRoute() exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}
	}

	@Override
	public void updateNumber(int numberKey, Collection<StringValue> number) throws SQLException {
		if (number == null)
			return;
		Connection c = super.getConnection();
		try {
			StringValuesRepository svRep = new StringValuesRepository(this.connManager);
			svRep.setRepositoryExternConnection(c);
			svRep.update(numberKey, number);
		} catch (Exception e) {
			log.error("updateNumber() exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}
	}

	private void removeRouteRelations(int routeWayID) throws SQLException {
		Connection c = super.getConnection();
		String query = "DELETE FROM bus.route_relations WHERE rway_id = ?; ";
		PreparedStatement ps = c.prepareStatement(query);
		ps.setInt(1, routeWayID);
		ps.execute();
	}

	@Override
	public void update(RouteWay routeWay) throws SQLException {
		this.removeRouteRelations(routeWay.getId());
		for (RouteRelation r : routeWay.getRouteRelations()) {
			System.out.println(r.toString());
			this.insertRouteRelation(r);
		}
		Connection c = super.getConnection();
		ScheduleRepository scheduleRep = new ScheduleRepository(this.connManager);
		scheduleRep.setRepositoryExternConnection(c);
		scheduleRep.update(routeWay.getSchedule());
	}

	@Override
	public Collection<RouteRelation> getRouteWayRelations(int routeWayID) throws SQLException {
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
		}

		return null;

	}

}
