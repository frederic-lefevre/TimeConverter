package org.fl.timeConverter.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;


public class TimeConverterGui  extends JFrame {
	
	private static final long serialVersionUID = 2368226038474247064L;
	
	private static final String DEFAULT_PROP_FILE = "file:///FredericPersonnel/PortableApps/TimeConverter/timeConverter.properties";

	public TimeConverterGui(String defaultPropFile) {
		// TODO Auto-generated constructor stub
	}

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
	
	
}
