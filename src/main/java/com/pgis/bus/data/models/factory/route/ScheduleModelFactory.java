package com.pgis.bus.data.models.factory.route;

import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.net.models.route.ScheduleModel;

public class ScheduleModelFactory {

	public static ScheduleModel createModel(Schedule schedule) {
		ScheduleModel model = new ScheduleModel();

		return model;
	}

}
