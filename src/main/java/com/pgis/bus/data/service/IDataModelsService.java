package com.pgis.bus.data.service;

import java.util.Locale;

import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.model.ICitiesModelRepository;
import com.pgis.bus.data.repositories.model.IRoutesModelRepository;
import com.pgis.bus.data.repositories.model.IStationsModelRepository;
import com.pgis.bus.net.models.LangEnumModel;

public interface IDataModelsService extends IDataService {

	ICitiesModelRepository Cities();

	IStationsModelRepository Stations();

	IRoutesModelRepository Routes();

	void setLocale(Locale locale);

	void setLocale(LangEnum langID);

	void setLocale(LangEnumModel langID);

}
