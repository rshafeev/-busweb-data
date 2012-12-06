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
			obj.copyFrom(this);
			return obj;
		} catch (CloneNotSupportedException e) {
			return null;
		}

	}

	public StringValue getNameByLanguage(String lang_id) {
		for (StringValue v : this.names) {
			if (v.lang_id.equals(lang_id)==true)
				return v;
		}
		return null;
	}

	@Override
	public String toString() {
		return "Station [id=" + id + ", city_id=" + city_id + ", location="
				+ location + ", name_key=" + name_key + ", names=" + names
				+ "]";
	}

	public void copyFrom(Station s) {

		this.name_key = s.name_key;
		this.city_id = s.city_id;

		if (s.id != null)
			this.id = new Integer(s.id);

		if (s.names != null)
			this.names = new ArrayList<StringValue>(s.names);

		if (this.location != null) {
			this.location = new Point(s.location.x, s.location.y);
			this.location.setSrid(s.location.getSrid());
		}

	}

}
