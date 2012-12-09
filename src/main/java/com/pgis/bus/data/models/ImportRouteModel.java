package com.pgis.bus.data.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.pgis.bus.data.orm.DirectRoute;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.RouteRelation;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.ScheduleGroup;
import com.pgis.bus.data.orm.ScheduleGroupDay;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.Timetable;
import com.pgis.bus.data.orm.type.DayEnum;

public class ImportRouteModel {

	private int cityID;

	private int routeID;

	private String routeType;

	private String number;

	/**
	 * in secs
	 */
	private int timeStart;

	/**
	 * in secs
	 */
	private int timeFinish;

	/**
	 * in secs
	 */
	private int intervalMin;
	private int intervalMax;

	private double cost;

	private Station directStations[];

	private GsonLineString directRelations[];

	private Station reverseStations[];

	private GsonLineString reverseRelations[];

	public int getCityID() {
		return cityID;
	}

	public void setCityID(int cityID) {
		this.cityID = cityID;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(int timeStart) {
		this.timeStart = timeStart;
	}

	public int getTimeFinish() {
		return timeFinish;
	}

	public void setTimeFinish(int timeFinish) {
		this.timeFinish = timeFinish;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public Station[] getDirectStations() {
		return directStations;
	}

	public void setDirectStations(Station[] directStations) {
		this.directStations = directStations;
	}

	public GsonLineString[] getDirectRelations() {
		return directRelations;
	}

	public void setDirectRelations(GsonLineString[] directRelations) {
		this.directRelations = directRelations;
	}

	public Station[] getReverseStations() {
		return reverseStations;
	}

	public void setReverseStations(Station[] reverseStations) {
		this.reverseStations = reverseStations;
	}

	public GsonLineString[] getReverseRelations() {
		return reverseRelations;
	}

	public void setReverseRelations(GsonLineString[] reverseRelations) {
		this.reverseRelations = reverseRelations;
	}

	public int getRouteID() {
		return routeID;
	}

	public void setRouteID(int routeID) {
		this.routeID = routeID;
	}

	public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;

	}

	public int getIntervalMin() {
		return intervalMin;
	}

	public void setIntervalMin(int intervalMin) {
		this.intervalMin = intervalMin;
	}

	public int getIntervalMax() {
		return intervalMax;
	}

	public void setIntervalMax(int intervalMax) {
		this.intervalMax = intervalMax;
	}

	public Route toRoute() {
		// create timetables
		Collection<Timetable> timeTables = new ArrayList<Timetable>();
		if (this.intervalMin != this.intervalMax) {
			int xS = this.timeStart;
			int xF = this.timeFinish;
			int x7 = 7 * 60 * 60;
			int x11 = 11 * 60 * 60;
			int x16 = 16 * 60 * 60;
			int x19 = 19 * 60 * 60;

			if (xS <= x7 && xF >= x19) {
				Timetable timetable1 = new Timetable();
				timetable1.setTime_A(xS);
				timetable1.setTime_B(x7);
				timetable1.setFrequancy(intervalMax);
				timeTables.add(timetable1);

				Timetable timetable2 = new Timetable();
				timetable2.setTime_A(x7);
				timetable2.setTime_B(x11);
				timetable2.setFrequancy(intervalMin);
				timeTables.add(timetable2);

				Timetable timetable3 = new Timetable();
				timetable3.setTime_A(x11);
				timetable3.setTime_B(x16);
				timetable3.setFrequancy(intervalMax);
				timeTables.add(timetable3);

				Timetable timetable4 = new Timetable();
				timetable4.setTime_A(x16);
				timetable4.setTime_B(x19);
				timetable4.setFrequancy(intervalMin);
				timeTables.add(timetable4);

				Timetable timetable5 = new Timetable();
				timetable5.setTime_A(x19);
				timetable5.setTime_B(xF);
				timetable5.setFrequancy(intervalMax);
				timeTables.add(timetable5);

			} else if (xF - xS > 6 * 60 * 60) {
				// Если время работы больше 6-ти часов, тогда разбиваем
				// на 4-е интервала
				Timetable timetable1 = new Timetable();
				timetable1.setTime_A(xS);
				timetable1.setTime_B(xS + 3 * 60 * 60);
				timetable1.setFrequancy(intervalMin);
				timeTables.add(timetable1);

				Timetable timetable2 = new Timetable();
				timetable2.setTime_A(xS + 3 * 60 * 60);
				timetable2.setTime_B(xF - 3 * 60 * 60);
				timetable2.setFrequancy(intervalMax);
				timeTables.add(timetable2);

				Timetable timetable3 = new Timetable();
				timetable3.setTime_A(xF - 3 * 60 * 60);
				timetable3.setTime_B(xF - 1 * 60 * 60);
				timetable3.setFrequancy(intervalMin);
				timeTables.add(timetable3);

				Timetable timetable4 = new Timetable();
				timetable4.setTime_A(xF - 1 * 60 * 60);
				timetable4.setTime_B(xF);
				timetable4.setFrequancy(intervalMax);
				timeTables.add(timetable4);
			} else {
				Timetable timetable1 = new Timetable();
				timetable1.setTime_A(xS);
				timetable1.setTime_B((xS + xF) / 2);
				timetable1.setFrequancy(intervalMin);
				timeTables.add(timetable1);

				Timetable timetable2 = new Timetable();
				timetable2.setTime_A((xS + xF) / 2);
				timetable2.setTime_B(xF);
				timetable2.setFrequancy(intervalMax);
				timeTables.add(timetable2);
			}

		} else {
			Timetable timetable1 = new Timetable();
			timetable1.setTime_A(this.timeStart);
			timetable1.setTime_B(this.timeFinish);
			timetable1.setFrequancy(intervalMin);
			timeTables.add(timetable1);

		}

		// create schedule
		Schedule schedule = new Schedule();
		ScheduleGroup scheduleGroup = new ScheduleGroup();
		scheduleGroup.setDays(Arrays
				.asList(new ScheduleGroupDay[] { new ScheduleGroupDay(
						DayEnum.c_all) }));
		scheduleGroup.setTimetables(timeTables);
		schedule.setScheduleGroups(Arrays
				.asList(new ScheduleGroup[] { scheduleGroup }));

		// direct
		DirectRoute directRouteWay = new DirectRoute();
		directRouteWay.setDirect(true);
		directRouteWay.setRoute_id(-1);
		directRouteWay.setSchedule(schedule);
		Collection<RouteRelation> directRouteRelations = new ArrayList<RouteRelation>();
		for (int i = 0; i < this.directStations.length; i++) {
			RouteRelation relation = new RouteRelation();
			Station stA = null, stB = null;
			GsonLineString geom = null;
			if (i > 0) {
				stA = this.directStations[i - 1];
				geom = this.directRelations[i - 1];
				relation.setStation_a_id(stA.getId());
				relation.setGeom(geom);
			}
			stB = this.directStations[i];
			relation.setPosition_index(i);
			relation.setStation_b_id(stB.getId());
			relation.setStationB(stB);
			directRouteRelations.add(relation);
		}
		directRouteWay.setRoute_relations(directRouteRelations);
		// reverse
		DirectRoute reverseRouteWay = new DirectRoute();
		reverseRouteWay.setDirect(false);
		reverseRouteWay.setRoute_id(-1);
		reverseRouteWay.setSchedule(schedule);
		Collection<RouteRelation> reverseRouteRelations = new ArrayList<RouteRelation>();
		for (int i = 0; i < this.reverseStations.length; i++) {
			RouteRelation relation = new RouteRelation();
			Station stA = null, stB = null;
			GsonLineString geom = null;
			if (i > 0) {
				stA = this.reverseStations[i - 1];
				geom = this.reverseRelations[i - 1];
				relation.setStation_a_id(stA.getId());
				relation.setGeom(geom);
			}
			stB = this.reverseStations[i];
			relation.setPosition_index(i);
			relation.setStation_b_id(stB.getId());
			relation.setStationB(stB);

			reverseRouteRelations.add(relation);
		}
		reverseRouteWay.setRoute_relations(reverseRouteRelations);
		// create route
		Route r = new Route();
		r.setCity_id(this.cityID);
		r.setCost(this.cost);
		r.setName(null);
		r.setNumber(this.number);
		r.setRoute_type_id(this.routeType);
		r.setDirectRouteWay(directRouteWay);
		r.setReverseRouteWay(reverseRouteWay);
		return r;
	}

	public void init() {
		if (this.directStations != null) {
			for (int i = 0; i < this.directStations.length; i++) {
				Station s = this.directStations[i];
				s.getLocation().setSrid(4326);
			}
		}
		if (this.reverseStations != null) {
			for (int i = 0; i < this.reverseStations.length; i++) {
				Station s = this.reverseStations[i];
				s.getLocation().setSrid(4326);
			}
		}

	}

	/**
	 * Валидация объекта
	 * 
	 * @return Если данные правильны, то функция возвращает true
	 */
	public boolean isValid() {

		if (this.routeType == null || this.routeType.length() == 0)
			return false;
		if (this.number == null || this.number.length() == 0)
			return false;
		if (this.intervalMin > this.intervalMax || this.intervalMin <= 0)
			return false;
		if (this.timeStart > this.timeFinish || this.timeStart < 0) {
			return false;
		}

		if (this.directRelations == null || this.directRelations.length == 0)
			return false;
		if (this.directStations == null || this.directStations.length == 0)
			return false;
		if (this.directStations.length != this.directRelations.length + 1) {
			return false;
		}

		if (this.reverseRelations == null || this.reverseRelations.length == 0)
			return false;
		if (this.reverseStations == null || this.reverseStations.length == 0)
			return false;
		if (this.reverseStations.length != this.reverseRelations.length + 1) {
			return false;
		}
		return true;
	}
}
