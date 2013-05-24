package test.com.pgis.data.repositories.orm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;
import org.postgresql.ds.PGPoolingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.RouteWay;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.service.IDataBaseService;
import com.pgis.bus.data.service.IDataModelsService;
import com.pgis.bus.data.service.impl.DataBaseService;
import com.pgis.bus.data.service.impl.DataModelsService;
import com.pgis.bus.net.models.route.RoutesListModel;

public class RoutesRepositpryTest_local {
	private static final Logger log = LoggerFactory.getLogger(RoutesRepositpryTest_local.class);

	@Test
	public void getTest() throws SQLException {
		log.debug("getTest()");

		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		IDataBaseService db = null;
		IDataModelsService dbModels = null;
		try {
			db = new DataBaseService(dbConnMngr);
			dbModels = new DataModelsService(LangEnum.c_en, dbConnMngr);
			// Получим город.
			City city = db.Cities().getByKey("kharkiv");
			assertNotNull(city);

			// Выберем первый маршрут из списка
			RoutesListModel routesList = dbModels.Routes().getRoutesList(city.getId(), "c_route_bus");
			assertTrue(routesList.getRoutesList().size() > 0);
			int routeID = routesList.getRoutesList().iterator().next().getId();

			// Тестирование
			Route route = db.Routes().get(routeID);
			assertNotNull(route);
			RouteWay directWay = route.getDirectRouteWay();
			assertNotNull(directWay);
			assertNotNull(directWay.getRouteRelations());
			assertTrue(directWay.getRouteRelations().size() > 1);

			RouteWay reverseWay = route.getReverseRouteWay();
			assertNotNull(reverseWay);
			assertTrue(reverseWay.getRouteRelations().size() > 1);

			System.out.println("route: " + route);

		} finally {
			db.rollback();
			db.dispose();
			dbConnMngr.dispose();
		}
	}

	@Test
	public void updateTest() throws SQLException {
		log.debug("updateTest()");

		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		IDataBaseService db = null;
		IDataModelsService dbModels = null;
		try {
			db = new DataBaseService(dbConnMngr);
			dbModels = new DataModelsService(LangEnum.c_en, dbConnMngr);
			// Получим город.
			City city = db.Cities().getByKey("kharkiv");
			assertNotNull(city);

			// Выберем первый маршрут из списка
			RoutesListModel routesList = dbModels.Routes().getRoutesList(city.getId(), "c_route_bus");
			assertTrue(routesList.getRoutesList().size() > 0);
			int routeID = routesList.getRoutesList().iterator().next().getId();

			// Тестирование
			Route route = db.Routes().get(routeID);

			// Догрузим данные
			route.getDirectRouteWay().getSchedule();
			route.getReverseRouteWay().getSchedule();

			// Запомним значения проверяемых полей
			double expCost = 3.0;
			String expRouteType = "c_route_metro";

			// Произведем модификацию стоимости и типа маршрута
			route.setCost(expCost);
			route.setRouteTypeID(expRouteType);

			db.Routes().update(route);

			Route actualRoute = db.Routes().get(routeID);
			assertEquals(expCost, actualRoute.getCost(), 0.00001);
			assertEquals(expRouteType, actualRoute.getRouteTypeID());

		} finally {
			dbModels.dispose();
			db.rollback();
			db.dispose();
			assertTrue(((TestDBConnectionManager) dbConnMngr).getInitialConnections() <= 0);
			dbConnMngr.dispose();
		}
	}

	@Test
	public void insertTest() throws SQLException {
		log.debug("insertTest()");

		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		IDataBaseService db = null;
		IDataModelsService dbModels = null;
		try {
			// Route route = new Route();

			db = new DataBaseService(dbConnMngr);
			dbModels = new DataModelsService(LangEnum.c_en, dbConnMngr);
			// Получим город.
			City city = db.Cities().getByKey("kharkiv");
			assertNotNull(city);

			// Выберем первый маршрут из списка
			RoutesListModel routesList = dbModels.Routes().getRoutesList(city.getId(), "c_route_bus");
			assertTrue(routesList.getRoutesList().size() > 0);
			int routeID = routesList.getRoutesList().iterator().next().getId();

			// Тестирование
			Route route = db.Routes().get(routeID);

			// Догрузим данные
			route.getDirectRouteWay().getSchedule();
			route.getReverseRouteWay().getSchedule();

			// Запомним значения проверяемых полей
			double expCost = 3.0;
			String expRouteType = "c_route_metro";

			// Произведем модификацию стоимости и типа маршрута
			route.setCost(expCost);
			route.setRouteTypeID(expRouteType);

			db.Routes().update(route);

			Route actualRoute = db.Routes().get(routeID);
			assertEquals(expCost, actualRoute.getCost(), 0.00001);
			assertEquals(expRouteType, actualRoute.getRouteTypeID());

		} finally {
			db.rollback();
			db.dispose();
			assertTrue(((TestDBConnectionManager) dbConnMngr).getInitialConnections() <= 0);
			dbConnMngr.dispose();
		}
	}

}
