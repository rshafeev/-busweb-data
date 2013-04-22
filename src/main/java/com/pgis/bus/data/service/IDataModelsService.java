package com.pgis.bus.data.service;

import java.util.Locale;

import com.pgis.bus.data.repositories.model.ICitiesModelRepository;
import com.pgis.bus.data.repositories.model.IJsonRouteObjectsModelRepository;
import com.pgis.bus.data.repositories.model.IRoutesModelRepository;
import com.pgis.bus.data.repositories.model.IStationsModelRepository;

public interface IDataModelsService extends IDataService {

	ICitiesModelRepository Cities();

	IStationsModelRepository Stations();

	IRoutesModelRepository Routes();

	IJsonRouteObjectsModelRepository JsonRouteObjects();

	void setLocale(Locale locale);

	void setLocale(String langID);

}
