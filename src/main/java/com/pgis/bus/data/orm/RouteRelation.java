package com.pgis.bus.data.orm;

import org.postgresql.util.PGInterval;

import com.pgis.bus.data.models.GsonLineString;

public class RouteRelation {

	private int id;
	private int direct_route_id;
	private int station_a_id;
	private int station_b_id;
	private int position_index;
	private double distance;
	private PGInterval ev_time;
	private GsonLineString geom;

	private Station stationB;

	public int getStation_a_id() {
		return station_a_id;
	}

	public void setStation_a_id(int station_a_id) {
		this.station_a_id = station_a_id;
	}

	public int getStation_b_id() {
		return station_b_id;
	}

	public void setStation_b_id(int station_b_id) {
		this.station_b_id = station_b_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDirect_route_id() {
		return direct_route_id;
	}

	public void setDirect_route_id(int direct_route_id) {
		this.direct_route_id = direct_route_id;
	}

	public Station getStationB() {
		return stationB;
	}

	public void setStationB(Station stationB) {
		if (stationB != null)
			this.station_b_id = stationB.getId();
		this.stationB = stationB;
	}

	public int getPosition_index() {
		return position_index;
	}

	public void setPosition_index(int position_index) {
		this.position_index = position_index;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public PGInterval getEv_time() {
		return ev_time;
	}

	public void setEv_time(PGInterval ev_time) {
		this.ev_time = ev_time;
	}

	public GsonLineString getGeom() {
		return geom;
	}

	public void setGeom(GsonLineString geom) {
		this.geom = geom;
	}

	@Override
	public String toString() {
		return "RouteRelation [id=" + id + ", direct_route_id="
				+ direct_route_id + ", station_a_id=" + station_a_id
				+ ", station_b_id=" + station_b_id + ", position_index="
				+ position_index + ", distance=" + distance + ", ev_time="
				+ ev_time + ", geom=" + geom + ", stationB=" + stationB + "]";
	}

}
