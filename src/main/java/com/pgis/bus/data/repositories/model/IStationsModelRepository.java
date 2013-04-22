package com.pgis.bus.data.repositories.model;

import java.sql.SQLException;
import java.util.Collection;

import org.postgis.Point;

import com.pgis.bus.data.repositories.IRepository;
import com.pgis.bus.net.models.station.StationModel;

public interface IStationsModelRepository extends IRepository {

	Collection<StationModel> find(String phrase, int cityID, int limitCount) throws SQLException;

	Collection<StationModel> getStationsList(int cityID) throws SQLException;

	Collection<StationModel> getStationsFromBox(int cityID, Point p1, Point p2) throws SQLException;
}
