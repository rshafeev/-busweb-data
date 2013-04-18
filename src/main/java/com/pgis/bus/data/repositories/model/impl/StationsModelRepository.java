package com.pgis.bus.data.repositories.model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.postgis.PGbox3d;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.models.factory.geom.PointModelFactory;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.model.IStationsModelRepository;
import com.pgis.bus.net.models.geom.PointModel;
import com.pgis.bus.net.models.station.StationModel;

public class StationsModelRepository extends ModelRepository implements IStationsModelRepository {

	private static final Logger log = LoggerFactory.getLogger(StationsModelRepository.class);

	public StationsModelRepository(Locale locale, IDBConnectionManager connManager) {
		super(locale, connManager);
	}

	public StationsModelRepository(Locale locale, IDBConnectionManager connManager, boolean isCommited) {
		super(locale, connManager, isCommited);
	}

	public StationsModelRepository(String langID, IDBConnectionManager connManager) {
		super(langID, connManager);
	}

	public StationsModelRepository(String langID, IDBConnectionManager connManager, boolean isCommited) {
		super(langID, connManager, isCommited);
	}

	public StationsModelRepository(IDBConnectionManager connManager) {
		super(connManager);
	}

	public StationsModelRepository(IDBConnectionManager connManager, boolean isCommited) {
		super(connManager, isCommited);
	}

	protected StationsModelRepository(String langID, IDBConnectionManager connManager, Connection c, boolean isClosed,
			boolean isCommited) {
		super(langID, connManager, isCommited);
		super.isClosed = isClosed;
		super.connection = c;
	}

	@Override
	public Collection<StationModel> getStationsList(int cityID) throws RepositoryException {
		Connection c = super.getConnection();
		Collection<StationModel> stations = null;

		try {
			String query = "select bus.stations.id as id, value as name from bus.stations "
					+ "join bus.string_values on string_values.key_id = stations.name_key "
					+ "where city_id = ? and lang_id = bus.lang_enum(?)";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, cityID);
			ps.setString(2, langID);
			ResultSet key = ps.executeQuery();
			stations = new ArrayList<StationModel>();

			while (key.next()) {
				StationModel station = new StationModel();
				station.setId(key.getInt("id"));
				station.setName(key.getString("name"));
				stations.add(station);
			}
		} catch (SQLException e) {
			stations = null;
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return stations;

	}

	@Override
	public Collection<StationModel> getStationsFromBox(int cityID, Point p1, Point p2) throws RepositoryException {
		Connection c = super.getConnection();
		Collection<StationModel> stations = null;
		PGbox3d box = new org.postgis.PGbox3d(p1, p2);

		try {
			String query = "SELECT bus.stations.id as id, geometry(location) as location, value as name FROM bus.stations "
					+ "left join bus.string_values on stations.name_key = string_values.key_id "
					+ "WHERE city_id = ? AND geometry(location) && ? AND lang_id = bus.lang_enum(?);";

			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, cityID);
			ps.setObject(2, box);
			ps.setString(3, langID);
			ResultSet key = ps.executeQuery();
			stations = new ArrayList<StationModel>();

			while (key.next()) {
				StationModel station = new StationModel();
				station.setId(key.getInt("id"));
				station.setName(key.getString("name"));
				// get location
				PGgeometry g_location = (PGgeometry) key.getObject("location");
				if (!(g_location.getGeometry() instanceof Point)) {
					throw new SQLException("can not convert geo_location to org.pgis.Point");
				}
				PointModel location = PointModelFactory.createModel((Point) g_location.getGeometry());
				station.setLocation(location);
				stations.add(station);
			}
		} catch (SQLException e) {
			stations = null;
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return stations;
	}

	@Override
	public Collection<StationModel> find(String phrase, int cityID, int limitCount) throws RepositoryException {
		Connection c = super.getConnection();
		Collection<StationModel> stations = null;

		try {
			String query = "select bus.stations.id as id, " + "bus.string_values.value as name, "
					+ "geometry(bus.stations.location) as location " + "from bus.stations  "
					+ "JOIN bus.string_values ON bus.string_values.key_id = bus.stations.name_key "
					+ "where city_id = ? and lang_id = bus.lang_enum(?) and  value @@ ? limit ?;";

			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, cityID);
			ps.setString(2, langID);
			ps.setString(3, phrase);
			ps.setInt(4, limitCount);
			ResultSet key = ps.executeQuery();
			stations = new ArrayList<StationModel>();

			while (key.next()) {
				StationModel station = new StationModel();
				// get id
				int id = key.getInt("id");
				station.setId(id);

				// get location
				PGgeometry g_location = (PGgeometry) key.getObject("location");
				if (!(g_location.getGeometry() instanceof Point)) {
					throw new SQLException("can not convert geo_location to org.pgis.Point");
				}
				PointModel location = PointModelFactory.createModel((Point) g_location.getGeometry());
				station.setLocation(location);

				// get name
				String name = key.getString("name");
				station.setName(name);

				stations.add(station);
			}
		} catch (SQLException e) {
			stations = null;
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return stations;
	}
}
