package com.pgis.bus.data.models;

import org.postgis.LineString;

public class ImportRouteModel {

	private int cityID;

	private int routeID;

	private String routeType;

	private String number;

	/**
	 * in secs
	 */
	private int timeStart;

	/**
	 * in secs
	 */
	private int timeFinish;

	/**
	 * in secs
	 */
	private int interval;

	private double cost;

	private StationModel directStations[];

	private LineString directRelations[];

	private StationModel reverseStations[];

	private LineString reverseRelations[];

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(int timeStart) {
		this.timeStart = timeStart;
	}

	public int getTimeFinish() {
		return timeFinish;
	}

	public void setTimeFinish(int timeFinish) {
		this.timeFinish = timeFinish;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public StationModel[] getDirectStations() {
		return directStations;
	}

	public void setDirectStations(StationModel[] directStations) {
		this.directStations = directStations;
	}



	public LineString[] getDirectRelations() {
		return directRelations;
	}

	public void setDirectRelations(LineString[] directRelations) {
		this.directRelations = directRelations;
	}

	public StationModel[] getReverseStations() {
		return reverseStations;
	}

	public void setReverseStations(StationModel[] reverseStations) {
		this.reverseStations = reverseStations;
	}

	public LineString[] getReverseRelations() {
		return reverseRelations;
	}

	public void setReverseRelations(LineString[] reverseRelations) {
		this.reverseRelations = reverseRelations;
	}

	public int getRouteID() {
		return routeID;
	}

	public void setRouteID(int routeID) {
		this.routeID = routeID;
	}

	public int getCityID() {
		return cityID;
	}

	public void setCityID(int cityID) {
		this.cityID = cityID;
	}

	public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;

	}

}
