package test.com.pgis.data.orm;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;

import org.junit.Test;

import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.orm.type.LangEnum;

public class CityTest {

	@Test
	public void cloneTest() throws CloneNotSupportedException, SQLException {
		City city = new City();
		
		city.setId(5);
		city.setKey("key");
		city.setLat(125.00);
		city.setLon(235.00);
		city.setScale(5);
		city.setShow(false);
		city.setNameKey(23);
		HashMap<LangEnum, StringValue> name = new HashMap<LangEnum, StringValue>();
		name.put(LangEnum.c_en, new StringValue(LangEnum.c_en,"Kharkov"));
		city.setName(name);
		
		City city_clone = city.clone();
		
		city.setId(7);
		city.setKey("key2");
		city.setLat(125);
		city.setLon(238);
		city.setScale(6);
		city.setShow(false);
		city.setNameKey(237);
		HashMap<LangEnum, StringValue> name2 = new HashMap<LangEnum, StringValue>();
		name2.put(LangEnum.c_en, new StringValue(LangEnum.c_en,"Kiev"));
		city.setName(name2);
		
		assertEquals(5,(int)(city_clone.getId()));
		assertEquals("key",city_clone.getKey());
		assertEquals(125.00,city_clone.getLat(), 0.0000001);
		assertEquals(235.00,city_clone.getLon(), 0.0000001);
		assertEquals(5,city_clone.getScale());
		assertEquals(23,city_clone.getNameKey());
		assertEquals(1,city_clone.getName().size());
		assertEquals(false, city_clone.isShow());
		
		
	}
	
	@Test
	public void cloneTest2() throws CloneNotSupportedException
	{
		City city = new City();
		City city_clone = city.clone();
		assertNotNull (city_clone);
	}

}
