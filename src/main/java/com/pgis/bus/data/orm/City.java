package com.pgis.bus.data.orm;

import java.util.HashMap;

public class City {
	public int id;
	public double lat;
	public double lon;
	public int name_key;
	public HashMap<String,String> name; //key - language id, value - name
}
