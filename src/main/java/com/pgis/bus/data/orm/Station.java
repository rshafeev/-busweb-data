package com.pgis.bus.data.orm;

import java.util.Collection;

import org.postgis.Point;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.models.factory.geom.PointModelFactory;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.orm.IStringValuesRepository;
import com.pgis.bus.data.repositories.orm.impl.StringValuesRepository;
import com.pgis.bus.net.models.geom.PointModel;

public class Station extends ORMObject implements Cloneable {
	private Integer id;
	private int city_id;
	private PointModel location;
	private int name_key;
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

	public int getCityID() {
		return city_id;
	}

	public void setCityID(int city_id) {
		this.city_id = city_id;
	}

	public PointModel getLocation() {
		return location;
	}

	// name

	// NodeTransports
	public void setLocation(Point location) {
		this.location = PointModelFactory.createModel(location);
	}

	public void setLocation(PointModel location) {
		this.location = location;
	}

	public void setLocation(double lat, double lon) {
		location = new PointModel(lat, lon);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getNameKey() {
		return name_key;
	}

	public void setNameKey(int name_key) {
		this.name_key = name_key;
		if (this.name != null) {
			for (StringValue v : this.name) {
				v.setKeyID(name_key);
			}
		}
	}

	public Collection<StringValue> getName() throws RepositoryException {
		if (name == null && super.connManager != null) {
			IStringValuesRepository rep = null;
			try {
				rep = new StringValuesRepository(super.connManager);
				this.name = rep.get(this.name_key);
			} catch (Exception e) {
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

	public StringValue getNameByLang(String langID) throws RepositoryException {
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
			this.location = new PointModel(from.location);
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

}
