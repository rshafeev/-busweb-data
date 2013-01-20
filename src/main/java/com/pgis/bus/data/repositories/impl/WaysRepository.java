package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
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
import com.pgis.bus.data.orm.WayElem;
import com.pgis.bus.data.params.DefaultParameters;
import com.pgis.bus.data.repositories.IWaysRepository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.net.request.FindPathsOptions;

public class WaysRepository extends Repository implements IWaysRepository {
	private static final Logger log = LoggerFactory
			.getLogger(WaysRepository.class);

	public WaysRepository(IDBConnectionManager connManager) {
		super(connManager);
	}

	public WaysRepository(IDBConnectionManager connManager, Connection c,
			boolean isClosed, boolean isCommited) {
		super(connManager);
		this.connection = c;
		this.isClosed = isClosed;
		this.isCommited = isCommited;
	}

	@Override
	public Collection<WayElem> getShortestWays(FindPathsOptions options)
			throws RepositoryException {
		Connection c = super.getConnection();
		Collection<WayElem> ways = null;
		Calendar calendar = Calendar.getInstance();
		
		try {
			String query = "select  * from  bus.find_shortest_paths(" + "?," /* city_id */
					+ " geography(?)," /* p1 */
					+ " geography(?)," /* p2 */
					+ " bus.day_enum(?)," /* day_id */
					+ " ?," /* time_start */
					+ " ?," // max_distance*/
					+ " ?," /* route_types */
					+ " ?," /* discount */
					+ " bus.alg_strategy(?)," /* alg_strategy */
					+ " bus.lang_enum(?)) ORDER BY path_id,index;";

			Point p1 = new Point(options.getP1().getLat(), options.getP1()
					.getLon());
			p1.setSrid(DefaultParameters.GEOMETRY_SRID);
			Point p2 = new Point(options.getP2().getLat(), options.getP2()
					.getLon());
			p2.setSrid(DefaultParameters.GEOMETRY_SRID);

			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, options.getCityID());

			ps.setObject(2, new PGgeometry(p1));
			ps.setObject(3, new PGgeometry(p2));
			ps.setString(4, options.getOutTime().getDayID().name());
			ps.setTime(5, DateTimeHelper.getTimeFromSeconds(options.getOutTime().getTimeStartSecs()));
			ps.setDouble(6, options.getMaxDistance());
			ps.setArray(7, c.createArrayOf("text", options.getRouteTypeArr()));
			ps.setArray(8, c.createArrayOf("float", options.getDiscountArr()));
			ps.setString(9, options.getAlgStrategy().name());
			ps.setString(10, options.getLangID());

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

				if (key.getObject("move_time") != null)
					wayElem.move_time = (PGInterval) key.getObject("move_time");
				wayElem.wait_time = (PGInterval) key.getObject("wait_time");
				wayElem.cost = key.getDouble("cost");
				wayElem.distance = key.getDouble("distance");
				ways.add(wayElem);
			}
			super.commit(c);
		} catch (Exception e) {
			log.error("can not read database", e);
			super.rollback(c);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}

		return ways;
	}
}
