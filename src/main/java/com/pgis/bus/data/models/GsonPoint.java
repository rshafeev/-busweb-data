package com.pgis.bus.data.models;

import org.postgis.Point;

public class GsonPoint {

	public double lat;
	public double lon;
	
	public GsonPoint(){
		
	}
	public GsonPoint(GsonPoint copy){
		lat = copy.lat;
		lon = copy.lon;
	}
	public GsonPoint(Point p){
		lat = p.x;
		lon = p.y;
	}
	
	public Point toPoint(){
		return new Point(lat,lon);
	}
}
