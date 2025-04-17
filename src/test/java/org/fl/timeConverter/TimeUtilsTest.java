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

import static org.assertj.core.api.Assertions.*;

import java.time.DateTimeException;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class TimeUtilsTest {

	private static final String DATE_PATTERN = "EEEE dd MMMM uuuu  HH:mm:ss.SSS VV";
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN).localizedBy(Locale.FRENCH);

	@Test
	void testParis() {

		String zeroTime = "jeudi 01 janvier 1970  01:00:00.000 Europe/Paris";
		String beginTimeAsString = TimeUtils.convertTime(0, ZoneId.of(ZoneId.SHORT_IDS.get("ECT")), dateTimeFormatter);

		assertThat(beginTimeAsString).isEqualTo(zeroTime);

	}

	@Test
	void testUTC() {

		String zeroTime = "jeudi 01 janvier 1970  00:00:00.000 UTC";
		String beginTimeAsString = TimeUtils.convertTime(0, ZoneId.of("UTC"), dateTimeFormatter);

		assertThat(beginTimeAsString).isEqualTo(zeroTime);

	}
	
	@Test
	void testUTC2() {

		String someTime = "mercredi 15 avril 2020  07:47:06.673 UTC";
		String beginTimeAsString = TimeUtils.convertTime(1586936826673L, ZoneId.of("UTC"), dateTimeFormatter);

		assertThat(beginTimeAsString).isEqualTo(someTime);

	}

	@Test
	void testZones() {

		Vector<ZoneId> zones = TimeUtils.getZoneIds();
		Set<String> zonesAsSet = ZoneId.getAvailableZoneIds();

		assertThat(zonesAsSet)
			.isNotNull()
			.hasSameSizeAs(zones)
			.allSatisfy(z -> zones.contains(ZoneId.of(z)));
		
		assertThat(zones).isNotNull()
			.allSatisfy(zone -> zonesAsSet.contains(zone.getId()));
	}

	@Test
	void testMonths() {

		DisplayableTemporalSet monthsMap = TimeUtils.getMonths();
		
		Month[] months = Month.values();
		
		assertThat(monthsMap).isNotNull()
			.hasSize(months.length)
			.containsKeys(months);
		
	}
	
	@Test
	void testGetAllDayOfMonth() {
		
		Stream.of(Month.values()).forEach(month -> {
			
			ZonedDateTime someTimeInThisMonth = ZonedDateTime.of(2020, month.getValue(), 1, 1, 1, 1, 0, ZoneId.of("UTC"));
			DisplayableTemporalSet dayOfMonthMap = TimeUtils.getAllDaysOfMonth(someTimeInThisMonth);
			
			assertThat(dayOfMonthMap).isNotNull()
				.hasSize(YearMonth.from(someTimeInThisMonth).lengthOfMonth());
		}
		);
	}
	
	@Test
	void testGuessZonedDateTimeOfInvalidMonth() {		
		assertThatExceptionOfType(DateTimeException.class).isThrownBy(() -> TimeUtils.guessZonedDateTimeOf(0, 13, 1, 1, 1, 1, 0, ZoneId.of("UTC")));
	}
	
	@Test
	void testGuessZonedDateTimeOfInvalidDay() {		
		assertThatExceptionOfType(DateTimeException.class).isThrownBy(() -> TimeUtils.guessZonedDateTimeOf(0, 1, 32, 1, 1, 1, 0, ZoneId.of("UTC")));
	}
	
	@Test
	void testGuessZonedDateTimeOfInvalidHour() {		
		assertThatExceptionOfType(DateTimeException.class).isThrownBy(() -> TimeUtils.guessZonedDateTimeOf(0, 1, 1, 25, 1, 1, 0, ZoneId.of("UTC")));
	}
	
	@Test
	void testGuessZonedDateTimeOfInvalidMinute() {		
		assertThatExceptionOfType(DateTimeException.class).isThrownBy(() -> TimeUtils.guessZonedDateTimeOf(0, 1, 1, 1, 61, 1, 0, ZoneId.of("UTC")));
	}
	
	@Test
	void testGuessZonedDateTimeOfInvalidSecond() {		
		assertThatExceptionOfType(DateTimeException.class).isThrownBy(() -> TimeUtils.guessZonedDateTimeOf(0, 1, 1, 1, 1, 61, 0, ZoneId.of("UTC")));
	}
	
	@Test
	void testGuessZonedDateTimeOf() {
		assertThat(TimeUtils.guessZonedDateTimeOf(2020, 1, 1, 1, 1, 1, 0, ZoneId.of("UTC")))
			.isInThePast().isEqualTo(ZonedDateTime.of(2020, 1, 1, 1, 1, 1, 0, ZoneId.of("UTC")));

	}
}
