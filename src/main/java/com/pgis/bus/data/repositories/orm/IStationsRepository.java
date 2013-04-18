package com.pgis.bus.data.repositories.orm;

import org.postgis.Point;

import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.repositories.IRepository;
import com.pgis.bus.data.repositories.RepositoryException;

public interface IStationsRepository extends IRepository {

	void insert(Station station) throws RepositoryException;

	void update(Station station) throws RepositoryException;

	void remove(int stationID) throws RepositoryException;

	Station get(int stationID) throws RepositoryException;

	void cleanUnsedStations() throws RepositoryException;

	Station get(StringValue name, Point location) throws RepositoryException;

}
