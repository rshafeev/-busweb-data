package com.pgis.bus.data.helpers;

import java.sql.Time;
import java.util.Calendar;

import org.postgresql.util.PGInterval;

public class DateTimeHelper {

	public static int toSeconds(Time time) {
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		int secs = c.get(Calendar.HOUR_OF_DAY) * 60 * 60
				+ c.get(Calendar.MINUTE) * 60 + c.get(Calendar.SECOND);
		return secs;
	}

	public static int toSeconds(PGInterval interval) {
		int secs = interval.getHours() * 60 * 60 + interval.getMinutes() * 60
				+ (int) interval.getSeconds();
		return secs;
	}

	public static PGInterval getIntervalFromSeconds(int secs) {
		int h = secs / 60 / 60;
		int m = (secs - h * 60 * 60) / 60;
		int s = secs - h * 60 * 60 - m * 60;

		PGInterval interval = new PGInterval();
		interval.setHours(h);
		interval.setMinutes(m);
		interval.setSeconds(s);
		return interval;
	}

	@SuppressWarnings("deprecation")
	public static Time getTimeFromSeconds(int secs) {
		int h = secs / 60 / 60;
		int m = (secs - h * 60 * 60) / 60;
		int s = secs - h * 60 * 60 - m * 60;
		Time t = new Time(h, m, s);
		return t;
	}
}
