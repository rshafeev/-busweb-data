package com.pgis.bus.data.models.factory.route;

import com.pgis.bus.data.orm.RouteWay;
import com.pgis.bus.net.models.route.RouteWayModel;

public class RouteWayModelFactory {

	RouteWayModel createModel(RouteWay way) throws Exception {
		RouteWayModel model = new RouteWayModel();
		model.setSchedule(ScheduleModelFactory.createModel(way.getSchedule()));
		model.setId(way.getId());
		model.setRelations(RouteRelationModelFactory.createModels(way.getRouteRelations()));
		return model;
	}
}
