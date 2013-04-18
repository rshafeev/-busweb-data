package test.com.pgis.data.repositories.model;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;
import org.postgis.Point;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.repositories.model.IStationsModelRepository;
import com.pgis.bus.data.repositories.model.impl.StationsModelRepository;
import com.pgis.bus.data.repositories.orm.ICitiesRepository;
import com.pgis.bus.data.repositories.orm.impl.CitiesRepository;
import com.pgis.bus.net.models.station.StationModel;

//import com.pgis.bus.data.repositories.UsersRepository;
public class StationsModelRepositoryTest_local {

	@Test
	public void getStationsListTest() throws Exception {
		// get city
		IDBConnectionManager dbConnMngr = TestDBConnectionManager.create();
		ICitiesRepository db = new CitiesRepository(dbConnMngr, false);
		City city = db.getByName("c_en", "Kyiv");
		assertNotNull(city);

		// get stations
		IStationsModelRepository stationsRepository = new StationsModelRepository(dbConnMngr, false);
		Collection<StationModel> stations = stationsRepository.getStationsList(city.getId(), "c_ru");
		System.out.println(stations.size());
		dbConnMngr.free();

	}

	@Test
	public void getStationsFromBoxTest() throws Exception {
		System.out.println("getAllStationsInBoxTest()");
		// get city
		IDBConnectionManager dbConnMngr = TestDBConnectionManager.create();
		ICitiesRepository db = new CitiesRepository(dbConnMngr, false);
		City city = db.getByName("c_en", "Kyiv");
		assertNotNull(city);

		// get stations
		IStationsModelRepository stationsRepository = new StationsModelRepository(dbConnMngr, false);
		Collection<StationModel> stations = stationsRepository.getStationsFromBox(city.getId(), new Point(49, 35),
				new Point(51, 37), "c_ru");
		System.out.println(stations.size());
		dbConnMngr.free();

	}

}
