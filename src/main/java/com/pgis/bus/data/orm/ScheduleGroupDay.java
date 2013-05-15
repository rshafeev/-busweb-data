package com.pgis.bus.data.orm;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.type.DayEnum;

public class ScheduleGroupDay extends ORMObject {

	private int id;
	private int scheduleGroupID;
	private DayEnum day_id;

	public ScheduleGroupDay() {
		super();
	}

	public ScheduleGroupDay(DayEnum day_id) {
		super();
		this.day_id = day_id;
	}

	public ScheduleGroupDay(IConnectionManager connManager) {
		super(connManager);
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
		return "ScheduleGroupDay [id=" + id + ", schedule_group_id=" + scheduleGroupID + ", day_id=" + day_id + "]";
	}

}
