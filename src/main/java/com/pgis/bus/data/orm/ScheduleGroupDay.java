package com.pgis.bus.data.orm;

import com.pgis.bus.net.orm.DayEnum;


public class ScheduleGroupDay {

	private int id;
	private int scheduleGroupID;
	private DayEnum day_id;

	public ScheduleGroupDay(){
		
	}
	public ScheduleGroupDay(DayEnum day){
		this.day_id = day;
		this.scheduleGroupID = -1;
		this.id = -1;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getScheduleGroupID() {
		return scheduleGroupID;
	}

	public void setScheduleGroupID(int schedule_group_id) {
		this.scheduleGroupID = schedule_group_id;
	}

	public DayEnum getDayID() {
		return day_id;
	}

	public void setDayID(DayEnum day_id) {
		this.day_id = day_id;
	}

	@Override
	public String toString() {
		return "ScheduleGroupDay [id=" + id + ", schedule_group_id="
				+ scheduleGroupID + ", day_id=" + day_id + "]";
	}

}
