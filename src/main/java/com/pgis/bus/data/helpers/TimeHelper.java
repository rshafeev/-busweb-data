package com.pgis.bus.data.helpers;

import java.sql.Time;
import java.util.Calendar;

import org.postgresql.util.PGInterval;

public class TimeHelper {

	public static int toSeconds(Time time) {
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		int secs = c.get(Calendar.HOUR_OF_DAY) * 60 * 60 + c.get(Calendar.MINUTE) * 60 + c.get(Calendar.SECOND);
		return secs;
	}

	public static Time fromSeconds(long secs) {
		Calendar c = Calendar.getInstance();

		int h = (int) secs / 60 / 60;
		int m = (int) (secs - h * 60 * 60) / 60;
		int s = (int) secs - h * 60 * 60 - m * 60;
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), h, m, s);

		return new Time(c.getTime().getTime());
	}

	public static int toSeconds(int hours, int mins, int secs) {
		return hours * 60 * 60 + mins * 60 + secs;
	}
	
	public static Time clone (Time time)
	{
		if (time == null)
			return null;
		return fromSeconds(toSeconds(time));
	}
	
	


}
