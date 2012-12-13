package com.pgis.bus.data.models;

import static java.lang.Math.*;
import org.postgis.Point;

public class GsonPoint {

	public double x;
	public double y;

	public GsonPoint() {

	}

	public GsonPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public GsonPoint(GsonPoint copy) {
		x = copy.x;
		y = copy.y;
	}

	public GsonPoint(Point p) {
		x = p.x;
		y = p.y;
	}

	public Point toPoint() {
		return new Point(x, y);
	}

	@Override
	public String toString() {
		return "GsonPoint [x=" + x + ", y=" + y + "]";
	}

}
