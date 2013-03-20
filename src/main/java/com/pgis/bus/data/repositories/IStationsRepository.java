package com.pgis.bus.data.repositories;

import java.util.Collection;

import org.postgis.Point;

import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.net.models.StationModel;

public interface IStationsRepository extends IRepository {

	Station getStation(StringValue name, Point location)
			throws RepositoryException;

	Collection<StationModel> findStations(String phrase, int cityID,
			String langID, int limitCount) throws RepositoryException;

	Collection<Station> getStationsByCity(int city_id)
			throws RepositoryException;


	Station insertStation(Station station) throws RepositoryException;

	Station updateStation(Station station) throws RepositoryException;

	void deleteStation(int station_id) throws RepositoryException;

	Station getStation(int station_id) throws RepositoryException;

	void cleanUnsedStations() throws RepositoryException;

	Collection<Station> getStationsList(int cityID, String langID)
			throws RepositoryException;

	Collection<Station> getStationsFromBox(int cityID, Point p1, Point p2,
			String langID) throws RepositoryException;

}
