package com.pgis.bus.data.repositories;

import java.util.Collection;

import com.pgis.bus.data.models.FindWaysOptions;
import com.pgis.bus.data.orm.WayElem;

public interface IWaysRepository {
  
	Collection<WayElem> getShortestWays(FindWaysOptions options) throws RepositoryException;
	
}
