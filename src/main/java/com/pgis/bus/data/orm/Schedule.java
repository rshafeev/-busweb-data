package com.pgis.bus.data.orm;

import java.util.Collection;

public class Schedule {

	private int id;
	private int directRouteId;
	private Collection<ScheduleGroup> scheduleGroups;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		if (scheduleGroups == null)
			return;
		for (ScheduleGroup g : scheduleGroups) {
			g.setScheduleID(id);
		}
	}

	public int getDirectRouteId() {
		return directRouteId;
	}

	public void setDirectRouteId(int direct_route_id) {
		this.directRouteId = direct_route_id;
	}

	public Collection<ScheduleGroup> getScheduleGroups() {
		return scheduleGroups;
	}

	public void setScheduleGroups(Collection<ScheduleGroup> scheduleGroups) {
		this.scheduleGroups = scheduleGroups;
	}

	@Override
	public String toString() {
		return "Schedule [id=" + id + ", direct_route_id=" + directRouteId
				+ ", scheduleGroups=" + scheduleGroups + "]";
	}

}
