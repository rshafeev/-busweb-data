package com.pgis.bus.data.orm;

import java.sql.Time;

import org.postgresql.util.PGInterval;

import com.pgis.bus.data.helpers.PGIntervalHelper;

public class Timetable {
	private int id;
	private int schedule_group_id;
	private int time_A;
	private int time_B;
	private int frequency;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSchedule_group_id() {
		return schedule_group_id;
	}

	public void setSchedule_group_id(int schedule_group_id) {
		this.schedule_group_id = schedule_group_id;
	}

	public int getTime_A() {
		return time_A;
	}

	public void setTime_A(int time_A) {
		this.time_A = time_A;
	}

	public int getTime_B() {
		return time_B;
	}

	public Time getTimeAObj() {
		Time t = new Time(this.time_A * 1000);
		return t;
	}

	public void setTime_B(int time_B) {
		this.time_B = time_B;
	}

	public Time getTimeBObj() {
		Time t = new Time(this.time_B * 1000);
		return t;
	}

	public int getFrequancy() {
		return frequency;
	}

	public PGInterval getFrequancyObj() {

		return PGIntervalHelper.fromSeconds(this.frequency);
	}

	public void setFrequancy(int frequancy) {
		this.frequency = frequancy;
	}

	public void setTime_A(Time time_A) {
		this.time_A = (int) time_A.getTime() / 1000;

	}

	public void setTime_B(Time time_B) {
		this.time_B = (int) time_B.getTime() / 1000;
	}

	public void setFrequancy(PGInterval frequency) {
		this.frequency = PGIntervalHelper.toSeconds(frequency);

	}
}
