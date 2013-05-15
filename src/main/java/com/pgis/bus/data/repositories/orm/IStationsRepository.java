package com.pgis.bus.data.repositories.orm;

import java.sql.SQLException;

import org.postgis.Point;

import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.repositories.IRepository;

public interface IStationsRepository extends IRepository {

	void insert(Station station) throws SQLException;

	void update(Station station) throws SQLException;

	void remove(int stationID) throws SQLException;

	Station get(int stationID) throws SQLException;

	void cleanUnsedStations() throws SQLException;

	Station get(StringValue name, Point location) throws SQLException;

}
