package test.com.pgis.data.repositories;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.com.pgis.data.FileManager;
import test.com.pgis.data.TestDBConnectionManager;
import test.com.pgis.data.TestDataSource;

import com.google.gson.Gson;
import com.pgis.bus.data.IAdminDataBaseService;
import com.pgis.bus.data.IDataBaseService;
import com.pgis.bus.data.helpers.LoadDirectRouteOptions;
import com.pgis.bus.data.helpers.LoadRouteOptions;
import com.pgis.bus.data.helpers.LoadRouteRelationOptions;
import com.pgis.bus.data.helpers.UpdateRouteOptions;
import com.pgis.bus.data.impl.AdminDataBaseService;
import com.pgis.bus.data.impl.DataBaseService;
import com.pgis.bus.data.models.RouteGeoData;
import com.pgis.bus.data.models.RoutePart;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.repositories.ICitiesRepository;
import com.pgis.bus.data.repositories.IRoutesRepository;
import com.pgis.bus.data.repositories.impl.CitiesRepository;
import com.pgis.bus.data.repositories.impl.Repository;
import com.pgis.bus.data.repositories.impl.RoutesRepository;

public class RoutesRepositoryTest_local {


	TestDBConnectionManager dbConnectionManager = null;
	@Before
	public void init() {
		TestDataSource source = new TestDataSource();
		dbConnectionManager = new TestDBConnectionManager(
				source.getDataSource());
		System.out.print("init test\n");
	}

	@After
	public void destroy() {
		dbConnectionManager.free();
	}
	
	
	@Test
	public void getGeoDataByRoutePart_Test() throws Exception {
		System.out.println("getGeoDataByRoutePart_Test()...");

		IRoutesRepository repository = new RoutesRepository(dbConnectionManager);
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
		System.out.println("getRoutes_Test()...");
		// set input data
		ICitiesRepository db = new CitiesRepository(dbConnectionManager);
		City city = db.getCityByName("c_en", "Kharkov");
		assertNotNull(city);

		// set options
		LoadRouteOptions opts = new LoadRouteOptions();
		opts.setLoadRouteNamesData(true);
		opts.setDirectRouteOptions(null);

		// get routes
		IRoutesRepository repository = new RoutesRepository(dbConnectionManager);
		Collection<Route> routes = repository.getRoutes("c_route_bus", city.id,
				opts);
		for (Route route : routes) {
			System.out.println(route.toString());
		}

	}

	@Test
	public void getRoutes_Test2() throws Exception {
		System.out.println("getRoutes2_Test()...");
		// set input data
		ICitiesRepository cityReps = new CitiesRepository(dbConnectionManager);
		City city = cityReps.getCityByName("c_en", "Kharkov");
		assertNotNull(city);

		String route_type = "c_route_metro";
		String lang_id = "c_ru";

		// get routes
		IDataBaseService db = new DataBaseService(dbConnectionManager);
		Collection<Route> routes = db.getRoutes(route_type, city.id,
				lang_id);
		for (Route route : routes) {
			
			System.out.println(route.toString());
		}

	}

	@Test
	public void getRoutesWithDirects_Test() throws Exception {
		System.out.println("getRoutesWithDirects_Test()...");
		// set input data
		ICitiesRepository db = new CitiesRepository(dbConnectionManager);
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
		IRoutesRepository repository = new RoutesRepository(dbConnectionManager);
		Collection<Route> routes = repository.getRoutes("c_route_metro",
				city.id, opts);
		for (Route route : routes) {
			System.out.println(route.toString());
		}

	}

	@Test
	public void getRoutesWithSchedule_Test() throws Exception {
		System.out.println("getRoutesWithSchedule_Test()...");
		// set input data
		ICitiesRepository db = new CitiesRepository(dbConnectionManager);
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
		IRoutesRepository repository = new RoutesRepository(dbConnectionManager);
		Collection<Route> routes = repository.getRoutes("c_route_metro",
				city.id, opts);
		for (Route route : routes) {
			System.out.println(route.toString());
		}

	}

	@Test
	public void getRoutesWithAllData_Test() throws Exception {
		System.out.println("getRoutesWithSchedule_Test()...");
		// set input data
		ICitiesRepository db = new CitiesRepository(dbConnectionManager);
		City city = db.getCityByName("c_en", "Kharkov");
		assertNotNull(city);

		// set options
		LoadRouteRelationOptions loadRouteRelationOptions = new LoadRouteRelationOptions();
		loadRouteRelationOptions.setLoadStationsData(true);

		LoadDirectRouteOptions loadDirectRouteOptions = new LoadDirectRouteOptions();
		loadDirectRouteOptions.setLoadScheduleData(true);
		loadDirectRouteOptions
				.setLoadRouteRelationOptions(loadRouteRelationOptions);

		LoadRouteOptions opts = new LoadRouteOptions();
		opts.setLoadRouteNamesData(true);
		opts.setDirectRouteOptions(loadDirectRouteOptions);

		// get routes
		IRoutesRepository repository = new RoutesRepository(dbConnectionManager);
		Collection<Route> routes = repository.getRoutes("c_route_metro",
				city.id, opts);
		for (Route route : routes) {
			System.out.println(route.toString());
		}

	}

