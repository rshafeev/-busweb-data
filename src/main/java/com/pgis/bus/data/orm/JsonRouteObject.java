package com.pgis.bus.data.orm;

public class JsonRouteObject {
	public int id;
	public String city_key;
	public String route_type;
	public String route_number;
	public String obj;

	public JsonRouteObject() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity_key() {
		return city_key;
	}

	public void setCity_key(String city_key) {
		this.city_key = city_key;
	}

	public String getRoute_type() {
		return route_type;
	}

	public void setRoute_type(String route_type) {
		this.route_type = route_type;
	}

	public String getRoute_number() {
		return route_number;
	}

	public void setRoute_number(String route_number) {
		this.route_number = route_number;
	}

	public String getObj() {
		return obj;
	}

	public void setObj(String obj) {
		this.obj = obj;
	}

}
