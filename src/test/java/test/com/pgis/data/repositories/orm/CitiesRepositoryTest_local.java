package test.com.pgis.data.repositories.orm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.orm.impl.CitiesRepository;

public class CitiesRepositoryTest_local {
	private static final Logger log = LoggerFactory.getLogger(CitiesRepositoryTest_local.class);

	@Test
	public void getAllTest() throws Exception {
		log.debug("getAllTest()");
		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		CitiesRepository rep = new CitiesRepository(dbConnMngr);
		try {
			Collection<City> cities = rep.getAll();
			System.out.print(cities.size());
		} finally {
			rep.rollback();
			rep.dispose();
			assertEquals(0, ((TestDBConnectionManager) dbConnMngr).getInitialConnections());
			dbConnMngr.dispose();
		}
	}

	@Test
	public void updateTest() throws Exception {
		log.debug("updateTest()");
		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		CitiesRepository rep = new CitiesRepository(dbConnMngr);
		try {
			City city = rep.getByName(LangEnum.c_en, "Kharkov");
			assertNotNull(city);
			city.setLat(55);
			city.setLon(55);
			rep.update(city);

			city.setLat(57);
			city.setLon(57);
			rep.update(city);

			City checkCity = rep.getByName(LangEnum.c_en, "Kharkov");
			assertNotNull(checkCity);

			assertEquals(city.getLat(), checkCity.getLat(), 0.0001);
			assertEquals(city.getLon(), checkCity.getLon(), 0.0001);

			System.out.print(city.getScale());

		} finally {
			rep.rollback();
			rep.dispose();
			assertEquals(0, ((TestDBConnectionManager) dbConnMngr).getInitialConnections());
			dbConnMngr.dispose();
		}
	}

	@Test
	public void insertTest() throws Exception {
		log.debug("insertTest()");
		// prepare data
		City newCity = new City();
		newCity.setLat(6);
		newCity.setLon(5);
		newCity.setScale(4);
		newCity.setKey("moscow");
		HashMap<LangEnum, StringValue> name = new HashMap<LangEnum, StringValue>();

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

		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		CitiesRepository rep = new CitiesRepository(dbConnMngr);
		try {
			rep.insert(newCity);
			assertTrue(newCity.getId() > 0);
			assertTrue(newCity.getNameKey() > 0);
			assertTrue(newCity.getName().size() > 0);
		} finally {
			rep.rollback();
			rep.dispose();
			assertEquals(0, ((TestDBConnectionManager) dbConnMngr).getInitialConnections());
			dbConnMngr.dispose();
		}
	}

	@Test
	public void getByName() throws Exception {
		log.debug("getByName()");
		// test
		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		CitiesRepository rep = new CitiesRepository(dbConnMngr);
		try {
			City responceCity = rep.getByName(LangEnum.c_en, "Kharkov");
			assertNotNull(responceCity);
			assertTrue(responceCity.getId() > 0);
			assertTrue(responceCity.getNameKey() > 0);
			assertTrue(responceCity.getName().size() > 0);
		} finally {
			rep.rollback();
			rep.dispose();
			assertEquals(0, ((TestDBConnectionManager) dbConnMngr).getInitialConnections());
			dbConnMngr.dispose();
		}
	}

}
