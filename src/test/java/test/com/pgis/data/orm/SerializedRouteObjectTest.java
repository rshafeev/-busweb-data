package test.com.pgis.data.orm;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pgis.bus.data.orm.SerializedRouteObject;
import com.pgis.bus.data.orm.Language;

public class SerializedRouteObjectTest {

	@Test
	public void cloneTest() throws CloneNotSupportedException {
		SerializedRouteObject jsro = new SerializedRouteObject();
		jsro.setId(56);
		jsro.setObj("str1");
		jsro.setRoute_number("56c");
		jsro.setRoute_type("bus");

		SerializedRouteObject jsro_clone = jsro.clone();

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
	public void cloneTest2() throws CloneNotSupportedException {
		SerializedRouteObject jsro = new SerializedRouteObject();
		SerializedRouteObject jsro_clone = jsro.clone();
		assertNotNull(jsro_clone);
	}

}
