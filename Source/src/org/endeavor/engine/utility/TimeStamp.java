package org.endeavor.engine.utility;

import java.util.Calendar;

public class TimeStamp {
	private final int minute;
	private final int hour;
	private final int day;
	private final int year;

	public TimeStamp() {
		minute = Calendar.getInstance().get(12);
		hour = Calendar.getInstance().get(10);
		day = Calendar.getInstance().get(6);
		year = Calendar.getInstance().get(1);
	}

	public int getMinutesElapsed() {
		return Misc.getMinutesElapsed(minute, hour, day, year);
	}

	public int getHoursElapsed() {
		return Misc.getMinutesElapsed(minute, hour, day, year) / 60;
	}
}
