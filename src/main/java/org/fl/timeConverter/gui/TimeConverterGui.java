package org.fl.timeConverter.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fl.util.RunningContext;

public class TimeConverterGui  extends JFrame {
	
	private static final long serialVersionUID = 2368226038474247064L;
	
	private static final String DEFAULT_PROP_FILE = "timeConverter.properties";

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
	
	public final static String DATE_PATTERN = "EEEE dd MMMM uuuu  HH:mm:ss.SSS" ;
	
	private JLabel timeField ;
	
	private final Logger logger ;
	
	public TimeConverterGui(String propertiesUri) {

		RunningContext runningContext = new RunningContext("TimeConverter", null, propertiesUri);
		logger = runningContext.getpLog() ;
		
		if (runningContext != null) {
			
			setBounds(50, 50, 1500, 1000);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setTitle("Time converter") ;
			getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
			
			MillisecondsAndZonePanel milliAndZonePane = new MillisecondsAndZonePanel(logger) ;
			add(milliAndZonePane) ;
			
			JPanel datePane = new JPanel() ;
			datePane.setLayout(new BoxLayout(datePane, BoxLayout.X_AXIS));
			timeField = new JLabel() ;
			timeField.setPreferredSize(new Dimension(600, 30)) ;	
			timeField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			datePane.add(new JLabel(" correspond à : ")) ;
			datePane.add(timeField) ;
			add (datePane) ;
			
			DateTimePanel dateTimePanel = new DateTimePanel(logger) ;			
			add(dateTimePanel) ;
			
			setFontForAll(this, new Font("Verdana", Font.BOLD, 16));
			
			milliAndZonePane.addActionListeners(dateTimePanel, timeField) ;
			dateTimePanel.addActionListeners(milliAndZonePane, timeField) ;
			
			pack() ;
			
			// init with current time
			milliAndZonePane.setMillisecondsField(System.currentTimeMillis());			
			milliAndZonePane.upDateTimeField();
		}
	}
	
	private static void setFontForAll(Component component, Font font) 	{
	    component.setFont(font);
	    if (component instanceof Container) {
	        for (Component child : ((Container)component).getComponents()) {
	        	setFontForAll(child, font);
	        }
	    }
	}

}
