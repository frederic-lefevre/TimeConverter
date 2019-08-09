package org.fl.timeConverter.gui;

import java.awt.EventQueue;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;

import com.ibm.lge.fl.util.RunningContext;


public class TimeConverterGui  extends JFrame {
	
	private static final long serialVersionUID = 2368226038474247064L;
	
	private static final String DEFAULT_PROP_FILE = "file:///FredericPersonnel/PortableApps/TimeConverter/timeConverter.properties";

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

		try {
			
			RunningContext runningContext = new RunningContext("TimeConverter", null, new URI(propertiesUri));
			
		} catch (URISyntaxException e) {
			System.out.println("Exception caught in Main (see default prop file processing)") ;
			e.printStackTrace();
		}
	}

}
