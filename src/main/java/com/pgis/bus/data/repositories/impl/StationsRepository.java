package com.pgis.bus.data.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.postgis.PGbox2d;
import org.postgis.PGbox3d;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.StationTransport;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.repositories.IStationsRepository;
import com.pgis.bus.data.repositories.IStringValuesRepository;
import com.pgis.bus.data.repositories.RepositoryException;

public class StationsRepository extends Repository implements
		IStationsRepository {

	private static final Logger log = LoggerFactory
			.getLogger(StationsRepository.class);

	public StationsRepository() {
		super();
	}

	public StationsRepository(Connection c, boolean isClosed, boolean isCommited) {
		super();
		this.connection = c;
		this.isClosed = isClosed;
		this.isCommited = isCommited;
	}

	@Override
	public Collection<Station> getStationsByCityAndTransport(int city_id,
			String transportType) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		Collection<Station> stations = null;

		try {
			String query = "SELECT id,city_id,geometry(location) as location,name_key"
					+ " FROM bus.stations JOIN bus.station_transports "
					+ "ON bus.stations.id = bus.station_transports.station_id "
					+ "WHERE city_id = ? AND transport_type_id=bus.transport_type_enum(?);";

			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, city_id);
			ps.setString(2, transportType);
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
				IStringValuesRepository stringValuesRepository = new StringValuesRepository();
				int name_key = key.getInt("name_key");
				Collection<StringValue> name = stringValuesRepository
						.getStringValues(name_key);
				station.setNames(name);
				station.setName_key(name_key);

				// get transport_types
				Collection<StationTransport> transports = this
						.getTransportTypesOfStation(station.getId());
				station.setTransports(transports);

				stations.add(station);
			}
		} catch (SQLException e) {
			stations = null;
			log.error("can not read database", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}
		return stations;

	}

	@Override
	public Station insertStation(Station station) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
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
				// insert transport_typesByStation

				if (responceStation.getTransports() != null) {
					IStationsRepository stationsRepository = new StationsRepository(
							c, false, false);
					Iterator<StationTransport> i = responceStation
							.getTransports().iterator();
					while (i.hasNext()) {
						StationTransport value = i.next();
						stationsRepository.insertStationTransport(id, value);
					}
				}
				if (this.isCommited)
					c.commit();
			}
		} catch (SQLException e) {
			try {
				log.error("insertStation() exception: ", e);
				c.rollback();
				throw new RepositoryException(
						RepositoryException.err_enum.c_transaction_err);
			} catch (SQLException sqx) {
				throw new RepositoryException(
						RepositoryException.err_enum.c_rollback_err);
			}
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}
		return responceStation;

	}

	@Override
	public Station updateStation(Station station) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
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

			// insert transport_typesByStation

			if (responceStation.getTransports() != null) {
				IStationsRepository stationsRepository = new StationsRepository(
						c, false, false);
				stationsRepository.updateStationTransports(station.getId(),
						station.getTransports());

				if (this.isCommited)
					c.commit();
			}
		} catch (SQLException e) {
			try {
				log.error("updateStation() exception: ", e);
				c.rollback();
				throw new RepositoryException(
						RepositoryException.err_enum.c_transaction_err);
			} catch (SQLException sqx) {
				throw new RepositoryException(
						RepositoryException.err_enum.c_rollback_err);
			}
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}
		return responceStation;
	}

	@Override
	public void deleteStation(int station_id) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		try {
			String query = "DELETE FROM bus.stations WHERE id = ? ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, station_id);
			ps.execute();
			if (isCommited)
				c.commit();
		} catch (SQLException e) {
			try {
				log.error("deleteStation() exception: ", e);
				c.rollback();
				throw new RepositoryException(
						RepositoryException.err_enum.c_transaction_err);
			} catch (SQLException sqx) {
				throw new RepositoryException(
						RepositoryException.err_enum.c_rollback_err);
			}
		} finally {
			if (this.isClosed)
				DBConnectionFactory.closeConnection(c);
		}

	}

	@Override
	public Collection<StationTransport> getTransportTypesOfStation(
			int station_id) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		Collection<StationTransport> transports = null;

		try {
			String query = "SELECT * FROM bus.stations JOIN bus.station_transports  ON"
					+ " bus.stations.id = bus.station_transports.station_id WHERE bus.stations.id = ?";

			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, station_id);
			ResultSet key = ps.executeQuery();
			transports = new ArrayList<StationTransport>();

			while (key.next()) {
				StationTransport transportType = new StationTransport();
				transportType.transport_type_id = key
						.getString("transport_type_id");
				transports.add(transportType);
			}
		} catch (SQLException e) {
			transports = null;
			log.error("can not read database", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}
		return transports;

	}

	@Override
	public void insertStationTransport(int station_id,
			StationTransport transport) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();

		try {
			String query = "INSERT INTO bus.station_transports (station_id,transport_type_id) VALUES "
					+ "(?, bus.transport_type_enum(?));";

			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, station_id);
			ps.setString(2, transport.transport_type_id);
			ps.execute();
			if (this.isCommited)
				c.commit();
		} catch (SQLException e) {
			try {
				log.error("insertStationTransport() exception: ", e);
				c.rollback();
				throw new RepositoryException(
						RepositoryException.err_enum.c_transaction_err);
			} catch (SQLException sqx) {
				throw new RepositoryException(
						RepositoryException.err_enum.c_rollback_err);
			}
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}

	}

	@Override
	public void updateStationTransports(int station_id,
			Collection<StationTransport> transports) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();

		try {
			IStationsRepository repository = new StationsRepository(c, false,
					false);
			repository.deleteStationTransports(station_id);
			Iterator<StationTransport> i = transports.iterator();
			while (i.hasNext()) {
				StationTransport value = i.next();
				repository.insertStationTransport(station_id, value);
			}

			if (isCommited)
				c.commit();

		} catch (SQLException | RepositoryException e) {
			try {
				log.error("updateStationTransports() exception: ", e);
				c.rollback();
				throw new RepositoryException(
						RepositoryException.err_enum.c_transaction_err);
			} catch (SQLException sqx) {
				throw new RepositoryException(
						RepositoryException.err_enum.c_rollback_err);
			}
		} finally {
			if (this.isClosed)
				DBConnectionFactory.closeConnection(c);
		}
	}

	@Override
	public void deleteStationTransports(int station_id)
			throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		try {
			String query = "DELETE FROM bus.station_transports WHERE station_id = ? ";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, station_id);
			ps.execute();
			if (isCommited)
				c.commit();
		} catch (SQLException e) {
			try {
				log.error("deleteStationTransports() exception: ", e);
				c.rollback();
				throw new RepositoryException(
						RepositoryException.err_enum.c_transaction_err);
			} catch (SQLException sqx) {
				throw new RepositoryException(
						RepositoryException.err_enum.c_rollback_err);
			}
		} finally {
			if (this.isClosed)
				DBConnectionFactory.closeConnection(c);
		}
	}

	@Override
	public Station getStation(int station_id) throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
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
				IStringValuesRepository stringValuesRepository = new StringValuesRepository();
				int name_key = key.getInt("name_key");
				Collection<StringValue> name = stringValuesRepository
						.getStringValues(name_key);
				station.setNames(name);
				station.setName_key(name_key);

				// get transport_types
				Collection<StationTransport> transports = this
						.getTransportTypesOfStation(station.getId());
				station.setTransports(transports);

			}
		} catch (SQLException e) {
			log.error("can not read database", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}
		return station;

	}

	@Override
	public Collection<Station> getStationsByBox(int city_id, Point p1, Point p2)
			throws RepositoryException {
		Connection c = this.connection;
		if (c == null)
			c = Repository.getConnection();
		Collection<Station> stations = null;
		PGbox3d box= new org.postgis.PGbox3d(p1,p2);
		
		try {
			String query = "SELECT id,city_id,geometry(location) as location,name_key"
					+ " FROM bus.stations "
					+ "WHERE city_id = ? AND geometry(location) && ?";

			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, city_id);
			ps.setObject(2, box);
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
				IStringValuesRepository stringValuesRepository = new StringValuesRepository();
				int name_key = key.getInt("name_key");
				Collection<StringValue> name = stringValuesRepository
						.getStringValues(name_key);
				station.setNames(name);
				station.setName_key(name_key);

				// get transport_types
				Collection<StationTransport> transports = this
						.getTransportTypesOfStation(station.getId());
				station.setTransports(transports);

				stations.add(station);
			}
		} catch (SQLException e) {
			stations = null;
			log.error("can not read database", e);
			throw new RepositoryException(
					RepositoryException.err_enum.c_sql_err);
		} finally {
			if (isClosed)
				DBConnectionFactory.closeConnection(c);
		}
		return stations;
	}

}
