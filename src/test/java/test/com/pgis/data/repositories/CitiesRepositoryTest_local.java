
package test.com.pgis.data.repositories;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.com.pgis.data.TestDBConnectionManager;
import test.com.pgis.data.TestDataSource;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.repositories.ICitiesRepository;
import com.pgis.bus.data.repositories.impl.CitiesRepository;

//import com.pgis.bus.data.repositories.UsersRepository;
public class CitiesRepositoryTest_local {

	@Before
	public void init() {
		TestDataSource source = new TestDataSource();
		TestDBConnectionManager dbConnectionManager = new TestDBConnectionManager(source.getDataSource());
		DBConnectionFactory.init(dbConnectionManager);
		System.out.print("init test\n");
	}

	@After
	public void destroy() {
		DBConnectionFactory.free();
	}

	@Test
	public void getAllCitiesTest() throws Exception {
		ICitiesRepository db = new CitiesRepository();
		Collection<City> cities = db.getAllCities();
		System.out.print(cities.size());
	}
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void updateCityTest() throws Exception{
		Connection c = DBConnectionFactory.getConnection();
		ICitiesRepository db = new CitiesRepository(c,false,true);
		City city = db.getCityByName("Kharkov", "c_en");
		assertNotNull(city);
		
		city.lat = 55;
		city.lon = 55;
		db.updateCity(city);
		
		city.lat = 57;
		city.lon = 57;
		db.updateCity(city);
		
		City checkCity = db.getCityByName("Kharkov", "c_en");
		assertNotNull(checkCity);
		
			
		assertEquals(city.lat, checkCity.lat,0.0001);
		assertEquals(city.lon, checkCity.lon,0.0001);
		
		System.out.print(city.scale);
		
		c.rollback();
		DBConnectionFactory.closeConnection(c);
	    
	}
	
	@Test
	public void insertCityTest() throws Exception{
		// prepare data
		City newCity = new City();
		newCity.lat = 6;
		newCity.lon = 5;
		newCity.scale = 4;
		newCity.name = new HashMap<String,StringValue>();
		
		StringValue ru_value = new StringValue();
		ru_value.lang_id = "c_ru";
		ru_value.value="Москва";
		
		StringValue en_value = new StringValue();
		en_value.lang_id = "c_en";
		en_value.value = "Moscow";
		
		newCity.name.put(ru_value.lang_id, ru_value);
		newCity.name.put(en_value.lang_id, en_value);
		
		// test
		Connection c = DBConnectionFactory.getConnection();
		ICitiesRepository db = new CitiesRepository(c,false,true);
		City responceCity = db.insertCity(newCity);
		assertNotNull(responceCity);
		assertTrue(responceCity.id>0);
		assertTrue(responceCity.name_key>0);
		assertTrue(responceCity.name.size()>0);
		
		c.commit();
		DBConnectionFactory.closeConnection(c);
	    
	}
}
