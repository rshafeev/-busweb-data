package com.pgis.bus.data.models.factory.route;

import com.pgis.bus.data.orm.RouteWay;
import com.pgis.bus.net.models.route.RouteWayModel;

public class RouteWayModelFactory {

	RouteWayModel createModel(RouteWay way) {
		RouteWayModel model = new RouteWayModel();
		model.setSchedule(ScheduleModelFactory.createModel(way.getSchedule()));
		model.setId(way.getId());
		// model.setRelations(way.g);
		return model;
	}
}
