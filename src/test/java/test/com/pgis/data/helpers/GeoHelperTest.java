package test.com.pgis.data.helpers;

import static org.junit.Assert.*;

import org.junit.Test;
import org.postgis.Point;

import com.pgis.bus.data.helpers.GeoHelper;

public class GeoHelperTest {

	@Test
	public void getDistanceFromPointToLineTest1() {
		Point p = new Point(50, 36.00001);

		Point p1 = new Point(50, 36);
		Point p2 = new Point(40, 30);

		double distance1 = GeoHelper.getDistance(p.x, p.y, p1.x, p1.y, p2.x,
				p2.y);
		double distance2 = GeoHelper.getDistance(p1.x, p1.y, p2.x, p2.y);
		System.out.println(distance1 - distance2);
		System.out.println(distance1);
		System.out.println(distance2);
		assertTrue(distance1 >= 0);
		assertTrue(distance2 >= 0);
		assertEquals(0, distance1 - distance2, 1);
	}

	@Test
	public void getDistanceTest() {
		Point p1 = new Point(50, 36);
		Point p2 = new Point(50, 36.1);

		double distance = GeoHelper.getDistance(p1.x, p1.y, p2.x, p2.y);
		System.out.println(distance);
		assertTrue(distance >= 0);
	}

}
