package com.pgis.bus.data.orm;

import java.util.ArrayList;
import java.util.Collection;

import org.postgis.Point;

public class Station implements Cloneable {
	private Integer id;
	private int city_id;
	private Point location;
	private int name_key;
	private Collection<StringValue> names; // key - language id, value -

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
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


	public Collection<StringValue> getNames() {
		return names;
	}

	public void setNames(Collection<StringValue> names) {
		this.names = names;
	}

	public Station clone() {

		try {
			Station obj = (Station) super.clone();
			obj.location = new Point(this.location.x, this.location.y);
			obj.name_key = this.name_key;

			if (this.id != null)
				obj.id = new Integer(this.id);

			if (this.names != null)
				obj.names = new ArrayList<StringValue>(this.names);

			if (obj.location != null) {
				obj.location.setSrid(this.location.getSrid());

			}
			return obj;
		} catch (CloneNotSupportedException e) {
			return null;
		}

	}

}
