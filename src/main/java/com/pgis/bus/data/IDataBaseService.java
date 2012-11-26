package com.pgis.bus.data;

import java.util.Collection;

import org.postgis.Point;

import com.pgis.bus.data.models.FindWaysOptions;
import com.pgis.bus.data.models.RouteGeoData;
import com.pgis.bus.data.models.RoutePart;
import com.pgis.bus.data.orm.*;
import com.pgis.bus.data.repositories.RepositoryException;

public interface IDataBaseService {

	Station getStation(int id);

	Station getStationByName(String name, Language lang);

	Collection<City> getAllCities() throws RepositoryException;

	Collection<Language> getAllLanguages() throws RepositoryException;

	Collection<Station> getStationsByCity(int city_id)
			throws RepositoryException;

	Collection<Station> getStationsByBox(int city_id, Point p1, Point p2)
			throws RepositoryException;

	City getCityByName(String lang_id, String value) throws RepositoryException;

	Collection<WayElem> getShortestWays(FindWaysOptions options)
			throws RepositoryException;

	Collection<RouteGeoData> getGeoDataByRoutePart(RoutePart routePart,
			String lang_id) throws RepositoryException;
}
