package com.pgis.bus.data.orm;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

import org.postgresql.util.PGInterval;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.helpers.PGIntervalHelper;
import com.pgis.bus.data.helpers.TimeHelper;
import com.pgis.bus.net.models.route.schedule.TimetableModel;

public class Timetable extends ORMObject {
	private int id;
	private int schedule_group_id;

	/**
	 * Стартовое время, сек
	 */
	private Time timeA;

	/**
	 * Конечное время, сек
	 */
	private Time timeB;

	/**
	 * Интервал между выездами передвижных средств, сек
	 */
	private PGInterval frequency;

	public Timetable() {
		super();
	}

	public Timetable(IConnectionManager connManager) {
		super(connManager);
	}

	public Timetable(TimetableModel model, int scheduleGroupID) {
		super();
		this.setScheduleGroupID(scheduleGroupID);
		this.setId(-1);
		this.setTimeA(TimeHelper.fromSeconds(model.getTimeA()));
		this.setTimeB(TimeHelper.fromSeconds(model.getTimeB()));
		this.setFrequency(PGIntervalHelper.fromSeconds(model.getFreq()));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getScheduleGroupID() {
		return schedule_group_id;
	}

	public void setScheduleGroupID(int scheduleGroupID) {
		this.schedule_group_id = scheduleGroupID;
	}

	public Time getTimeA() {
		return timeA;
	}

	public void setTimeA(int secsA) {
		this.timeA = TimeHelper.fromSeconds(secsA);
	}

	public void setTimeA(Time timeA) {
		this.timeA = timeA;
	}

	public Time getTimeB() {
		return timeB;
	}

	public void setTimeB(Time timeB) {
		this.timeB = timeB;
	}

	public void setTimeB(int secsB) {
		this.timeA = TimeHelper.fromSeconds(secsB);
	}

	public PGInterval getFrequency() {
		return frequency;
	}

	public void setFrequency(PGInterval frequency) {
		this.frequency = frequency;
	}

	public void setFrequency(int frequencySecs) {
		this.frequency = PGIntervalHelper.fromSeconds(frequencySecs);
	}

	public static TimetableModel createModel(Timetable timetable) {
		if (timetable == null)
			return null;
		TimetableModel model = new TimetableModel();
		model.setTimeA(TimeHelper.toSeconds(timetable.getTimeA()));
		model.setTimeB(TimeHelper.toSeconds(timetable.getTimeB()));
		model.setFreq(PGIntervalHelper.toSeconds(timetable.getFrequency()));
		return model;
	}

	public static Collection<TimetableModel> createModels(Collection<Timetable> timetable) {
		if (timetable == null)
			return null;
		Collection<TimetableModel> models = new ArrayList<TimetableModel>();
		for (Timetable t : timetable) {
			models.add(createModel(t));
		}
		return models;
	}

	public TimetableModel toModel() {
		return createModel(this);
	}
}
