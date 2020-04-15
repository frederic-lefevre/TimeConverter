package org.fl.timeConverter.TimeConverter;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;

import org.fl.timeConverter.gui.TimeUtils;
import org.junit.jupiter.api.Test;

class TimeUtilsTest {

	private final static String datePattern = "EEEE dd MMMM uuuu  HH:mm:ss.SSS VV" ;
	
	@Test
	void testParis() {
		
		String zeroTime = "jeudi 01 janvier 1970  01:00:00.000 Europe/Paris" ;
		String beginTimeAsString = TimeUtils.convertTime(0, ZoneId.of(ZoneId.SHORT_IDS.get("ECT")), datePattern) ;
		
		assertEquals(zeroTime, beginTimeAsString) ;

	}

	@Test
	void testUTC() {
		
		String someTime = "mercredi 15 avril 2020  07:47:06.673 UTC" ;
		String beginTimeAsString = TimeUtils.convertTime(1586936826673L, ZoneId.of("UTC"), datePattern) ;
		
		assertEquals(someTime, beginTimeAsString) ;

	}
}
