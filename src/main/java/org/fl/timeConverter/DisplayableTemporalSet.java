package org.fl.timeConverter;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Vector;

public class DisplayableTemporalSet extends HashMap<TemporalAccessor, DisplayableTemporal> {

	private static final long serialVersionUID = 1L;

	public Vector<DisplayableTemporal> getVector() {		
		return new Vector<DisplayableTemporal>(this.values()) ;
	}
	
	public void addElement(DateTimeFormatter formatter, TemporalAccessor time) {
		put(time, new DisplayableTemporal(formatter, time)) ;
	}
}
