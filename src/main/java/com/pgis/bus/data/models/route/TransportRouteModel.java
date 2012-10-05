package com.pgis.bus.data.models.route;

import org.postgresql.util.PGInterval;

import com.pgis.bus.data.orm.WayElem;

public class TransportRouteModel extends RouteModel {
	private int directRouteID;
	private String routeName;
	private PGInterval interval;
	private double cost;
	/**
	 * Станция, с которой начинается движение
	 */
	private String stationStart;

	/**
	 * Станция, где движние заканчивается
	 */
	private String stationFinish;
	
	/**
	 * индекс станции A
	 */
	private int indexA;

	/**
	 * индекс станции A
	 */
	private int indexB;

	public int getDirectRouteID() {
		return directRouteID;
	}

	public void setDirectRouteID(int directRouteID) {
		this.directRouteID = directRouteID;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public PGInterval getInterval() {
		return interval;
	}

	public void setInterval(PGInterval interval) {
		this.interval = interval;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public int getIndexA() {
		return indexA;
	}

	public void setIndexA(int indexA) {
		this.indexA = indexA;
	}

	public int getIndexB() {
		return indexB;
	}

	public void setIndexB(int indexB) {
		this.indexB = indexB;
	}

	public String getStationStart() {
		return stationStart;
	}

	public void setStationStart(String stationStart) {
		this.stationStart = stationStart;
	}

	public String getStationFinish() {
		return stationFinish;
	}

	public void setStationFinish(String stationFinish) {
		this.stationFinish = stationFinish;
	}
	
	public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}

	public String toString() {
		String out = "";
		out += "  Name     : " + this.routeName + "\n";
		if (this.interval != null)
			out += "  Interval : " + this.interval.toString() + "\n";
		out += "  Distance : " + this.getDistance() + " meters \n";
		out += "  Time     : " + this.getMoveTime() + " \n";
		out += "  Cost     : " + this.cost + "\n";
		out += "  StationA : " + this.stationStart + "\n";
		out += "  StationB : " + this.stationFinish + "\n";
		out += "  indexA   : " + this.indexA + "\n";
		out += "  indexB   : " + this.indexB + "\n";
		return out;

	}
}
