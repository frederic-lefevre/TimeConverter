/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.fl.timeConverter;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.Month;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TimeUtils {

	private static final Logger log = Logger.getLogger(TimeUtils.class.getName());
	
	// Convert milliseconds since 1/1/1970 UTC in a date string in the zone ZoneId
	public static String convertTime(long milli, ZoneId zoneId, DateTimeFormatter formatter) {
		return formatter.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(milli), zoneId));
	}
	
	// Get all ZoneId in a vector
	public static Vector<ZoneId> getZoneIds() {
		return ZoneId.getAvailableZoneIds().stream()
				.sorted()
				.map((z) -> ZoneId.of(z))
				.collect(Collectors.toCollection(Vector::new));
	}

	// Get all months
	private static final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM").localizedBy(Locale.FRENCH);
	public static DisplayableTemporalSet getMonths() {

		DisplayableTemporalSet monthsSet = new DisplayableTemporalSet();
		Month[] months = Month.values();
		for (Month month : months) {
			monthsSet.addElement(monthFormatter, month);
		}
		return monthsSet;
	}
	
	// Get all days in the month of a date
	private static final DateTimeFormatter dayOfMonthFormatter = DateTimeFormatter.ofPattern("EEEE dd").localizedBy(Locale.FRENCH);
	public static DisplayableTemporalSet getAllDaysOfMonth(ZonedDateTime time) {

		DisplayableTemporalSet daysSet = new DisplayableTemporalSet();

		int lastDay = YearMonth.from(time).lengthOfMonth();
		ZonedDateTime zoneDateTimeForTheDay;
		for (int day = 1; day <= lastDay; day++) {
			zoneDateTimeForTheDay = time.with(ChronoField.DAY_OF_MONTH, day);
			daysSet.addElement(dayOfMonthFormatter, MonthDay.from(zoneDateTimeForTheDay), zoneDateTimeForTheDay);
		}
		return daysSet;
	}

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/uuuu HH:mm:ss.n VV")
			.withResolverStyle(ResolverStyle.SMART);
	
	public static ZonedDateTime guessZonedDateTimeOf(int year, int month, int day, int hour, int minute, int second, int nano, ZoneId zone) {

		try {
			return ZonedDateTime.of(year, month, day, hour, minute, second, nano, zone);
		} catch (DateTimeException e) {

			try {
				return ZonedDateTime.parse(buildDate(year, month, day, hour, minute, second, nano, zone), dateTimeFormatter);
			} catch (DateTimeException e2) {
				// Re-throw original exception for better message
				throw e;
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "Excepion converting date " + buildDate(year, month, day, hour, minute, second, nano, zone), e);
			return null;
		}
	}
	
	private static StringBuilder buildDate(int year, int month, int day, int hour, int minute, int second, int nano,
			ZoneId zone) {
		StringBuilder dateString = new StringBuilder();
		dateString.append(day).append("/").append(month).append("/").append(year).append(" ");
		dateString.append(hour).append(":").append(minute).append(":").append(second).append(".").append(nano);
		dateString.append(" ").append(zone);
		return dateString;
	}
}
