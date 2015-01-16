package test.com.pgis.data.repositories.orm;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.pgis.bus.data.models.RouteGeoData;
import com.pgis.bus.data.models.RoutePart;
import com.pgis.bus.data.repositories.orm.IPathsRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.pgis.data.TestDBConnectionManager;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.type.Path_t;
import com.pgis.bus.data.params.FindPathsParams;
import com.pgis.bus.data.repositories.orm.impl.PathsRepository;
import com.pgis.bus.net.models.AlgStrategyEnumModel;
import com.pgis.bus.net.models.DayEnumModel;
import com.pgis.bus.net.models.LangEnumModel;
import com.pgis.bus.net.models.geom.PointModel;
import com.pgis.bus.net.request.FindPathsRequest;
import com.pgis.bus.net.request.data.OutTimeModel;
import com.pgis.bus.net.request.data.RouteTypeDiscount;

public class PathsRepositoryTest_local {
	private static final Logger log = LoggerFactory.getLogger(PathsRepositoryTest_local.class);

	@Test
	public void getGeoDataByRoutePart_Test() throws Exception {
		System.out.println("getGeoDataByRoutePart_Test()...");

		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		PathsRepository rep = new PathsRepository(dbConnMngr);
		RoutePart routePart = new RoutePart();
		routePart.setID(5);
		routePart.setStartInd(0);
		routePart.setFinishInd(3);
		String lang_id = "c_ru";

		Collection<RouteGeoData> relations = rep.getGeoDataByRoutePart(
				routePart, lang_id);
		Iterator<RouteGeoData> i = relations.iterator();
		System.out.println("getGeoDataByRoutePart_Test()");
		while (i.hasNext()) {
			RouteGeoData d = i.next();
			System.out.println(d.toString());
		}
	}
	
	@Test
	public void getShortestPaths1_Test() throws Exception {
		log.debug("getShortestPaths1_Test()");
		// prepare data
		PointModel p1 = new PointModel(50.026350246659, 36.3360857963562);
		PointModel p2 = new PointModel(50.0355169337227, 36.2198925018311);

		OutTimeModel outTime = new OutTimeModel(DayEnumModel.Monday, 10, 0);
		RouteTypeDiscount[] route_types = { new RouteTypeDiscount("trolley", 1.0), new RouteTypeDiscount("metro", 0.5),
				new RouteTypeDiscount("bus", 1.0) };
		FindPathsRequest request = new FindPathsRequest();
		request.setCityID(1);
		request.setP1(p1);
		request.setP2(p2);
		request.setOutTime(outTime);
		request.setMaxDistance(300);
		request.setRouteTypes(Arrays.asList(route_types));
		request.setTransitions(true);
		request.setAlgStrategy(AlgStrategyEnumModel.cost);
		request.setLangID(LangEnumModel.ru);

		// get ways
		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		PathsRepository rep = new PathsRepository(dbConnMngr);
		try {
			Collection<Path_t> paths = rep.findShortestPaths(new FindPathsParams(request));
			for (Path_t p : paths) {
				System.out.println(p.toString());
				if (p.rway_id != 0) {
					assertTrue(p.wait_time != null);
				}
			}
		} finally {
			rep.rollback();
			rep.dispose();
			dbConnMngr.dispose();
		}

	}

	@Test
	public void getShortestPaths2_Test() throws Exception {
		log.debug("getShortestPaths2_Test()");
		// prepare data
		PointModel p1 = new PointModel(50.01303427698978, 36.22690200805664);
		PointModel p2 = new PointModel(50.00365685169585, 36.30380630493164);

		OutTimeModel outTime = new OutTimeModel(DayEnumModel.Sunday, 10, 10);
		RouteTypeDiscount[] route_types = { new RouteTypeDiscount("trolley", 1.0), new RouteTypeDiscount("metro", 0.5),
				new RouteTypeDiscount("bus", 1.0) };
		FindPathsRequest request = new FindPathsRequest();
		request.setCityID(1);
		request.setP1(p1);
		request.setP2(p2);
		request.setOutTime(outTime);
		request.setMaxDistance(300);
		request.setRouteTypes(Arrays.asList(route_types));
		request.setTransitions(true);
		request.setAlgStrategy(AlgStrategyEnumModel.opt);
		request.setLangID(LangEnumModel.ru);

		// get ways
		IConnectionManager dbConnMngr = TestDBConnectionManager.create();
		PathsRepository rep = new PathsRepository(dbConnMngr);
		try {
			Collection<Path_t> paths = rep.findShortestPaths(new FindPathsParams(request));
			for (Path_t p : paths) {
				// System.out.println(p.toString());
			}
		} finally {
			rep.rollback();
			rep.dispose();
			dbConnMngr.dispose();
		}
	}
}
