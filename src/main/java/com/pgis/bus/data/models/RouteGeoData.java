package com.pgis.bus.data.models;

import java.util.ArrayList;
import java.util.Collection;

import org.postgis.LineString;
import org.postgis.Point;

public class RouteGeoData {
	private int index;
	private String stationName;
	private LineString relationGeom;
	private Point stationLocation;

	public int getIndex() {
		return index;
	}

	public String getStringGeom() {
		String out = "";
		int count = relationGeom.numPoints();
		out += "[";
		for (int i = 0; i < count; i++) {
			Point p = relationGeom.getPoint(i);
			out += "[";
			out += p.x + "," + p.y;
			out += "]";
			if (i != count - 1) {
				out += ",";
			}
		}
		out += "]";
		return out;

	}
	public Collection<Double[]> relationGeomToArray(){
		 Collection<Double[]> points = new  ArrayList<Double[]>();
		 for(Point p : relationGeom.getPoints()){
			 points.add(new Double[]{p.x,p.y});
		 }
		 return points;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public LineString getRelationGeom() {
		return relationGeom;
	}

	public void setRelationGeom(LineString relationGeom) {
		this.relationGeom = relationGeom;
	}

	public Point getStationLocation() {
		return stationLocation;
	}

	public void setStationLocation(Point stationLocation) {
		this.stationLocation = stationLocation;
	}

	@Override
	public String toString() {
		return "RouteGeoData [index=" + index + ", stationName=" + stationName
				+ ", relationGeom=" + relationGeom + ", stationLocation="
				+ stationLocation + "]";
	}
}
