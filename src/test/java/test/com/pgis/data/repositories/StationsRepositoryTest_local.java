package test.com.pgis.data.repositories;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.Collection;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.postgis.Point;

import test.com.pgis.data.TestDBConnectionManager;
import test.com.pgis.data.TestDataSource;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.orm.Station;

import com.pgis.bus.data.repositories.ICitiesRepository;
import com.pgis.bus.data.repositories.IStationsRepository;
import com.pgis.bus.data.repositories.impl.CitiesRepository;
import com.pgis.bus.data.repositories.impl.Repository;
import com.pgis.bus.data.repositories.impl.StationsRepository;


//import com.pgis.bus.data.repositories.UsersRepository;
public class StationsRepositoryTest_local {
	@Before
	public void init() {
		TestDataSource source = new TestDataSource();
		TestDBConnectionManager dbConnectionManager = new TestDBConnectionManager(
				source.getDataSource());
		DBConnectionFactory.init(dbConnectionManager);
		System.out.print("init test\n");
	}

	@After
	public void destroy() {
		DBConnectionFactory.free();
	}

	@Test
	public void test() throws Exception {
		assertFalse(false);
	}

	@Test
	public void getAllStationsTest() throws Exception {
		// get city
		Connection c = Repository.getConnection();
		ICitiesRepository db = new CitiesRepository(c, false, false);
		City city = db.getCityByName("c_en", "Kharkov");
		assertNotNull(city);

		// get stations
		IStationsRepository stationsRepository = new StationsRepository(c,
				false, false);
		Collection<Station> stations = stationsRepository
				.getStationsByCity(city.id);
		for (Station s : stations) {
			System.out.println("Station id : " + s.getLocation().x);
			System.out.println("Station id : " + s.getLocation().y);
		}

		c.rollback();
		DBConnectionFactory.closeConnection(c);

	}

	@Test
	public void getAllStationsInBoxTest() throws Exception {
		System.out.println("getAllStationsInBoxTest()");
		// get city
		Connection c = Repository.getConnection();
		ICitiesRepository db = new CitiesRepository(c, false, false);
		City city = db.getCityByName("c_en", "Kharkov");
		assertNotNull(city);

		// get stations
		IStationsRepository stationsRepository = new StationsRepository(c,
				false, false);
		Collection<Station> stations = stationsRepository
				.getStationsByBox(city.id, new Point(49,35), new Point(51,37));
		for (Station s : stations) {
			System.out.println("Station id : " + s.getLocation().x);
			System.out.println("Station id : " + s.getLocation().y);
		}

		c.rollback();
		DBConnectionFactory.closeConnection(c);

	}
	
	@Test
	public void insertStationTest() throws Exception {
		// get city
		Connection c = Repository.getConnection();
		ICitiesRepository db = new CitiesRepository(c, false, false);
		City city = db.getCityByName("c_en", "Kharkov");
		assertNotNull(city);

		// insert station
		
		Station newStation = new Station();
		newStation.setCity_id(city.id);
		newStation.setLocation(new Point(50, 40));
		newStation.getLocation().setSrid(4326);
		
		IStationsRepository stationsRepository = new StationsRepository(c,
				false, false);
		newStation = stationsRepository.insertStation(newStation);
		System.out.println("New station: " + newStation.getId());
		c.rollback();
		DBConnectionFactory.closeConnection(c);

	}
	@Test
	public void updateStationTest() throws Exception {
		// get city
		Connection c = Repository.getConnection();
		ICitiesRepository db = new CitiesRepository(c, false, false);
		City city = db.getCityByName("c_ru", "Харьков");
		assertNotNull(city);

		// get station
		IStationsRepository stationsRepository = new StationsRepository(c,
				false, false);
		Collection<Station> stations = stationsRepository
				.getStationsByCity(city.id);
		
		Station station = stations.iterator().next();
		
		// update station
		Station updateStation  = station.clone();
		updateStation = stationsRepository.updateStation(updateStation);
		System.out.println("update station:");
		System.out.println(updateStation.getCity_id());
		
		c.rollback();
		DBConnectionFactory.closeConnection(c);

	}
	


}
