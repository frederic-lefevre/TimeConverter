package org.fl.timeConverter.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.ZoneId;
import java.util.Vector;

import javax.swing.BorderFactory;
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
		
		zones = TimeUtils.getZoneIds() ;
		
		if (runningContext != null) {
			
			setBounds(50, 50, 1500, 1000);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setTitle("Time converter") ;
			getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
			
			Font font = new Font("Verdana", Font.BOLD, 16);
			JPanel milliTitlePane = new JPanel() ;
			milliTitlePane.setLayout(new BoxLayout(milliTitlePane, BoxLayout.X_AXIS));
			JLabel milliTitle = new JLabel("Number of milliseconds since midnight, January 1, 1970 UTC :") ;
			milliTitle.setFont(font);
			milliTitle.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			milliTitlePane.add(milliTitle) ;
			add(milliTitlePane) ;
			
			millisField = new JTextField(10) ;
			millisField.setFont(font);
			millisField.setPreferredSize(new Dimension(300, 40)) ;
			millisField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			millisField.setText(Long.toString(System.currentTimeMillis()));			
			add(millisField) ;
			
			JPanel actionPane = new JPanel() ;
			actionPane.setLayout(new BoxLayout(actionPane, BoxLayout.X_AXIS));
			
			pStart = new JButton("Convert to readable time") ;
			pStart.setFont(font);
			pStart.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			actionPane.add(pStart) ;

			JLabel zoneTitle = new JLabel(" in the time zone : ") ;
			zoneTitle.setFont(font);
			actionPane.add(zoneTitle) ;
			
			zoneIdsField = new JComboBox<ZoneId>(zones) ;
			zoneIdsField.setSelectedItem(ZoneId.systemDefault()) ;
			zoneIdsField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			zoneIdsField.setFont(font);
			actionPane.add(zoneIdsField) ;
			
			add (actionPane) ;
			
			JPanel resultPane = new JPanel() ;
			resultPane.setLayout(new BoxLayout(resultPane, BoxLayout.X_AXIS));
			timeField = new JLabel() ;
			timeField.setFont(font);
			timeField.setPreferredSize(new Dimension(600, 30)) ;	
			timeField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			resultPane.add(timeField) ;
			add(resultPane) ;

			pStart.addActionListener(new StartProc());
			millisField.addActionListener(new StartProc());
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
		
		String milliText = millisField.getText() ;
		if (milliText.equalsIgnoreCase(NOW)) {
			long now = System.currentTimeMillis() ;
			String prefix = NOW + " = " + now + " = " ;
			timeField.setText(prefix + TimeUtils.convertTime(now, (ZoneId) zoneIdsField.getSelectedItem(), datePattern));
		} else {
			try {
				timeField.setText(TimeUtils.convertTime(Long.parseLong(millisField.getText()), (ZoneId) zoneIdsField.getSelectedItem(), datePattern));
			} catch (NumberFormatException ex) {
				timeField.setText("Please provide a valid number of milliseconds or \"now\"") ;
			}
		}
	}
	

}
