/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.fl.timeConverter.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Month;
import java.time.MonthDay;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;

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
	private final JComboBox<DisplayableTemporal> daysField;
	private final DefaultComboBoxModel<DisplayableTemporal> daysFieldModel;
	private final JComboBox<DisplayableTemporal> monthsField;
	private final JTextField yearField;
	private final JTextField hourField;
	private final JTextField minuteField;
	private final JTextField secondField;
	private final JTextField milliField;

	private final DisplayableTemporalSet months;

	private final DateTimeListener dateTimeListener;

	private MillisecondsAndZonePanel millisecondsAndZonePanel;
	private JLabel infoLabel;

	public DateTimePanel() {
		super();

		dateTimeListener = new DateTimeListener();

		months = TimeUtils.getMonths();

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		daysFieldModel = new DefaultComboBoxModel<>();
		daysField = new JComboBox<DisplayableTemporal>(daysFieldModel);
		add(daysField);
		monthsField = new JComboBox<DisplayableTemporal>(months.getVector());
		add(monthsField);
		yearField = new JTextField(5);
		add(yearField);
		hourField = new JTextField(2);
		add(hourField);
		add(new JLabel("h "));
		minuteField = new JTextField(2);
		add(minuteField);
		add(new JLabel("m "));
		secondField = new JTextField(2);
		add(secondField);
		add(new JLabel("s "));
		milliField = new JTextField(3);
		add(milliField);
		add(new JLabel("ms "));
	}

	public void addActionListeners(MillisecondsAndZonePanel mzp, JLabel il) {
		millisecondsAndZonePanel = mzp;
		infoLabel = il;
		addActionListeners();
	}

	private void addActionListeners() {
		daysField.addActionListener(dateTimeListener);
		monthsField.addActionListener(dateTimeListener);
		yearField.addActionListener(dateTimeListener);
		hourField.addActionListener(dateTimeListener);
		minuteField.addActionListener(dateTimeListener);
		secondField.addActionListener(dateTimeListener);
		milliField.addActionListener(dateTimeListener);
	}

	private void removeActionListeners() {
		daysField.removeActionListener(dateTimeListener);
		monthsField.removeActionListener(dateTimeListener);
		yearField.removeActionListener(dateTimeListener);
		hourField.removeActionListener(dateTimeListener);
		minuteField.removeActionListener(dateTimeListener);
		secondField.removeActionListener(dateTimeListener);
		milliField.removeActionListener(dateTimeListener);
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
			int y = TimeUtils.parseYears(yearField.getText());
			int h = TimeUtils.parseHours(hourField.getText());
			int m = TimeUtils.parseMinutes(minuteField.getText());
			int s = TimeUtils.parseSeconds(secondField.getText());
			int n = TimeUtils.parseMilliSeconds(milliField.getText());

			int mo = ((DisplayableTemporal) monthsField.getSelectedItem()).getTemporalAccessor()
					.get(ChronoField.MONTH_OF_YEAR);
			int da = ((DisplayableTemporal) daysField.getSelectedItem()).getTemporalAccessor()
					.get(ChronoField.DAY_OF_MONTH);

			ZoneId zo = mzp.getZoneId();

			ZonedDateTime zdt = TimeUtils.guessZonedDateTimeOf(y, mo, da, h, m, s, n * 1000000, zo);
			if (zdt == null) {
				timeField.setForeground(Color.RED);
				timeField.setText("Problème dans l'évaluation de la date");
			} else {
				// Realign fields (useful for day)
				setDateTimeFields(zdt);

				long milli = zdt.toInstant().toEpochMilli();
				mzp.setMillisecondsField(milli);
				timeField.setForeground(Color.BLACK);
				timeField.setText(TimeUtils.convertTime(milli, zo, TimeConverterGui.DATE_PATTERN));
			}
		} catch (NumberFormatException ex) {
			timeField.setForeground(Color.RED);timeField.setForeground(Color.RED);
			timeField.setText("Rentrez un nombre valide: " + ex.getMessage());
		}
	}

	
	public void setDateTimeFields(ZonedDateTime zdt) {

		// Avoid to trigger listener in loop between dateTime and milli
		removeActionListeners();

		daysField.removeAllItems();
		DisplayableTemporalSet daysOfMonth = TimeUtils.getAllDaysOfMonth(zdt);
		daysFieldModel.addAll(daysOfMonth.getVector());
		daysField.setSelectedItem(daysOfMonth.get(MonthDay.from(zdt)));

		// update the month field
		Month m = zdt.getMonth();
		DisplayableTemporal item = months.get(m);
		monthsField.setSelectedItem(item);

		// Update year, hour, minute, second, nano fields
		yearField.setText(Integer.toString(zdt.getYear()));
		hourField.setText(Integer.toString(zdt.getHour()));
		minuteField.setText(Integer.toString(zdt.getMinute()));
		secondField.setText(Integer.toString(zdt.getSecond()));
		milliField.setText(Integer.toString(zdt.getNano() / 1000000));

		addActionListeners();
	}
}
