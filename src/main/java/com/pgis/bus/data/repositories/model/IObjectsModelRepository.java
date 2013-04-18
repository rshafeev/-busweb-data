package com.pgis.bus.data.repositories.model;

import com.pgis.bus.data.models.ImportRouteModel;
import com.pgis.bus.data.repositories.RepositoryException;

public interface IObjectsModelRepository {
	ImportRouteModel getByID(int objID) throws RepositoryException;

}
