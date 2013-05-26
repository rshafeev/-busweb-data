package com.pgis.bus.data.repositories.orm.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.postgis.LineString;
import org.postgis.PGgeometry;
import org.postgresql.util.PGInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.exp.RepositoryException;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.RouteRelation;
import com.pgis.bus.data.orm.RouteWay;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.orm.type.LineStringEx;
import com.pgis.bus.data.repositories.orm.IRoutesRepository;
import com.pgis.bus.net.models.city.CityModel;
import com.pgis.bus.net.models.geom.PointModel;

public class RoutesRepository extends Repository implements IRoutesRepository {

	private static final Logger log = LoggerFactory.getLogger(RoutesRepository.class);

	public RoutesRepository(IConnectionManager connManager) {
		super(connManager);
	}

	@Override
	public Collection<Route> getAll(LangEnum langID, int cityID, String routeTypeID) throws RepositoryException {

		Collection<Route> routes = null;
		try {
			Connection c = super.getConnection();
			String query = "select routes.id, number_key,cost, visible, value as number from bus.routes  "
					+ " left join bus.string_values on string_values.key_id = number_key "
					+ "where city_id = ? and route_type_id = bus.route_type_enum(?) and "
					+ "(lang_id = bus.lang_enum(?) or lang_id is null);";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, cityID);
			ps.setString(2, routeTypeID);
			ps.setString(3, langID.name());
			ResultSet rs = ps.executeQuery();

			routes = new ArrayList<Route>();
			while (rs.next()) {
				int id = rs.getInt("id");
				int number_key = rs.getInt("number_key");
				double cost = rs.getDouble("cost");
				boolean visible = rs.getBoolean("visible");
				String numb = rs.getString("number");
				Route route = new Route(this.connManager);
				route.setId(id);
				route.setCost(cost);
				route.setNumberKey(number_key);
				route.setCityID(cityID);
				route.setRouteTypeID(routeTypeID);
				route.setVisible(visible);
				route.setNumber(new ArrayList<StringValue>(Arrays.asList(new StringValue(langID, numb))));
				routes.add(route);
			}
		} catch (Exception e) {
			routes = null;
			super.handeThrowble(e);
		}
		return routes;
	}

	@Override
	public Route get(int routeID) throws RepositoryException {
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
				boolean visible = key.getBoolean("visible");
				route = new Route(this.connManager);
				route.setId(id);
				route.setCost(cost);
				route.setNumberKey(number_key);
				route.setCityID(city_id);
				route.setRouteTypeID(routeTypeID);
				route.setVisible(visible);
			}

		} catch (Exception e) {
			route = null;
			super.handeThrowble(e);
		}
		return route;
	}

	@Override
	public RouteWay getRouteWay(int routeID, boolean directType) throws RepositoryException {

		RouteWay routeWay = null;

		try {
			Connection c = super.getConnection();
			int direct_typeDB = directType ? 1 : 0;
			String query = "SELECT * from bus.route_ways WHERE " + "route_id = ? AND " + "direct = pg_catalog.bit(?);";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, routeID);
			ps.setString(2, Integer.toString(direct_typeDB));
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				int id = key.getInt("id");
				routeWay = new RouteWay(this.connManager);
				routeWay.setId(id);
				routeWay.setDirect(directType);
				routeWay.setRouteID(routeID);
			}
		} catch (Exception e) {
			super.handeThrowble(e);
		}
		return routeWay;
	}

	@Override
	public void insert(Route route) throws RepositoryException {
		if (route == null)
			return;
		try {
			Connection c = super.getConnection();
			route.setConnManager(null);

			// validate
			if (route.getDirectRouteWay() == null) {
				throw new RepositoryException(RepositoryException.err_enum.orm_obj_invalid);
			}
			if (route.getReverseRouteWay() == null) {
				RouteWay reverseWay = Route.makeReverseWay(route.getDirectRouteWay().clone());
				route.setReverseRouteWay(reverseWay);
			}
			if (route.getNumber() == null || route.getNumber().size() == 0) {
				throw new RepositoryException(RepositoryException.err_enum.orm_obj_invalid);
			}
			for (StringValue number : route.getNumber()) {
				boolean isExist = this.isExist(route.getCityID(), route.getRouteTypeID(), number);
				log.debug("isExist {} {} {} : {}", new Object[] { route.getCityID(), route.getRouteTypeID(), number,
						isExist });

				if (isExist == true) {
					throw new RepositoryException(RepositoryException.err_enum.already_exist);
				}
			}

			// insert
			this.insertStationsForRoute(route);
			String query = "INSERT INTO bus.routes (city_id,cost,route_type_id,visible) "
					+ "VALUES(?,?,bus.route_type_enum(?),false) RETURNING id,number_key; ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, route.getCityID());
			ps.setDouble(2, route.getCost());
			ps.setString(3, route.getRouteTypeID());
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				int id = key.getInt("id");
				int number_key = key.getInt("number_key");
				route.setId(id);
				route.setVisible(false);
				route.setNumberKey(number_key);
			} else {
				throw new Exception("not found new id and name_key");
			}

			this.insertRouteWay(route.getDirectRouteWay());
			this.insertRouteWay(route.getReverseRouteWay());

			StringValuesRepository svRep = new StringValuesRepository(this.connManager);
			svRep.setRepositoryExternConnection(c);

			for (StringValue v : route.getNumber()) {
				v.setKeyID(route.getNumberKey());
				svRep.insert(v);
			}

			// Заполим маршрут новыми данными
			route.setDirectRouteWay(this.getRouteWay(route.getId(), true));
			route.setReverseRouteWay(this.getRouteWay(route.getId(), false));
		} catch (Exception e) {
			super.handeThrowble(e);
		} finally {
			route.setConnManager(super.connManager);
		}
	}

	private void insertRouteWay(RouteWay routeWay) throws RepositoryException {
		try {
			routeWay.setConnManager(null);
			Connection c = super.getConnection();
			String query = "INSERT INTO bus.route_ways (route_id,direct) "
					+ "VALUES(?,pg_catalog.bit(?)) RETURNING id; ";
			PreparedStatement ps = c.prepareStatement(query);

			ps.setInt(1, routeWay.getRouteID());
			routeWay.updateIDs();
			ps.setString(2, Integer.toString(routeWay.isDirect() ? 1 : 0));
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				int id = key.getInt("id");
				routeWay.setId(id);
			} else {
				throw new RepositoryException(RepositoryException.err_enum.insert_rway);
			}
			ScheduleRepository scheduleRep = new ScheduleRepository(this.connManager);
			scheduleRep.setRepositoryExternConnection(c);
			scheduleRep.insert(routeWay.getSchedule());
			if (routeWay.getRouteRelations() == null || routeWay.getRouteRelations().size() < 2)
				throw new RepositoryException(RepositoryException.err_enum.orm_obj_invalid);
			for (RouteRelation r : routeWay.getRouteRelations()) {
				this.insertRouteRelation(r);
			}
		} catch (Exception e) {
			super.handeThrowble(e);
		} finally {
			routeWay.setConnManager(this.connManager);
		}
	}

	private void insertRouteRelation(RouteRelation r) throws RepositoryException {
		try {
			r.setConnManager(null);
			Connection c = super.getConnection();
			String query = "SELECT * from bus.insert_route_relation(?,?,?,?,?); ";
			PreparedStatement ps = c.prepareStatement(query);

			// System.out.println
			ps.setInt(1, r.getRouteWayID());
			ps.setInt(2, r.getStationAId());
			ps.setInt(3, r.getStationBId());
			ps.setInt(4, r.getPositionIndex());

			PGgeometry geom = null;
			if (r.getGeom() != null) {
				geom = new PGgeometry(r.getGeom());
			}
			ps.setObject(5, geom);
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				int id = key.getInt(1);
				r.setId(id);
			} else {
				throw new RepositoryException("not found new id and name_key");
			}
		} catch (Exception e) {
			super.handeThrowble(e);
		} finally {
			r.setConnManager(this.connManager);
		}
	}

	private void insertStationsForRoute(Route route) throws RepositoryException {
		// insert stations from directWay
		try {
			Connection c = super.getConnection();
			if (route.getDirectRouteWay() == null && route.getReverseRouteWay() == null)
				return;
			StationsRepository stationsRepository = new StationsRepository(this.connManager);
			stationsRepository.setRepositoryExternConnection(c);

			if (route.getDirectRouteWay() != null) {
				for (RouteRelation r : route.getDirectRouteWay().getRouteRelations()) {
					try {
						r.setConnManager(null);
						Station s = r.getStationB();
						if (s == null && r.getStationBId() > 0)
							continue;
						s.setCityID(route.getCityID());
						Integer id = s.getId();
						if (id == null || id <= 0) {
							stationsRepository.insert(s);
							r.setStationBId(s.getId());
							r.setStationB(s);

							for (RouteRelation rr : route.getDirectRouteWay().getRouteRelations()) {
								if (rr.getStationB() != null && rr.getStationB().getId() == id)
									rr.setStationB(s);
							}
							if (route.getReverseRouteWay() != null) {
								for (RouteRelation rr : route.getReverseRouteWay().getRouteRelations()) {
									if (rr.getStationB() != null && rr.getStationB().getId() == id)
										rr.setStationB(s);
								}
							}
						} else {
							r.setStationB(s);
						}
					} finally {
						r.setConnManager(this.connManager);
					}

				}
			}
			if (route.getReverseRouteWay() != null) {
				// insert stations from reverseWay
				for (RouteRelation r : route.getReverseRouteWay().getRouteRelations()) {
					try {
						r.setConnManager(null);
						Station s = r.getStationB();
						if (s == null && r.getStationBId() > 0)
							continue;
						s.setCityID(route.getCityID());
						int id = s.getId();
						if (id <= 0) {
							stationsRepository.insert(s);
							r.setStationBId(s.getId());
							r.setStationB(s);
						} else {
							r.setStationB(s);
						}
					} finally {
						r.setConnManager(this.connManager);
					}
				}
			}
		} catch (Exception e) {
			super.handeThrowble(e);
		}
	}

	@Override
	public void remove(int routeID) throws RepositoryException {
		try {
			Connection c = super.getConnection();
			String query = "DELETE FROM bus.routes WHERE id=?;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, routeID);
			ps.execute();
		} catch (Exception e) {
			super.handeThrowble(e);
		}

	}

	@Override
	public void update(Route route) throws RepositoryException {
		if (route == null)
			return;

		try {
			Connection c = super.getConnection();
			route.setConnManager(null);
			if (route.getId() != null) {
				String query = "UPDATE bus.routes SET city_id = ?, cost = ?, visible = ?, "
						+ "route_type_id = bus.route_type_enum(?) WHERE id = ? ;";
				PreparedStatement ps = c.prepareStatement(query);
				ps.setInt(1, route.getCityID());
				ps.setDouble(2, route.getCost());
				ps.setBoolean(3, route.isVisible());
				ps.setString(4, route.getRouteTypeID());
				ps.setInt(5, route.getId());
				ps.execute();
			}
			this.insertStationsForRoute(route);
			this.updateNumber(route.getNumberKey(), route.getNumber());
			this.update(route.getDirectRouteWay());
			this.update(route.getReverseRouteWay());
		} catch (Exception e) {
			super.handeThrowble(e);
		} finally {
			route.setConnManager(this.connManager);
		}
	}

	@Override
	public void updateNumber(Integer numberKey, Collection<StringValue> number) throws RepositoryException {
		if (number == null || numberKey == null || numberKey <= 0)
			return;
		try {
			Connection c = super.getConnection();
			StringValuesRepository svRep = new StringValuesRepository(this.connManager);
			svRep.setRepositoryExternConnection(c);
			svRep.update(numberKey, number);
		} catch (Exception e) {
			super.handeThrowble(e);
		}
	}

	private void removeRouteRelations(int routeWayID) throws RepositoryException {
		try {
			Connection c = super.getConnection();
			String query = "DELETE FROM bus.route_relations WHERE rway_id = ?; ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, routeWayID);
			ps.execute();
		} catch (Exception e) {
			super.handeThrowble(e);
		}
	}

	private void updateRouteRelations(Integer routeWayID, Collection<RouteRelation> relations)
			throws RepositoryException {
		if (relations == null || routeWayID == null || routeWayID <= 0)
			return;
		try {
			Connection c = super.getConnection();
			StationsRepository stationsRepository = new StationsRepository(this.connManager);
			stationsRepository.setRepositoryExternConnection(c);
			this.removeRouteRelations(routeWayID);
			for (RouteRelation r : relations) {
				log.debug("insert relation");
				r.setConnManager(null);
				try {
					this.insertRouteRelation(r);
				} finally {
					r.setConnManager(this.connManager);
				}
			}
		} catch (Exception e) {
			super.handeThrowble(e);
		}
	}

	@Override
	public void update(RouteWay routeWay) throws RepositoryException {
		if (routeWay == null)
			return;
		try {
			routeWay.setConnManager(null);
			this.updateRouteRelations(routeWay.getId(), routeWay.getRouteRelations());
			if (routeWay.getSchedule() != null) {
				Connection c = super.getConnection();
				ScheduleRepository scheduleRep = new ScheduleRepository(this.connManager);
				scheduleRep.setRepositoryExternConnection(c);
				scheduleRep.update(routeWay.getSchedule());
			}
		} catch (Exception e) {
			super.handeThrowble(e);
		} finally {
			routeWay.setConnManager(this.connManager);
		}
	}

	@Override
	public Collection<RouteRelation> getRouteWayRelations(int routeWayID) throws RepositoryException {

		try {
			Connection c = super.getConnection();
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
				} else if (geomObj != null)
					throw new RepositoryException("can not convert geom to org.pgis.LineString");
				RouteRelation relation = new RouteRelation(this.connManager);
				relation.setId(id);
				relation.setRouteWayID(routeWayID);
				relation.setStationAId(station_a_id);
				relation.setStationBId(station_b_id);
				relation.setPositionIndex(position_index);
				relation.setDistance(distance);
				relation.setMoveTime(ev_time);
				if (geom != null)
					relation.setGeom(new LineStringEx((LineString) geom.getGeometry()));
				relations.add(relation);
			}
			return relations;
		} catch (Exception e) {
			super.handeThrowble(e);
		}

		return null;

	}

	@Override
	public boolean isExist(int cityID, String routeTypeID, StringValue number) throws RepositoryException {
		try {
			Connection c = super.getConnection();
			String query = "select count(*) as count from bus.routes join bus.string_values on key_id = number_key "
					+ "where city_id = ? and route_type_id = bus.route_type_enum(?) and "
					+ "lang_id = bus.lang_enum(?) and value = ? ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, cityID);
			ps.setString(2, routeTypeID);
			ps.setString(3, number.getLangID().name());
			ps.setString(4, number.getValue());
			ResultSet key = ps.executeQuery();
			if (key.next()) {
				int count = key.getInt("count");
				if (count > 0)
					return true;
				return false;
			} else
				throw new RepositoryException(RepositoryException.err_enum.return_data_not_found);

		} catch (Exception e) {
			super.handeThrowble(e);
		}
		return false;
	}

}
