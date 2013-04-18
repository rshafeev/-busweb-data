package test.com.pgis.data.repositories.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.repositories.model.ICitiesModelRepository;
import com.pgis.bus.data.repositories.model.impl.CitiesModelRepository;
import com.pgis.bus.net.models.city.CityModel;
import com.pgis.bus.net.models.route.RouteTypeModel;

public class CitiesModelRepositoryTest_local {

	@Test
	public void getAllTest() throws Exception {
		IDBConnectionManager dbConnMngr = TestDBConnectionManager.create();
		ICitiesModelRepository db = new CitiesModelRepository(dbConnMngr, false);
		Collection<CityModel> cities = db.getAll("c_en");
		System.out.print(cities.size());
		dbConnMngr.free();
	}

	@Test
	public void getByKeyTest() throws Exception {
		// test
		IDBConnectionManager dbConnMngr = TestDBConnectionManager.create();
		ICitiesModelRepository db = new CitiesModelRepository(dbConnMngr, false);
		CityModel city = db.getByKey("kharkiv", "c_en");
		assertNotNull(city);
		assertTrue(city.getId() > 0);
		assertTrue(city.getName().length() > 0);
		assertTrue(city.getKey().length() > 0);
		assertNotNull(city.getLocation());
		assertTrue(city.getLocation().getLat() > 0);
		assertTrue(city.getLocation().getLon() > 0);
		assertTrue(city.getScale() > 0);
		dbConnMngr.free();
	}

	@Test
	public void getRouteTypesByCity() throws Exception {
		// test
		IDBConnectionManager dbConnMngr = TestDBConnectionManager.create();
		ICitiesModelRepository db = new CitiesModelRepository(dbConnMngr, false);
		CityModel city = db.getByKey("kharkiv", "c_en");
		Collection<RouteTypeModel> routeTypes = db.getRouteTypesByCity(city.getId());
		assertNotNull(routeTypes);
		assertTrue(routeTypes.size() > 0);
		dbConnMngr.free();
	}

}
