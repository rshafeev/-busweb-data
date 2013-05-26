package com.pgis.bus.data.repositories.orm;

import java.sql.SQLException;

import com.pgis.bus.data.exp.RepositoryException;
import com.pgis.bus.data.orm.SerializedRouteObject;
import com.pgis.bus.data.repositories.IRepository;

public interface ISerializedRoutesRepository extends IRepository {

	SerializedRouteObject get(String cityKey, String routeType, String number) throws RepositoryException;

	SerializedRouteObject get(int objID) throws RepositoryException;

	void updateByID(SerializedRouteObject importObject) throws RepositoryException;

	void insert(SerializedRouteObject importObject) throws RepositoryException;

	void remove(int ID) throws RepositoryException;

}
