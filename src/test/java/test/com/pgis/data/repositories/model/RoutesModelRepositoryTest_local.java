package test.com.pgis.data.repositories.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.model.impl.CitiesModelRepository;
import com.pgis.bus.data.service.IDataBaseService;
import com.pgis.bus.data.service.IDataModelsService;
import com.pgis.bus.data.service.impl.DataBaseService;
import com.pgis.bus.data.service.impl.DataModelsService;
import com.pgis.bus.net.models.route.RoutesListModel;

public class RoutesModelRepositoryTest_local {
	private static final Logger log = LoggerFactory.getLogger(RoutesModelRepositoryTest_local.class);

	@Test
	public void getRoutesListServiceTest() throws Exception {
		log.debug("getRoutesListServiceTest()");
		// init
		LangEnum langID = LangEnum.c_en;
		String routeTypeID = "c_route_bus";

		IDataBaseService dbService = null;
		IConnectionManager dbConnMngr = null;
		IDataModelsService dbModel = null;
		try {
			dbConnMngr = TestDBConnectionManager.create();
			dbService = new DataBaseService(dbConnMngr);
			City city = dbService.Cities().getByName(langID, "Kyiv");
			assertNotNull(city);

			// get stations list
			dbModel = new DataModelsService(langID, dbConnMngr);
			RoutesListModel model = dbModel.Routes().getRoutesList(city.getId(), routeTypeID);
			log.debug("Routes list size: {}", model.getRoutesList().size());

		} finally {
			dbService.dispose();
			dbModel.dispose();
			assertEquals(0, ((TestDBConnectionManager) dbConnMngr).getInitialConnections());
			dbConnMngr.dispose();
		}

	}
}
