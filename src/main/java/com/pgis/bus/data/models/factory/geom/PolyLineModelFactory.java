package com.pgis.bus.data.models.factory.geom;

import org.postgis.Geometry;
import org.postgis.LineString;
import org.postgis.Point;

import com.pgis.bus.net.models.geom.PolyLineModel;

public class PolyLineModelFactory {

	public static PolyLineModel createModel(LineString line) {
		Point[] linePoints = line.getPoints();
		double[][] points = new double[linePoints.length][2];
		for (int i = 0; i < linePoints.length; i++) {
			points[i][0] = linePoints[i].x;
			points[i][1] = linePoints[i].y;
		}
		return new PolyLineModel(points);
	}

	public static PolyLineModel createModel(Geometry geometry) throws Exception {
		if (geometry instanceof LineString) {
			return createModel((LineString) geometry);
		} else
			throw new Exception("can not convert Geometry object to  LineString type.");
	}

}
