package test.com.pgis.data.repositories.model;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;
import org.postgis.Point;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.repositories.model.impl.StationsModelRepository;
import com.pgis.bus.data.repositories.orm.impl.CitiesRepository;
import com.pgis.bus.data.service.IDataBaseService;
import com.pgis.bus.data.service.IDataModelsService;
import com.pgis.bus.data.service.impl.DataBaseService;
import com.pgis.bus.data.service.impl.DataModelsService;
import com.pgis.bus.net.models.station.StationModel;

//import com.pgis.bus.data.repositories.UsersRepository;
public class StationsModelRepositoryTest_local {

	@Test
	public void getStationsListTest() throws Exception {
		System.out.println("getStationsListTest()");
		// get city
		String langID = "c_en";
		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		CitiesRepository cityRep = new CitiesRepository(dbConnMngr);
		City city = cityRep.getByName(langID, "Kyiv");
		assertNotNull(city);

		// get stations
		StationsModelRepository stationsRep = new StationsModelRepository("c_en", dbConnMngr);
		Collection<StationModel> stations = stationsRep.getStationsList(city.getId());
		System.out.println(stations.size());
		stationsRep.rollback();
		stationsRep.dispose();
		cityRep.rollback();
		cityRep.dispose();

		dbConnMngr.dispose();

	}

	@Test
	public void getStationsListServiceTest() throws Exception {
		System.out.println("getStationsListServiceTest()");
		// init
		String langID = "c_en";
		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		IDataBaseService dbService = new DataBaseService(dbConnMngr);
		City city = dbService.Cities().getByName(langID, "Kyiv");
		dbService.dispose();
		assertNotNull(city);

		// get stations list
		IDataModelsService dbModel = new DataModelsService(langID, dbConnMngr);
		Collection<StationModel> stations = dbModel.Stations().getStationsList(city.getId());
		System.out.println(stations.size());
		dbModel.dispose();

		// clear resources
		dbConnMngr.dispose();

	}

	@Test
	public void getStationsFromBoxTest() throws Exception {
		System.out.println("getStationsFromBoxTest()");
		// get city
		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		CitiesRepository cityRep = new CitiesRepository(dbConnMngr);
		City city = cityRep.getByName("c_en", "Kyiv");
		assertNotNull(city);

		// get stations
		StationsModelRepository stationsRep = new StationsModelRepository(dbConnMngr);
		Collection<StationModel> stations = stationsRep.getStationsFromBox(city.getId(), new Point(49, 35), new Point(
				51, 37));
		System.out.println(stations.size());
		stationsRep.rollback();
		stationsRep.dispose();
		cityRep.rollback();
		cityRep.dispose();

		dbConnMngr.dispose();

	}

}
