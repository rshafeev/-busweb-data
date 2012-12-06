package com.pgis.bus.data.repositories;

import java.util.Collection;

import com.pgis.bus.data.helpers.LoadImportObjectOptions;
import com.pgis.bus.data.models.ImportRouteModel;
import com.pgis.bus.data.orm.ImportObject;

public interface IimportRepository {

	ImportObject getObject(int cityID, String routeType, String number)
			throws RepositoryException;

	void updateObjectByID(ImportObject importObject) throws RepositoryException;

	void insertObject(ImportObject importObject) throws RepositoryException;

	ImportRouteModel getRouteModelForObj(int objID) throws RepositoryException;

	void removeObject(int ID) throws RepositoryException;;

	Collection<ImportObject> getObjects(int cityID, String routeType,
			LoadImportObjectOptions opts) throws RepositoryException;

	ImportObject getObject(int objID) throws RepositoryException;

}
