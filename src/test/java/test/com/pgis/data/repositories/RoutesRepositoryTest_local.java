package test.com.pgis.data.repositories;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Iterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.com.pgis.data.TestDBConnectionManager;
import test.com.pgis.data.TestDataSource;
import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.helpers.LoadDirectRouteOptions;
import com.pgis.bus.data.helpers.LoadRouteOptions;
import com.pgis.bus.data.helpers.LoadRouteRelationOptions;
import com.pgis.bus.data.models.RouteGeoData;
import com.pgis.bus.data.models.RoutePart;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.repositories.ICitiesRepository;
import com.pgis.bus.data.repositories.IRoutesRepository;
import com.pgis.bus.data.repositories.impl.CitiesRepository;
import com.pgis.bus.data.repositories.impl.RoutesRepository;

public class RoutesRepositoryTest_local {

	@Before
	public void init() {
		TestDataSource source = new TestDataSource();
		TestDBConnectionManager dbConnectionManager = new TestDBConnectionManager(
				source.getDataSource());
		DBConnectionFactory.init(dbConnectionManager);
		System.out.print("init test\n");
	}

	@After
	public void destroy() {
		DBConnectionFactory.free();
	}

	@Test
	public void getGeoDataByRoutePart_Test() throws Exception {
		IRoutesRepository repository = new RoutesRepository();
		RoutePart routePart = new RoutePart();
		routePart.setDirectRouteID(3);
		routePart.setIndexStart(0);
		routePart.setIndexFinish(3);
		String lang_id = "c_ru";

		Collection<RouteGeoData> relations = repository.getGeoDataByRoutePart(
				routePart, lang_id);
		Iterator<RouteGeoData> i = relations.iterator();
		System.out.println("getGeoDataByRoutePart_Test()");
		while (i.hasNext()) {
			RouteGeoData d = i.next();
			System.out.println(d.toString());
		}
	}

	@Test
	public void getRoutes_Test() throws Exception {

		// set input data
		ICitiesRepository db = new CitiesRepository();
		City city = db.getCityByName("c_en", "Kharkov");
		assertNotNull(city);

		// set options
		LoadRouteOptions opts = new LoadRouteOptions();
		opts.setLoadRouteNamesData(true);
		opts.setDirectRouteOptions(null);

		// get routes
		IRoutesRepository repository = new RoutesRepository();
		Collection<Route> routes = repository.getRoutes("c_route_bus", city.id,
				opts);
		for (Route route : routes) {
			System.out.println(route.toString());
		}

	}
	
	@Test
	public void getRoutesWithDirects_Test() throws Exception {

		// set input data
		ICitiesRepository db = new CitiesRepository();
		City city = db.getCityByName("c_en", "Kharkov");
		assertNotNull(city);

		// set options
		LoadDirectRouteOptions loadDirectRouteOptions = new LoadDirectRouteOptions();
		loadDirectRouteOptions.setLoadScheduleData(false);
		loadDirectRouteOptions.setLoadRouteRelationOptions(null);
		
		LoadRouteOptions opts = new LoadRouteOptions();
		opts.setLoadRouteNamesData(true);
		opts.setDirectRouteOptions(loadDirectRouteOptions);

		// get routes
		IRoutesRepository repository = new RoutesRepository();
		Collection<Route> routes = repository.getRoutes("c_route_metro", city.id,
				opts);
		for (Route route : routes) {
			System.out.println(route.toString());
		}

	}
	
	@Test
	public void getRoutesWithSchedule_Test() throws Exception {
		System.out.println("getRoutesWithSchedule_Test()...");
		// set input data
		ICitiesRepository db = new CitiesRepository();
		City city = db.getCityByName("c_en", "Kharkov");
		assertNotNull(city);

		// set options
		LoadDirectRouteOptions loadDirectRouteOptions = new LoadDirectRouteOptions();
		loadDirectRouteOptions.setLoadScheduleData(true);
		loadDirectRouteOptions.setLoadRouteRelationOptions(null);
		
		LoadRouteOptions opts = new LoadRouteOptions();
		opts.setLoadRouteNamesData(true);
		opts.setDirectRouteOptions(loadDirectRouteOptions);

		// get routes
		IRoutesRepository repository = new RoutesRepository();
		Collection<Route> routes = repository.getRoutes("c_route_metro", city.id,
				opts);
		for (Route route : routes) {
			System.out.println(route.toString());
		}

	}
	
	@Test
	public void getRoutesWithAllData_Test() throws Exception {
		System.out.println("getRoutesWithSchedule_Test()...");
		// set input data
		ICitiesRepository db = new CitiesRepository();
		City city = db.getCityByName("c_en", "Kharkov");
		assertNotNull(city);

		// set options
		LoadRouteRelationOptions loadRouteRelationOptions = new LoadRouteRelationOptions();
		loadRouteRelationOptions.setLoadStationsData(false);
		
		LoadDirectRouteOptions loadDirectRouteOptions = new LoadDirectRouteOptions();
		loadDirectRouteOptions.setLoadScheduleData(true);
		loadDirectRouteOptions.setLoadRouteRelationOptions(loadRouteRelationOptions);
		
		LoadRouteOptions opts = new LoadRouteOptions();
		opts.setLoadRouteNamesData(true);
		opts.setDirectRouteOptions(loadDirectRouteOptions);

		// get routes
		IRoutesRepository repository = new RoutesRepository();
		Collection<Route> routes = repository.getRoutes("c_route_metro", city.id,
				opts);
		for (Route route : routes) {
			System.out.println(route.toString());
		}

	}
}
