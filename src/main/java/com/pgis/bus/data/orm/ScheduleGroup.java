package com.pgis.bus.data.orm;

import java.util.Collection;

public class ScheduleGroup {
	private int id;
	private int scheduleID;
	private Collection<ScheduleGroupDay> days;
	private Collection<Timetable> timetables;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		if (days != null) {
			for (ScheduleGroupDay day : days) {
				day.setScheduleGroupID(id);
			}
		}
		if (timetables != null) {
			for (Timetable t : timetables) {
				t.setSchedule_group_id(id);
			}
		}
	}

	public int getScheduleID() {
		return scheduleID;
	}

	public void setScheduleID(int schedule_id) {
		this.scheduleID = schedule_id;
	}

	public Collection<ScheduleGroupDay> getDays() {
		return days;
	}

	public void setDays(Collection<ScheduleGroupDay> days) {
		this.days = days;
	}

	public Collection<Timetable> getTimetables() {
		return timetables;
	}

	public void setTimetables(Collection<Timetable> timetables) {
		this.timetables = timetables;
	}

}
