package com.pgis.bus.data.models;

import java.util.ArrayList;
import java.util.Collection;

public class JsonRouteObjectsListModel {

	public class ImportRoute {
		int id;
		String number;
	}

	private String cityKey;
	private String routeType;

	private Collection<ImportRoute> routes;

	public String getCityKey() {
		return cityKey;
	}

	public void setCityKey(String cityKey) {
		this.cityKey = cityKey;
	}

	public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}

	public Collection<ImportRoute> getRoutes() {
		return routes;
	}

	public void setRoutes(Collection<ImportRoute> routes) {
		this.routes = routes;
	}

	public void addImportRouteObject(int importObjID, String routeNumber) {
		if (this.routes == null)
			this.routes = new ArrayList<ImportRoute>();
		ImportRoute r = new ImportRoute();
		r.id = importObjID;
		r.number = routeNumber;
		this.routes.add(r);
	}
}
