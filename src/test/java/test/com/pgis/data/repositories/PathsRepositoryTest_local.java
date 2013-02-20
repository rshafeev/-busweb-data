package test.com.pgis.data.repositories;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.com.pgis.data.DBTestConnectionFactory;
import test.com.pgis.data.TestDBConnectionManager;
import test.com.pgis.data.TestDataSource;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.orm.type.Path_t;
import com.pgis.bus.data.repositories.IPathsRepository;
import com.pgis.bus.data.repositories.impl.PathsRepository;
import com.pgis.bus.net.models.Location;
import com.pgis.bus.net.orm.AlgStrategyEnum;
import com.pgis.bus.net.orm.DayEnum;
import com.pgis.bus.net.request.FindPathsOptions;
import com.pgis.bus.net.request.data.OutTime;
import com.pgis.bus.net.request.data.RouteTypeDiscount;

public class PathsRepositoryTest_local {


	IDBConnectionManager dbConnectionManager = null;
	@Before
	public void init() {
		dbConnectionManager = DBTestConnectionFactory.getTestDBConnectionManager();
	}

	@Test
	public void getShortestWays1_Test() throws Exception {
		// prepare data
		Location p1 = new Location(50.026350246659, 36.3360857963562);
		Location p2 = new Location(50.0355169337227, 36.2198925018311);

		OutTime outTime = new OutTime(DayEnum.c_Monday, 10, 0);
		RouteTypeDiscount[] route_types = {
				new RouteTypeDiscount("c_route_trolley", 1.0),
				new RouteTypeDiscount("c_route_metro", 0.5),
				new RouteTypeDiscount("c_route_bus", 1.0) };
		FindPathsOptions opts = new FindPathsOptions();
		opts.setCityID(1);
		opts.setP1(p1);
		opts.setP2(p2);
		opts.setOutTime(outTime);
		opts.setMaxDistance(300);
		opts.setRouteTypes(Arrays.asList(route_types));
		opts.setTransitions(true);
		opts.setAlgStrategy(AlgStrategyEnum.c_cost);
		opts.setLangID("c_ru");
		
		// get ways
		IPathsRepository r = new PathsRepository(dbConnectionManager);
		Collection<Path_t> paths = r.getShortestPaths(opts);
		for(Path_t p : paths ){
			System.out.println(p.toString());
		}
		
	}
}
