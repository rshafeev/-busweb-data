package test.com.pgis.data.repositories.orm;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.postgis.Point;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.repositories.orm.impl.CitiesRepository;
import com.pgis.bus.data.repositories.orm.impl.StationsRepository;

//import com.pgis.bus.data.repositories.UsersRepository;
public class StationsRepositoryTest_local {

	@Test
	public void insertStationTest() throws Exception {
		// get city
		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		CitiesRepository citiesRep = new CitiesRepository(dbConnMngr);
		City city = citiesRep.getByName("c_en", "Kharkov");
		citiesRep.dispose();
		assertNotNull(city);

		// insert station

		Station newStation = new Station();
		newStation.setCityID(city.getId());
		newStation.setLocation(new Point(50, 40));

		StationsRepository stationsRep = new StationsRepository(dbConnMngr);
		stationsRep.insert(newStation);
		System.out.println("New station: " + newStation.getId());
		stationsRep.rollback();
		stationsRep.dispose();
		dbConnMngr.dispose();

	}

}
