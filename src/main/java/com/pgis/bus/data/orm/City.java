package com.pgis.bus.data.orm;

import java.sql.SQLException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.orm.impl.StringValuesRepository;
import com.pgis.bus.net.models.city.CityModel;
import com.pgis.bus.net.models.geom.PointModel;

public class City extends ORMObject implements Cloneable {
	private static final Logger log = LoggerFactory.getLogger(City.class);

	private Integer id;
	private String key;
	private double lat;
	private double lon;
	private int scale;
	private boolean isShow;
	private int name_key;
	private HashMap<LangEnum, StringValue> name;

	public City() {
		super();
	}

	public City(IConnectionManager connManager) {
		super(connManager);
	}

	public String getName(LangEnum langID) throws SQLException {
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

	public HashMap<LangEnum, StringValue> getName() throws SQLException {
		if (name == null && super.connManager != null) {
			StringValuesRepository rep = null;
			try {
				rep = new StringValuesRepository(super.connManager);
				this.name = rep.getToHashMap(this.name_key);
			} finally {
				if (rep != null)
					rep.dispose();
			}

		}
		return name;
	}

	public void setName(HashMap<LangEnum, StringValue> name) {
		this.name = name;
	}

	public City clone() {
		City city = null;
		try {
			city = (City) super.clone();
			city.name = new HashMap<LangEnum, StringValue>(this.name);
			city.id = this.id;
			city.lat = this.lat;
			city.lon = this.lon;
			city.scale = this.scale;
			city.name_key = this.name_key;
			city.isShow = this.isShow;
			city.key = this.key;
		} catch (CloneNotSupportedException e) {
			log.error("Clone city error.", e);
		}
		return city;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", key=" + key + ", lat=" + lat + ", lon=" + lon + ", scale=" + scale + ", isShow="
				+ isShow + ", name_key=" + name_key + ", name=" + name + "]";
	}

	public static CityModel createModel(City city, LangEnum langID) throws SQLException {
		CityModel cityModel = new CityModel();
		cityModel.setId(city.getId());
		cityModel.setLocation(new PointModel(city.getLat(), city.getLon()));
		cityModel.setScale(city.getScale());
		cityModel.setKey(city.getKey());
		cityModel.setName(city.getName(langID));
		return cityModel;
	}

	public CityModel toModel(LangEnum langID) throws SQLException {
		return createModel(this, langID);
	}
}
