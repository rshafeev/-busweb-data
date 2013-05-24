package test.com.pgis.data.repositories.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.pgis.data.TestDBConnectionManager;
import test.com.pgis.data.repositories.orm.CitiesRepositoryTest_local;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.model.impl.CitiesModelRepository;
import com.pgis.bus.net.models.city.CityModel;
import com.pgis.bus.net.models.route.RouteTypeModel;

public class CitiesModelRepositoryTest_local {
	private static final Logger log = LoggerFactory.getLogger(CitiesModelRepositoryTest_local.class);

	@Test
	public void getAllTest() throws Exception {
		log.debug("getAllTest()");
		CitiesModelRepository rep = null;
		IConnectionManager dbConnMngr = null;
		try {
			dbConnMngr = TestDBConnectionManager.create();
			rep = new CitiesModelRepository(LangEnum.c_en, dbConnMngr);
			Collection<CityModel> cities = rep.getAll();
			log.debug("Cities count : {}", cities.size());
		} finally {
			rep.rollback();
			rep.dispose();
			assertEquals(0, ((TestDBConnectionManager) dbConnMngr).getInitialConnections());
			dbConnMngr.dispose();
		}

	}

	@Test
	public void getByKeyTest() throws Exception {
		log.debug("getByKeyTest()");
		// test
		CitiesModelRepository rep = null;
		IConnectionManager dbConnMngr = null;
		try {
			dbConnMngr = TestDBConnectionManager.create();
			rep = new CitiesModelRepository(LangEnum.c_en, dbConnMngr);
			CityModel city = rep.getByKey("kharkiv");
			assertNotNull(city);
			assertTrue(city.getId() > 0);
			assertTrue(city.getName().length() > 0);
			assertTrue(city.getKey().length() > 0);
			assertNotNull(city.getLocation());
			assertTrue(city.getLocation().getLat() > 0);
			assertTrue(city.getLocation().getLon() > 0);
			assertTrue(city.getScale() > 0);
		} finally {
			rep.rollback();
			rep.dispose();
			assertEquals(0, ((TestDBConnectionManager) dbConnMngr).getInitialConnections());
			dbConnMngr.dispose();
		}
	}

	@Test
	public void getRouteTypesByCity() throws Exception {
		log.debug("getRouteTypesByCity()");
		CitiesModelRepository rep = null;
		IConnectionManager dbConnMngr = null;
		try {
			// test
			dbConnMngr = TestDBConnectionManager.create();
			rep = new CitiesModelRepository(LangEnum.c_en, dbConnMngr);
			CityModel city = rep.getByKey("kharkiv");
			Collection<RouteTypeModel> routeTypes = rep.getRouteTypesByCity(city.getId());
			assertNotNull(routeTypes);
			assertTrue(routeTypes.size() > 0);
		} finally {
			rep.rollback();
			rep.dispose();
			assertEquals(0, ((TestDBConnectionManager) dbConnMngr).getInitialConnections());
			dbConnMngr.dispose();
		}
	}

}
