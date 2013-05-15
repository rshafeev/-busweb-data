package com.pgis.bus.data.orm;

import java.sql.SQLException;
import java.util.Collection;

import org.postgis.Point;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.helpers.GeoObjectsHelper;
import com.pgis.bus.data.models.factory.geom.PointModelFactory;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.orm.impl.StringValuesRepository;
import com.pgis.bus.net.models.station.StationModel;

public class Station extends ORMObject implements Cloneable {
	private Integer id;
	private Integer city_id;
	private Point location;
	private Integer name_key;
	private Collection<StringValue> name; // key - language id, value -

	public Station() {
		super();
	}

	public Station(IConnectionManager connManager) {
		super(connManager);
	}

	public Station(Station s) {
		super();
		this.copy(s);
	}

	public Integer getCityID() {
		return city_id;
	}

	public void setCityID(Integer city_id) {
		this.city_id = city_id;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public void setLocation(double lat, double lon) {
		location = GeoObjectsHelper.createPoint(lat, lon);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNameKey() {
		return name_key;
	}

	public void setNameKey(Integer name_key) {
		this.name_key = name_key;
		if (this.name != null) {
			for (StringValue v : this.name) {
				v.setKeyID(name_key);
			}
		}
	}

	public Collection<StringValue> getName() throws SQLException {
		if (name == null && super.connManager != null) {
			StringValuesRepository rep = null;
			try {
				rep = new StringValuesRepository(super.connManager);
				this.name = rep.get(this.name_key);
			} finally {
				if (rep != null)
					rep.dispose();
			}
		}
		return this.name;
	}

	public void setName(Collection<StringValue> name) {
		this.name = name;
	}

	public String getName(LangEnum langID) throws SQLException {
		Collection<StringValue> name = this.getName();
		for (StringValue v : name) {
			if (v.getLangID().equals(langID) == true)
				return v.getValue();
		}
		return null;
	}

	public StringValue getValName(LangEnum langID) throws SQLException {
		Collection<StringValue> name = this.getName();
		for (StringValue v : name) {
			if (v.getLangID().equals(langID) == true)
				return v;
		}
		return null;
	}

	public void copy(Station from) {
		super.copy(from);
		this.name_key = from.name_key;
		this.city_id = from.city_id;

		if (this.id != null)
			this.id = new Integer(from.id);

		if (from.name != null)
			this.name = from.name;

		if (from.location != null) {
			this.location = GeoObjectsHelper.createPoint(from.location.x, from.location.y);
		}
	}

	public Station clone() {

		try {
			Station obj = (Station) super.clone();
			obj.copy(this);
			return obj;
		} catch (CloneNotSupportedException e) {
			return null;
		}

	}

	@Override
	public String toString() {
		return "Station [id=" + id + ", city_id=" + city_id + ", location=" + location + ", name_key=" + name_key
				+ ", names=" + name + "]";
	}

	public static StationModel createModel(Station st, LangEnum langID) throws SQLException {
		StationModel model = new StationModel();
		model.setId(st.id);
		model.setLocation(PointModelFactory.createModel(st.location));
		model.setName(st.getName(langID));
		return model;
	}

	public StationModel toModel(LangEnum langID) throws SQLException {
		return createModel(this, langID);

	}

}
