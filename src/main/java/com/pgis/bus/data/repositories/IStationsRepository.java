package com.pgis.bus.data.repositories;

import java.util.Collection;

import org.postgis.Point;

import com.pgis.bus.data.orm.Station;

public interface IStationsRepository extends IRepository {

	Collection<Station> getStationsByCity(int city_id) throws RepositoryException;

	Collection<Station> getStationsByBox(int city_id,
			Point p1, Point p2) throws RepositoryException;
	
	Station insertStation(Station station) throws RepositoryException;

	Station updateStation(Station station) throws RepositoryException;

	void deleteStation(int station_id) throws RepositoryException;

	Station getStation(int station_id)
			throws RepositoryException;
	
	
}
