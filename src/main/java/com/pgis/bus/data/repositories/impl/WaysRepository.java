package com.pgis.bus.data.repositories.impl;

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

import com.pgis.bus.data.models.FindWaysOptions;
import com.pgis.bus.data.orm.WayElem;
import com.pgis.bus.data.repositories.IWaysRepository;
import com.pgis.bus.data.repositories.RepositoryException;

public class WaysRepository extends Repository implements IWaysRepository {
	private static final Logger log = LoggerFactory
			.getLogger(WaysRepository.class);

	public WaysRepository() {
		super();
	}

	public WaysRepository(Connection c, boolean isClosed, boolean isCommited) {
		super();
		this.connection = c;
		this.isClosed = isClosed;
		this.isCommited = isCommited;
	}

	@Override
	public Collection<WayElem> getShortestWays(FindWaysOptions options)
			throws RepositoryException {
		Connection c = super.getConnection();
		Collection<WayElem> ways = null;
		try {
			String query = "select  * from  bus.shortest_ways(" + "?," /* city_id */
					+ " geography(?)," /* p1 */
					+ " geography(?)," /* p2 */
					+ " day_enum(?)," /* day_id */
					+ " ?," /* time_start */
					+ " ?," // max_distance*/
					+ " ?," /* route_types */
					+ " ?," /* discount */
					+ " bus.alg_strategy(?)," /* alg_strategy */
					+ " lang_enum(?)) ORDER BY path_id,index;";
			PreparedStatement ps = c.prepareStatement(query);

			ps.setInt(1, options.getCity_id());
			ps.setObject(2, new PGgeometry(options.getP1()));
			ps.setObject(3, new PGgeometry(options.getP2()));
			ps.setString(4, options.getDay_id().name());
			ps.setTime(5, options.getTime_start());
			ps.setDouble(6, options.getMaxDistance());
			ps.setArray(7,
					c.createArrayOf("text", options.getTransportTypeArray()));
			ps.setArray(8, c.createArrayOf("float", options.getDiscountArray()));
			ps.setString(9, options.getAlg_strategy().name());
			ps.setString(10, options.getLang_id());

			ResultSet key = ps.executeQuery();
			ways = new ArrayList<WayElem>();
			while (key.next()) {
				WayElem wayElem = new WayElem();
				wayElem.path_id = key.getInt("path_id");
				wayElem.index = key.getInt("index");
				wayElem.direct_route_id = key.getInt("direct_route_id");
				wayElem.route_type = key.getString("route_type");
				wayElem.relation_index = key.getInt("relation_index");
				wayElem.route_name = key.getString("route_name");
				wayElem.station_name = key.getString("station_name");

				wayElem.move_time = (PGInterval) key.getObject("move_time");
				wayElem.wait_time = (PGInterval) key.getObject("wait_time");
				wayElem.cost = key.getDouble("cost");
				wayElem.distance = key.getDouble("distance");
				ways.add(wayElem);
			}
			super.commit(c);
		} catch (SQLException e) {
			super.rollback(c);
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}

		return ways;
	}
}
