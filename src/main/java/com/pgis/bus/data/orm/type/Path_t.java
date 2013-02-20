package com.pgis.bus.data.orm.type;

import org.postgresql.util.PGInterval;

public class Path_t {

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

	   
}
