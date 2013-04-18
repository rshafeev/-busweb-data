package com.pgis.bus.data.models.factory.route;

import com.pgis.bus.data.orm.Route;
import com.pgis.bus.net.models.route.RouteModel;

public class RouteModelFactory {

	public static RouteModel createModel(Route route, String langID) {
		RouteModel model = new RouteModel();
		model.setCityID(route.getCityID());
		model.setNumber(route.getNumber(langID));
		model.setId(route.getId());
		model.setRouteTypeID(route.getRouteTypeID());
		model.setCost(route.getCost());
		model.setId(route.getId());

		return model;

	}
}
