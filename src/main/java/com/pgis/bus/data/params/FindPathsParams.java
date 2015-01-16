package com.pgis.bus.data.params;

import java.sql.Time;
import java.util.ArrayList;

import com.pgis.bus.data.helpers.TimeHelper;
import com.pgis.bus.data.orm.type.AlgStrategyEnum;
import com.pgis.bus.data.orm.type.DayEnum;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.net.models.geom.PointModel;
import com.pgis.bus.net.request.FindPathsRequest;
import com.pgis.bus.net.request.data.RouteTypeDiscount;

public class FindPathsParams {

	private int cityID;
	private PointModel p1;
	private PointModel p2;
	private DayEnum dayID;
	private Time timeStart;
	private String[] route_types;
	private Double[] discounts;
	private boolean isTransitions;
	private AlgStrategyEnum algStrategy;
	private LangEnum langID;
	private double maxDistance;

	public FindPathsParams() {

	}

	public FindPathsParams(FindPathsRequest request) {
		this.cityID = request.getCityID();
		this.p1 = new PointModel(request.getP1().getLat(), request.getP1().getLon());
		this.p2 = new PointModel(request.getP2().getLat(), request.getP2().getLon());
		this.timeStart = TimeHelper.fromSeconds(request.getOutTime().getTimeStartSecs());
		this.route_types = getDbRouteTypes(request);
		this.discounts = getDbDiscounts(request);
		this.dayID = DayEnum.valueOf(request.getOutTime().getDayID());
		this.maxDistance = request.getMaxDistance();
		this.algStrategy = AlgStrategyEnum.valueOf(request.getAlgStrategy());
		this.isTransitions = request.isTransitions();
		this.langID = LangEnum.valueOf(request.getLangID());
	}

	public int getCityID() {
		return cityID;
	}

	public void setCityID(int cityID) {
		this.cityID = cityID;
	}

	public PointModel getP1() {
		return p1;
	}

	public void setP1(PointModel p1) {
		this.p1 = p1;
	}

	public PointModel getP2() {
		return p2;
	}

	public void setP2(PointModel p2) {
		this.p2 = p2;
	}

	public DayEnum getDayID() {
		return dayID;
	}

	public void setDayID(DayEnum dayID) {
		this.dayID = dayID;
	}

	public Time getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Time timeStart) {
		this.timeStart = timeStart;
	}

	public String[] getRouteTypes() {
		return route_types;
	}

	public void setRouteTypes(String[] route_types) {
		this.route_types = route_types;
	}

	public Double[] getDiscounts() {
		return discounts;
	}

	public void setDiscounts(Double[] discounts) {
		this.discounts = discounts;
	}

	public boolean isTransitions() {
		return isTransitions;
	}

	public void setTransitions(boolean isTransitions) {
		this.isTransitions = isTransitions;
	}

	public AlgStrategyEnum getAlgStrategy() {
		return algStrategy;
	}

	public void setAlgStrategy(AlgStrategyEnum algStrategy) {
		this.algStrategy = algStrategy;
	}

	public LangEnum getLangID() {
		return langID;
	}

	public void setLangID(LangEnum langID) {
		this.langID = langID;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}

	private static String[] getDbRouteTypes(FindPathsRequest request) {
		ArrayList<String> arr = new ArrayList<String>();
		for (RouteTypeDiscount r : request.getRouteTypes()) {
			arr.add("c_route_" + r.getId());
		}
		return arr.toArray(new String[arr.size()]);
	}

	private static Double[] getDbDiscounts(FindPathsRequest request) {
		ArrayList<Double> arr = new ArrayList<Double>();
		for (RouteTypeDiscount r : request.getRouteTypes()) {
			arr.add(r.getDiscount());
		}
		return arr.toArray(new Double[arr.size()]);
	}
}
