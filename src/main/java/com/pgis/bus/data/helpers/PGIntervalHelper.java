package com.pgis.bus.data.helpers;

import org.postgresql.util.PGInterval;

public class PGIntervalHelper {

	public static int toSeconds(PGInterval interval) {
		int secs = interval.getHours() * 60 * 60 + interval.getMinutes() * 60
				+ (int) interval.getSeconds();
		return 0;
	}

	public static PGInterval fromSeconds(int secs) {
		PGInterval interval = new PGInterval();
		interval.setHours(secs / (60 * 60));
		interval.setMinutes(secs / (60) - 60 * secs / (60 * 60));
		interval.setSeconds((int) (secs - secs / (60)));
		return interval;
	}
}
