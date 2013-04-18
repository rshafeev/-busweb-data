package com.pgis.bus.data.repositories.orm;

import com.pgis.bus.data.orm.ImportObject;
import com.pgis.bus.data.repositories.RepositoryException;

public interface IObjectsRepository {

	ImportObject get(String cityKey, String routeType, String number) throws RepositoryException;

	ImportObject get(int objID) throws RepositoryException;

	void updateByID(ImportObject importObject) throws RepositoryException;

	void insert(ImportObject importObject) throws RepositoryException;

	void remove(int ID) throws RepositoryException;;

}
