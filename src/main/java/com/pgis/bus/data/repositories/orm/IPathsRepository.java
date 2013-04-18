package com.pgis.bus.data.repositories.orm;

import java.util.Collection;

import com.pgis.bus.data.orm.type.Path_t;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.net.request.FindPathsRequest;

public interface IPathsRepository {

	Collection<Path_t> findShortestPaths(FindPathsRequest options) throws RepositoryException;

}
