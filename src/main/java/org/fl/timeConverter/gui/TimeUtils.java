package org.fl.timeConverter.gui;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import java.util.stream.Collectors;

public class TimeUtils {

	// Convert milliseconds since 1/1/1970 UTC in a date string in the zone ZoneId
	public static String convertTime(long milli, ZoneId zoneId, String datePattern) {
		return DateTimeFormatter.ofPattern(datePattern).format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(milli), zoneId));
	}
	
	public static Vector<ZoneId> getZoneIds() {
		return ZoneId.getAvailableZoneIds().stream()
					.sorted()
					.map((z) -> ZoneId.of(z))
					.collect(Collectors.toCollection(Vector::new)) ;
	}
}
