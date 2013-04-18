package com.pgis.bus.data.models.factory;

import java.sql.Time;
import java.util.Calendar;

import org.postgresql.util.PGInterval;

import com.pgis.bus.net.models.TimeIntervalModel;

public class TimeIntervalModelFactory {

	public static TimeIntervalModel createModel(Time time) throws Exception {
		if (time == null)
			throw new Exception(
					"Can not create object of com.pgis.bus.net.models.DayTimeModel");
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		int secs = c.get(Calendar.HOUR_OF_DAY) * 60 * 60
				+ c.get(Calendar.MINUTE) * 60 + c.get(Calendar.SECOND);
		return new TimeIntervalModel(secs);
	}

	public static TimeIntervalModel createModel(PGInterval interval)
			throws Exception {
		if (interval == null)
			throw new Exception(
					"Can not create object of com.pgis.bus.net.models.DayTimeModel");
		int secs = interval.getHours() * 60 * 60 + interval.getMinutes() * 60
				+ (int) interval.getSeconds();
		return new TimeIntervalModel(secs);
	}

}
