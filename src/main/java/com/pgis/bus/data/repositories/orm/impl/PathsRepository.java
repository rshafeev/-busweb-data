package com.pgis.bus.data.repositories.orm.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import com.pgis.bus.data.models.RouteGeoData;
import com.pgis.bus.data.models.RoutePart;
import org.postgis.LineString;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgresql.util.PGInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.exp.RepositoryException;
import com.pgis.bus.data.helpers.GeoObjectsHelper;
import com.pgis.bus.data.orm.type.Path_t;
import com.pgis.bus.data.params.FindPathsParams;
import com.pgis.bus.data.repositories.orm.IPathsRepository;

public class PathsRepository extends Repository implements IPathsRepository {
	private static final Logger log = LoggerFactory.getLogger(PathsRepository.class);

	public PathsRepository(IConnectionManager connManager) {
		super(connManager);
	}

	@Override
	public Collection<Path_t> findShortestPaths(FindPathsParams params) throws RepositoryException {
		Collection<Path_t> paths = null;
		try {
			Connection c = super.getConnection();
			String query = "select  * from  bus.find_shortest_paths(" + "?," /* city_id */
					+ " geography(?)," /* p1 */
					+ " geography(?)," /* p2 */
					+ " bus.day_enum(?)," /* day_id */
					+ " time  '10:00:00'," /* time_start */
					+ " ?," // max_distance*/
					+ " ?," /* route_types */
					+ " ?," /* _has_transitions */
					+ " ?," /* discount */
					+ " bus.alg_strategy(?)," /* alg_strategy */
					+ " bus.lang_enum(?)) ORDER BY path_id,index;";

			PreparedStatement ps = c.prepareStatement(query);
			int ind = 1;
			ps.setLong(ind++, params.getCityID());
			//ps.setInt(ind++, params.getCityID());
			ps.setObject(ind++, new PGgeometry(GeoObjectsHelper.createPoint(params.getP1())));
			ps.setObject(ind++, new PGgeometry(GeoObjectsHelper.createPoint(params.getP2())));
			ps.setString(ind++, params.getDayID().name());
			//ps.setTime(ind++, params.getTimeStart(), Calendar.getInstance());
			ps.setDouble(ind++, params.getMaxDistance());
			ps.setArray(ind++, c.createArrayOf("text", params.getRouteTypes()));
			ps.setBoolean(ind++, params.isTransitions());
			ps.setArray(ind++, c.createArrayOf("float", params.getDiscounts()));
			ps.setString(ind++, params.getAlgStrategy().name());
			ps.setString(ind++, params.getLangID().name());

			ResultSet key = ps.executeQuery();
			paths = new ArrayList<Path_t>();
			while (key.next()) {
				Path_t pathElem = new Path_t();
				pathElem.path_id = key.getInt("path_id");
				pathElem.index = key.getInt("index");
				pathElem.rway_id = key.getInt("rway_id");
				pathElem.route_type = key.getString("route_type");
				pathElem.route_name = key.getString("route_name");
				pathElem.relation_index_a = key.getInt("relation_index_a");
				pathElem.relation_index_b = key.getInt("relation_index_b");
				pathElem.station_name_a = key.getString("station_name_a");
				pathElem.station_name_b = key.getString("station_name_b");

				if (key.getObject("move_time") != null)
					pathElem.move_time = (PGInterval) key.getObject("move_time");
				if (key.getObject("wait_time") != null)
					pathElem.wait_time = (PGInterval) key.getObject("wait_time");
				if (key.getObject("frequency") != null)
					pathElem.frequency = (PGInterval) key.getObject("frequency");
				pathElem.cost = key.getDouble("cost");
				pathElem.distance = key.getDouble("distance");
				paths.add(pathElem);
			}
		} catch (Exception e) {
			super.handeThrowble(e);
		}
		return paths;
	}

	@Override
	public Collection<RouteGeoData> getGeoDataByRoutePart(RoutePart routePart,
														  String lang_id) throws RepositoryException {
		Connection c = super.getConnection();
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
					+ " WHERE rway_id = ? AND position_index >= ? AND position_index <= ?"
					+ " AND lang_id = bus.lang_enum(?) ORDER BY position_index; ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, routePart.getID());
			ps.setInt(2, routePart.getStartInd());
			ps.setInt(3, routePart.getFinishInd());
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
			super.handeThrowble(e);
		}
		return relations;
	}

}
