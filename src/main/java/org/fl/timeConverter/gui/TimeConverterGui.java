package org.fl.timeConverter.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.lge.fl.util.RunningContext;

public class TimeConverterGui  extends JFrame {
	
	private static final long serialVersionUID = 2368226038474247064L;
	
	private static final String DEFAULT_PROP_FILE = "timeConverter.properties";

	private final static String datePattern = "EEEE dd MMMM uuuu  HH:mm:ss.SSS VV" ;
	
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
	
	private JButton pStart ;
	private JTextField millisField ;
	private JLabel timeField ;
	private JComboBox<ZoneId> zoneIdsField ;
	private Vector<ZoneId> zones ;

	
	public TimeConverterGui(String propertiesUri) {

		RunningContext runningContext = null ;
		runningContext = new RunningContext("TimeConverter", null, propertiesUri);
		
		zones = ZoneId.getAvailableZoneIds().stream()
					.sorted()
					.map((z) -> ZoneId.of(z))
					.collect(Collectors.toCollection(Vector::new)) ;
		
		if (runningContext != null) {
			
			setBounds(50, 50, 1500, 1000);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setTitle("Time converter") ;
			getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
			
			Font font = new Font("Verdana", Font.BOLD, 16);
			JLabel milliTitle = new JLabel("Number of milliseconds since midnight, January 1, 1970 UTC :") ;
			add(milliTitle) ;
			
			millisField = new JTextField(10) ;
			millisField.setFont(font);
			millisField.setPreferredSize(new Dimension(300, 30)) ;
			millisField.setText(Long.toString(System.currentTimeMillis()));			
			add(millisField) ;
			
			JPanel actionPane = new JPanel() ;
			actionPane.setLayout(new BoxLayout(actionPane, BoxLayout.X_AXIS));
			
			pStart = new JButton("Convert to readable time") ;			
			actionPane.add(pStart) ;

			JLabel zoneTitle = new JLabel(" in the time zone : ") ;
			actionPane.add(zoneTitle) ;
			
			zoneIdsField = new JComboBox<ZoneId>(zones) ;
			zoneIdsField.setSelectedItem(ZoneId.systemDefault()) ;
			actionPane.add(zoneIdsField) ;
			
			add (actionPane) ;
			
			timeField = new JLabel() ;
			timeField.setFont(font);
			timeField.setPreferredSize(new Dimension(600, 30)) ;			
			add(timeField) ;

			pStart.addActionListener(new StartProc());
			pack() ;
		}
	}

	private class StartProc implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			timeField.setText(convertTime(Long.parseLong(millisField.getText()), (ZoneId) zoneIdsField.getSelectedItem()));
		}
		
	}
	
	private String convertTime(long milli, ZoneId zoneId) {
		return DateTimeFormatter.ofPattern(datePattern).format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(milli), zoneId));
	}
}
