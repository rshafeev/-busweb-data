package com.pgis.bus.data.helpers;

public class UpdateRouteOptions {

	private boolean isUpdateSchedule = false;
	private boolean isUpdateMainInfo = false;
	private boolean isUpdateRouteRelations = false;

	public boolean isUpdateSchedule() {
		return isUpdateSchedule;
	}

	public void setUpdateSchedule(boolean isUpdateSchedule) {
		this.isUpdateSchedule = isUpdateSchedule;
	}

	public boolean isUpdateMainInfo() {
		return isUpdateMainInfo;
	}

	public void setUpdateMainInfo(boolean isUpdateMainInfo) {
		this.isUpdateMainInfo = isUpdateMainInfo;
	}

	public boolean isUpdateRouteRelations() {
		return isUpdateRouteRelations;
	}

	public void setUpdateRouteRelations(boolean isUpdateRouteRelations) {
		this.isUpdateRouteRelations = isUpdateRouteRelations;
	}

}
