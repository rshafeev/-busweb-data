package com.pgis.bus.data.models.route;


public class InputRouteModel extends RouteModel {

	public InputRouteModel() {
      this.routeType = "c_route_station_input";
	}

	public String toString() {
		String out = "";
		out += "  Transition to point A. \n";
		out += "  Distance : " + this.getDistance() + " meters \n";
		out += "  Time     : " + this.getMoveTime() + " \n";
		return out;
	}
}
