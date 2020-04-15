package org.fl.timeConverter.TimeConverter;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;

import org.fl.timeConverter.gui.TimeUtils;
import org.junit.jupiter.api.Test;

class TimeUtilsTest {

	private final static String datePattern = "EEEE dd MMMM uuuu  HH:mm:ss.SSS VV" ;
	
	@Test
	void test() {
		
		String zeroTime = "jeudi 01 janvier 1970  01:00:00.000 Europe/Paris" ;
		String beginTimeAsString = TimeUtils.convertTime(0, ZoneId.of(ZoneId.SHORT_IDS.get("ECT")), datePattern) ;
		
		assertEquals(zeroTime, beginTimeAsString) ;

	}

}
