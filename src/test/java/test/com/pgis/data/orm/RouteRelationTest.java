package test.com.pgis.data.orm;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;
import org.postgis.LineString;
import org.postgresql.util.PGInterval;

import com.pgis.bus.data.orm.RouteRelation;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.User;

public class RouteRelationTest {

	@Test
	public void cloneTest() throws CloneNotSupportedException, SQLException {
		RouteRelation route_relation = new RouteRelation();
		route_relation.setId(58);
		route_relation.setRouteWayID(89);
		route_relation.setStationAId(74);
		route_relation.setStationBId(55);
		route_relation.setPositionIndex(3);
		route_relation.setDistance(988);
		route_relation.setMoveTime(new PGInterval());
		route_relation.setGeom(new LineString());
		route_relation.setStationA(new Station());
		route_relation.setStationB(new Station());
		
		RouteRelation route_relation_clone = route_relation.clone();
		
		route_relation.setId(61);
		route_relation.setRouteWayID(77);
		route_relation.setStationAId(98);
		route_relation.setStationBId(41);
		route_relation.setPositionIndex(2);
		route_relation.setDistance(942);
		route_relation.setMoveTime(new PGInterval());
		route_relation.setGeom(new LineString());
		route_relation.setStationA(new Station());
		route_relation.setStationB(new Station());
		
		assertEquals(58, route_relation_clone.getId());
		assertEquals (89, route_relation_clone.getRouteWayID());
		assertEquals(74, route_relation_clone.getStationAId());
		assertEquals (55, route_relation_clone.getStationBId());
		assertEquals (3, route_relation_clone.getPositionIndex());
		assertEquals (988, route_relation_clone.getDistance(), 0.00001);
		assertTrue(route_relation.getMoveTime()!= route_relation_clone.getMoveTime() );
		assertTrue(route_relation.getGeom()!=route_relation_clone.getGeom());
		assertTrue(route_relation.getStationA()!=route_relation_clone.getStationA());
		assertTrue(route_relation.getStationB()!=route_relation_clone.getStationB());
	
	}
	
	@Test
	public void cloneTest2() throws CloneNotSupportedException
	{
		RouteRelation route_relation = new RouteRelation();
		RouteRelation route_relation_clone = route_relation.clone();
		assertNotNull (route_relation_clone);
	}

}
