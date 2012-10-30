package com.pgis.bus.data.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.pgis.bus.data.models.route.OutputRouteModel;
import com.pgis.bus.data.models.route.TransitionRouteModel;
import com.pgis.bus.data.models.route.TransportRouteModel;
import com.pgis.bus.data.models.route.RouteModel;
import com.pgis.bus.data.models.route.InputRouteModel;
import com.pgis.bus.data.orm.WayElem;

public class WayModel {
	ArrayList<RouteModel> routes;
	public String  getFullTime(){
		String out="";
		
		
		return out;
	}
	public double getCost(){
		double cost=0;
		
		for (int i = 0; i < routes.size(); i++) {
			RouteModel routeModel = routes.get(i);
			if (routeModel instanceof TransportRouteModel) {
				TransportRouteModel r = (TransportRouteModel) routeModel;
				cost += r.getCost();
			}
		}
        cost = (((int)cost*100))/100.0;		
		return cost;
	}
	
	
	public WayModel(List<WayElem> elems) {
		if (elems.size() == 0)
			return;

		Collections.sort(elems);
		routes = new ArrayList<RouteModel>();
		// добавим первый маршрут: "пешком от точки А до первой остановки"
		WayElem firstElem = elems.get(0);
		InputRouteModel firstFootRoute = new InputRouteModel();
		firstFootRoute.setDistance(firstElem.distance);
		firstFootRoute.setMoveTime(firstElem.move_time);
		routes.add(firstFootRoute);

		// добавим все послед. машруты
		for (int i = 0; i < elems.size() - 1; i += 2) {
			WayElem elem1 = elems.get(i);
			WayElem elem2 = elems.get(i + 1);
			TransportRouteModel route = new TransportRouteModel();
			route.setStationStart(elem1.station_name);
			route.setStationFinish(elem2.station_name);
			route.setDistance(elem2.distance);
			route.setMoveTime(elem2.move_time);
			route.setRouteType(elem1.route_type);
			route.setDirectRouteID(elem1.direct_route_id);
			route.setCost(elem1.cost);
			route.setInterval(elem1.wait_time);
			route.setIndexA(elem1.relation_index);
			route.setIndexB(elem2.relation_index);
			route.setRouteName(elem1.route_name);
			if (i != 0 && i != elems.size() - 2) {
				TransitionRouteModel transition = new TransitionRouteModel();
				transition.setDistance(elem1.distance);
				transition.setMoveTime(elem1.move_time);
				routes.add(transition);
			}
			routes.add(route);
		}

		// добавим последний маршрут:
		// "пешком последней остановки до точки назначения B"
		WayElem finishElem = elems.get(elems.size() - 1);
		OutputRouteModel finishFootRoute = new OutputRouteModel();
		finishFootRoute.setDistance(finishElem.distance);
		finishFootRoute.setMoveTime(finishElem.move_time);
		routes.add(finishFootRoute);
	}

	public ArrayList<RouteModel> getRoutes() {
		return routes;
	}

	public void setRoutes(ArrayList<RouteModel> routes) {
		this.routes = routes;
	}

	public RoutePart[] createRoutePartArr() {
		ArrayList<RoutePart> parts = new ArrayList<RoutePart>();
		Iterator<RouteModel> i = routes.iterator();
		while (i.hasNext()) {
			RouteModel route = i.next();
			if (route instanceof TransportRouteModel) {
				TransportRouteModel transportRooute = (TransportRouteModel) route;
				RoutePart part = new RoutePart();
				part.setDirectRouteID(transportRooute.getDirectRouteID());
				part.setIndexStart(transportRooute.getIndexA());
				part.setIndexFinish(transportRooute.getIndexB());
				parts.add(part);
			}
		}
		return parts.toArray(new RoutePart[parts.size()]);

	}

	public String createRoutePartArrStr() {
		RoutePart[] parts = createRoutePartArr();
		String out = "";
		out += "[";
		for (int i = 0; i < parts.length; i++) {
			out += "[";
			out += Integer.toString(parts[i].getDirectRouteID()) + ",";
			out += Integer.toString(parts[i].getIndexStart()) + ",";
			out += Integer.toString(parts[i].getIndexFinish());
			out += "]";
			if (i != parts.length - 1) {
				out += ",";
			}
		}
		out += "]";
		return out;

	}

	public String toString() {
		Iterator<RouteModel> i = routes.iterator();
		Integer ind = 1;
		String out = "";
		while (i.hasNext()) {
			out += "  Route [ " + ind + " ]:\n";
			out += i.next().toString();
			out += "-- --\n";
			ind += 1;
		}
		return out;
	}
	

	
	public double getTime()
	{
		double time = 0;
		for (int i = 0; i < routes.size(); i++) {
			RouteModel routeModel = routes.get(i);
			if (routeModel instanceof TransportRouteModel) {
			TransportRouteModel r = (TransportRouteModel) routeModel;
			time += r.getInterval().getMinutes();
			}
			}
		time = (((int)time*100))/100.0;
		return time;
		
	}
}
