package test.com.pgis.data.repositories.orm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;

import org.junit.Test;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.repositories.orm.ICitiesRepository;
import com.pgis.bus.data.repositories.orm.impl.CitiesRepository;

public class CitiesRepositoryTest_local {

	@Test
	public void getAllTest() throws Exception {
		IDBConnectionManager dbConnMngr = TestDBConnectionManager.create();
		ICitiesRepository db = new CitiesRepository(dbConnMngr, false);
		Collection<City> cities = db.getAll();
		System.out.print(cities.size());
		dbConnMngr.free();
	}

	@Test
	public void updateTest() throws Exception {
		IDBConnectionManager dbConnMngr = TestDBConnectionManager.create();
		ICitiesRepository db = new CitiesRepository(dbConnMngr, false);

		City city = db.getByName("c_en", "Kharkov");
		assertNotNull(city);
		city.setLat(55);
		city.setLon(55);
		db.update(city);

		city.setLat(57);
		city.setLon(57);
		db.update(city);

		City checkCity = db.getByName("c_en", "Kharkov");
		assertNotNull(checkCity);

		assertEquals(city.getLat(), checkCity.getLat(), 0.0001);
		assertEquals(city.getLon(), checkCity.getLon(), 0.0001);

		System.out.print(city.getScale());

		dbConnMngr.free();
	}

	@Test
	public void insertTest() throws Exception {
		// prepare data
		City newCity = new City();
		newCity.setLat(6);
		newCity.setLon(5);
		newCity.setScale(4);
		newCity.setKey("moscow");
		HashMap<String, StringValue> name = new HashMap<String, StringValue>();

		StringValue ru_value = new StringValue();
		ru_value.setLangID("c_ru");
		ru_value.setValue("Москва");

		StringValue en_value = new StringValue();
		en_value.setLangID("c_en");
		en_value.setValue("Moscow");

		name.put(ru_value.getLangID(), ru_value);
		name.put(en_value.getLangID(), en_value);
		newCity.setName(name);
		// test

		IDBConnectionManager dbConnMngr = TestDBConnectionManager.create();
		ICitiesRepository db = new CitiesRepository(dbConnMngr, false);
		db.insert(newCity);
		assertTrue(newCity.getId() > 0);
		assertTrue(newCity.getNameKey() > 0);
		assertTrue(newCity.getName().size() > 0);
		dbConnMngr.free();

	}

	@Test
	public void getByName() throws Exception {
		// test
		IDBConnectionManager dbConnMngr = TestDBConnectionManager.create();
		ICitiesRepository db = new CitiesRepository(dbConnMngr, false);
		City responceCity = db.getByName("c_en", "Kharkov");
		assertNotNull(responceCity);
		assertTrue(responceCity.getId() > 0);
		assertTrue(responceCity.getNameKey() > 0);
		assertTrue(responceCity.getName().size() > 0);

		dbConnMngr.free();
	}

}
