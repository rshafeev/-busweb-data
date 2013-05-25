package com.pgis.bus.data.repositories.orm;

import java.sql.SQLException;

import com.pgis.bus.data.orm.SerializedRouteObject;
import com.pgis.bus.data.repositories.IRepository;

public interface ISerializedRoutesRepository extends IRepository {

	SerializedRouteObject get(String cityKey, String routeType, String number) throws SQLException;

	SerializedRouteObject get(int objID) throws SQLException;

	void updateByID(SerializedRouteObject importObject) throws SQLException;

	void insert(SerializedRouteObject importObject) throws SQLException;

	void remove(int ID) throws SQLException;;

}
