package test.com.pgis.data.orm;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pgis.bus.data.orm.JsonRouteObject;
import com.pgis.bus.data.orm.Language;

public class JsonRouteObjectTest {

	@Test
	public void cloneTest() throws CloneNotSupportedException {
		JsonRouteObject jsro = new JsonRouteObject();
		jsro.setId(56);
		jsro.setObj("str1");
		jsro.setRoute_number("56c");
		jsro.setRoute_type("bus");
		
		JsonRouteObject jsro_clone = jsro.clone();
		
		jsro.setId(89);
		jsro.setObj("str2");
		jsro.setRoute_number("46c");
		jsro.setRoute_type("tram");
		
		assertEquals(56, jsro_clone.getId());
		assertEquals("str1", jsro_clone.getObj());
		assertEquals("56c", jsro_clone.getRoute_number());
		assertEquals("bus", jsro_clone.getRoute_type());

	}
	
	@Test
	public void cloneTest2() throws CloneNotSupportedException
	{
		JsonRouteObject jsro = new JsonRouteObject();
		JsonRouteObject jsro_clone = jsro.clone();
		assertNotNull (jsro_clone);
	}

}
