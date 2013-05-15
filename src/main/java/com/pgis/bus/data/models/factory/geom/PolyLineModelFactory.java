package com.pgis.bus.data.models.factory.geom;

import org.postgis.Geometry;
import org.postgis.LineString;
import org.postgis.Point;

import com.pgis.bus.net.models.geom.PolyLineModel;

public class PolyLineModelFactory {

	public static PolyLineModel createModel(LineString line) {
		if (line == null)
			return null;
		Point[] linePoints = line.getPoints();
		double[][] points = new double[linePoints.length][2];
		for (int i = 0; i < linePoints.length; i++) {
			points[i][0] = linePoints[i].x;
			points[i][1] = linePoints[i].y;
		}
		return new PolyLineModel(points);
	}

	public static PolyLineModel createModel(Geometry geometry) {
		if (geometry == null)
			return null;
		if (geometry instanceof LineString) {
			return createModel((LineString) geometry);
		} else
			throw new ClassCastException("can not convert Geometry object to  LineString type.");
	}

}