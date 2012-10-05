package com.pgis.bus.data.orm;

import org.postgresql.util.PGInterval;

public class WayElem implements Comparable<WayElem> {
	public int path_id; // is not null
	public int index; // is not null
	public Integer direct_route_id; // is not null
	public String route_type; // is not null
	public Integer relation_index; // is not null
	public String route_name;
	public String station_name;
	public PGInterval move_time;
	public PGInterval wait_time;
	public double cost;
	public double distance;

	public String toString() {
		String s = "";
		s = Integer.toString(path_id) + " ";
		s += Integer.toString(index) + " ";
		s += Integer.toString(direct_route_id) + " ";
		s += route_type + " ";
		s += Integer.toString(relation_index) + " ";
		s += route_name + " ";
		s += station_name + " ";

		if (move_time != null)
			s += move_time.getMinutes() + ":" + move_time.getSeconds() + " ";
		if (wait_time != null)
			s += wait_time.getMinutes() + ":" + wait_time.getSeconds() + " ";
		s += cost + " ";
		s += distance + " ";
		return s;

	}

	@Override
	public int compareTo(WayElem o) {
		return this.index - o.index;
	}
}
