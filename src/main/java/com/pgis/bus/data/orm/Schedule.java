package com.pgis.bus.data.orm;

import java.util.ArrayList;
import java.util.Collection;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.net.models.route.ScheduleModel;
import com.pgis.bus.net.models.route.schedule.ScheduleGroupModel;

public class Schedule extends ORMObject {

	private int id;
	private int rway_id;
	private Collection<ScheduleGroup> scheduleGroups;

	public Schedule() {
		super();
	}

	public Schedule(IConnectionManager connManager) {
		super(connManager);
	}

	public Schedule(ScheduleModel model) {
		super();
		if (model == null)
			return;
		this.setId(model.getId());
		this.setRouteWayId(model.getRouteWayID());

		scheduleGroups = new ArrayList<ScheduleGroup>();
		for (ScheduleGroupModel grp : model.getGroups()) {
			scheduleGroups.add(new ScheduleGroup(grp, model.getId()));
		}
	}

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

	public int getRouteWayId() {
		return rway_id;
	}

	public void setRouteWayId(int rwayId) {
		this.rway_id = rwayId;
	}

	public Collection<ScheduleGroup> getScheduleGroups() {
		return scheduleGroups;
	}

	public void setScheduleGroups(Collection<ScheduleGroup> scheduleGroups) {
		this.scheduleGroups = scheduleGroups;
	}

	public void addScheduleGroup(ScheduleGroup grp) {
		if (this.scheduleGroups == null)
			this.scheduleGroups = new ArrayList<ScheduleGroup>();
		this.scheduleGroups.add(grp);
	}

	@Override
	public String toString() {
		return "Schedule [id=" + id + ", rway_id=" + rway_id + ", scheduleGroups=" + scheduleGroups + "]";
	}

	public static ScheduleModel createModel(Schedule schedule) {
		if (schedule == null)
			return null;
		ScheduleModel model = new ScheduleModel();
		if (schedule.getScheduleGroups() != null) {

		}
		Collection<ScheduleGroupModel> groups = new ArrayList<ScheduleGroupModel>();
		if (schedule.getScheduleGroups() != null) {
			for (ScheduleGroup grp : schedule.getScheduleGroups()) {

				groups.add(grp.toModel());
			}
			model.setGroups(groups);
		}

		model.setId(schedule.getId());
		model.setRouteWayID(schedule.getRouteWayId());
		return model;
	}

	public ScheduleModel toModel() {
		return createModel(this);
	}

}
