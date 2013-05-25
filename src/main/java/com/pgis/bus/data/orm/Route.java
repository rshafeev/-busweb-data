package com.pgis.bus.data.orm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.orm.impl.RoutesRepository;
import com.pgis.bus.data.repositories.orm.impl.StringValuesRepository;
import com.pgis.bus.net.models.route.RouteModel;

public class Route extends ORMObject implements Cloneable   {
	private static final Logger log = LoggerFactory.getLogger(Route.class);

	private Integer id;
	private int city_id;
	private double cost;
	private String route_type_id;
	private Integer number_key;
	private boolean visible;
	private Collection<StringValue> number; // key - language id, value -
	private RouteWay directRouteWay;
	private RouteWay reverseRouteWay;

	public Route() {
		super();
	}

	public Route(IConnectionManager connManager) {
		super(connManager);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
		if (directRouteWay != null) {
			directRouteWay.setRouteID(id);
		}
		if (reverseRouteWay != null) {
			reverseRouteWay.setRouteID(id);
		}
	}

	public Collection<StringValue> getNumber() throws SQLException {
		if (number == null && super.connManager != null) {
			StringValuesRepository rep = null;
			try {
				rep = new StringValuesRepository(super.connManager);
				this.number = rep.get(this.number_key);
			} finally {
				if (rep != null)
					rep.dispose();
			}
		}
		return number;
	}

	public String getNumber(LangEnum langID) throws SQLException {
		this.number = this.getNumber();
		for (StringValue s : this.number) {
			if (s.getLangID().equals(langID)) {
				return s.getValue();
			}
		}
		return null;
	}

	public StringValue getValNumber(LangEnum langID) throws SQLException {
		this.number = this.getNumber();
		for (StringValue s : this.number) {
			if (s.getLangID().equals(langID)) {
				return s;
			}
		}
		return null;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
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

	public Integer getNumberKey() {
		return number_key;
	}

	public void setNumberKey(Integer number_key) {
		this.number_key = number_key;
	}

	public void setNumber(Collection<StringValue> number) {
		this.number = number;
	}

	public RouteWay getDirectRouteWay() throws SQLException {
		if (this.directRouteWay == null && super.connManager != null) {
			RoutesRepository rep = null;
			try {
				rep = new RoutesRepository(super.connManager);
				this.directRouteWay = rep.getRouteWay(this.id, true);
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

	public RouteWay getReverseRouteWay() throws SQLException {
		if (this.reverseRouteWay == null && super.connManager != null) {
			RoutesRepository rep = null;
			try {
				rep = new RoutesRepository(super.connManager);
				this.reverseRouteWay = rep.getRouteWay(this.id, false);
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

	public static RouteModel createModel(Route route, LangEnum langID) throws SQLException {
		RouteModel model = new RouteModel();
		model.setCityID(route.getCityID());
		model.setNumber(route.getNumber(langID));
		model.setId(route.getId());
		model.setRouteTypeID(route.getRouteTypeID());
		model.setCost(route.getCost());
		model.setId(route.getId());
		return model;
	}

	public RouteModel toModel(LangEnum langID) throws SQLException {
		return createModel(this, langID);
	}

	@Override
	public String toString() {
		return "Route [id=" + id + ", city_id=" + city_id + ", cost=" + cost + ", route_type_id=" + route_type_id
				+ ", number_key=" + number_key + ", number=" + number + ", directRouteWay=" + directRouteWay
				+ ", reverseRouteWay=" + reverseRouteWay + "]";
	}

	/**
	 * Создает обратный путь из прямого пути
	 */
	public void makeReverseFromDirect() {
		try {
			RouteWay reverseWay = this.directRouteWay.clone();

			reverseWay.getRouteRelations();
		} catch (Exception e) {

		}

	}
	
	@Override
	public Route clone() throws CloneNotSupportedException {
		Route route = (Route) super.clone();
		Collection<StringValue> numb = new ArrayList<StringValue>();
		if (this.number != null)
		{
		for (StringValue n : this.number) {
			numb.add(n.clone());
		}
		}
		route.id = this.id;
		route.city_id = this.city_id;
		route.cost = this.cost;
		if (this.route_type_id != null)
		{
		route.route_type_id = new String(this.route_type_id);
		}
		route.number_key = this.number_key;
		route.number = numb;
		if(this.directRouteWay != null)
		{
		route.directRouteWay = this.directRouteWay.clone();
		}
		if(this.reverseRouteWay != null)
		{
		route.reverseRouteWay = this.reverseRouteWay.clone();
		}
		return route;
	}
	
}
