package com.pgis.bus.data.models;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

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
	private Time time_start;
	private Collection<RouteTypeDiscount> usage_routeTypes;

	public Collection<RouteTypeDiscount> getUsage_routeTypes() {
		return usage_routeTypes;
	}
    public String[] getTransportTypeArray(){
    	ArrayList<String> arr = new ArrayList<String>();
    	for(RouteTypeDiscount r : usage_routeTypes){
    		arr.add(r.getRoute_type_id());
    	}
    	return arr.toArray(new String[arr.size()]);
    }
    public Double[] getDiscountArray(){
    	ArrayList<Double> arr = new ArrayList<Double>();
    	for(RouteTypeDiscount r : usage_routeTypes){
    		arr.add(r.getDiscount());
    	}
    	return arr.toArray(new Double[arr.size()]);
    }
	public void setUsage_routeTypes(
			Collection<RouteTypeDiscount> usage_routeTypes) {
		this.usage_routeTypes = usage_routeTypes;
	}

	private AlgStrategyEnum alg_strategy;
	private String lang_id;
	private double maxDistance;

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
		return time_start;
	}

	public void setTime_start(Time time_start) {
		this.time_start = time_start;
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

}
