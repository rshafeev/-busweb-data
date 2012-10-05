package com.pgis.bus.data.repositories;

import java.util.Collection;

import com.pgis.bus.data.models.RouteGeoData;
import com.pgis.bus.data.models.RoutePart;

public interface IRoutesRepository {
	Collection<RouteGeoData> getGeoDataByRoutePart(RoutePart routePart, String lang_id) throws RepositoryException;
}
