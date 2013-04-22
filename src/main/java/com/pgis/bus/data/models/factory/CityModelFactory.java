package com.pgis.bus.data.models.factory;

import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.net.models.city.CityModel;
import com.pgis.bus.net.models.geom.PointModel;

public class CityModelFactory {

	public static CityModel createModel(City city, String langID) throws RepositoryException {
		CityModel cityModel = new CityModel();
		cityModel.setId(city.getId());
		cityModel.setLocation(new PointModel(city.getLat(), city.getLon()));
		cityModel.setScale(city.getScale());
		cityModel.setKey(city.getKey());
		cityModel.setName(city.getNameByLang(langID));
		return cityModel;
	}
}
