package com.pgis.bus.data.orm;

import java.util.HashMap;

public class City implements Cloneable {
	public Integer id;
	public double lat;
	public double lon;
	public int scale;
	public boolean isShow;
	public int name_key;
	public HashMap<String, StringValue> name; // key - language id, value - name

	public City clone() {
		try {
			City city;
			city = (City) super.clone();
			city.name = new HashMap<String, StringValue>(this.name);
			city.id = this.id;
			city.lat = this.lat;
			city.lon = this.lon;
			city.scale = this.scale;
			city.name_key = this.name_key;
			city.isShow = this.isShow;
			return city;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
