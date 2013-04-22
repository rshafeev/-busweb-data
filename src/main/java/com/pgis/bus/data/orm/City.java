package com.pgis.bus.data.orm;

import java.util.HashMap;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.repositories.RepositoryException;
import com.pgis.bus.data.repositories.orm.IStringValuesRepository;
import com.pgis.bus.data.repositories.orm.impl.StringValuesRepository;

public class City extends ORMObject implements Cloneable {
	private Integer id;
	private String key;
	private double lat;
	private double lon;
	private int scale;
	private boolean isShow;
	private int name_key;
	private HashMap<String, StringValue> name;

	public City() {
		super();
	}

	public City(IConnectionManager connManager) {
		super(connManager);
	}

	public String getNameByLang(String langID) throws RepositoryException {
		if (this.name == null) {
			this.name = getName();
		}
		StringValue nameValue = this.name.get(langID);
		if (nameValue == null)
			return null;
		return nameValue.getValue();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public int getNameKey() {
		return name_key;
	}

	public void setNameKey(int name_key) {
		this.name_key = name_key;
		if (name != null) {
			for (StringValue v : name.values()) {
				v.setKeyID(this.name_key);
			}
		}
	}

	public HashMap<String, StringValue> getName() throws RepositoryException {
		if (name == null && super.connManager != null) {
			IStringValuesRepository rep = null;
			try {
				rep = new StringValuesRepository(super.connManager);
				this.name = rep.getToHashMap(this.name_key);
			} catch (Exception e) {
			} finally {
				if (rep != null)
					rep.dispose();
			}

		}
		System.out.println(name.size());
		return name;
	}

	public void setName(HashMap<String, StringValue> name) {
		this.name = name;
	}

	public City clone() {
		try {
			City city;
			city = (City) super.clone();
			city.name = new HashMap<String, StringValue>(this.name);
			city.id = this.id;
			city.lat = this.lat;
			city.lon = this.lon;
			city.scale = this.scale;
			city.name_key = this.name_key;
			city.isShow = this.isShow;
			city.key = this.key;
			return city;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
