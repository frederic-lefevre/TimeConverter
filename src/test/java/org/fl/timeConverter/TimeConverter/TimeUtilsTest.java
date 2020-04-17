package org.fl.timeConverter.TimeConverter;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import org.fl.timeConverter.TimeUtils;
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
	
	@Test
	void testZones() {
		
		Vector<ZoneId> zones = TimeUtils.getZoneIds() ;
		Set<String> zonesAsSet = ZoneId.getAvailableZoneIds() ;
		
		assertNotNull(zones) ;
		assertNotNull(zonesAsSet) ;
		assertEquals(zonesAsSet.size(), zones.size()) ;
		
		assertTrue(zones.containsAll(zonesAsSet.stream().map(z -> ZoneId.of(z)).collect(Collectors.toList()))) ;
		
		assertTrue(zonesAsSet.containsAll(zones.stream().map(z -> z.getId()).collect(Collectors.toList()))) ;
	}
}
