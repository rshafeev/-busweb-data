package com.pgis.bus.data.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.postgis.Point;

import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.StationTransport;
import com.pgis.bus.data.orm.StringValue;

public class StationModel {
	int id;
	int city_id;
	Point location;
	int name_key;
	StringValue[] names;
	StationTransport[] transports;

	public StationModel() {

	}

	public StationModel(Station station) {
		this.id = station.getId().intValue();
		this.city_id = station.getCity_id();
		this.name_key = station.getName_key();
		this.location = new Point(station.getLocation().x,station.getLocation().y);
		this.location.setSrid(station.getLocation().getSrid());
		this.names = station.getName().values()
				.toArray(new StringValue[station.getName().size()]);
		this.transports = station.getTransports().toArray(
				new StationTransport[station.getTransports().size()]);
	}

	public Station toStation() {
		Station s = new Station();
		s.setCity_id(this.city_id);
		s.setId(this.id);
		s.setLocation(this.location);
		s.setName_key(this.name_key);
		s.setTransports(Arrays.asList(this.transports));

		HashMap<String, StringValue> name = new HashMap<String, StringValue>();

		for (int i = 0; i < this.names.length; i++) {
			name.put(this.names[i].lang_id, this.names[i]);
		}
		s.setName(name);

		return s;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public int getName_key() {
		return name_key;
	}

	public void setName_key(int name_key) {
		this.name_key = name_key;
	}

	public StringValue[] getNames() {
		return names;
	}

	public void setNames(StringValue[] names) {
		this.names = names;
	}

	public StationTransport[] getTransports() {
		return transports;
	}

	public void setTransports(StationTransport[] transports) {
		this.transports = transports;
	}

}
