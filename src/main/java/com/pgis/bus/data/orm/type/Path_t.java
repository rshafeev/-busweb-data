package com.pgis.bus.data.orm.type;

import org.postgresql.util.PGInterval;

import com.pgis.bus.data.helpers.PGIntervalHelper;

public class Path_t implements Cloneable {

	public int path_id; // is not null
	public int index; // is not null
	public Integer direct_route_id; // is not null
	public String route_type; // is not null
	public String route_name;
	public Integer relation_index_a; // is not null
	public Integer relation_index_b; // is not null
	public String station_name_a;
	public String station_name_b;
	public PGInterval move_time;
	public PGInterval wait_time;
	public PGInterval frequency;
	public double cost = 0.0;
	public double distance;

	@Override
	public String toString() {
		return "Path_t [path_id=" + path_id + ", index=" + index
				+ ", direct_route_id=" + direct_route_id + ", route_type="
				+ route_type + ", route_name=" + route_name
				+ ", relation_index_a=" + relation_index_a
				+ ", relation_index_b=" + relation_index_b
				+ ", station_name_a=" + station_name_a + ", station_name_b="
				+ station_name_b + ", move_time=" + move_time + ", wait_time="
				+ wait_time + ", frequency=" + frequency + ", cost=" + cost
				+ ", distance=" + distance + "]";
	}

	public Path_t clone() throws CloneNotSupportedException {
		Path_t clone = (Path_t) super.clone();
		clone.cost = cost;
		clone.direct_route_id = direct_route_id;
		clone.distance = distance;
		clone.index = index;
		clone.path_id = path_id;
		clone.relation_index_a = this.relation_index_a;
		clone.relation_index_b = this.relation_index_b;
		if(station_name_a!=null)
		clone.station_name_a = new String(station_name_a);
		if(station_name_b!=null)
		clone.station_name_b = new String(station_name_b);
		if(route_type!=null)
		clone.route_type = new String(route_type);
		if(route_name!=null)
		clone.route_name = new String(route_name);
		
		clone.move_time = PGIntervalHelper.clone(this.move_time);
		clone.wait_time = PGIntervalHelper.clone(this.wait_time);
		clone.frequency = PGIntervalHelper.clone(this.frequency);

		return clone;
	}

}
