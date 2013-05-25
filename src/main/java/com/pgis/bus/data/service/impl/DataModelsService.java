package com.pgis.bus.data.service.impl;

import java.sql.SQLException;
import java.util.Locale;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.model.ICitiesModelRepository;
import com.pgis.bus.data.repositories.model.IRoutesModelRepository;
import com.pgis.bus.data.repositories.model.IStationsModelRepository;
import com.pgis.bus.data.repositories.model.impl.CitiesModelRepository;
import com.pgis.bus.data.repositories.model.impl.RoutesModelRepository;
import com.pgis.bus.data.repositories.model.impl.StationsModelRepository;
import com.pgis.bus.data.service.IDataModelsService;
import com.pgis.bus.net.models.LangEnumModel;

public class DataModelsService extends DataService implements IDataModelsService {
	protected LangEnum langID = null;
	protected CitiesModelRepository citiesRepotitory = null;
	protected StationsModelRepository stationsRepository = null;
	protected RoutesModelRepository routesRepository = null;

	public DataModelsService(Locale locale, IConnectionManager rootConnectionManager) throws SQLException {
		super(rootConnectionManager);
		this.setLocale(locale);
	}

	public DataModelsService(LangEnum langID, IConnectionManager rootConnectionManager) throws SQLException {
		super(rootConnectionManager);
		this.setLocale(langID);
	}

	@Override
	public ICitiesModelRepository Cities() {
		if (citiesRepotitory == null) {
			citiesRepotitory = new CitiesModelRepository(langID, connectionManager);
			citiesRepotitory.useOnlyExternConnection(true);
		}
		return citiesRepotitory;
	}

	@Override
	public IStationsModelRepository Stations() {
		if (stationsRepository == null) {
			stationsRepository = new StationsModelRepository(langID, connectionManager);
			stationsRepository.useOnlyExternConnection(true);
		}
		return stationsRepository;
	}

	@Override
	public IRoutesModelRepository Routes() {
		if (routesRepository == null) {
			routesRepository = new RoutesModelRepository(langID, connectionManager);
			routesRepository.useOnlyExternConnection(true);
		}
		return routesRepository;
	}

	@Override
	public void setLocale(Locale locale) {
		this.setLocale(LangEnum.valueOf(locale));
	}

	@Override
	public void setLocale(LangEnum langID) {
		this.langID = langID;

		if (routesRepository != null) {
			routesRepository.setLocale(langID);
		}

		if (citiesRepotitory != null) {
			citiesRepotitory.setLocale(langID);
		}
	}

	@Override
	public void setLocale(LangEnumModel langID) {
		this.setLocale(LangEnum.valueOf(langID));

	}

}
