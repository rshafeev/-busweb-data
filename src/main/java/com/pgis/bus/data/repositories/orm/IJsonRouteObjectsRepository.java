package com.pgis.bus.data.repositories.orm;

import java.sql.SQLException;

import com.pgis.bus.data.orm.JsonRouteObject;
import com.pgis.bus.data.repositories.IRepository;

public interface IJsonRouteObjectsRepository extends IRepository {

	JsonRouteObject get(String cityKey, String routeType, String number) throws SQLException;

	JsonRouteObject get(int objID) throws SQLException;

	void updateByID(JsonRouteObject importObject) throws SQLException;

	void insert(JsonRouteObject importObject) throws SQLException;

	void remove(int ID) throws SQLException;;

}
