package com.pgis.bus.data.models.route.serialized.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.pgis.bus.data.helpers.GeoObjectsHelper;
import com.pgis.bus.data.models.route.serialized.SerializedRouteObjectModel;
import com.pgis.bus.data.orm.Route;
import com.pgis.bus.data.orm.RouteRelation;
import com.pgis.bus.data.orm.RouteWay;
import com.pgis.bus.data.orm.Schedule;
import com.pgis.bus.data.orm.ScheduleGroup;
import com.pgis.bus.data.orm.ScheduleGroupDay;
import com.pgis.bus.data.orm.Station;
import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.data.orm.Timetable;
import com.pgis.bus.data.orm.type.DayEnum;
import com.pgis.bus.data.orm.type.LangEnum;

public class JsonRouteObjectModel implements SerializedRouteObjectModel, Serializable {

	private static final long serialVersionUID = 1L;

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

	private Schedule makeSchedule() {
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
				timetable1.setTimeA(xS);
				timetable1.setTimeB(x7);
				timetable1.setFrequency(intervalMax);
				timeTables.add(timetable1);

				Timetable timetable2 = new Timetable();
				timetable2.setTimeA(x7);
				timetable2.setTimeB(x11);
				timetable2.setFrequency(intervalMin);
				timeTables.add(timetable2);

				Timetable timetable3 = new Timetable();
				timetable3.setTimeA(x11);
				timetable3.setTimeB(x16);
				timetable3.setFrequency(intervalMax);
				timeTables.add(timetable3);

				Timetable timetable4 = new Timetable();
				timetable4.setTimeA(x16);
				timetable4.setTimeB(x19);
				timetable4.setFrequency(intervalMin);
				timeTables.add(timetable4);

				Timetable timetable5 = new Timetable();
				timetable5.setTimeA(x19);
				timetable5.setTimeB(xF);
				timetable5.setFrequency(intervalMax);
				timeTables.add(timetable5);

			} else if (xF - xS > 6 * 60 * 60) {
				// Если время работы больше 6-ти часов, тогда разбиваем
				// на 4-е интервала
				Timetable timetable1 = new Timetable();
				timetable1.setTimeA(xS);
				timetable1.setTimeB(xS + 3 * 60 * 60);
				timetable1.setFrequency(intervalMin);
				timeTables.add(timetable1);

				Timetable timetable2 = new Timetable();
				timetable2.setTimeA(xS + 3 * 60 * 60);
				timetable2.setTimeB(xF - 3 * 60 * 60);
				timetable2.setFrequency(intervalMax);
				timeTables.add(timetable2);

				Timetable timetable3 = new Timetable();
				timetable3.setTimeA(xF - 3 * 60 * 60);
				timetable3.setTimeB(xF - 1 * 60 * 60);
				timetable3.setFrequency(intervalMin);
				timeTables.add(timetable3);

				Timetable timetable4 = new Timetable();
				timetable4.setTimeA(xF - 1 * 60 * 60);
				timetable4.setTimeB(xF);
				timetable4.setFrequency(intervalMax);
				timeTables.add(timetable4);
			} else {
				Timetable timetable1 = new Timetable();
				timetable1.setTimeA(xS);
				timetable1.setTimeB((xS + xF) / 2);
				timetable1.setFrequency(intervalMin);
				timeTables.add(timetable1);

				Timetable timetable2 = new Timetable();
				timetable2.setTimeA((xS + xF) / 2);
				timetable2.setTimeB(xF);
				timetable2.setFrequency(intervalMax);
				timeTables.add(timetable2);
			}

		} else {
			Timetable timetable1 = new Timetable();
			timetable1.setTimeA(this.timeStart);
			timetable1.setTimeB(this.timeFinish);
			timetable1.setFrequency(intervalMin);
			timeTables.add(timetable1);

		}

		// create schedule
		Schedule schedule = new Schedule();
		ScheduleGroup scheduleGroup = new ScheduleGroup();
		scheduleGroup.setDays(Arrays.asList(new ScheduleGroupDay[] { new ScheduleGroupDay(DayEnum.c_all) }));
		scheduleGroup.setTimetables(timeTables);
		schedule.setScheduleGroups(Arrays.asList(new ScheduleGroup[] { scheduleGroup }));
		return schedule;
	}

	@Override
	public Route toORMObject() {
		Schedule schedule = makeSchedule();
		// direct
		RouteWay directRouteWay = new RouteWay();
		directRouteWay.setDirect(true);
		directRouteWay.setRouteID(-1);
		directRouteWay.setSchedule(schedule);
		Collection<RouteRelation> directRouteRelations = new ArrayList<RouteRelation>();
		for (int i = 0; i < this.directStations.length; i++) {
			RouteRelation relation = new RouteRelation();
			Station stA = null, stB = null;
			GsonLineString geom = null;
			if (i > 0) {
				stA = this.directStations[i - 1];
				geom = this.directRelations[i - 1];
				if (stA.getId() != null)
					relation.setStationAId(stA.getId());
				relation.setGeom(geom.toLineString());
			}
			stB = this.directStations[i];
			relation.setPositionIndex(i);
			if (stB.getId() != null)
				relation.setStationBId(stB.getId());
			relation.setStationB(stB);
			directRouteRelations.add(relation);
		}
		directRouteWay.setRouteRelations(directRouteRelations);

		// reverse
		RouteWay reverseRouteWay = new RouteWay();
		reverseRouteWay.setDirect(false);
		reverseRouteWay.setRouteID(-1);
		reverseRouteWay.setSchedule(schedule);
		Collection<RouteRelation> reverseRouteRelations = new ArrayList<RouteRelation>();
		for (int i = 0; i < this.reverseStations.length; i++) {
			RouteRelation relation = new RouteRelation();
			Station stA = null, stB = null;
			GsonLineString geom = null;
			if (i > 0) {
				stA = this.reverseStations[i - 1];
				geom = this.reverseRelations[i - 1];
				if (stA.getId() != null)
					relation.setStationAId(stA.getId());
				relation.setGeom(geom.toLineString());
			}
			stB = this.reverseStations[i];
			relation.setPositionIndex(i);
			if (stB.getId() != null)
				relation.setStationBId(stB.getId());
			relation.setStationB(stB);

			reverseRouteRelations.add(relation);
		}
		reverseRouteWay.setRouteRelations(reverseRouteRelations);
		// create route
		Route r = new Route();
		r.setCityID(this.cityID);
		r.setCost(this.cost);

		Collection<StringValue> numb = new ArrayList<StringValue>();
		numb.add(new StringValue(LangEnum.c_ru, this.number));
		numb.add(new StringValue(LangEnum.c_en, this.number));
		numb.add(new StringValue(LangEnum.c_uk, this.number));

		r.setNumber(numb);
		r.setRouteTypeID(this.routeType);
		r.setDirectRouteWay(directRouteWay);
		r.setReverseRouteWay(reverseRouteWay);
		return r;
	}

	public void init() {
		if (this.directStations != null) {
			for (int i = 0; i < this.directStations.length; i++) {
				Station s = this.directStations[i];
				s.getLocation().setSrid(GeoObjectsHelper.GEOMETRY_SRID);
			}
		}
		if (this.reverseStations != null) {
			for (int i = 0; i < this.reverseStations.length; i++) {
				Station s = this.reverseStations[i];
				s.getLocation().setSrid(GeoObjectsHelper.GEOMETRY_SRID);
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

	public long getSerialVersionUID() {
		return serialVersionUID;
	}
}
