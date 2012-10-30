package com.pgis.bus.data.models;

import java.util.ArrayList;

import org.postgis.Geometry;
import org.postgis.LineString;
import org.postgis.Point;

public class GsonLineString {

	private GsonPoint points[];

	public GsonLineString() {

	}

	public GsonLineString(GsonPoint[] points) {
		this.points = points;
	}

	public GsonLineString(Geometry lineString) {
		if (lineString != null && lineString instanceof LineString) {
			LineString line = (LineString) lineString;
			this.points = fromLineString(line);
		}
	}

	public GsonPoint[] fromLineString(LineString lineString) {
		if (lineString == null)
			return null;
		ArrayList<GsonPoint> points = new ArrayList<GsonPoint>();
		for (Point p : lineString.getPoints()) {
			points.add(new GsonPoint(p));
		}
		return points.toArray(new GsonPoint[points.size()]);

	}

	public GsonLineString(LineString lineString) {
		this.points = fromLineString(lineString);
	}

	LineString toLineString() {
		ArrayList<Point> points = new ArrayList<Point>();
		for (GsonPoint p : this.points) {
			points.add(p.toPoint());
		}
		return new LineString(points.toArray(new Point[points.size()]));
	}

	public GsonPoint getFirstPoint() {
		if (points == null || points.length == 0) {
			return null;
		}
		return points[0];
	}

	public GsonPoint getLastPoint() {
		if (points == null || points.length == 0) {
			return null;
		}
		return points[points.length - 1];
	}
}
