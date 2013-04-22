package com.pgis.bus.data.service.impl;

import java.sql.SQLException;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.repositories.orm.ICitiesRepository;
import com.pgis.bus.data.repositories.orm.IJsonRouteObjectsRepository;
import com.pgis.bus.data.repositories.orm.ILanguagesRepository;
import com.pgis.bus.data.repositories.orm.IPathsRepository;
import com.pgis.bus.data.repositories.orm.IRoutesRepository;
import com.pgis.bus.data.repositories.orm.IStationsRepository;
import com.pgis.bus.data.repositories.orm.IUsersRepository;
import com.pgis.bus.data.repositories.orm.impl.CitiesRepository;
import com.pgis.bus.data.repositories.orm.impl.JsonRouteObjectsRepository;
import com.pgis.bus.data.repositories.orm.impl.LanguagesRepository;
import com.pgis.bus.data.repositories.orm.impl.PathsRepository;
import com.pgis.bus.data.repositories.orm.impl.RoutesRepository;
import com.pgis.bus.data.repositories.orm.impl.StationsRepository;
import com.pgis.bus.data.repositories.orm.impl.UsersRepository;
import com.pgis.bus.data.service.IDataBaseService;

public class DataBaseService extends DataService implements IDataBaseService {

	protected UsersRepository usersRepotitory = null;
	protected CitiesRepository citiesRepotitory = null;
	protected LanguagesRepository langsRepository = null;
	protected StationsRepository stationsRepository = null;
	protected PathsRepository pathsRepository = null;
	protected RoutesRepository routesRepository = null;
	protected JsonRouteObjectsRepository objectsRepository = null;

	public DataBaseService(IConnectionManager rootConnectionManager) throws SQLException {
		super(rootConnectionManager);
	}

	@Override
	public IUsersRepository Users() {
		if (usersRepotitory == null) {
			usersRepotitory = new UsersRepository(connectionManager);
			usersRepotitory.useOnlyExternConnection(true);
		}

		return usersRepotitory;
	}

	@Override
	public ICitiesRepository Cities() {
		if (citiesRepotitory == null) {
			citiesRepotitory = new CitiesRepository(connectionManager);
			citiesRepotitory.useOnlyExternConnection(true);
		}

		return citiesRepotitory;
	}

	@Override
	public ILanguagesRepository Langs() {
		if (langsRepository == null) {
			langsRepository = new LanguagesRepository(connectionManager);
			langsRepository.useOnlyExternConnection(true);
		}
		return langsRepository;
	}

	@Override
	public IStationsRepository Stations() {
		if (stationsRepository == null) {
			stationsRepository = new StationsRepository(connectionManager);
			stationsRepository.useOnlyExternConnection(true);

		}
		return stationsRepository;
	}

	@Override
	public IPathsRepository Paths() {
		if (pathsRepository == null) {
			pathsRepository = new PathsRepository(connectionManager);
			pathsRepository.useOnlyExternConnection(true);

		}
		return pathsRepository;
	}

	@Override
	public IRoutesRepository Routes() {
		if (routesRepository == null) {
			routesRepository = new RoutesRepository(connectionManager);
			routesRepository.useOnlyExternConnection(true);

		}
		return routesRepository;
	}

	@Override
	public IJsonRouteObjectsRepository JsonRouteObjects() {
		if (objectsRepository == null) {
			objectsRepository = new JsonRouteObjectsRepository(connectionManager);
			objectsRepository.useOnlyExternConnection(true);

		}
		return objectsRepository;
	}

}
