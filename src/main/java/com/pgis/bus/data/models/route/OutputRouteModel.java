package com.pgis.bus.data.models.route;

public class OutputRouteModel extends RouteModel {
	public OutputRouteModel() {
	      this.routeType = "c_route_station_output";
		}

	public String toString() {
		String out = "";
		out += "  Transition to point B. \n";
		out += "  Distance : " + this.getDistance() + " meters \n";
		out += "  Time     : " + this.getMoveTime() + " \n";
		return out;
	}
}
