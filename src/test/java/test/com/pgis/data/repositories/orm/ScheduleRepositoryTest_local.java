package test.com.pgis.data.repositories.orm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.ScheduleGroup;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.service.IDataBaseService;
import com.pgis.bus.data.service.IDataModelsService;
import com.pgis.bus.data.service.impl.DataBaseService;
import com.pgis.bus.data.service.impl.DataModelsService;
import com.pgis.bus.net.models.route.RoutesListModel;

public class ScheduleRepositoryTest_local {

	@Test
	public void getByRouteWayTest() throws Exception {
		System.out.println("getTest()");

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
			assertEquals(route.getDirectRouteWay().getId(), schedule.getRouteWayId());
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
			dbConnMngr.dispose();
		}
	}
}
