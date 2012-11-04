package com.pgis.bus.data.models;

import org.postgis.Point;

public class GsonPoint {

	public double x;
	public double y;
	
	public GsonPoint(){
		
	}
	public GsonPoint(GsonPoint copy){
		x = copy.x;
		y = copy.y;
	}
	public GsonPoint(Point p){
		x = p.x;
		y = p.y;
	}
	
	public Point toPoint(){
		return new Point(x,y);
	}
	
	@Override
	public String toString() {
		return "GsonPoint [x=" + x + ", y=" + y + "]";
	}
}
