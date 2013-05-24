package test.com.pgis.data.repositories.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;
import org.postgis.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.model.impl.StationsModelRepository;
import com.pgis.bus.data.repositories.orm.impl.CitiesRepository;
import com.pgis.bus.data.service.IDataBaseService;
import com.pgis.bus.data.service.IDataModelsService;
import com.pgis.bus.data.service.impl.DataBaseService;
import com.pgis.bus.data.service.impl.DataModelsService;
import com.pgis.bus.net.models.station.StationModel;

public class StationsModelRepositoryTest_local {
	private static final Logger log = LoggerFactory.getLogger(StationsModelRepositoryTest_local.class);

	@Test
	public void getStationsListTest() throws Exception {
		log.debug("getStationsListTest()");
		// get city
		LangEnum langID = LangEnum.c_en;
		StationsModelRepository stationsRep = null;
		IConnectionManager dbConnMngr = null;
		CitiesRepository cityRep = null;
		try {
			dbConnMngr = TestDBConnectionManager.create();
			cityRep = new CitiesRepository(dbConnMngr);
			City city = cityRep.getByName(langID, "Kyiv");
			assertNotNull(city);

			// get stations
			stationsRep = new StationsModelRepository(langID, dbConnMngr);
			Collection<StationModel> stations = stationsRep.getStationsList(city.getId());
			System.out.println(stations.size());
		} finally {
			stationsRep.rollback();
			stationsRep.dispose();
			cityRep.rollback();
			cityRep.dispose();
			assertEquals(0, ((TestDBConnectionManager) dbConnMngr).getInitialConnections());
			dbConnMngr.dispose();
		}
	}

	@Test
	public void getStationsListServiceTest() throws Exception {
		log.debug("getStationsListServiceTest()");
		// init
		LangEnum langID = LangEnum.c_en;
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
			Collection<StationModel> stations = dbModel.Stations().getStationsList(city.getId());
			System.out.println(stations.size());

		} finally {
			dbService.dispose();
			dbModel.dispose();
			assertEquals(0, ((TestDBConnectionManager) dbConnMngr).getInitialConnections());
			dbConnMngr.dispose();
		}

	}

	@Test
	public void getStationsFromBoxTest() throws Exception {
		log.debug("getStationsFromBoxTest()");
		// get city
		StationsModelRepository stationsRep = null;
		IConnectionManager dbConnMngr = null;
		CitiesRepository cityRep = null;
		try {
			dbConnMngr = TestDBConnectionManager.create();
			cityRep = new CitiesRepository(dbConnMngr);
			City city = cityRep.getByName(LangEnum.c_en, "Kyiv");
			assertNotNull(city);

			// get stations
			stationsRep = new StationsModelRepository(dbConnMngr);
			Collection<StationModel> stations = stationsRep.getStationsFromBox(city.getId(), new Point(49, 35),
					new Point(51, 37));
			log.debug("Stations count: {}", stations.size());
		} finally {
			stationsRep.dispose();
			cityRep.rollback();
			cityRep.dispose();
			assertEquals(0, ((TestDBConnectionManager) dbConnMngr).getInitialConnections());
			dbConnMngr.dispose();
		}
	}

}
