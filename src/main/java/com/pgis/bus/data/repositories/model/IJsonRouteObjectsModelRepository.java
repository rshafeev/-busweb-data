package com.pgis.bus.data.repositories.model;

import java.sql.SQLException;

import com.pgis.bus.data.models.JsonRouteObjectModel;
import com.pgis.bus.data.models.JsonRouteObjectsListModel;

public interface IJsonRouteObjectsModelRepository {
	JsonRouteObjectModel get(int id) throws SQLException;

	JsonRouteObjectsListModel getmportObjectsList(String cityKey, String routeType) throws SQLException;
}
