package com.pgis.bus.data.orm;

import java.util.ArrayList;
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
		if (schedule != null) {
			schedule.setDirect_route_id(id);
		}
		if (route_relations != null) {
			for (RouteRelation r : route_relations) {
				r.setDirect_route_id(id);
			}

		}
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

	public static DirectRoute createReverseByDirect(DirectRoute directRouteWay) {
		DirectRoute r = new DirectRoute();
		r.setDirect(false);
		r.setId(-1);
		r.setRoute_id(directRouteWay.getRoute_id());
		r.setSchedule(directRouteWay.getSchedule());

		ArrayList<RouteRelation> relations = new ArrayList<RouteRelation>();
		RouteRelation[] arr = directRouteWay.getRoute_relations().toArray(
				new RouteRelation[directRouteWay.getRoute_relations().size()]);
		for (int i = 0; i < arr.length; i++) {
			RouteRelation newRelation = new RouteRelation();
			newRelation.setPosition_index(i);
			newRelation.setId(-i);
			newRelation.setDirect_route_id(arr[arr.length - i - 1]
					.getDirect_route_id());
			if (i == 0) {
				newRelation.setStation_a_id(0);
			} else {
				newRelation.setStation_a_id(arr[arr.length - i]
						.getStation_b_id());
				newRelation.setGeom(arr[arr.length - i].getGeom());
				newRelation.setDistance(arr[arr.length - i].getDistance());
				newRelation.setEv_time(arr[arr.length - i].getEv_time());
			}
			newRelation.setStation_b_id(arr[arr.length - i - 1]
					.getStation_b_id());
			newRelation.setStationB(arr[arr.length - i - 1].getStationB());
			relations.add(newRelation);
		}
		r.setRoute_relations(relations);
		return r;
	}

	public void updateIDs() {
		RouteRelation[] arr = this.getRoute_relations().toArray(
				new RouteRelation[this.getRoute_relations().size()]);
		for (int i = 0; i < arr.length; i++) {
			if (i == 0) {
				arr[0].setStation_a_id(0);

			} else {
				arr[i].setStation_a_id(arr[i - 1].getStation_b_id());
			}
			Station stB = arr[i].getStationB();
			if (stB != null) {
				arr[i].setStation_b_id(stB.getId());
			}
			arr[i].setPosition_index(i);
		}
	}

}
