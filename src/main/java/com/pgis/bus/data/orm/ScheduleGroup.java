package com.pgis.bus.data.orm;

import java.util.ArrayList;
import java.util.Collection;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.type.DayEnum;
import com.pgis.bus.net.models.DayEnumModel;
import com.pgis.bus.net.models.route.schedule.ScheduleGroupModel;
import com.pgis.bus.net.models.route.schedule.TimetableModel;

public class ScheduleGroup extends ORMObject implements Cloneable {
	private int id;
	private int scheduleID;
	private Collection<ScheduleGroupDay> days;
	private Collection<Timetable> timetables;

	public ScheduleGroup() {
		super();
	}

	public ScheduleGroup(IConnectionManager connManager) {
		super(connManager);
	}

	public ScheduleGroup(ScheduleGroupModel model, int scheduleID) {
		super();
		this.setId(model.getId());
		this.setScheduleID(scheduleID);

		if (model.getDays() != null) {
			Collection<ScheduleGroupDay> ormDays = new ArrayList<ScheduleGroupDay>();
			for (DayEnumModel day : model.getDays()) {
				ScheduleGroupDay ormDay = new ScheduleGroupDay();
				ormDay.setId(-1);
				ormDay.setScheduleGroupID(model.getId());
				ormDay.setDayID(DayEnum.valueOf(day));
				ormDays.add(ormDay);
			}
			this.setDays(ormDays);
		}
		if (model.getTimetable() != null) {
			Collection<Timetable> ormTimetable = new ArrayList<Timetable>();
			for (TimetableModel timetable : model.getTimetable()) {
				ormTimetable.add(new Timetable(timetable, this.getId()));
			}
			this.setTimetables(ormTimetable);
		}
	}

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
				t.setScheduleGroupID(id);
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

	public void addDay(ScheduleGroupDay d) {
		if (this.days == null)
			this.days = new ArrayList<ScheduleGroupDay>();
		this.days.add(d);
	}

	public void addTimetable(Timetable t) {
		if (this.timetables == null)
			this.timetables = new ArrayList<Timetable>();
		this.timetables.add(t);
	}

	public Collection<Timetable> getTimetables() {
		return timetables;
	}

	public void setTimetables(Collection<Timetable> timetables) {
		this.timetables = timetables;
	}

	public ScheduleGroupModel toModel() {
		ScheduleGroupModel model = new ScheduleGroupModel();

		model.setId(this.id);

		model.setTimetable(Timetable.createModels(this.getTimetables()));
		if (this.days == null)
			return model;
		Collection<DayEnumModel> days = new ArrayList<DayEnumModel>();
		for (ScheduleGroupDay day : this.getDays()) {
			days.add(day.getDayID().toModel());
		}
		model.setDays(days);
		return model;
	}

	@Override
	public ScheduleGroup clone() throws CloneNotSupportedException {
		ScheduleGroup scheduleGroup = (ScheduleGroup) super.clone();
		Collection<ScheduleGroupDay> scheduleGroupDay = new ArrayList<ScheduleGroupDay>();
		if (days != null) {
			for (ScheduleGroupDay sgd : this.days) {
				scheduleGroupDay.add(sgd.clone());
			}
		}
		
		Collection<Timetable> timetable = new ArrayList<Timetable>();
		if (timetables != null) {
		for (Timetable t : this.timetables) {
			timetable.add(t.clone());
		}
		}
		scheduleGroup.id = this.id;
		scheduleGroup.scheduleID = this.scheduleID;
		scheduleGroup.days = scheduleGroupDay;
		scheduleGroup.timetables = timetable;
		return scheduleGroup;
	}

}
