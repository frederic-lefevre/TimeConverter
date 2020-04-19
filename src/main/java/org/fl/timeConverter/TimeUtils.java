package org.fl.timeConverter;

import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Vector;
import java.util.stream.Collectors;

public class TimeUtils {

	// Convert milliseconds since 1/1/1970 UTC in a date string in the zone ZoneId
	public static String convertTime(long milli, ZoneId zoneId, String datePattern) {
		return DateTimeFormatter.ofPattern(datePattern).localizedBy(Locale.FRENCH).format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(milli), zoneId));
	}
	
	// Get all ZoneId in a vector
	public static Vector<ZoneId> getZoneIds() {
		return ZoneId.getAvailableZoneIds().stream()
					.sorted()
					.map((z) -> ZoneId.of(z))
					.collect(Collectors.toCollection(Vector::new)) ;
	}
	
	// Get all months in a vector
	public static DisplayableTemporalSet getMonths() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM").localizedBy(Locale.FRENCH) ;
		DisplayableTemporalSet monthsSet = new DisplayableTemporalSet() ;
		Month[] months = Month.values() ;
		for (Month month : months) {			
			monthsSet.put(month, new DisplayableTemporal(formatter, month)) ;
		}
		return monthsSet ;
	}
}
