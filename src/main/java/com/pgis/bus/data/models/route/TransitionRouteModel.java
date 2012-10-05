package com.pgis.bus.data.models.route;

public class TransitionRouteModel extends RouteModel {
	public TransitionRouteModel() {
		this.routeType = "c_route_transition";
	}

	public String toString() {
		String out = "";
		out += "  Transition between routes. \n";
		out += "  Distance : " + this.getDistance() + " meters \n";
		out += "  Time     : " + this.getMoveTime() + " \n";
		return out;
	}

}
