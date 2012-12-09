package com.pgis.bus.data.orm;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pgis.bus.data.models.ImportRouteModel;

public class ImportObject {
	public int id;
	public String city_key;
	public String route_type;
	public String route_number;
	public String obj;

	public ImportObject() {

	}

	public ImportObject(String cityKey, ImportRouteModel model) {
		this.city_key = cityKey;
		this.route_number = model.getNumber();
		this.route_type = model.getRouteType();
		obj = (new Gson()).toJson(model);
	}

	public ImportRouteModel ObjToModel() throws JsonSyntaxException {
		return (new Gson()).fromJson(this.obj, ImportRouteModel.class);
	}
}
