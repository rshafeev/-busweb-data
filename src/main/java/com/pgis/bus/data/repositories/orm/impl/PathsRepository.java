package com.pgis.bus.data.repositories.orm.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.postgis.PGgeometry;
import org.postgresql.util.PGInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.helpers.GeoObjectsHelper;
import com.pgis.bus.data.orm.type.Path_t;
import com.pgis.bus.data.params.FindPathsParams;
import com.pgis.bus.data.repositories.Repository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.orm.IPathsRepository;

public class PathsRepository extends Repository implements IPathsRepository {
	private static final Logger log = LoggerFactory.getLogger(PathsRepository.class);

	public PathsRepository(IConnectionManager connManager) {
		super(connManager);
	}

	@Override
	public Collection<Path_t> findShortestPaths(FindPathsParams params) throws SQLException {
		Connection c = super.getConnection();
		Collection<Path_t> paths = null;
		try {
			String query = "select  * from  bus.find_shortest_paths(" + "?," /* city_id */
					+ " geography(?)," /* p1 */
					+ " geography(?)," /* p2 */
					+ " bus.day_enum(?)," /* day_id */
					+ " ?," /* time_start */
					+ " ?," // max_distance*/
					+ " ?," /* route_types */
					+ " ?," /* _has_transitions */
					+ " ?," /* discount */
					+ " bus.alg_strategy(?)," /* alg_strategy */
					+ " bus.lang_enum(?)) ORDER BY path_id,index;";

			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, params.getCityID());
			ps.setObject(2, new PGgeometry(GeoObjectsHelper.createPoint(params.getP1())));
			ps.setObject(3, new PGgeometry(GeoObjectsHelper.createPoint(params.getP2())));
			ps.setString(4, params.getDayID().name());
			ps.setTime(5, params.getTimeStart());
			ps.setDouble(6, params.getMaxDistance());
			ps.setArray(7, c.createArrayOf("text", params.getRouteTypes()));
			ps.setBoolean(8, params.isTransitions());
			ps.setArray(9, c.createArrayOf("float", params.getDiscounts()));
			ps.setString(10, params.getAlgStrategy().name());
			ps.setString(11, params.getLangID().name());

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
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}
		return paths;
	}
}
