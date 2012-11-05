package com.pgis.bus.data.repositories;

import java.util.Collection;

import org.postgis.Point;

import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.StationTransport;

public interface IStationsRepository extends IRepository {

	Collection<Station> getStationsByCityAndTransport(int city_id,
			String transportType) throws RepositoryException;

	Collection<Station> getStationsByBox(int city_id,
			Point p1, Point p2) throws RepositoryException;
	
	Station insertStation(Station station) throws RepositoryException;

	Station updateStation(Station station) throws RepositoryException;

	void deleteStation(int station_id) throws RepositoryException;

	Collection<StationTransport> getTransportTypesOfStation(int station_id)
			throws RepositoryException;

	void updateStationTransports(int station_id,
			Collection<StationTransport> transports) throws RepositoryException;

	void deleteStationTransports(int station_id) throws RepositoryException;

	void insertStationTransport(int station_id, StationTransport transport)
			throws RepositoryException;

	Station getStation(int station_id)
			throws RepositoryException;
	
	
}
