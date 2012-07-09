
package test.com.pgis.data.repositories;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.com.pgis.data.repositories.TestDataSource;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.orm.City;
import com.pgis.bus.data.repositories.ICitiesRepository;
import com.pgis.bus.data.repositories.impl.CitiesRepository;

//import com.pgis.bus.data.repositories.UsersRepository;
public class CitiesRepositoryTest_local {

	TestDataSource source = null;

	@Before
	public void init() {
		source = new TestDataSource();
		DBConnectionFactory.init(source.getDataSource());
		System.out.print("init test\n");
	}

	@After
	public void destroy() {
		if (source != null) {
			source.close();
			DBConnectionFactory.free();
			source = null;
		}
	}

	@Test
	public void getAllCitiesTest() throws Exception {
		ICitiesRepository db = new CitiesRepository();
		ArrayList<City> cities = db.getAllCities();
		System.out.print(cities.size());
	}
}
