package com.pgis.bus.data.models.factory;

import com.pgis.bus.data.orm.City;
import com.pgis.bus.net.models.city.CityModel;
import com.pgis.bus.net.models.geom.PointModel;

public class CityModelFactory {

	public static CityModel createModel(City city, String langID) {
		CityModel cityModel = new CityModel();
		cityModel.setId(city.getId());
		cityModel.setLocation(new PointModel(city.getLat(), city.getLon()));
		cityModel.setScale(city.scale);
		cityModel.setKey(city.key);
		cityModel.setName(city.getNameByLang(langID));
		return cityModel;
	}
}
