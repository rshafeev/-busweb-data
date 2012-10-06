package test.com.pgis.data.repositories;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Iterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.com.pgis.data.TestDBConnectionManager;
import test.com.pgis.data.TestDataSource;
import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.models.RouteGeoData;
import com.pgis.bus.data.models.RoutePart;
import com.pgis.bus.data.repositories.IRoutesRepository;
import com.pgis.bus.data.repositories.impl.RoutesRepository;


public class RoutesRepositoryTest_local {

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
	public void getGeoDataByRoutePart_Test() throws Exception {
		IRoutesRepository repository = new RoutesRepository();
		RoutePart routePart = new RoutePart();
		routePart.setDirectRouteID(3);
		routePart.setIndexStart(0);
		routePart.setIndexFinish(3);
		String lang_id = "c_ru";

		Collection<RouteGeoData> relations = repository.getGeoDataByRoutePart(
				routePart, lang_id);
		Iterator<RouteGeoData> i = relations.iterator();
		System.out.println("getGeoDataByRoutePart_Test()");
		while(i.hasNext()){
			RouteGeoData d = i.next();
			System.out.println(d.toString());
		}
		
	}
}