	@Test
	public void insertRoute_Test() throws Exception {
		System.out.println("insertRoute_Test()");
		String data = FileManager.getFileData(FileManager.getTestResourcePath()
				+ "route2.dat");
		System.out.println(data);

		Route newRoute = (new Gson()).fromJson(data, Route.class);

		Connection c = dbConnectionManager.getConnection();
		IRoutesRepository repository = new RoutesRepository(c, false, false);
		// repository.insertRoute(newRoute);

		c.rollback();
		dbConnectionManager.closeConnection(c);
	}

	@Test
	public void insertRouteWithoutReverseWay_Test() throws Exception {
		System.out.println("insertRouteWithoutReverseWay_Test()");
		String data = FileManager.getFileData(FileManager.getTestResourcePath()
				+ "route3.dat");
		System.out.println(data);

		Route newRoute = (new Gson()).fromJson(data, Route.class);

		Connection c = dbConnectionManager.getConnection();
		IRoutesRepository repository = new RoutesRepository(c, false, false);
		// repository.insertRoute(newRoute);

		c.rollback();
		dbConnectionManager.closeConnection(c);
	}

	@Test
	public void removeRoute_Test() throws Exception {
		System.out.println("removeRoute_Test()...");
		// set input data
		Connection c = dbConnectionManager.getConnection();
		ICitiesRepository db = new CitiesRepository(c, false, false);
		IRoutesRepository repository = new RoutesRepository(c, false, false);

		City city = db.getCityByName("c_en", "Kharkov");
		assertNotNull(city);

		// set options
		LoadRouteRelationOptions loadRouteRelationOptions = new LoadRouteRelationOptions();
		loadRouteRelationOptions.setLoadStationsData(true);

		LoadDirectRouteOptions loadDirectRouteOptions = new LoadDirectRouteOptions();
		loadDirectRouteOptions.setLoadScheduleData(true);
		loadDirectRouteOptions
				.setLoadRouteRelationOptions(loadRouteRelationOptions);

		LoadRouteOptions opts = new LoadRouteOptions();
		opts.setLoadRouteNamesData(true);
		opts.setDirectRouteOptions(loadDirectRouteOptions);

		// get routes
		Collection<Route> routes = repository.getRoutes("c_route_bus", city.id,
				opts);
		Route removeRoute = routes.iterator().next();
		assertTrue(routes.size() > 0);
		// insert new route
		repository.removeRoute(removeRoute.getId());
		System.out.println("removeRoute: " + removeRoute.toString());

		// get routes
		Collection<Route> newRoutes = repository.getRoutes("c_route_bus",
				city.id, opts);
		assertTrue(routes.size() - newRoutes.size() == 1);

		c.rollback();
		dbConnectionManager.closeConnection(c);

	}

	@Test
	public void updateRoute_Test() throws Exception {
		System.out.println("updateRoute_Test()...");
		// set input data
		Connection c = dbConnectionManager.getConnection();
		ICitiesRepository db = new CitiesRepository(c, false, false);
		IRoutesRepository repository = new RoutesRepository(c, false, false);

		City city = db.getCityByName("c_en", "Kharkov");
		assertNotNull(city);

		// set options
		LoadRouteRelationOptions loadRouteRelationOptions = new LoadRouteRelationOptions();
		loadRouteRelationOptions.setLoadStationsData(true);

		LoadDirectRouteOptions loadDirectRouteOptions = new LoadDirectRouteOptions();
		loadDirectRouteOptions.setLoadScheduleData(true);
		loadDirectRouteOptions
				.setLoadRouteRelationOptions(loadRouteRelationOptions);

		LoadRouteOptions opts = new LoadRouteOptions();
		opts.setLoadRouteNamesData(true);
		opts.setDirectRouteOptions(loadDirectRouteOptions);

		// get routes
		Collection<Route> routes = repository.getRoutes("c_route_bus", city.id,
				opts);
		assertTrue(routes.size() > 0);
		Route updateRoute = routes.iterator().next();
		UpdateRouteOptions updateRouteOpts = new UpdateRouteOptions();
		updateRouteOpts.setUpdateMainInfo(true);
		updateRouteOpts.setUpdateRouteRelations(true);
		updateRouteOpts.setUpdateSchedule(true);
		repository.updateRoute(updateRoute, updateRouteOpts);
		System.out.println("updateRoute: " + updateRoute.toString());

		c.rollback();
		dbConnectionManager.closeConnection(c);

	}
}
