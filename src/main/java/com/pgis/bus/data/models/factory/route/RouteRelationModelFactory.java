package com.pgis.bus.data.models.factory.route;

import java.util.ArrayList;
import java.util.Collection;

import com.pgis.bus.data.models.factory.TimeIntervalModelFactory;
import com.pgis.bus.data.orm.RouteRelation;
import com.pgis.bus.net.models.route.RouteRelationModel;

public class RouteRelationModelFactory {

	static public RouteRelationModel createModel(RouteRelation r) throws Exception {
		RouteRelationModel model = new RouteRelationModel();
		model.setDistance(r.getDistance());
		model.setId(r.getId());
		model.setMoveTime(TimeIntervalModelFactory.createModel(r.getEv_time()));

		return model;
	}

	static public Collection<RouteRelationModel> createModels(Collection<RouteRelation> arr) throws Exception {
		Collection<RouteRelationModel> models = new ArrayList<RouteRelationModel>();
		for (RouteRelation r : arr) {
			models.add(createModel(r));
		}
		return models;
	}
}
