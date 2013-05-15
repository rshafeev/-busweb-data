package test.com.pgis.data.repositories.orm;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.postgis.Point;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.orm.impl.CitiesRepository;
import com.pgis.bus.data.repositories.orm.impl.StationsRepository;

//import com.pgis.bus.data.repositories.UsersRepository;
public class StationsRepositoryTest_local {

	@Test
	public void insertStationTest() throws Exception {
		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		CitiesRepository citiesRep = new CitiesRepository(dbConnMngr);
		StationsRepository stationsRep = new StationsRepository(dbConnMngr);
		try {
			City city = citiesRep.getByName(LangEnum.c_en, "Kharkov");
			assertNotNull(city);
			Station newStation = new Station();
			newStation.setCityID(city.getId());
			newStation.setLocation(new Point(50, 40));

			stationsRep.insert(newStation);
			System.out.println("New station: " + newStation.getId());
		} finally {
			citiesRep.dispose();
			stationsRep.rollback();
			stationsRep.dispose();
			dbConnMngr.dispose();
		}

	}

}
