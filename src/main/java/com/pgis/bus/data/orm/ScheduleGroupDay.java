package com.pgis.bus.data.orm;

import com.pgis.bus.data.orm.type.DayEnum;

public class ScheduleGroupDay {

	private int id;
	private int schedule_group_id;
	private DayEnum day_id;

	public ScheduleGroupDay(){
		
	}
	public ScheduleGroupDay(DayEnum day){
		this.day_id = day;
		this.schedule_group_id = -1;
		this.id = -1;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSchedule_group_id() {
		return schedule_group_id;
	}

	public void setSchedule_group_id(int schedule_group_id) {
		this.schedule_group_id = schedule_group_id;
	}

	public DayEnum getDay_id() {
		return day_id;
	}

	public void setDay_id(DayEnum day_id) {
		this.day_id = day_id;
	}

	@Override
	public String toString() {
		return "ScheduleGroupDay [id=" + id + ", schedule_group_id="
				+ schedule_group_id + ", day_id=" + day_id + "]";
	}

}
