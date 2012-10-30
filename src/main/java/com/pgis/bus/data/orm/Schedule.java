package com.pgis.bus.data.orm;

import java.util.Collection;

public class Schedule {

	private int id;
	private int direct_route_id;
	private Collection<ScheduleGroup> scheduleGroups;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDirect_route_id() {
		return direct_route_id;
	}

	public void setDirect_route_id(int direct_route_id) {
		this.direct_route_id = direct_route_id;
	}

	public Collection<ScheduleGroup> getScheduleGroups() {
		return scheduleGroups;
	}

	public void setScheduleGroups(Collection<ScheduleGroup> scheduleGroups) {
		this.scheduleGroups = scheduleGroups;
	}

	@Override
	public String toString() {
		return "Schedule [id=" + id + ", direct_route_id=" + direct_route_id
				+ ", scheduleGroups=" + scheduleGroups + "]";
	}

}
