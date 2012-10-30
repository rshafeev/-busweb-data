package com.pgis.bus.data.helpers;

public class LoadDirectRouteOptions {

	private boolean isLoadScheduleData;
	private LoadRouteRelationOptions loadRouteRelationOptions;

	public boolean isLoadRouteRelationsData() {
		return loadRouteRelationOptions == null ? false : true;
	}

	public boolean isLoadScheduleData() {
		return isLoadScheduleData;
	}

	public LoadRouteRelationOptions getLoadRouteRelationOptions() {
		return loadRouteRelationOptions;
	}

	public void setLoadRouteRelationOptions(
			LoadRouteRelationOptions loadRouteRelationOptions) {
		this.loadRouteRelationOptions = loadRouteRelationOptions;
	}

	public void setLoadScheduleData(boolean isLoadScheduleData) {
		this.isLoadScheduleData = isLoadScheduleData;
	}

}
