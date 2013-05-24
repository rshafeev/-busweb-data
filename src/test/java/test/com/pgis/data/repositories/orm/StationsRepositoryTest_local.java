package test.com.pgis.data.repositories.orm;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.postgis.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.orm.impl.CitiesRepository;
import com.pgis.bus.data.repositories.orm.impl.StationsRepository;

//import com.pgis.bus.data.repositories.UsersRepository;
public class StationsRepositoryTest_local {
	private static final Logger log = LoggerFactory.getLogger(StationsRepositoryTest_local.class);

	@Test
	public void insertStationTest() throws Exception {
		log.debug("insertStationTest()");

		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		CitiesRepository citiesRep = new CitiesRepository(dbConnMngr);
		StationsRepository stationsRep = new StationsRepository(dbConnMngr);
		try {
			City city = citiesRep.getByName(LangEnum.c_en, "Kharkov");
			assertNotNull(city);
			Station newStation = new Station();
			newStation.setCityID(city.getId());
			newStation.setLocation(new Point(50, 40));
			Collection<StringValue> name = new ArrayList<StringValue>();

			newStation.setName(name);
			stationsRep.get(1);
			stationsRep.insert(newStation);

			log.debug("New station: " + newStation.getId());
		} finally {
			citiesRep.dispose();
			stationsRep.rollback();
			stationsRep.dispose();
			assertEquals(0, ((TestDBConnectionManager) dbConnMngr).getInitialConnections());
			dbConnMngr.dispose();
		}

	}
}
