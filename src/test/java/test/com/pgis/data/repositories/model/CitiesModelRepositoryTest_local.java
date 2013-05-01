package test.com.pgis.data.repositories.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.model.impl.CitiesModelRepository;
import com.pgis.bus.net.models.city.CityModel;
import com.pgis.bus.net.models.route.RouteTypeModel;

public class CitiesModelRepositoryTest_local {

	@Test
	public void getAllTest() throws Exception {
		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		CitiesModelRepository rep = new CitiesModelRepository(LangEnum.c_en, dbConnMngr);
		Collection<CityModel> cities = rep.getAll();
		System.out.print(cities.size());
		rep.rollback();
		rep.dispose();
		dbConnMngr.dispose();
	}

	@Test
	public void getByKeyTest() throws Exception {
		// test
		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		CitiesModelRepository rep = new CitiesModelRepository(LangEnum.c_en, dbConnMngr);
		CityModel city = rep.getByKey("kharkiv");
		assertNotNull(city);
		assertTrue(city.getId() > 0);
		assertTrue(city.getName().length() > 0);
		assertTrue(city.getKey().length() > 0);
		assertNotNull(city.getLocation());
		assertTrue(city.getLocation().getLat() > 0);
		assertTrue(city.getLocation().getLon() > 0);
		assertTrue(city.getScale() > 0);
		rep.rollback();
		rep.dispose();
		dbConnMngr.dispose();
	}

	@Test
	public void getRouteTypesByCity() throws Exception {
		// test
		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		CitiesModelRepository rep = new CitiesModelRepository(LangEnum.c_en, dbConnMngr);
		CityModel city = rep.getByKey("kharkiv");
		Collection<RouteTypeModel> routeTypes = rep.getRouteTypesByCity(city.getId());
		assertNotNull(routeTypes);
		assertTrue(routeTypes.size() > 0);
		rep.rollback();
		rep.dispose();
		dbConnMngr.dispose();
	}

}
