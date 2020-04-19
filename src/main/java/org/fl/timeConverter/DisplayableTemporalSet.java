package org.fl.timeConverter;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.LinkedHashMap;
import java.util.Vector;

public class DisplayableTemporalSet extends LinkedHashMap<TemporalAccessor, DisplayableTemporal> {

	private static final long serialVersionUID = 1L;

	public Vector<DisplayableTemporal> getVector() {				
		return new Vector<DisplayableTemporal>(this.values()) ;
	}
	
	public void addElement(DateTimeFormatter formatter, TemporalAccessor time) {
		addElement(formatter, time, time);
	}
	
	public void addElement(DateTimeFormatter formatter, TemporalAccessor key, TemporalAccessor time) {
		put(key, new DisplayableTemporal(formatter, time)) ;
	}
}
