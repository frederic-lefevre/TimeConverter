package org.fl.timeConverter;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DisplayableTemporal {

	private final DateTimeFormatter dateTimeFormatter ;
	private final TemporalAccessor temporalAccessor ;
	
	public DisplayableTemporal(DateTimeFormatter d, TemporalAccessor t) {
		dateTimeFormatter = d ;
		temporalAccessor  = t ;
	}
	
	@Override
	public String toString() {
		return dateTimeFormatter.format(temporalAccessor) ;
	}

	public TemporalAccessor getTemporalAccessor() {
		return temporalAccessor;
	}
}
