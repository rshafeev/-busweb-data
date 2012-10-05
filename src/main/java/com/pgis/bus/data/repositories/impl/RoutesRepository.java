package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.postgis.MultiLineString;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.models.RouteGeoData;
import com.pgis.bus.data.models.RoutePart;
import com.pgis.bus.data.repositories.IRoutesRepository;
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
				if (key.getObject("relation_geom") != null)
					relationGeom = (PGgeometry) key.getObject("relation_geom");

				RouteGeoData data = new RouteGeoData();
				data.setIndex(index);
				if (relationGeom != null)
					data.setRelationGeom((MultiLineString) relationGeom
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

}
