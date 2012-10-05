package com.pgis.bus.data.models;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

import org.postgis.Point;

import com.pgis.bus.data.orm.type.AlgStrategyEnum;
import com.pgis.bus.data.orm.type.DayEnum;

/**
 * Класс хранит параметры поиска мршрутов м/у точками назначения, которые
 * необходимо задать при выхове хранимой процедуры bus.shortest_ways()
 * 
 * @author romario
 */
public class FindWaysOptions {

	private int city_id;
	private Point p1;
	private Point p2;
	private DayEnum day_id;
	private int time_start_hours, time_start_minutes;
	private RouteTypeDiscount usage_routeTypes[];
	private AlgStrategyEnum alg_strategy;
	private String lang_id;
	private double maxDistance;

	public RouteTypeDiscount[] getUsage_routeTypes() {
		return usage_routeTypes;
	}

	public String[] getTransportTypeArray() {
		ArrayList<String> arr = new ArrayList<String>();
		for (RouteTypeDiscount r : usage_routeTypes) {
			arr.add(r.getRoute_type_id());
		}
		return arr.toArray(new String[arr.size()]);
	}

	public Double[] getDiscountArray() {
		ArrayList<Double> arr = new ArrayList<Double>();
		for (RouteTypeDiscount r : usage_routeTypes) {
			arr.add(r.getDiscount());
		}
		return arr.toArray(new Double[arr.size()]);
	}

	public void setUsage_routeTypes(RouteTypeDiscount[] usage_routeTypes) {
		this.usage_routeTypes = usage_routeTypes;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}

	public int getCity_id() {

		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	public DayEnum getDay_id() {
		return day_id;
	}

	public void setDay_id(DayEnum day_id) {
		this.day_id = day_id;
	}

	public Time getTime_start() {
		int milsecs = (this.time_start_hours * 60 * 60 + this.time_start_minutes * 60) * 1000;
		return new Time(milsecs);
	}

	public void setTime_start(int hours, int minutes) {
		this.time_start_hours = hours;
		this.time_start_minutes = minutes;

	}

	public AlgStrategyEnum getAlg_strategy() {
		return alg_strategy;
	}

	public void setAlg_strategy(AlgStrategyEnum alg_strategy) {
		this.alg_strategy = alg_strategy;
	}

	public String getLang_id() {
		return lang_id;
	}

	public void setLang_id(String lang_id) {
		this.lang_id = lang_id;
	}

	@Override
	public String toString() {
		return "FindWaysOptions [city_id=" + city_id + ", p1=" + p1 + ", p2="
				+ p2 + ", day_id=" + day_id + ", time_start_hours="
				+ time_start_hours + ", time_start_minutes="
				+ time_start_minutes + ", usage_routeTypes="
				+ Arrays.toString(usage_routeTypes) + ", alg_strategy="
				+ alg_strategy + ", lang_id=" + lang_id + ", maxDistance="
				+ maxDistance + "]";
	}
}
