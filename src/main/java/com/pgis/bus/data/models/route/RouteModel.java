package com.pgis.bus.data.models.route;

import org.postgresql.util.PGInterval;

public abstract class RouteModel {

	/**
	 * Тим маршрута: переход, метро, автобус, другие "c_route_transition",
	 * "c_route_metro_transition", "c_route_metro", "c_route_bus", etc
	 */
	protected String routeType;

	/**
	 * Время движения по заданному маршруту
	 */
	protected PGInterval moveTime;

	

	public String getRouteType() {
		return routeType;
	}

	/**
	 * Расстояние
	 */
	protected double distance;


	


	public PGInterval getMoveTime() {
		return moveTime;
	}

	public void setMoveTime(PGInterval moveTime) {
		this.moveTime = moveTime;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}


}
