package org.fl.timeConverter.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.fl.timeConverter.TimeUtils;

public class MillisecondsAndZonePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final JTextField millisField ;
	private final JComboBox<ZoneId> zoneIdsField ;
		
	private final MillisAndZoneListener millisAndZoneListener ;
	
	private final Logger logger ;
	
	private DateTimePanel dateTimePanel ;
	private JLabel timeField ;
	
	public MillisecondsAndZonePanel(Logger l) {
		super();
		
		logger = l ;
		millisAndZoneListener = new MillisAndZoneListener() ;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel milliTitle = new JLabel("Le nombre de millisecondes depuis le 1 janvier 1970, minuit, UTC") ;
		milliTitle.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		add(milliTitle) ;
		
		millisField = new JTextField(13) ;
		millisField.setPreferredSize(new Dimension(300, 40)) ;
		millisField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		millisField.setText(Long.toString(System.currentTimeMillis()));	
		add(millisField) ;
		
		Vector<ZoneId> zones = TimeUtils.getZoneIds() ;
		JPanel zonetPane = new JPanel() ;
		zonetPane.setLayout(new BoxLayout(zonetPane, BoxLayout.X_AXIS));
		zonetPane.add(new JLabel(" dans le fuseau horaire : ")) ;
		zoneIdsField = new JComboBox<ZoneId>(zones) ;
		zoneIdsField.setSelectedItem(ZoneId.systemDefault()) ;
		zoneIdsField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		zonetPane.add(zoneIdsField) ;
		add(zonetPane) ;

	}

	public void addActionListeners(DateTimePanel dtp, JLabel tf) {
		dateTimePanel = dtp ;
		timeField = tf ;
		millisField.addActionListener(millisAndZoneListener);
		zoneIdsField.addActionListener(millisAndZoneListener);
	}
	
	public void removeActionListeners() {
		millisField.removeActionListener(millisAndZoneListener);
		zoneIdsField.removeActionListener(millisAndZoneListener);
	}
	
	public ZoneId getZoneId() {		
		return (ZoneId)zoneIdsField.getSelectedItem() ;
	}
	
	public void setMillisecondsField(long millis) {
		millisField.setText(Long.toString(millis)) ;
	}
	
	private class MillisAndZoneListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			logger.info("MillisAndZoneListener ActionEvent source=" + e.getSource().toString()) ;

			upDateTimeField();			
		}	
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
			
			timeField.setText(TimeUtils.convertTime(milli, zone, TimeConverterGui.DATE_PATTERN)) ;
			
			// Get the ZonedDateTime corresponding to the milliseconds and the zone
			ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(millisField.getText())), zone) ;
					
			// Update day, month, year, hour, minute, second, millisecond fields
			dateTimePanel.setDateTimeFields(zdt);
			
			logger.info("End updateDateTime");
		} catch (NumberFormatException ex) {
			timeField.setText("Rentrez un nombre valide de millisecondes ou \"now\"") ;
		}
	}
}
