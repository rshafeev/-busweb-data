package com.pgis.bus.data.orm;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pgis.bus.data.models.JsonRouteObjectModel;

public class JsonRouteObject {
	public int id;
	public String city_key;
	public String route_type;
	public String route_number;
	public String obj;

	public JsonRouteObject() {

	}

	public JsonRouteObject(String cityKey, JsonRouteObjectModel model) {
		this.city_key = cityKey;
		this.route_number = model.getNumber();
		this.route_type = model.getRouteType();
		obj = (new Gson()).toJson(model);
	}

	public JsonRouteObjectModel ObjToModel() throws JsonSyntaxException {
		return (new Gson()).fromJson(this.obj, JsonRouteObjectModel.class);
	}
}
