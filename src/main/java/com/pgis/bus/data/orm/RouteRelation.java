package com.pgis.bus.data.orm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.postgis.LineString;
import org.postgresql.util.PGInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.helpers.GeoObjectsHelper;
import com.pgis.bus.data.helpers.PGIntervalHelper;
import com.pgis.bus.data.models.factory.TimeIntervalModelFactory;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.orm.type.LineStringEx;
import com.pgis.bus.data.repositories.orm.impl.StationsRepository;
import com.pgis.bus.net.models.route.RouteRelationModel;

public class RouteRelation extends ORMObject implements Cloneable {
	private static final Logger log = LoggerFactory
			.getLogger(RouteRelation.class);

	private int id;
	private int rway_id;
	private int station_a_id;
	private int station_b_id;
	private int position_index;
	private double distance;
	private PGInterval ev_time;
	private LineStringEx geom;
	private Station stationA;
	private Station stationB;

	public RouteRelation() {
		super();
	}

	public RouteRelation(IConnectionManager connManager) {
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

	public Station getStationA() throws SQLException {
		if (stationA == null && super.connManager != null && station_a_id > 0) {
			StationsRepository rep = null;
			try {
				rep = new StationsRepository(super.connManager);
				this.stationA = rep.get(station_a_id);
			} finally {
				if (rep != null)
					rep.dispose();
			}
		}
		return stationA;
	}

	public Station getStationB() throws SQLException {
		if (stationB == null && super.connManager != null && station_b_id > 0) {
			StationsRepository rep = null;
			try {
				rep = new StationsRepository(super.connManager);
				this.stationB = rep.get(station_b_id);
			} finally {
				if (rep != null)
					rep.dispose();
			}
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

	public int getPositionIndex() {
		return position_index;
	}

	public void setPositionIndex(int position_index) {
		this.position_index = position_index;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public PGInterval getMoveTime() {
		return ev_time;
	}

	public void setMoveTime(PGInterval ev_time) {
		this.ev_time = ev_time;
	}

	public LineStringEx getGeom() {
		return geom;
	}

	public void setGeom(LineStringEx geom) {
		this.geom = geom;
	}

	@Override
	public String toString() {
		return "RouteRelation [id=" + id + ", rway_id=" + rway_id
				+ ", station_a_id=" + station_a_id + ", station_b_id="
				+ station_b_id + ", position_index=" + position_index
				+ ", distance=" + distance + ", ev_time=" + ev_time + ", geom="
				+ geom + ", stationB=" + stationB + "]";
	}

	static public RouteRelationModel createModel(RouteRelation r,
			LangEnum langID) throws SQLException {
		RouteRelationModel model = new RouteRelationModel();
		model.setDistance(r.getDistance());
		model.setId(r.getId());
		model.setMoveTime(TimeIntervalModelFactory.createModel(r.getMoveTime()));
		model.setCurrStation(r.getStationB().toModel(langID));
		return model;
	}

	static public Collection<RouteRelationModel> createModels(
			Collection<RouteRelation> arr, LangEnum langID) throws SQLException {
		Collection<RouteRelationModel> models = new ArrayList<RouteRelationModel>();
		for (RouteRelation r : arr) {
			models.add(createModel(r, langID));
		}
		return models;
	}

	RouteRelationModel toModel(LangEnum langID) throws Exception {
		return createModel(this, langID);
	}

	@Override
	public RouteRelation clone() throws CloneNotSupportedException {
		RouteRelation relation = (RouteRelation) super.clone();
		relation.id = this.id;
		relation.rway_id = this.rway_id;
		relation.station_a_id = this.station_a_id;
		relation.station_b_id = this.station_b_id;
		relation.position_index = this.position_index;
		relation.distance = this.distance;
		if (this.ev_time != null) {
			relation.ev_time = PGIntervalHelper.clone(this.ev_time);
		}
		if (this.geom != null) {
			relation.geom = GeoObjectsHelper.clone(this.geom);
		}
		if (this.stationA != null || this.stationB != null) {
			relation.stationA = this.stationA.clone();
			relation.stationB = this.stationB.clone();
		}
		return relation;
	}
}
