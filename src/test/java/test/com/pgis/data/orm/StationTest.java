package test.com.pgis.data.orm;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.postgis.Point;

import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.orm.User;
import com.pgis.bus.data.orm.type.LangEnum;

public class StationTest {

	@Test
	public void cloneTest() throws CloneNotSupportedException, SQLException {
		Station station = new Station ();
		station.setId(56);
		station.setCityID(23);
		station.setLocation(new Point());
		station.setNameKey(89);
		Collection <StringValue> name = new ArrayList <StringValue>();
		
		StringValue stringValue = new StringValue();
		stringValue.setId(48);
		stringValue.setKeyID(20);
		stringValue.setLangID(LangEnum.c_en);
		stringValue.setValue("styd");
		
		name.add(stringValue);
		station.setName(name);
		
		Station station_clone = station.clone();
		
		station.setId(11);
		station.setCityID(12);
		station.setLocation(new Point());
		station.setNameKey(36);
		
		StringValue stringValue2 = new StringValue();
		stringValue2.setId(56);
		stringValue2.setKeyID(43);
		stringValue2.setLangID(LangEnum.c_ru);
		stringValue2.setValue("styggd");
		name.add(stringValue2);
		
		assertEquals(56, (int)station_clone.getId());
		assertEquals(23, (int)station_clone.getCityID());
		assertTrue(station.getLocation()!= station_clone.getLocation());
		assertEquals(89, (int)station_clone.getNameKey());
		assertEquals(1,station_clone.getName().size() )	;	
		
	}
	
	@Test
	public void cloneTest2() throws CloneNotSupportedException
	{
		Station station = new Station();
		Station station_clone = station.clone();
		assertNotNull (station_clone);
	}


}
