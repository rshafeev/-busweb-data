package com.pgis.bus.data.orm;

import java.util.HashMap;

public class City {
	public Integer id;
	public double lat;
	public double lon;
	public int scale;
	public int name_key;
	public HashMap<String,StringValue> name; //key - language id, value - name
	
	public City Clone(){
		City city = new City();
		city.name = new HashMap<String, StringValue>(this.name);
		city.id = this.id;
		city.lat = this.lat;
		city.lon = this.lon;
		city.scale = this.scale;
		city.name_key = this.name_key;
		return city;
	}
}
