package com.pgis.bus.data.repositories;

import java.util.Collection;

import com.pgis.bus.data.orm.type.Path_t;
import com.pgis.bus.net.request.FindPathsOptions;

public interface IPathsRepository {
  
	Collection<Path_t> getShortestPaths(FindPathsOptions options) throws RepositoryException;
	
}
