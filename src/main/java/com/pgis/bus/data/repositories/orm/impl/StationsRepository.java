package com.pgis.bus.data.repositories.orm.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgis.PGgeometry;
import org.postgis.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.repositories.Repository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.orm.IStationsRepository;

public class StationsRepository extends Repository implements IStationsRepository {

	private static final Logger log = LoggerFactory.getLogger(StationsRepository.class);

	public StationsRepository(IConnectionManager connManager) {
		super(connManager);
	}

	@Override
	public void insert(Station station) throws SQLException {
		Connection c = super.getConnection();

		try {
			String query = "INSERT INTO bus.stations (city_id,location) VALUES(?,?) RETURNING  id,name_key;";

			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, station.getCityID());
			ps.setObject(2, new PGgeometry(station.getLocation()));
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				int id = key.getInt("id");
				int name_key = key.getInt("name_key");
				station.setConnManager(connManager);
				station.setId(id);
				station.setNameKey(name_key);

				StringValuesRepository strValuesRep = new StringValuesRepository(super.connManager);
				strValuesRep.setRepositoryExternConnection(c);
				for (StringValue v : station.getName()) {
					v.setKeyID(name_key);
					strValuesRep.insert(v);
				}

			}
		} catch (SQLException e) {
			log.error("insertStation() exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}

	}

	@Override
	public void update(Station station) throws SQLException {
		Connection c = super.getConnection();

		try {
			station.setConnManager(connManager);
			String query = "UPDATE  bus.stations SET city_id=?, location=? WHERE id=?; ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, station.getCityID());
			ps.setObject(2, new PGgeometry(station.getLocation()));
			ps.setInt(3, station.getId());
			ps.execute();

			// insert name values
			StringValuesRepository svRep = new StringValuesRepository(super.connManager);
			svRep.setRepositoryExternConnection(c);
			svRep.update(station.getNameKey(), station.getName());

		} catch (SQLException e) {
			log.error("updateStation() exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}
	}

	@Override
	public void remove(int stationID) throws SQLException {
		Connection c = super.getConnection();
		try {
			String query = "DELETE FROM bus.stations WHERE id = ? ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, stationID);
			ps.execute();
		} catch (SQLException e) {
			log.error("deleteStation() exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}

	}

	@Override
	public Station get(int stationID) throws SQLException {
		Connection c = super.getConnection();
		Station station = null;
		try {
			String query = "SELECT id,city_id,geometry(location) as location,name_key"
					+ " FROM bus.stations WHERE id = ?;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, stationID);
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				station = new Station(super.connManager);
				station.setCityID(key.getInt("city_id"));
				int id = key.getInt("id");
				station.setId(id);
				int name_key = key.getInt("name_key");
				station.setNameKey(name_key);
				// get location
				PGgeometry g_location = (PGgeometry) key.getObject("location");
				if (!(g_location.getGeometry() instanceof Point)) {
					throw new SQLException("can not convert geo_location to org.pgis.Point");
				}
				station.setLocation((Point) g_location.getGeometry());
			}
		} catch (SQLException e) {
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}
		return station;

	}

	@Override
	public Station get(StringValue name, Point location) throws SQLException {
		if (name == null || location == null)
			return null;
		Connection c = super.getConnection();
		try {
			String query = "SELECT bus.stations.id as id FROM bus.stations "
					+ "JOIN bus.string_values ON bus.string_values.key_id = bus.stations.name_key "
					+ "WHERE st_distance(bus.stations.location,geography(?)) < 10 AND "
					+ " lang_id = bus.lang_enum(?) AND value = ?;";

			PreparedStatement ps = c.prepareStatement(query);
			ps.setObject(1, new PGgeometry(location));
			ps.setString(2, name.getLangID().name());
			ps.setString(3, name.getValue());
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				int station_id = key.getInt("id");
				return this.get(station_id);
			}
		} catch (SQLException e) {
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}
		return null;
	}

	@Override
	public void cleanUnsedStations() throws SQLException {
		Connection c = super.getConnection();
		try {
			String query = "DELETE FROM bus.stations " + "WHERE bus.stations.id IN ( "
					+ "SELECT bus.stations.id from bus.stations " + "LEFT JOIN   bus.route_relations "
					+ "ON bus.stations.id = bus.route_relations.station_b_id "
					+ "WHERE bus.route_relations.id IS NULL);";
			PreparedStatement ps = c.prepareStatement(query);
			ps.execute();
		} catch (SQLException e) {
			log.error("cleanUnsedStations() exception: ", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		}

	}

}
