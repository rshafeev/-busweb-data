package com.pgis.bus.data.orm.type;

import org.postgis.LineString;
import org.postgis.Point;

import com.pgis.bus.net.models.geom.PolyLineModel;

public class LineStringEx extends LineString implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LineStringEx(LineString line) {
		super(line.getPoints());
	}

	public LineStringEx() {
		super();
	}

	public LineStringEx(Point[] points) {
		super(points);
	}

	public LineStringEx(PolyLineModel m) {
		super(convertToArray(m));
	}

	private static Point[] convertToArray(PolyLineModel m) {
		if (m == null)
			return null;
		Point[] points = new Point[m.getPointsCount()];
		for (int i = 0; i < m.getPointsCount(); i++) {
			points[i] = new Point(m.getPoint(i)[0], m.getPoint(i)[1]);
		}
		return points;
	}

	public PolyLineModel toModel() {
		Point[] linePoints = this.getPoints();
		double[][] points = new double[linePoints.length][2];
		for (int i = 0; i < linePoints.length; i++) {
			points[i][0] = linePoints[i].x;
			points[i][1] = linePoints[i].y;
		}
		return new PolyLineModel(points);
	}

	@Override
	public LineStringEx clone() {
		if (this.isEmpty())
			return new LineStringEx();
		Point[] points = this.getPoints();
		Point[] copiedPoints = new Point[points.length];
		for (int i = 0; i < points.length; i++) {
			copiedPoints[i] = new Point(points[i].x, points[i].y);
		}
		LineStringEx copy = new LineStringEx(copiedPoints);
		copy.srid = this.srid;
		return copy;
	}

	public LineStringEx reverse() {
		Point[] points = this.getPoints();
		Point[] copiedPoints = new Point[points.length];
		for (int i = 0; i < points.length; i++) {
			copiedPoints[points.length - 1 - i] = new Point(points[i].x, points[i].y);
		}
		LineStringEx reverse = new LineStringEx(copiedPoints);
		reverse.srid = this.srid;
		return reverse;
	}
}
