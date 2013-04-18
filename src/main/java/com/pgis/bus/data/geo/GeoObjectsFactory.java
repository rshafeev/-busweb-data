package com.pgis.bus.data.geo;

import org.postgis.LineString;
import org.postgis.Point;

import com.pgis.bus.data.params.DefaultParameters;
import com.pgis.bus.net.models.geom.PointModel;
import com.pgis.bus.net.models.geom.PolyLineModel;

public class GeoObjectsFactory {

	public static Point createPoint(PointModel m) {
		Point p = new Point();
		p.setX(m.getLat());
		p.setY(m.getLon());
		p.setSrid(DefaultParameters.GEOMETRY_SRID);
		return p;
	}

	public static Point createPoint(double[] xy) {
		Point p = new Point();
		p.setX(xy[0]);
		p.setY(xy[1]);
		p.setSrid(DefaultParameters.GEOMETRY_SRID);
		return p;
	}

	public static LineString createLine(PolyLineModel m) {
		Point[] points = new Point[m.getPointsCount()];
		for (int i = 0; i < m.getPointsCount(); i++) {
			points[i] = createPoint(m.getPoint(i));
		}
		LineString line = new LineString(points);
		line.setSrid(DefaultParameters.GEOMETRY_SRID);
		return line;
	}
}
