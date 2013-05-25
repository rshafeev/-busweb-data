package com.pgis.bus.data.helpers;

import org.postgis.LineString;
import org.postgis.Point;

import com.pgis.bus.net.models.geom.PointModel;
import com.pgis.bus.net.models.geom.PolyLineModel;

public class GeoObjectsHelper {

	public final static int GEOMETRY_SRID = 4326;

	public static Point createPoint(PointModel m) {
		Point p = new Point();
		p.setX(m.getLat());
		p.setY(m.getLon());
		p.setSrid(GEOMETRY_SRID);
		return p;
	}

	public static Point createPoint(double lat, double lon) {
		Point p = new Point();
		p.setX(lat);
		p.setY(lon);
		p.setSrid(GEOMETRY_SRID);
		return p;
	}

	public static Point createPoint(double[] xy) {
		Point p = new Point();
		p.setX(xy[0]);
		p.setY(xy[1]);
		p.setSrid(GEOMETRY_SRID);
		return p;
	}

	public static LineString createLine(PolyLineModel m) {
		if (m == null)
			return null;
		Point[] points = new Point[m.getPointsCount()];
		for (int i = 0; i < m.getPointsCount(); i++) {
			points[i] = createPoint(m.getPoint(i));
		}
		LineString line = new LineString(points);
		line.setSrid(GEOMETRY_SRID);
		return line;
	}
	
	public static LineString clone(LineString line){
		if (line == null)
			return null;
		if(line.isEmpty())
			return new LineString();
		Point[] points = line.getPoints();
		Point[] copiedPoints = new Point[points.length];
		for(int i=0;i < points.length; i++ ){
			copiedPoints[i] = new Point(points[i].x, points[i].y);
		}
		LineString copy = new LineString(copiedPoints);
		copy.srid = line.srid;
		return copy;
	}
}
