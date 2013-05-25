package test.com.pgis.data.models.factory.geom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.postgis.LineString;
import org.postgis.Point;

import com.pgis.bus.data.orm.type.LineStringEx;
import com.pgis.bus.net.models.geom.PolyLineModel;

public class PolyLineModelExTest {
	@Test
	public void createModelTest() {
		Point pointA = new Point(30, 56);
		Point pointB = new Point(45, 78);
		Point[] points = new Point[2];
		points[0] = pointA;
		points[1] = pointB;
		LineStringEx line1 = new LineStringEx(points);
		PolyLineModel model = line1.toModel();
		assertEquals(30, model.getPoint(0)[0], 0.000001);
		assertEquals(56, model.getPoint(0)[1], 0.000001);
		assertEquals(45, model.getPoint(1)[0], 0.000001);
		assertEquals(78, model.getPoint(1)[1], 0.000001);
		assertTrue(model.getPointsCount() == 2);
		assertTrue(model.getPoints().length == 2);

		LineStringEx line2 = new LineStringEx(new PolyLineModel());

	}
}
