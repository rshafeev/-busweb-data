package com.pgis.bus.data.orm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.postgis.Point;

public class Station implements Cloneable {
	private Integer id;
	private int city_id;
	private Point location;
	private int name_key;
	private HashMap<String, StringValue> name; // key - language id, value -
	private Collection<StationTransport> transports;

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public Collection<StationTransport> getTransports() {
		return transports;
	}

	public void setTransports(Collection<StationTransport> transports) {
		this.transports = transports;
	}

	public Point getLocation() {
		return location;
	}

	// name

	// NodeTransports
	public void setLocation(Point location) {
		this.location = location;
	}

	public void setLocation(double lat, double lon) {
		location = new Point(lat, lon);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getName_key() {
		return name_key;
	}

	public void setName_key(int name_key) {
		this.name_key = name_key;
	}

	public HashMap<String, StringValue> getName() {
		return name;
	}

	public void setName(HashMap<String, StringValue> name) {
		this.name = name;
	}

	public Station clone() {
		Station obj;
		try {
			obj = (Station) super.clone();
			obj.location = new Point(this.location.x, this.location.y);
			obj.name_key = this.name_key;

			if (this.id != null)
				obj.id = new Integer(this.id);

			if (this.name != null)
				obj.name = new HashMap<String, StringValue>(this.name);

			if (this.transports != null)
				obj.transports = new ArrayList<StationTransport>(
						this.transports);
			if(obj.location!=null){
				obj.location.setSrid(this.location.getSrid());
				
			}
			return obj;
		} catch (CloneNotSupportedException e) {
			return null;
		}

	}

}
