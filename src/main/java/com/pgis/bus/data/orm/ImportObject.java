package com.pgis.bus.data.orm;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pgis.bus.data.models.ImportRouteModel;

public class ImportObject {
	public int id;
	public int city_id;
	public String route_type;
	public String route_number;
	public String obj;
	public ImportObject(){
		
		
	}
	public ImportObject(ImportRouteModel model) {
		city_id = model.getCityID();
		id = model.getRouteID();
		route_number = model.getNumber();
		route_type = model.getRouteType();
		obj = (new Gson()).toJson(model);
	}

	public ImportRouteModel ObjToModel() throws JsonSyntaxException {
		return (new Gson()).fromJson(this.obj, ImportRouteModel.class);
	}
}
