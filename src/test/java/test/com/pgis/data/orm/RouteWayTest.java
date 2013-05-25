package test.com.pgis.data.orm;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.postgis.LineString;
import org.postgresql.util.PGInterval;

import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.RouteRelation;
import com.pgis.bus.data.orm.RouteWay;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.Station;

public class RouteWayTest {

	@Test
	public void cloneTest() throws CloneNotSupportedException, SQLException {
		RouteWay routeWay = new RouteWay();
		routeWay.setId(23);
		routeWay.setRouteID(41);
		routeWay.setDirect(false);
		routeWay.setSchedule(new Schedule());
		Collection <RouteRelation> routeRelation = new ArrayList<RouteRelation>();
		
		RouteRelation rr = new RouteRelation();
		rr.setId(58);
		rr.setRouteWayID(89);
		rr.setStationAId(74);
		rr.setStationBId(55);
		rr.setPositionIndex(3);
		rr.setDistance(988);
		rr.setMoveTime(new PGInterval());
		rr.setGeom(new LineString());
		rr.setStationA(new Station());
		rr.setStationB(new Station());
		
		routeRelation.add(rr);
		routeWay.setRouteRelations(routeRelation);
		
		
		RouteWay routeWay_clone = routeWay.clone();
		
		routeWay.setId(11);
		routeWay.setRouteID(42);
		routeWay.setDirect(true);
		routeWay.setSchedule(new Schedule());
		
		RouteRelation rr2 = new RouteRelation();
		rr2.setId(47);
		rr2.setRouteWayID(20);
		rr2.setStationAId(21);
		rr2.setStationBId(36);
		rr2.setPositionIndex(4);
		rr2.setDistance(456);
		rr2.setMoveTime(new PGInterval());
		rr2.setGeom(new LineString());
		rr2.setStationA(new Station());
		rr2.setStationB(new Station());
		routeRelation.add(rr2);
			
		assertEquals(23, (int)routeWay_clone.getId());
		assertEquals(41, routeWay_clone.getRouteID());
		assertEquals(false, routeWay_clone.isDirect());
		assertTrue(routeWay.getSchedule() != routeWay_clone.getSchedule());
		assertEquals(1, routeWay_clone.getRouteRelations().size());

	}
	
	@Test
	public void cloneTest2() throws CloneNotSupportedException
	{
		RouteRelation routeWay = new RouteRelation();
		RouteRelation routeWay_clone = routeWay.clone();
		assertNotNull (routeWay_clone);
	}

}
