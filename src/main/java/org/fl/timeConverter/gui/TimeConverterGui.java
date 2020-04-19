package org.fl.timeConverter.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.Month;
import java.time.MonthDay;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.fl.timeConverter.DisplayableTemporal;
import org.fl.timeConverter.DisplayableTemporalSet;
import org.fl.timeConverter.TimeUtils;

import com.ibm.lge.fl.util.RunningContext;

public class TimeConverterGui  extends JFrame {
	
	private static final long serialVersionUID = 2368226038474247064L;
	
	private static final String DEFAULT_PROP_FILE = "timeConverter.properties";

//	private final static String datePattern = "EEEE dd MMMM uuuu  HH:mm:ss.SSS VV" ;
	private final static String datePattern = "EEEE dd MMMM uuuu  HH:mm:ss.SSS" ;
	
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
	
	// Millisecond
	private JTextField millisField ;
	private JLabel timeField ;
	
	// Zone
	private JComboBox<ZoneId> zoneIdsField ;
	
	// Date Time fields
	private JComboBox<DisplayableTemporal> daysField ;
	private DefaultComboBoxModel<DisplayableTemporal> daysFieldModel ;
	private JComboBox<DisplayableTemporal> monthsField ;
	private JTextField yearField ;
	private JTextField hourField ;
	private JTextField minuteField ;
	private JTextField secondField ;
	private JTextField nanoField ;
		
	private DisplayableTemporalSet months ;
	private DisplayableTemporalSet daysOfMonth ;
	
	public TimeConverterGui(String propertiesUri) {

		RunningContext runningContext = null ;
		runningContext = new RunningContext("TimeConverter", null, propertiesUri);
		
		Vector<ZoneId> zones = TimeUtils.getZoneIds() ;
		
		months = TimeUtils.getMonths() ;
		
		if (runningContext != null) {
			
			setBounds(50, 50, 1500, 1000);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setTitle("Time converter") ;
			getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
			
			Font font = new Font("Verdana", Font.BOLD, 16);
			JPanel milliTitlePane = new JPanel() ;
			milliTitlePane.setLayout(new BoxLayout(milliTitlePane, BoxLayout.X_AXIS));
			JLabel milliTitle = new JLabel("Le nombre de millisecondes depuis le 1 janvier 1970, minuit, UTC") ;
			milliTitle.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			milliTitlePane.add(milliTitle) ;
			add(milliTitlePane) ;
			
			millisField = new JTextField(13) ;
			millisField.setPreferredSize(new Dimension(300, 40)) ;
			millisField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			millisField.setText(Long.toString(System.currentTimeMillis()));			
			add(millisField) ;
			
			JPanel datePane = new JPanel() ;
			datePane.setLayout(new BoxLayout(datePane, BoxLayout.X_AXIS));
			timeField = new JLabel() ;
			timeField.setPreferredSize(new Dimension(600, 30)) ;	
			timeField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			datePane.add(new JLabel(" correspond à : ")) ;
			datePane.add(timeField) ;
			add (datePane) ;
			
			JPanel zonetPane = new JPanel() ;
			zonetPane.setLayout(new BoxLayout(zonetPane, BoxLayout.X_AXIS));
			zonetPane.add(new JLabel(" dans le fuseau horaire : ")) ;
			zoneIdsField = new JComboBox<ZoneId>(zones) ;
			zoneIdsField.setSelectedItem(ZoneId.systemDefault()) ;
			zoneIdsField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			zonetPane.add(zoneIdsField) ;
			add(zonetPane) ;

			JPanel dateTimePanel = new JPanel() ;
			dateTimePanel.setLayout(new BoxLayout(dateTimePanel, BoxLayout.X_AXIS));
			
			daysFieldModel = new DefaultComboBoxModel<>() ;
			daysField = new JComboBox<DisplayableTemporal>(daysFieldModel) ;
			dateTimePanel.add(daysField) ;
			monthsField = new JComboBox<DisplayableTemporal>(months.getVector()) ;
			dateTimePanel.add(monthsField) ;
			yearField = new JTextField(5) ;
			dateTimePanel.add(yearField) ;
			hourField = new JTextField(2) ;
			dateTimePanel.add(hourField) ;			
			dateTimePanel.add(new JLabel("h ")) ;
			minuteField = new JTextField(2) ;
			dateTimePanel.add(minuteField) ;			
			dateTimePanel.add(new JLabel("m ")) ;
			secondField = new JTextField(2) ;
			dateTimePanel.add(secondField) ;
			dateTimePanel.add(new JLabel("s ")) ;
			nanoField = new JTextField(3) ;
			dateTimePanel.add(nanoField) ;
			dateTimePanel.add(new JLabel("ms ")) ;
			add(dateTimePanel) ;
			
			setFontForAll(this, font);
			
			MillisAndZoneListener millisAndZoneListener = new MillisAndZoneListener() ;
			millisField.addActionListener(millisAndZoneListener);
			zoneIdsField.addActionListener(millisAndZoneListener);
			
			DateTimeListener dateTimeListener = new DateTimeListener() ;
			daysField.addActionListener(dateTimeListener) ;
			monthsField.addActionListener(dateTimeListener) ;
			yearField.addActionListener(dateTimeListener) ;
			hourField.addActionListener(dateTimeListener) ;
			minuteField.addActionListener(dateTimeListener) ;
			secondField.addActionListener(dateTimeListener) ;
			nanoField.addActionListener(dateTimeListener) ;
			
			pack() ;
			millisAndZoneListener.upDateTimeField();
		}
	}

