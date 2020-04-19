package org.fl.timeConverter.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
	
	private JTextField millisField ;
	private JLabel timeField ;
	
	private JComboBox<ZoneId> zoneIdsField ;
	private JComboBox<DisplayableTemporal> monthsField ;
	private JComboBox<DisplayableTemporal> daysField ;
	private DefaultComboBoxModel<DisplayableTemporal> daysFieldModel ;
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
			milliTitle.setFont(font);
			milliTitle.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			milliTitlePane.add(milliTitle) ;
			add(milliTitlePane) ;
			
			millisField = new JTextField(13) ;
			millisField.setFont(font);
			millisField.setPreferredSize(new Dimension(300, 40)) ;
			millisField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			millisField.setText(Long.toString(System.currentTimeMillis()));			
			add(millisField) ;
			
			JPanel datePane = new JPanel() ;
			datePane.setLayout(new BoxLayout(datePane, BoxLayout.X_AXIS));
			JLabel dateTitle = new JLabel(" correspond à : ") ;
			dateTitle.setFont(font) ;
			timeField = new JLabel() ;
			timeField.setFont(font);
			timeField.setPreferredSize(new Dimension(600, 30)) ;	
			timeField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			datePane.add(dateTitle) ;
			datePane.add(timeField) ;
			add (datePane) ;
			
			JPanel zonetPane = new JPanel() ;
			zonetPane.setLayout(new BoxLayout(zonetPane, BoxLayout.X_AXIS));
			JLabel zoneTitle = new JLabel(" dans le fuseau horaire : ") ;
			zoneTitle.setFont(font);
			zonetPane.add(zoneTitle) ;
			zoneIdsField = new JComboBox<ZoneId>(zones) ;
			zoneIdsField.setSelectedItem(ZoneId.systemDefault()) ;
			zoneIdsField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			zoneIdsField.setFont(font);
			zonetPane.add(zoneIdsField) ;
			add(zonetPane) ;

			JPanel dateTimePanel = new JPanel() ;
			dateTimePanel.setLayout(new BoxLayout(dateTimePanel, BoxLayout.X_AXIS));
			
			daysFieldModel = new DefaultComboBoxModel<>() ;
			daysField = new JComboBox<DisplayableTemporal>(daysFieldModel) ;
			dateTimePanel.add(daysField) ;
			monthsField = new JComboBox<DisplayableTemporal>(months.getVector()) ;
			dateTimePanel.add(monthsField) ;
			add(dateTimePanel) ;
			
			millisField.addActionListener(new StartProc());
			zoneIdsField.addActionListener(new StartProc());
			pack() ;
			upDateTimeField();
		}
	}

	private class StartProc implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			upDateTimeField();
		}
		
	}
	
	private static final String NOW = "now";
	
	private void upDateTimeField() {
		
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
			
			ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(millisField.getText())), zone) ;
			Month m = zdt.getMonth() ;
			DisplayableTemporal item = months.get(m) ;
			monthsField.setSelectedItem(item);
			
			daysOfMonth = TimeUtils.getAllDaysOfMonth(zdt) ;
			daysField.removeAllItems();
			daysFieldModel.addAll(TimeUtils.getAllDaysOfMonth(zdt).getVector());
			
		} catch (NumberFormatException ex) {
			timeField.setText("Rentrez un nombre valide de millisecondes ou \"now\"") ;
		}
	}
	

}
