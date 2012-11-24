package com.pgis.bus.data.orm;

import java.sql.Time;
import org.postgresql.util.PGInterval;
import com.pgis.bus.data.helpers.DateTimeHelper;

public class Timetable {
	private int id;
	private int schedule_group_id;

	/**
	 * Стартовое время, сек
	 */
	private int time_A;

	/**
	 * Конечное время, сек
	 */
	private int time_B;

	/**
	 * Интервал между выездами передвижных средств, сек
	 */
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
		return DateTimeHelper.getTimeFromSeconds(this.time_A);
	}

	public void setTime_B(int time_B) {
		this.time_B = time_B;
	}

	public Time getTimeBObj() {
		
		return DateTimeHelper.getTimeFromSeconds(this.time_B);
	}

	public int getFrequancy() {
		return frequency;
	}

	public PGInterval getFrequancyObj() {

		return DateTimeHelper.getIntervalFromSeconds(this.frequency);
	}

	public void setFrequancy(int frequancy) {
		this.frequency = frequancy;
	}

	public void setTime_A(Time time_A) {
		this.time_A = DateTimeHelper.toSeconds(time_A);
	}

	public void setTime_B(Time time_B) {
		this.time_B = DateTimeHelper.toSeconds(time_B);
	}

	public void setFrequancy(PGInterval frequency) {
		this.frequency = DateTimeHelper.toSeconds(frequency);

	}
}