	private class MillisAndZoneListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {			
			upDateTimeField();
		}	
		
		private static final String NOW = "now";
		public void upDateTimeField() {
			
			ZoneId zone = (ZoneId)zoneIdsField.getSelectedItem() ;
			String milliText = millisField.getText() ;
			try {
				long milli ;
				if (milliText.equalsIgnoreCase(NOW)) {
					milli = System.currentTimeMillis() ;
					millisField.setText(Long.toString(milli)) ;			
				} else {				
					milli = Long.parseLong(millisField.getText()) ;			
				}
				
				timeField.setText(TimeUtils.convertTime(milli, zone, datePattern)) ;
				
				// Get the ZonedDateTime corresponding to the milliseconds and the zone
				ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(millisField.getText())), zone) ;
							
				// Update day field
				daysOfMonth = TimeUtils.getAllDaysOfMonth(zdt) ;
				daysField.removeAllItems();
				daysOfMonth = TimeUtils.getAllDaysOfMonth(zdt) ;
				daysFieldModel.addAll(daysOfMonth.getVector());					
				daysField.setSelectedItem(daysOfMonth.get(MonthDay.from(zdt)));
				
				// update the month field
				Month m = zdt.getMonth() ;
				DisplayableTemporal item = months.get(m) ;
				monthsField.setSelectedItem(item);
				
				// Update year, hour, minute, second, nano fields
				yearField.setText(Integer.toString(zdt.getYear())) ;
				hourField.setText(Integer.toString(zdt.getHour())) ;
				minuteField.setText(Integer.toString(zdt.getMinute())) ;
				secondField.setText(Integer.toString(zdt.getSecond())) ;
				nanoField.setText(Integer.toString(zdt.getNano()/1000000)) ;
				
			} catch (NumberFormatException ex) {
				timeField.setText("Rentrez un nombre valide de millisecondes ou \"now\"") ;
			}
		}
	}
	
	private class DateTimeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {			
			updateMilliField();
		}	
		
		private void updateMilliField() {
		
			// Get date and time field
			try {
				int y = Integer.parseInt(yearField.getText()) ;
				int h = Integer.parseInt(hourField.getText()) ;
				int m = Integer.parseInt(minuteField.getText()) ;
				int s = Integer.parseInt(secondField.getText()) ;
				int n = Integer.parseInt(nanoField.getText()) ;
				
				int mo = ((DisplayableTemporal) monthsField.getSelectedItem()).getTemporalAccessor().get(ChronoField.MONTH_OF_YEAR) ;
				int da = ((DisplayableTemporal) daysField.getSelectedItem()).getTemporalAccessor().get(ChronoField.DAY_OF_MONTH) ;
				
				ZoneId zo = (ZoneId)zoneIdsField.getSelectedItem() ;
				
				ZonedDateTime zdt = ZonedDateTime.of(y, mo, da, h, m, s, n*1000000, zo) ;
				
				millisField.setText(Long.toString(zdt.toInstant().toEpochMilli()));
				
			} catch (NumberFormatException ex) {
				timeField.setText("Rentrez un nombre valide") ;
			}
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
