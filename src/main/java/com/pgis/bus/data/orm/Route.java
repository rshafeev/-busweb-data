package com.pgis.bus.data.orm;

import java.util.Collection;

public class Route {
	private int id;
	private int city_id;
	private double cost;
	private String route_type_id;
	private String number;
	private int name_key;

	private Collection<StringValue> name; // key - language id, value -
	private DirectRoute directRouteWay;
	private DirectRoute reverseRouteWay;

	// Упрощенная информация о расписании для маршрута
	private Timetable simpleTimeTable;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		if (directRouteWay != null) {
			directRouteWay.setRoute_id(id);
		}
		if (reverseRouteWay != null) {
			reverseRouteWay.setRoute_id(id);
		}
	}

	public int getName_key() {
		return name_key;
	}

	public void setName_key(int name_key) {
		this.name_key = name_key;
		if (this.name != null) {
			for (StringValue s : this.name) {
				s.key_id = this.name_key;
			}

		}
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getRoute_type_id() {
		return route_type_id;
	}

	public void setRoute_type_id(String route_type_id) {
		this.route_type_id = route_type_id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Collection<StringValue> getName() {
		return name;
	}

	public void setName(Collection<StringValue> name) {
		this.name = name;
	}

	public DirectRoute getDirectRouteWay() {
		return directRouteWay;
	}

	public void setDirectRouteWay(DirectRoute directRouteWay) {
		this.directRouteWay = directRouteWay;
	}

	public DirectRoute getReverseRouteWay() {
		return reverseRouteWay;
	}

	public void setReverseRouteWay(DirectRoute reverseRouteWay) {
		this.reverseRouteWay = reverseRouteWay;
	}

	@Override
	public String toString() {
		return "Route [id=" + id + ", city_id=" + city_id + ", cost=" + cost
				+ ", route_type_id=" + route_type_id + ", number=" + number
				+ ", name_key=" + name_key + ", name=" + name
				+ ", directRouteWay=" + directRouteWay + ", reverseRouteWay="
				+ reverseRouteWay + "]";
	}

	public void updateIDs() {
		if (this.directRouteWay != null)
			this.directRouteWay.updateIDs();
		if (this.reverseRouteWay != null)
			this.reverseRouteWay.updateIDs();

	}

	public String getFullName(String lang_id) {
		String fullName = this.number;
		if (this.name != null) {
			for (StringValue v : this.name) {
				if (v.lang_id.equals(lang_id)) {
					if(v.value!=null)
						fullName += v.value;
					break;
				}
			}
		}
		return fullName;

	}
	
	public boolean isValid(){
		if(this.directRouteWay == null|| this.directRouteWay.isValid()==false)
			return false;
		if(this.reverseRouteWay == null || this.reverseRouteWay.isValid()== false)
			return false;
		return true;
	}
}
