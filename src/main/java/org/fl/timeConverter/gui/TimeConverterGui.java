package org.fl.timeConverter.gui;

import java.awt.EventQueue;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.ibm.lge.fl.util.RunningContext;


public class TimeConverterGui  extends JFrame {
	
	private static final long serialVersionUID = 2368226038474247064L;
	
	private static final String DEFAULT_PROP_FILE = "timeConverter.properties";

	private final static String datePattern = "uuuu-MM-dd HH:mm:ss.SSS VV" ;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TimeConverterGui window = new TimeConverterGui(DEFAULT_PROP_FILE);
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public TimeConverterGui(String propertiesUri) {

		RunningContext runningContext = null ;
		runningContext = new RunningContext("TimeConverter", null, propertiesUri);
		
		if (runningContext != null) {
			
			Logger log = runningContext.getpLog() ;
			String formattedDate = DateTimeFormatter.ofPattern(datePattern).format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault()));
			log.info(formattedDate);
		}
	}

}
