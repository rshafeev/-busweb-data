package com.pgis.bus.data.models.factory.geom;

import org.postgis.Point;

import com.pgis.bus.net.models.geom.PointModel;

public class PointModelFactory {

	public static PointModel createModel(Point p) {
		return new PointModel(p.x, p.y);
	}
}
