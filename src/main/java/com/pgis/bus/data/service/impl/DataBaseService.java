package com.pgis.bus.data.service.impl;

import java.sql.SQLException;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.repositories.orm.ICitiesRepository;
import com.pgis.bus.data.repositories.orm.ISerializedRoutesRepository;
import com.pgis.bus.data.repositories.orm.ILanguagesRepository;
import com.pgis.bus.data.repositories.orm.IPathsRepository;
import com.pgis.bus.data.repositories.orm.IRoutesRepository;
import com.pgis.bus.data.repositories.orm.IScheduleRepository;
import com.pgis.bus.data.repositories.orm.IStationsRepository;
import com.pgis.bus.data.repositories.orm.IUsersRepository;
import com.pgis.bus.data.repositories.orm.impl.CitiesRepository;
import com.pgis.bus.data.repositories.orm.impl.SerializedRoutesRepository;
import com.pgis.bus.data.repositories.orm.impl.LanguagesRepository;
import com.pgis.bus.data.repositories.orm.impl.PathsRepository;
import com.pgis.bus.data.repositories.orm.impl.RoutesRepository;
import com.pgis.bus.data.repositories.orm.impl.ScheduleRepository;
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
	protected SerializedRoutesRepository objectsRepository = null;
	protected ScheduleRepository scheduleRepository = null;

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
	public ISerializedRoutesRepository JsonRouteObjects() {
		if (objectsRepository == null) {
			objectsRepository = new SerializedRoutesRepository(connectionManager);
			objectsRepository.useOnlyExternConnection(true);

		}
		return objectsRepository;
	}

	@Override
	public IScheduleRepository Schedule() {
		if (scheduleRepository == null) {
			scheduleRepository = new ScheduleRepository(connectionManager);
			scheduleRepository.useOnlyExternConnection(true);

		}
		return scheduleRepository;
	}

}
