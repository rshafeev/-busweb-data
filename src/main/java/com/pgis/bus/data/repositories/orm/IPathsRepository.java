package com.pgis.bus.data.repositories.orm;

import java.sql.SQLException;
import java.util.Collection;

import com.pgis.bus.data.orm.type.Path_t;
import com.pgis.bus.data.repositories.IRepository;
import com.pgis.bus.net.request.FindPathsRequest;

public interface IPathsRepository extends IRepository {

	Collection<Path_t> findShortestPaths(FindPathsRequest options) throws SQLException;

}
