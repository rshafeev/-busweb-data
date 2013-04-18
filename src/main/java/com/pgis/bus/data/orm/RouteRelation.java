package com.pgis.bus.data.orm;

import org.postgis.LineString;
import org.postgresql.util.PGInterval;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.orm.IStationsRepository;
import com.pgis.bus.data.repositories.orm.impl.StationsRepository;

public class RouteRelation extends ORMObject {

	private int id;
	private int rway_id;
	private int station_a_id;
	private int station_b_id;
	private int position_index;
	private double distance;
	private PGInterval ev_time;
	private LineString geom;
	private Station stationA;
	private Station stationB;

	public RouteRelation() {
		super();
	}

	public RouteRelation(IDBConnectionManager connManager) {
		super(connManager);
	}

	public int getStationAId() {
		return station_a_id;
	}

	public void setStationAId(int station_a_id) {
		this.station_a_id = station_a_id;
	}

	public int getStationBId() {
		return station_b_id;
	}

	public void setStationBId(int station_b_id) {
		this.station_b_id = station_b_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRouteWayID() {
		return rway_id;
	}

	public void setRouteWayID(int rway_id) {
		this.rway_id = rway_id;
	}

	public Station getStationA() throws RepositoryException {
		if (stationA == null && super.connManager != null) {
			IStationsRepository rep = new StationsRepository(super.connManager);
			this.stationA = rep.get(station_a_id);
		}
		return stationA;
	}

	public Station getStationB() throws RepositoryException {
		if (stationB == null && super.connManager != null) {
			IStationsRepository rep = new StationsRepository(super.connManager);
			this.stationB = rep.get(station_b_id);
		}
		return stationB;
	}

	public void setStationA(Station stationA) {
		if (stationA != null && stationA.getId() != null)
			this.station_a_id = stationA.getId();
		this.stationA = stationA;
	}

	public void setStationB(Station stationB) {
		if (stationB != null && stationB.getId() != null)
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

	public LineString getGeom() {
		return geom;
	}

	public void setGeom(LineString geom) {
		this.geom = geom;
	}

	@Override
	public String toString() {
		return "RouteRelation [id=" + id + ", rway_id=" + rway_id + ", station_a_id=" + station_a_id
				+ ", station_b_id=" + station_b_id + ", position_index=" + position_index + ", distance=" + distance
				+ ", ev_time=" + ev_time + ", geom=" + geom + ", stationB=" + stationB + "]";
	}

}
