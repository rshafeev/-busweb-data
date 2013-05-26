package com.pgis.bus.data.repositories.orm;

import java.sql.SQLException;
import java.util.Collection;

import com.pgis.bus.data.exp.RepositoryException;
import com.pgis.bus.data.orm.type.Path_t;
import com.pgis.bus.data.params.FindPathsParams;
import com.pgis.bus.data.repositories.IRepository;

public interface IPathsRepository extends IRepository {

	Collection<Path_t> findShortestPaths(FindPathsParams params) throws RepositoryException;

}
