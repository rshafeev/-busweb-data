package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import org.postgis.PGbox3d;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.repositories.IStationsRepository;
import com.pgis.bus.data.repositories.IStringValuesRepository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.net.models.Location;
import com.pgis.bus.net.models.StationModel;

public class StationsRepository extends Repository implements
		IStationsRepository {

	private static final Logger log = LoggerFactory
			.getLogger(StationsRepository.class);

	public StationsRepository(IDBConnectionManager connManager) {
		super(connManager);
	}

	public StationsRepository(Connection c, boolean isClosed, boolean isCommited) {
		super();
		this.connection = c;
		this.isClosed = isClosed;
		this.isCommited = isCommited;
	}

	@Override
	public Collection<Station> getStationsList(int cityID, String langID)
			throws RepositoryException {
		Connection c = super.getConnection();
		Collection<Station> stations = null;

		try {
			String query = "select bus.stations.id as id, value as name from bus.stations "
					+ "join bus.string_values on string_values.key_id = stations.name_key "
					+ "where city_id = ? and lang_id = bus.lang_enum(?)";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, cityID);
			ps.setString(2, langID);
			ResultSet key = ps.executeQuery();
			stations = new ArrayList<Station>();

			while (key.next()) {
				Station station = new Station();
				station.setId(key.getInt("id"));
				Collection<StringValue> name = new ArrayList<StringValue>();
				name.add(new StringValue(langID ,key.getString("name") ));
				station.setNames(name);
				station.setCity_id(cityID);
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
	public Collection<Station> getStationsByCity(int city_id)
			throws RepositoryException {
		Connection c = super.getConnection();
		Collection<Station> stations = null;

		try {
			String query = "SELECT id, geometry(location) as location, name_key "
					+ " FROM bus.stations WHERE city_id = ? ;";

			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, city_id);
			ResultSet key = ps.executeQuery();
			stations = new ArrayList<Station>();

			while (key.next()) {
				Station station = new Station();
				station.setCity_id(city_id);
				int id = key.getInt("id");
				station.setId(id);

				// get location
				PGgeometry g_location = (PGgeometry) key.getObject("location");
				if (!(g_location.getGeometry() instanceof Point)) {
					throw new SQLException(
							"can not convert geo_location to org.pgis.Point");
				}
				station.setLocation((Point) g_location.getGeometry());

				// get names
				IStringValuesRepository stringValuesRepository = new StringValuesRepository(
						c, false, false);
				int name_key = key.getInt("name_key");
				Collection<StringValue> name = stringValuesRepository
						.getStringValues(name_key);
				station.setNames(name);
				station.setName_key(name_key);

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
	public Station insertStation(Station station) throws RepositoryException {
		Connection c = super.getConnection();
		Station responceStation = station.clone();

		try {
			String query = "INSERT INTO bus.stations (city_id,location) VALUES(?,?) RETURNING  id,name_key;";

			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, station.getCity_id());
			ps.setObject(2, new PGgeometry(station.getLocation()));
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				int id = key.getInt("id");
				int name_key = key.getInt("name_key");

				responceStation.setId(id);
				responceStation.setName_key(name_key);

				// insert name values
				IStringValuesRepository stringValuesRepository = new StringValuesRepository(
						c, false, false);
				if (responceStation.getNames() != null) {

					for (StringValue s : responceStation.getNames()) {
						s.key_id = responceStation.getName_key();
						s.id = stringValuesRepository.insertStringValue(s);
					}
				}
				super.commit(c);
			}
		} catch (SQLException e) {
			log.error("insertStation() exception: ", e);
			super.rollback(c);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return responceStation;

	}

	@Override
	public Station updateStation(Station station) throws RepositoryException {
		Connection c = super.getConnection();
		Station responceStation = station.clone();

		try {
			String query = "UPDATE  bus.stations SET city_id=?, location=? WHERE id=?; ";

			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, station.getCity_id());
			ps.setObject(2, new PGgeometry(station.getLocation()));
			ps.setInt(3, station.getId());
			ps.execute();

			// insert name values
			IStringValuesRepository stringValuesRepository = new StringValuesRepository(
					c, false, false);
			stringValuesRepository.updateStringValues(station.getName_key(),
					station.getNames());

			super.commit(c);

		} catch (SQLException e) {
			log.error("updateStation() exception: ", e);
			super.rollback(c);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return responceStation;
	}

	@Override
	public void deleteStation(int station_id) throws RepositoryException {
		Connection c = super.getConnection();
		try {
			String query = "DELETE FROM bus.stations WHERE id = ? ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, station_id);
			ps.execute();
			if (isCommited)
				c.commit();
		} catch (SQLException e) {
			log.error("deleteStation() exception: ", e);
			super.rollback(c);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}

	}

	@Override
	public Station getStation(int station_id) throws RepositoryException {
		Connection c = super.getConnection();
		Station station = null;

		try {
			String query = "SELECT id,city_id,geometry(location) as location,name_key"
					+ " FROM bus.stations WHERE id = ?;";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, station_id);
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				station = new Station();
				station.setCity_id(key.getInt("city_id"));
				int id = key.getInt("id");
				station.setId(id);

				// get location
				PGgeometry g_location = (PGgeometry) key.getObject("location");
				if (!(g_location.getGeometry() instanceof Point)) {
					throw new SQLException(
							"can not convert geo_location to org.pgis.Point");
				}
				station.setLocation((Point) g_location.getGeometry());

				// get names
				IStringValuesRepository stringValuesRepository = new StringValuesRepository(
						c, false, false);
				int name_key = key.getInt("name_key");
				Collection<StringValue> name = stringValuesRepository
						.getStringValues(name_key);
				station.setNames(name);
				station.setName_key(name_key);

			}
		} catch (SQLException e) {
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return station;

	}

	@Override
	public Collection<Station> getStationsFromBox(int cityID, Point p1, Point p2, String langID )
			throws RepositoryException {
		Connection c = super.getConnection();
		Collection<Station> stations = null;
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
			stations = new ArrayList<Station>();

			while (key.next()) {
				Station station = new Station();
				station.setCity_id(cityID);
				int id = key.getInt("id");
				station.setId(id);

				// get location
				PGgeometry g_location = (PGgeometry) key.getObject("location");
				if (!(g_location.getGeometry() instanceof Point)) {
					throw new SQLException(
							"can not convert geo_location to org.pgis.Point");
				}
				station.setLocation((Point) g_location.getGeometry());
				Collection<StringValue> name = new ArrayList<StringValue>();
				name.add(new StringValue(langID ,key.getString("name") ));
				station.setNames(name);
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
	public Station getStation(StringValue name, Point location)
			throws RepositoryException {
		Connection c = super.getConnection();
		Station station = null;
		if (name == null || location == null)
			return null;
		try {
			String query = "SELECT bus.stations.id as id FROM bus.stations "
					+ "JOIN bus.string_values ON bus.string_values.key_id = bus.stations.name_key "
					+ "WHERE st_distance(bus.stations.location,geography(?)) < 10 AND "
					+ " lang_id = bus.lang_enum(?) AND value = ?;";

			PreparedStatement ps = c.prepareStatement(query);
			ps.setObject(1, new PGgeometry(location));
			ps.setString(2, name.lang_id);
			ps.setString(3, name.value);
			ResultSet key = ps.executeQuery();

			if (key.next()) {
				int station_id = key.getInt("id");
				station = getStation(station_id);
			}
		} catch (SQLException e) {
			station = null;
			log.error("can not read database", e);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}
		return station;
	}

	@Override
	public void cleanUnsedStations() throws RepositoryException {
		Connection c = super.getConnection();
		try {
			String query = "DELETE FROM bus.stations "
					+ "WHERE bus.stations.id IN ( "
					+ "SELECT bus.stations.id from bus.stations "
					+ "LEFT JOIN   bus.route_relations "
					+ "ON bus.stations.id = bus.route_relations.station_b_id "
					+ "WHERE bus.route_relations.id IS NULL);";
			PreparedStatement ps = c.prepareStatement(query);
			ps.execute();
			if (isCommited)
				c.commit();
		} catch (SQLException e) {
			log.error("cleanUnsedStations() exception: ", e);
			super.rollback(c);
			super.throwable(e, RepositoryException.err_enum.c_sql_err);
		} finally {
			super.closeConnection(c);
		}

	}

	@Override
	public Collection<StationModel> findStations(String phrase, int cityID,
			String langID, int limitCount) throws RepositoryException {
		Connection c = super.getConnection();
		Collection<StationModel> stations = null;

		try {
			String query = "select bus.stations.id as id, "
					+ "bus.string_values.value as name, "
					+ "geometry(bus.stations.location) as location "
					+ "from bus.stations  "
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
					throw new SQLException(
							"can not convert geo_location to org.pgis.Point");
				}
				Point dbLocation = (Point) g_location.getGeometry();
				Location location = new Location();
				location.setLat(dbLocation.x);
				location.setLon(dbLocation.y);
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
