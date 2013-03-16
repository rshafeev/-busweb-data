package com.pgis.bus.data;

import java.util.Collection;

import org.postgis.Point;

import com.pgis.bus.data.helpers.LoadRouteOptions;
import com.pgis.bus.data.models.RouteGeoData;
import com.pgis.bus.data.models.RoutePart;
import com.pgis.bus.data.orm.*;
import com.pgis.bus.data.orm.type.Path_t;
import com.pgis.bus.data.repositories.ICitiesRepository;
import com.pgis.bus.data.repositories.IMainRepository;
import com.pgis.bus.data.repositories.IPathsRepository;
import com.pgis.bus.data.repositories.IRoutesRepository;
import com.pgis.bus.data.repositories.IStationsRepository;
import com.pgis.bus.data.repositories.IUsersRepository;
import com.pgis.bus.data.repositories.IimportRepository;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.net.request.FindPathsOptions;

public interface IDataBaseService {
	IUsersRepository Users();
	ICitiesRepository Cities();
	IMainRepository Main();
	IStationsRepository Stations();
	IPathsRepository Paths();
	IRoutesRepository Routes();
	IimportRepository Import();
}
