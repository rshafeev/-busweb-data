package com.pgis.bus.data.orm;

import java.util.Collection;

public class DirectRoute {
	private int id;
	private int route_id;
	private boolean direct;
	private Collection<RouteRelation> route_relations;
	private Schedule schedule;
	public Schedule getSchedule() {
		return schedule;
	}
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRoute_id() {
		return route_id;
	}
	public void setRoute_id(int route_id) {
		this.route_id = route_id;
	}
	public boolean isDirect() {
		return direct;
	}
	public void setDirect(boolean direct) {
		this.direct = direct;
	}
	public Collection<RouteRelation> getRoute_relations() {
		return route_relations;
	}
	public void setRoute_relations(Collection<RouteRelation> route_relations) {
		this.route_relations = route_relations;
	}
	
	@Override
	public String toString() {
		return "DirectRoute [id=" + id + ", route_id=" + route_id + ", direct="
				+ direct + ", route_relations=" + route_relations
				+ ", schedule=" + schedule + "]";
	}
	
	
}
