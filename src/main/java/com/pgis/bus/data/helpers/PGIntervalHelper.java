package com.pgis.bus.data.helpers;

import org.postgresql.util.PGInterval;

import com.pgis.bus.net.models.TimeIntervalModel;

public class PGIntervalHelper {

	public static int toSeconds(PGInterval interval) {
		int secs = interval.getHours() * 60 * 60 + interval.getMinutes() * 60 + (int) interval.getSeconds();
		return secs;
	}

	public static PGInterval fromSeconds(int secs) {
		int h = secs / 60 / 60;
		int m = (secs - h * 60 * 60) / 60;
		int s = secs - h * 60 * 60 - m * 60;

		PGInterval interval = new PGInterval();
		interval.setHours(h);
		interval.setMinutes(m);
		interval.setSeconds(s);
		return interval;
	}

	public static PGInterval fromModel(TimeIntervalModel model) {
		if (model == null)
			return null;
		int secs = (int) model.getTime();
		int h = secs / 60 / 60;
		int m = (secs - h * 60 * 60) / 60;
		int s = secs - h * 60 * 60 - m * 60;

		PGInterval interval = new PGInterval();
		interval.setHours(h);
		interval.setMinutes(m);
		interval.setSeconds(s);
		return interval;
	}

	public static PGInterval clone(PGInterval interval) {
		if (interval == null)
			return null;
		return new PGInterval(interval.getYears(), interval.getMonths(), interval.getDays(), interval.getHours(),
				interval.getMinutes(), interval.getSeconds());

	}
}
