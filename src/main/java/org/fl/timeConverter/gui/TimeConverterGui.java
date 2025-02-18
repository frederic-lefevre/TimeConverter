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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fl.timeConverter.Config;
import org.fl.util.RunningContext;
import org.fl.util.swing.ApplicationTabbedPane;

public class TimeConverterGui  extends JFrame {
	
	private static final long serialVersionUID = 2368226038474247064L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TimeConverterGui window = new TimeConverterGui();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static final int WINDOW_WIDTH  = 1000;
	public static final int WINDOW_HEIGHT = 500;
	
	public static final String DATE_PATTERN = "EEEE dd MMMM uuuu  HH:mm:ss.SSS";

	private JLabel timeField;

	public TimeConverterGui() {

		// access to properties and logger
		Config.initConfig(Config.DEFAULT_PROP_FILE);
		RunningContext runningContext = Config.getRunningContext();
		
		if (runningContext != null) {

	   		setBounds(20, 20, WINDOW_WIDTH, WINDOW_HEIGHT);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setTitle("Convertiseur de temps");
			
			ApplicationTabbedPane timeConverterTabs = new ApplicationTabbedPane(runningContext);
			
			JPanel timeConverterPane = new JPanel();
			timeConverterPane.setLayout(new BoxLayout(timeConverterPane, BoxLayout.Y_AXIS));

			MillisecondsAndZonePanel milliAndZonePane = new MillisecondsAndZonePanel();
			timeConverterPane.add(milliAndZonePane);

			JPanel datePane = new JPanel();
			datePane.setLayout(new BoxLayout(datePane, BoxLayout.X_AXIS));
			timeField = new JLabel();
			timeField.setPreferredSize(new Dimension(600, 30));
			timeField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			datePane.add(new JLabel(" correspond à : "));
			datePane.add(timeField);
			timeConverterPane.add(datePane);

			DateTimePanel dateTimePanel = new DateTimePanel();
			timeConverterPane.add(dateTimePanel);

			setFontForAll(timeConverterPane, new Font("Verdana", Font.BOLD, 16));

			milliAndZonePane.addActionListeners(dateTimePanel, timeField);
			dateTimePanel.addActionListeners(milliAndZonePane, timeField);

			//datePane.pack();

			// init with current time
			milliAndZonePane.setMillisecondsField(System.currentTimeMillis());
			milliAndZonePane.upDateTimeField();
			
			timeConverterTabs.add(timeConverterPane, "Convertion de temps", 0);
			timeConverterTabs.setSelectedIndex(0);
			
			getContentPane().add(timeConverterTabs) ;
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
