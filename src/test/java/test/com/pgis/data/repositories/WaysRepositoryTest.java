package test.com.pgis.data.repositories;

import static org.junit.Assert.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.postgis.Point;

import test.com.pgis.data.TestDBConnectionManager;
import test.com.pgis.data.TestDataSource;

import com.pgis.bus.data.DBConnectionFactory;
import com.pgis.bus.data.models.FindWaysOptions;
import com.pgis.bus.data.models.RouteTypeDiscount;
import com.pgis.bus.data.models.WaysModel;
import com.pgis.bus.data.orm.WayElem;
import com.pgis.bus.data.orm.type.AlgStrategyEnum;
import com.pgis.bus.data.orm.type.DayEnum;
import com.pgis.bus.data.repositories.IWaysRepository;
import com.pgis.bus.data.repositories.impl.WaysRepository;

public class WaysRepositoryTest {

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

	@SuppressWarnings("deprecation")
	@Test
	public void getShortestWays1_Test() throws Exception {
		// prepare data
		Point p1 = new Point(50.026350246659, 36.3360857963562);
		p1.setSrid(4326);
		//Point p2 = new Point(50.004634132497, 36.2337112426758);
		Point p2 = new Point(50.0355169337227, 36.2198925018311);
		p2.setSrid(4326);

		ArrayList<RouteTypeDiscount> route_types = new ArrayList<RouteTypeDiscount>();
		route_types.add(new RouteTypeDiscount("c_route_station_input", 1.0));
		route_types.add(new RouteTypeDiscount("c_route_transition", 1.0));
		route_types.add(new RouteTypeDiscount("c_route_trolley", 1.0));
		route_types.add(new RouteTypeDiscount("c_route_metro", 0.5));
		route_types.add(new RouteTypeDiscount("c_route_bus", 1.0));

		FindWaysOptions opts = new FindWaysOptions();
		opts.setCity_id(1);
		opts.setP1(p1);
		opts.setP2(p2);
		opts.setDay_id(DayEnum.c_Monday);
		opts.setTime_start(new Time(10, 0, 0));
		opts.setMaxDistance(300);
		opts.setUsage_routeTypes(route_types);
		opts.setAlg_strategy(AlgStrategyEnum.c_cost);
		opts.setLang_id("c_ru");
		// get ways
		IWaysRepository r = new WaysRepository();
		Collection<WayElem> ways = r.getShortestWays(opts);
		WaysModel model = new WaysModel(ways);
		System.out.println(model.toString());
	}
}
