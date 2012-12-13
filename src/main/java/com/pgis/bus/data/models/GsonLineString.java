package com.pgis.bus.data.models;

import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.postgis.Geometry;
import org.postgis.LineString;
import org.postgis.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.helpers.GeoHelper;
import com.pgis.bus.data.orm.ScheduleGroupDay;
import com.pgis.bus.data.orm.type.DayEnum;
import com.pgis.bus.data.repositories.impl.CitiesRepository;

public class GsonLineString {
	private static final Logger log = LoggerFactory
			.getLogger(GsonLineString.class);
	private GsonPoint points[];

	public GsonLineString() {

	}
	public GsonLineString(Collection<GsonPoint> points) {
		this.points = points.toArray(new GsonPoint[points.size()]);
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

	public LineString toLineString() {
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

	public GsonPoint[] getPoints() {
		return points;
	}

	public void setPoints(GsonPoint[] points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "GsonLineString [points=" + Arrays.toString(points) + "]";
	}

	private boolean isCanDeletePoint(GsonPoint p, GsonPoint p1, GsonPoint p2) {
		double d1 = GeoHelper.getDistance(p1.x, p1.y, p2.x, p2.y);
		double d2 = GeoHelper.getDistance(p1.x, p1.y, p.x, p.y, p2.x, p2.y);
		if (Math.abs(d1 - d2) < 0.01) {
			return true;
		}
		return false;
	}

	public void optimizePoints() {
		log.debug("before optimize: " + Integer.toString(this.points.length));
		if (this.points == null || this.points.length < 3) {
			return;
		}

		Collection<GsonPoint> optPoints = new ArrayList<GsonPoint>(
				this.points.length);
		optPoints.add(this.points[0]);
		GsonPoint prevPoint = this.points[0];
		for (int i = 1; i < this.points.length - 1; i++) {
			if (isCanDeletePoint(points[i], prevPoint, points[i + 1]) == false) {
				optPoints.add(this.points[i]);
				prevPoint = this.points[i];
			}
		}
		optPoints.add(this.points[this.points.length - 1]);

		this.points = optPoints.toArray(new GsonPoint[optPoints.size()]);
		log.debug("after  optimize: " + Integer.toString(this.points.length));
	}

	public boolean isValid() {
		if (this.points == null || this.points.length < 2)
			return false;

		return true;
	}
}
