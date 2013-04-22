package com.pgis.bus.data.orm;

import java.util.ArrayList;
import java.util.Collection;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.orm.IRoutesRepository;
import com.pgis.bus.data.repositories.orm.IScheduleRepository;
import com.pgis.bus.data.repositories.orm.impl.RoutesRepository;
import com.pgis.bus.data.repositories.orm.impl.ScheduleRepository;

public class RouteWay extends ORMObject {
	private int id;
	private int routeID;
	private boolean direct;

	private Collection<RouteRelation> route_relations;
	private Schedule schedule;

	public RouteWay() {
		super();
	}

	public RouteWay(IConnectionManager connManager) {
		super(connManager);
	}

	public Schedule getSchedule() throws RepositoryException {
		if (schedule == null && super.connManager != null) {
			IScheduleRepository rep = null;
			try {
				rep = new ScheduleRepository(super.connManager);
				this.schedule = rep.getByRouteWay(this.id);
			} catch (Exception e) {
			} finally {

				if (rep != null)
					rep.dispose();
			}
		}
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
			schedule.setDirectRouteId(id);
		}
		if (route_relations != null) {
			for (RouteRelation r : route_relations) {
				r.setRouteWayID(id);
			}

		}
	}

	public int getRouteID() {
		return routeID;
	}

	public void setRouteID(int routeID) {
		this.routeID = routeID;
	}

	public boolean isDirect() {
		return direct;
	}

	public void setDirect(boolean direct) {
		this.direct = direct;
	}

	public Collection<RouteRelation> getRouteRelations() throws RepositoryException {
		if (route_relations == null && super.connManager != null) {
			IRoutesRepository rep = null;
			try {
				rep = new RoutesRepository(super.connManager);
				this.route_relations = rep.getRouteWayRelations(this.id);
			} catch (Exception e) {
			} finally {
				if (rep != null)
					rep.dispose();
			}

		}
		return route_relations;

	}

	public void setRouteRelations(Collection<RouteRelation> route_relations) {
		this.route_relations = route_relations;
	}

	@Override
	public String toString() {
		return "DirectRoute [id=" + id + ", route_id=" + routeID + ", direct=" + direct + ", route_relations="
				+ route_relations + ", schedule=" + schedule + "]";
	}

	public static RouteWay createReverseByDirect(RouteWay directRouteWay) throws RepositoryException {
		RouteWay r = new RouteWay();
		r.setDirect(false);
		r.setId(-1);
		r.setRouteID(directRouteWay.getRouteID());
		r.setSchedule(directRouteWay.getSchedule());

		ArrayList<RouteRelation> relations = new ArrayList<RouteRelation>();
		RouteRelation[] arr = directRouteWay.getRouteRelations().toArray(
				new RouteRelation[directRouteWay.getRouteRelations().size()]);
		for (int i = 0; i < arr.length; i++) {
			RouteRelation newRelation = new RouteRelation();
			newRelation.setPosition_index(i);
			newRelation.setId(-i);
			newRelation.setRouteWayID(arr[arr.length - i - 1].getRouteWayID());
			if (i == 0) {
				newRelation.setStationAId(0);
			} else {
				newRelation.setStationAId(arr[arr.length - i].getStationBId());
				newRelation.setGeom(arr[arr.length - i].getGeom());
				newRelation.setDistance(arr[arr.length - i].getDistance());
				newRelation.setEv_time(arr[arr.length - i].getEv_time());
			}
			newRelation.setStationBId(arr[arr.length - i - 1].getStationBId());
			newRelation.setStationB(arr[arr.length - i - 1].getStationB());
			relations.add(newRelation);
		}
		r.setRouteRelations(relations);
		return r;
	}

	public void updateIDs() throws RepositoryException {
		RouteRelation[] arr = this.getRouteRelations().toArray(new RouteRelation[this.getRouteRelations().size()]);
		for (int i = 0; i < arr.length; i++) {
			if (i == 0) {
				arr[0].setStationAId(0);

			} else {
				arr[i].setStationAId(arr[i - 1].getStationBId());
			}
			Station stB = arr[i].getStationB();
			if (stB != null) {
				arr[i].setStationBId(stB.getId());
			}
			arr[i].setPosition_index(i);
		}
	}

}
