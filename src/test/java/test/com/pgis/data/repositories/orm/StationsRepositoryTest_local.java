package test.com.pgis.data.repositories.orm;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.postgis.Point;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.repositories.orm.ICitiesRepository;
import com.pgis.bus.data.repositories.orm.IStationsRepository;
import com.pgis.bus.data.repositories.orm.impl.CitiesRepository;
import com.pgis.bus.data.repositories.orm.impl.StationsRepository;

//import com.pgis.bus.data.repositories.UsersRepository;
public class StationsRepositoryTest_local {

	@Test
	public void insertStationTest() throws Exception {
		// get city
		IDBConnectionManager dbConnMngr = TestDBConnectionManager.create();
		ICitiesRepository db = new CitiesRepository(dbConnMngr, false);
		City city = db.getByName("c_en", "Kharkov");
		assertNotNull(city);

		// insert station

		Station newStation = new Station();
		newStation.setCityID(city.getId());
		newStation.setLocation(new Point(50, 40));

		IStationsRepository stationsRepository = new StationsRepository(dbConnMngr, false);
		stationsRepository.insert(newStation);
		System.out.println("New station: " + newStation.getId());
		dbConnMngr.free();

	}

}
