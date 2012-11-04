package com.pgis.bus.data.models;

import org.postgis.LineString;

import com.pgis.bus.data.orm.Station;

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
	private int intervalMin;
	private int intervalMax;
	



	private double cost;

	private Station directStations[];

	private GsonLineString directRelations[];

	private Station reverseStations[];

	private GsonLineString reverseRelations[];

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



	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public Station[] getDirectStations() {
		return directStations;
	}

	public void setDirectStations(Station[] directStations) {
		this.directStations = directStations;
	}



	public GsonLineString[] getDirectRelations() {
		return directRelations;
	}

	public void setDirectRelations(GsonLineString[] directRelations) {
		this.directRelations = directRelations;
	}

	public Station[] getReverseStations() {
		return reverseStations;
	}

	public void setReverseStations(Station[] reverseStations) {
		this.reverseStations = reverseStations;
	}

	public GsonLineString[] getReverseRelations() {
		return reverseRelations;
	}

	public void setReverseRelations(GsonLineString[] reverseRelations) {
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
	public int getIntervalMin() {
		return intervalMin;
	}

	public void setIntervalMin(int intervalMin) {
		this.intervalMin = intervalMin;
	}

	public int getIntervalMax() {
		return intervalMax;
	}

	public void setIntervalMax(int intervalMax) {
		this.intervalMax = intervalMax;
	}
}
