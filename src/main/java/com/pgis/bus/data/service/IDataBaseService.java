package com.pgis.bus.data.service;

import com.pgis.bus.data.repositories.orm.ICitiesRepository;
import com.pgis.bus.data.repositories.orm.IJsonRouteObjectsRepository;
import com.pgis.bus.data.repositories.orm.ILanguagesRepository;
import com.pgis.bus.data.repositories.orm.IPathsRepository;
import com.pgis.bus.data.repositories.orm.IRoutesRepository;
import com.pgis.bus.data.repositories.orm.IScheduleRepository;
import com.pgis.bus.data.repositories.orm.IStationsRepository;
import com.pgis.bus.data.repositories.orm.IUsersRepository;

public interface IDataBaseService extends IDataService {

	IUsersRepository Users();

	ICitiesRepository Cities();

	ILanguagesRepository Langs();

	IStationsRepository Stations();

	IPathsRepository Paths();

	IRoutesRepository Routes();

	IJsonRouteObjectsRepository JsonRouteObjects();

	IScheduleRepository Schedule();
}
