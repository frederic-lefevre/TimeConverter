package org.fl.timeConverter.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Month;
import java.time.MonthDay;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.fl.timeConverter.DisplayableTemporal;
import org.fl.timeConverter.DisplayableTemporalSet;
import org.fl.timeConverter.TimeUtils;

public class DateTimePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// Date Time fields
	private final JComboBox<DisplayableTemporal> daysField ;
	private final DefaultComboBoxModel<DisplayableTemporal> daysFieldModel ;
	private final JComboBox<DisplayableTemporal> monthsField ;
	private final JTextField yearField ;
	private final JTextField hourField ;
	private final JTextField minuteField ;
	private final JTextField secondField ;
	private final JTextField nanoField ;
		
	private final DisplayableTemporalSet months ;
	
	private final Logger logger ;
	
	private final DateTimeListener dateTimeListener ;
	
	private MillisecondsAndZonePanel millisecondsAndZonePanel ;
	private JLabel infoLabel ;
	
	public DateTimePanel(Logger l) {
		super();
		
		logger = l ;
		dateTimeListener = new DateTimeListener() ;
		
		months = TimeUtils.getMonths() ;
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		daysFieldModel = new DefaultComboBoxModel<>() ;
		daysField = new JComboBox<DisplayableTemporal>(daysFieldModel) ;
		add(daysField) ;
		monthsField = new JComboBox<DisplayableTemporal>(months.getVector()) ;
		add(monthsField) ;
		yearField = new JTextField(5) ;
		add(yearField) ;
		hourField = new JTextField(2) ;
		add(hourField) ;			
		add(new JLabel("h ")) ;
		minuteField = new JTextField(2) ;
		add(minuteField) ;			
		add(new JLabel("m ")) ;
		secondField = new JTextField(2) ;
		add(secondField) ;
		add(new JLabel("s ")) ;
		nanoField = new JTextField(3) ;
		add(nanoField) ;
		add(new JLabel("ms ")) ;
	}

	public void addActionListeners(MillisecondsAndZonePanel mzp, JLabel il) {
		millisecondsAndZonePanel = mzp ;
		infoLabel = il ;
		addActionListeners() ;
	}
	
	private void addActionListeners() {
		daysField.addActionListener(dateTimeListener) ;
		monthsField.addActionListener(dateTimeListener) ;
		yearField.addActionListener(dateTimeListener) ;
		hourField.addActionListener(dateTimeListener) ;
		minuteField.addActionListener(dateTimeListener) ;
		secondField.addActionListener(dateTimeListener) ;
		nanoField.addActionListener(dateTimeListener) ;
	}
	
	private void removeActionListeners() {
		
		daysField.removeActionListener(dateTimeListener);
		monthsField.removeActionListener(dateTimeListener) ;
		yearField.removeActionListener(dateTimeListener) ;
		hourField.removeActionListener(dateTimeListener) ;
		minuteField.removeActionListener(dateTimeListener) ;
		secondField.removeActionListener(dateTimeListener) ;
		nanoField.removeActionListener(dateTimeListener) ;
	}
	
	private class DateTimeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			updateMilliField(millisecondsAndZonePanel, infoLabel);			
		}			
	}
	
	public void updateMilliField(MillisecondsAndZonePanel mzp, JLabel timeField) {
		
		// Get date and time field
		try {
			int y = Integer.parseInt(yearField.getText()) ;
			int h = Integer.parseInt(hourField.getText()) ;
			int m = Integer.parseInt(minuteField.getText()) ;
			int s = Integer.parseInt(secondField.getText()) ;
			int n = Integer.parseInt(nanoField.getText()) ;
			
			int mo = ((DisplayableTemporal) monthsField.getSelectedItem()).getTemporalAccessor().get(ChronoField.MONTH_OF_YEAR) ;
			int da = ((DisplayableTemporal) daysField.getSelectedItem()).getTemporalAccessor().get(ChronoField.DAY_OF_MONTH) ;
			
			ZoneId zo = mzp.getZoneId() ;
			
			ZonedDateTime zdt = TimeUtils.guessZonedDateTimeOf(y, mo, da, h, m, s, n*1000000, zo, logger) ;
			if (zdt == null) {
				timeField.setText("Probl??me dans l'??valuation de la date") ;
			} else {
				// Realign fields (useful for day)
				setDateTimeFields(zdt) ;
				
				long milli = zdt.toInstant().toEpochMilli() ;
				mzp.setMillisecondsField(milli);
				timeField.setText(TimeUtils.convertTime(milli, zo, TimeConverterGui.DATE_PATTERN)) ;
			}
		} catch (NumberFormatException ex) {
			timeField.setText("Rentrez un nombre valide") ;
		}
	}
	
	public void setDateTimeFields(ZonedDateTime zdt) {
		
		// Avoid to trigger listener in loop between dateTime and milli
		removeActionListeners();
		
		daysField.removeAllItems();
		DisplayableTemporalSet daysOfMonth = TimeUtils.getAllDaysOfMonth(zdt) ;
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
		
		addActionListeners() ;
	}
}
