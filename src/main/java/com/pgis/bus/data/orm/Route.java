package com.pgis.bus.data.orm;

import java.util.Collection;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.orm.IRoutesRepository;
import com.pgis.bus.data.repositories.orm.IStringValuesRepository;
import com.pgis.bus.data.repositories.orm.impl.RoutesRepository;
import com.pgis.bus.data.repositories.orm.impl.StringValuesRepository;

public class Route extends ORMObject {
	private int id;
	private int city_id;
	private double cost;
	private String route_type_id;
	private int number_key;

	private Collection<StringValue> number; // key - language id, value -
	private RouteWay directRouteWay;
	private RouteWay reverseRouteWay;

	public Route() {
		super();
	}

	public Route(IConnectionManager connManager) {
		super(connManager);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		if (directRouteWay != null) {
			directRouteWay.setRouteID(id);
		}
		if (reverseRouteWay != null) {
			reverseRouteWay.setRouteID(id);
		}
	}

	public Collection<StringValue> getNumber() throws RepositoryException {
		if (number == null && super.connManager != null) {
			IStringValuesRepository rep = null;
			try {
				rep = new StringValuesRepository(super.connManager);
				this.number = rep.get(this.number_key);
			} catch (Exception e) {
			} finally {
				if (rep != null)
					rep.dispose();
			}
		}
		return number;
	}

	public String getNumber(String langID) throws RepositoryException {
		this.number = this.getNumber();
		for (StringValue s : this.number) {
			if (s.getLangID().equals(langID) == true) {
				return s.getValue();
			}
		}
		return "";
	}

	public int getCityID() {
		return city_id;
	}

	public void setCityID(int cityID) {
		this.city_id = cityID;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getRouteTypeID() {
		return route_type_id;
	}

	public void setRouteTypeID(String route_type_id) {
		this.route_type_id = route_type_id;
	}

	public int getNumberKey() {
		return number_key;
	}

	public void setNumberKey(int number_key) {
		this.number_key = number_key;
	}

	public void setNumber(Collection<StringValue> number) {
		this.number = number;
	}

	public RouteWay getDirectRouteWay() throws RepositoryException {
		if (this.directRouteWay == null && super.connManager != null) {
			IRoutesRepository rep = null;
			try {
				rep = new RoutesRepository(super.connManager);
				this.directRouteWay = rep.getRouteWay(this.id, false);
			} catch (Exception e) {
			} finally {
				if (rep != null)
					rep.dispose();
			}
		}
		return this.directRouteWay;
	}

	public void setDirectRouteWay(RouteWay directRouteWay) {
		this.directRouteWay = directRouteWay;
	}

	public RouteWay getReverseRouteWay() throws RepositoryException {
		if (this.reverseRouteWay == null && super.connManager != null) {
			IRoutesRepository rep = null;
			try {
				rep = new RoutesRepository(super.connManager);
				this.reverseRouteWay = rep.getRouteWay(this.id, false);
			} catch (Exception e) {
			} finally {
				if (rep != null)
					rep.dispose();
			}
		}
		return this.reverseRouteWay;
	}

	public void setReverseRouteWay(RouteWay reverseRouteWay) {
		this.reverseRouteWay = reverseRouteWay;
	}

}
