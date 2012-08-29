package com.pgis.bus.data.geo;

import org.postgis.Point;

/**
 * @author romario
 * 
 */
public class Location {
	public String address;
	public double lat;
	public double lon;

	public Location() {

	}

	public Location(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public Location(Point location) {
		this.lat = location.x;
		this.lon = location.y;
	}
	
	public Point toPGPoint(){
		Point p = new Point();
		p.x = lat;
		p.y = lon;
		p.setSrid(4326);
		return p;
	}

}
