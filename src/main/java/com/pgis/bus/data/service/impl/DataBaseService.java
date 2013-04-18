package com.pgis.bus.data.service.impl;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.repositories.orm.ICitiesRepository;
import com.pgis.bus.data.repositories.orm.ILanguagesRepository;
import com.pgis.bus.data.repositories.orm.IObjectsRepository;
import com.pgis.bus.data.repositories.orm.IPathsRepository;
import com.pgis.bus.data.repositories.orm.IRoutesRepository;
import com.pgis.bus.data.repositories.orm.IStationsRepository;
import com.pgis.bus.data.repositories.orm.IUsersRepository;
import com.pgis.bus.data.repositories.orm.impl.CitiesRepository;
import com.pgis.bus.data.repositories.orm.impl.LanguagesRepository;
import com.pgis.bus.data.repositories.orm.impl.PathsRepository;
import com.pgis.bus.data.repositories.orm.impl.RoutesRepository;
import com.pgis.bus.data.repositories.orm.impl.StationsRepository;
import com.pgis.bus.data.repositories.orm.impl.UsersRepository;
import com.pgis.bus.data.service.IDataBaseService;

public class DataBaseService implements IDataBaseService {
	protected IDBConnectionManager connectionManager = null;

	protected IUsersRepository usersRepotitory = null;
	protected ICitiesRepository citiesRepotitory = null;
	protected ILanguagesRepository langsRepository = null;
	protected IStationsRepository stationsRepository = null;
	protected IPathsRepository pathsRepository = null;
	protected IRoutesRepository routesRepository = null;
	protected IObjectsRepository importRepository = null;

	public void setUsersRepotitory(IUsersRepository usersRepotitory) {
		this.usersRepotitory = usersRepotitory;
	}

	public void setCitiesRepotitory(ICitiesRepository citiesRepotitory) {
		this.citiesRepotitory = citiesRepotitory;
	}

	public void setLangsRepository(ILanguagesRepository langsRepository) {
		this.langsRepository = langsRepository;
	}

	public void setStationsRepository(IStationsRepository stationsRepository) {
		this.stationsRepository = stationsRepository;
	}

	public void setPathsRepository(IPathsRepository pathsRepository) {
		this.pathsRepository = pathsRepository;
	}

	public void setRoutesRepository(IRoutesRepository routesRepository) {
		this.routesRepository = routesRepository;
	}

	public void setImportRepository(IObjectsRepository importRepository) {
		this.importRepository = importRepository;
	}

	public void setConnectionManager(IDBConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	public DataBaseService(IDBConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	@Override
	public IUsersRepository Users() {
		if (usersRepotitory == null && connectionManager != null) {
			usersRepotitory = new UsersRepository(connectionManager);
		}

		return usersRepotitory;
	}

	@Override
	public ICitiesRepository Cities() {
		if (citiesRepotitory == null && connectionManager != null) {
			citiesRepotitory = new CitiesRepository(connectionManager);
		}

		return citiesRepotitory;
	}

	@Override
	public ILanguagesRepository Langs() {
		if (langsRepository == null && connectionManager != null) {
			langsRepository = new LanguagesRepository(connectionManager);
		}
		return langsRepository;
	}

	@Override
	public IStationsRepository Stations() {
		if (stationsRepository == null && connectionManager != null) {
			stationsRepository = new StationsRepository(connectionManager);
		}
		return stationsRepository;
	}

	@Override
	public IPathsRepository Paths() {
		if (pathsRepository == null && connectionManager != null) {
			pathsRepository = new PathsRepository(connectionManager);
		}
		return pathsRepository;
	}

	@Override
	public IRoutesRepository Routes() {
		if (routesRepository == null && connectionManager != null) {
			routesRepository = new RoutesRepository(connectionManager);
		}

		return routesRepository;
	}

	@Override
	public IObjectsRepository ImportObjects() {
		return importRepository;
	}

}
