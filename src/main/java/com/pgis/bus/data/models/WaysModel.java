package com.pgis.bus.data.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.pgis.bus.data.orm.WayElem;

public class WaysModel {
	private Collection<WayModel> ways;
    
	/**
	 * Время поиска маршрутов, сек
	 */
	private double findTime;
	
	public WaysModel() {
		ways = new ArrayList<WayModel>();
	}

	public WaysModel(Collection<WayElem> elems) {
		Iterator<WayElem> i = elems.iterator();
		HashMap<Integer, List<WayElem>> arr = new HashMap<Integer, List<WayElem>>();
		while (i.hasNext()) {
			WayElem elem = i.next();
			if (arr.get(elem.path_id) == null) {
				List<WayElem> pathWays = new ArrayList<WayElem>();
				pathWays.add(elem);
				arr.put(elem.path_id, pathWays);
			} else {
				Collection<WayElem> pathWays = arr.get(elem.path_id);
				pathWays.add(elem);
			}
		}

		ways = new ArrayList<WayModel>();
		Collection<List<WayElem>> arrs = arr.values();
		Iterator<List<WayElem>> it = arrs.iterator();
		while (it.hasNext()) {
			ways.add(new WayModel(it.next()));
		}

	}

	
	public double getFindTimeSecs() {
		return findTime;
	}

	public void setFindTime(long findTime) {
		this.findTime = findTime/1000.0;
	}

	public Collection<WayModel> getWays() {
		return ways;
	}

	public void setWays(Collection<WayModel> ways) {
		this.ways = ways;
	}

	public void addWay(WayModel way) {
		this.ways.add(way);

	}

	public String toString() {
		String out = "";
		Iterator<WayModel> i = ways.iterator();
		Integer ind = 1;
		while (i.hasNext()) {
			out += "Way [ " + ind + " ]:\n";
			out += i.next().toString();
			out += "===============================\n";
			ind += 1;
		}
		return out;
	}
}
