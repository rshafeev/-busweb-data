package com.pgis.bus.data.impl;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.IDataBaseService;
import com.pgis.bus.data.repositories.ICitiesRepository;
import com.pgis.bus.data.repositories.IMainRepository;
import com.pgis.bus.data.repositories.IPathsRepository;
import com.pgis.bus.data.repositories.IRoutesRepository;
import com.pgis.bus.data.repositories.IStationsRepository;
import com.pgis.bus.data.repositories.IUsersRepository;
import com.pgis.bus.data.repositories.IimportRepository;
import com.pgis.bus.data.repositories.impl.CitiesRepository;
import com.pgis.bus.data.repositories.impl.MainRepository;
import com.pgis.bus.data.repositories.impl.RoutesRepository;
import com.pgis.bus.data.repositories.impl.StationsRepository;
import com.pgis.bus.data.repositories.impl.UsersRepository;
import com.pgis.bus.data.repositories.impl.PathsRepository;

public class DataBaseService implements IDataBaseService {
	protected IUsersRepository usersRepotitory= null;
	protected ICitiesRepository citiesRepotitory= null;
	protected IMainRepository mainRepository= null;
	protected IStationsRepository stationsRepository = null;
	protected IPathsRepository pathsRepository= null;
	protected IRoutesRepository routesRepository= null;
    protected IimportRepository importRepository = null;
	protected IDBConnectionManager connectionManager= null;

	public DataBaseService(){
		
	}
	
	public void setUsersRepotitory(IUsersRepository usersRepotitory) {
		this.usersRepotitory = usersRepotitory;
	}

	public void setCitiesRepotitory(ICitiesRepository citiesRepotitory) {
		this.citiesRepotitory = citiesRepotitory;
	}

	public void setMainRepository(IMainRepository mainRepository) {
		this.mainRepository = mainRepository;
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

	public void setConnectionManager(IDBConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}
	
	

	public void setImportRepository(IimportRepository importRepository) {
		this.importRepository = importRepository;
	}

	public DataBaseService(IDBConnectionManager connectionManager) {
		usersRepotitory = new UsersRepository(connectionManager);
		citiesRepotitory = new CitiesRepository(connectionManager);
		mainRepository = new MainRepository(connectionManager);
		stationsRepository = new StationsRepository(connectionManager);
		pathsRepository = new PathsRepository(connectionManager);
		routesRepository = new RoutesRepository(connectionManager);

	}

	@Override
	public IUsersRepository Users() {
		return usersRepotitory;
	}

	@Override
	public ICitiesRepository Cities() {
		return citiesRepotitory;
	}

	@Override
	public IMainRepository Main() {
		return mainRepository;
	}

	@Override
	public IStationsRepository Stations() {
		return stationsRepository;
	}

	@Override
	public IPathsRepository Paths() {
		return pathsRepository;
	}

	@Override
	public IRoutesRepository Routes() {
		return routesRepository;
	}

	@Override
	public IimportRepository Import() {
		return importRepository;
	}

}
