package com.pgis.bus.data.models.factory.route;

import java.util.ArrayList;
import java.util.Collection;

import com.pgis.bus.net.models.route.RouteTypeModel;

public class RouteTypeModelFactory {

	public static RouteTypeModel createModel(String dbRouteTypeID) {
		return new RouteTypeModel(dbRouteTypeID);
	}

	public static Collection<RouteTypeModel> createModels(Collection<String> dbRouteTypeIDs) {
		Collection<RouteTypeModel> models = new ArrayList<RouteTypeModel>();
		for (String dbRouteTypeID : dbRouteTypeIDs) {
			models.add(createModel(dbRouteTypeID));
		}
		return models;
	}
}
