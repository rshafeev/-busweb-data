package test.com.pgis.data.repositories.orm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.RouteWay;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.ScheduleGroup;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.service.IDataBaseService;
import com.pgis.bus.data.service.IDataModelsService;
import com.pgis.bus.data.service.impl.DataBaseService;
import com.pgis.bus.data.service.impl.DataModelsService;
import com.pgis.bus.net.models.route.RoutesListModel;

public class ScheduleRepositoryTest_local {
	private static final Logger log = LoggerFactory.getLogger(ScheduleRepositoryTest_local.class);

	@Test
	public void getByRouteWayTest() throws Exception {
		log.debug("getByRouteWayTest()");

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
			Route route = db.Routes().get(routeID);
			assertNotNull(route);
			System.out.println("route: " + route);
			System.out.println("Direct routeWay: " + route.getDirectRouteWay());

			// Получим расписание через ORM объект
			Schedule schedule = route.getDirectRouteWay().getSchedule();
			assertEquals(route.getDirectRouteWay().getId().intValue(), schedule.getRouteWayId());
			assertFalse(schedule.getScheduleGroups().isEmpty());
			for (ScheduleGroup g : schedule.getScheduleGroups()) {
				assertFalse(g.getDays().isEmpty());
				assertFalse(g.getTimetables().isEmpty());
				assertTrue(g.getId() > 0);
			}
			// Получим расписание напрямую из репозитория для того же маршрута
			Schedule rSchedule = db.Schedule().getByRouteWay(route.getDirectRouteWay().getId());
			assertEquals(schedule.getId(), rSchedule.getId());

		} finally {
			db.rollback();
			db.dispose();
			assertTrue(((TestDBConnectionManager) dbConnMngr).getInitialConnections() <= 0);
			dbConnMngr.dispose();
		}
	}

	@Test
	public void updateTest() throws Exception {
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
			Route route = db.Routes().get(routeID);
			assertNotNull(route);
			System.out.println("route: " + route);
			System.out.println("Direct routeWay: " + route.getDirectRouteWay());

			// Получим расписание через ORM объект
			Schedule schedule = route.getDirectRouteWay().getSchedule();

			Route requestRoute = new Route();
			RouteWay way = new RouteWay();
			way.setSchedule(schedule);

			db.Routes().update(requestRoute);
			Route respRoute = db.Routes().get(routeID);
			assertNotNull(respRoute);
			assertNotNull(respRoute.getDirectRouteWay().getSchedule());
			assertNotNull(respRoute.getReverseRouteWay().getSchedule());

		} finally {
			db.rollback();
			db.dispose();
			assertTrue(((TestDBConnectionManager) dbConnMngr).getInitialConnections() <= 0);
			dbConnMngr.dispose();
		}
	}
}
