package test.com.pgis.data.repositories.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.service.IDataBaseService;
import com.pgis.bus.data.service.IDataModelsService;
import com.pgis.bus.data.service.impl.DataBaseService;
import com.pgis.bus.data.service.impl.DataModelsService;
import com.pgis.bus.net.models.route.RoutesListModel;

//import com.pgis.bus.data.repositories.UsersRepository;
public class RoutesModelRepositoryTest_local {

	@Test
	public void getRoutesListServiceTest() throws Exception {
		System.out.println("getRoutesListServiceTest()");
		// init
		LangEnum langID = LangEnum.c_en;
		String routeTypeID = "c_route_bus";
		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		IDataBaseService dbService = new DataBaseService(dbConnMngr);
		City city = dbService.Cities().getByName(langID, "Kyiv");
		dbService.dispose();
		assertNotNull(city);

		// get stations list
		IDataModelsService dbModel = new DataModelsService(langID, dbConnMngr);
		RoutesListModel model = dbModel.Routes().getRoutesList(city.getId(), routeTypeID);
		System.out.println(model.getRoutesList().size());
		dbModel.dispose();

		// clear resources
		dbConnMngr.dispose();

	}

}
