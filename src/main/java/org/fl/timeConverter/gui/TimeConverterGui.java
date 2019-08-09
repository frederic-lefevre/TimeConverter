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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.ibm.lge.fl.util.RunningContext;

public class TimeConverterGui  extends JFrame {
	
	private static final long serialVersionUID = 2368226038474247064L;
	
	private static final String DEFAULT_PROP_FILE = "timeConverter.properties";

	private final static String datePattern = "uuuu-MM-dd HH:mm:ss.SSS VV" ;
	
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
	
	public TimeConverterGui(String propertiesUri) {

		RunningContext runningContext = null ;
		runningContext = new RunningContext("TimeConverter", null, propertiesUri);
		
		if (runningContext != null) {
			
			setBounds(50, 50, 1500, 1000);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setTitle("Time converter") ;
			getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
			
			Font font = new Font("Verdana", Font.BOLD, 16);
			millisField = new JTextField(10) ;
			millisField.setFont(font);
			millisField.setPreferredSize(new Dimension(300, 30)) ;
			millisField.setText(Long.toString(System.currentTimeMillis()));
			
			add(millisField) ;
			
			pStart = new JButton("Convert to string") ;
			
			add(pStart) ;

			timeField = new JLabel() ;
			timeField.setFont(font);
			timeField.setPreferredSize(new Dimension(300, 30)) ;
			
			add(timeField) ;

			pStart.addActionListener(new StartProc());
			pack() ;
		}
	}

	private class StartProc implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			timeField.setText(convertTime(Long.parseLong(millisField.getText())));
		}
		
	}
	
	private String convertTime(long milli) {
		return DateTimeFormatter.ofPattern(datePattern).format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneId.systemDefault()));
	}
}
