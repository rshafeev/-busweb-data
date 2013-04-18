package com.pgis.bus.data.repositories.orm.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgresql.util.PGInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.helpers.DateTimeHelper;
import com.pgis.bus.data.orm.type.Path_t;
import com.pgis.bus.data.params.DefaultParameters;
import com.pgis.bus.data.repositories.Repository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.orm.IPathsRepository;
import com.pgis.bus.net.request.FindPathsRequest;

public class PathsRepository extends Repository implements IPathsRepository {
	private static final Logger log = LoggerFactory.getLogger(PathsRepository.class);

	public PathsRepository(IDBConnectionManager connManager) {
		super(connManager);
	}

	public PathsRepository(IDBConnectionManager connManager, boolean isCommited) {
		super(connManager, isCommited);
	}

	@Override
	public Collection<Path_t> findShortestPaths(FindPathsRequest options) throws RepositoryException {
		Connection c = super.getConnection();
		Collection<Path_t> paths = null;
		Calendar calendar = Calendar.getInstance();

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

			Point p1 = new Point(options.getP1().getLat(), options.getP1().getLon());
			p1.setSrid(DefaultParameters.GEOMETRY_SRID);
			Point p2 = new Point(options.getP2().getLat(), options.getP2().getLon());
			p2.setSrid(DefaultParameters.GEOMETRY_SRID);

			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, options.getCityID());

			ps.setObject(2, new PGgeometry(p1));
			ps.setObject(3, new PGgeometry(p2));
			ps.setString(4, options.getOutTime().getDayID().name());
			ps.setTime(5, DateTimeHelper.getTimeFromSeconds(options.getOutTime().getTimeStartSecs()));
			ps.setDouble(6, options.getMaxDistance());
			ps.setArray(7, c.createArrayOf("text", options.getRouteTypeArr()));
			ps.setBoolean(8, options.isTransitions());
			ps.setArray(9, c.createArrayOf("float", options.getDiscountArr()));
			ps.setString(10, options.getAlgStrategy().name());
			ps.setString(11, options.getLangID());

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
			super.commit(c);
		} catch (Exception e) {
			log.error("can not read database", e);
			super.rollback(c);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}

		return paths;
	}
}
