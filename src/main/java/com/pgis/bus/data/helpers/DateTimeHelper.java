package com.pgis.bus.data.helpers;

import java.sql.Time;
import java.util.Calendar;

import org.postgresql.util.PGInterval;

public class DateTimeHelper {

	public static int toSeconds(Time time) {
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		int secs = c.get(Calendar.HOUR_OF_DAY) * 60 * 60 + c.get(Calendar.MINUTE) * 60 + c.get(Calendar.SECOND);
		return secs;
	}

	public static int toSeconds(PGInterval interval) {
		if (interval == null)
			return 0;
		int secs = interval.getHours() * 60 * 60 + interval.getMinutes() * 60 + (int) interval.getSeconds();
		return secs;
	}

	public static int toSeconds(int hours, int mins, int secs) {
		return hours * 60 * 60 + mins * 60 + secs;
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

	public static Time getTimeFromSeconds(long secs) {
		Calendar c = Calendar.getInstance();

		int h = (int) secs / 60 / 60;
		int m = (int) (secs - h * 60 * 60) / 60;
		int s = (int) secs - h * 60 * 60 - m * 60;
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), h, m, s);

		return new Time(c.getTime().getTime());
	}
}
