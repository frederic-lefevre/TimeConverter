/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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
	
	@Test
	void testMonths() {
		
		TimeUtils.getMonths(); 
	}
}
