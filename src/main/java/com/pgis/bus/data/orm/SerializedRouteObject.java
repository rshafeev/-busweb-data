package com.pgis.bus.data.orm;

public class SerializedRouteObject implements Cloneable {
	public int id;
	public String city_key;
	public String route_type;
	public String route_number;
	public String obj;
	public String ver;

	public SerializedRouteObject() {

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

	@Override
	public SerializedRouteObject clone() throws CloneNotSupportedException {
		SerializedRouteObject jro = (SerializedRouteObject) super.clone();
		jro.id = this.id;

		if (this.city_key != null) {
			jro.city_key = new String(this.city_key);
		}
		if (this.route_type != null) {
			jro.route_type = new String(this.route_type);
		}
		if (this.route_number != null) {
			jro.route_number = new String(this.route_number);
		}
		if (this.obj != null) {
			jro.obj = new String(this.obj);
		}
		return jro;
	}
}
