package test.com.pgis.data.orm;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.RouteWay;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.ScheduleGroup;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.orm.User;
import com.pgis.bus.data.orm.type.LangEnum;

public class RouteTest {

	@Test
	public void cloneTest() throws CloneNotSupportedException, SQLException {
		Route route = new Route();
		route.setId(23);
		route.setCityID(1);
		route.setCost(2.50);
		route.setRouteTypeID("str1");
		route.setNumberKey(12);
		
		Collection<StringValue> number_collection = new ArrayList<StringValue>();
		
		StringValue numb = new StringValue();
		numb.setId(123);
		numb.setKeyID(23);
		numb.setLangID(LangEnum.c_en);
		
		numb.setValue("strr");
		
		
		StringValue numb2 = new StringValue();
		numb2.setId(127);
		numb2.setKeyID(27);
		numb2.setLangID(LangEnum.c_ru);
		numb2.setValue("ds");
		
		number_collection.add(numb);
		number_collection.add(numb2);
		
		route.setNumber(number_collection);
		
		route.setDirectRouteWay(new RouteWay());
		route.setReverseRouteWay(new RouteWay());
		Route route_clone = route.clone();
		
		route.setId(69);
		route.setCityID(54);
		route.setCost(2.78);
		route.setRouteTypeID("str2");
		route.setNumberKey(7);
		
		StringValue numb3 = new StringValue();
		numb3.setId(96);
		numb3.setKeyID(8);
		numb3.setLangID(LangEnum.c_ru);
		numb3.setValue("dsvs");
		number_collection.add(numb3);
		
		assertEquals(23, (int)route_clone.getId());
		assertEquals(1, route_clone.getCityID());
		assertEquals(2.50, route_clone.getCost(), 0.00001);
		assertEquals("str1",route_clone.getRouteTypeID() );
		assertEquals(12,(int)route_clone.getNumberKey() );
		assertEquals(2, route_clone.getNumber().size());
		assertTrue(route.getDirectRouteWay() != route_clone.getDirectRouteWay());
		assertTrue(route.getReverseRouteWay() != route_clone.getReverseRouteWay());

	}
	
	@Test
	public void cloneTest2() throws CloneNotSupportedException
	{
		Route route = new Route();
		Route route_clone = route.clone();
		assertNotNull (route_clone);
	}

}
