package com.pgis.bus.data.repositories;

import java.util.Collection;

import com.pgis.bus.data.orm.WayElem;
import com.pgis.bus.net.request.FindPathsOptions;

public interface IWaysRepository {
  
	Collection<WayElem> getShortestWays(FindPathsOptions options) throws RepositoryException;
	
}
