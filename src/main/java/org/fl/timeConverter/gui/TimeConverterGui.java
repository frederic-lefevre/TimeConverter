/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fl.timeConverter.Config;
import org.fl.util.RunningContext;

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
	
	public final static String DATE_PATTERN = "EEEE dd MMMM uuuu  HH:mm:ss.SSS";

	private JLabel timeField;

	public TimeConverterGui() {

		// access to properties and logger
		Config.initConfig(Config.DEFAULT_PROP_FILE);
		RunningContext runningContext = Config.getRunningContext();
		Logger logger = Config.getLogger();
		
		if (runningContext != null) {

			setBounds(50, 50, 1500, 1000);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setTitle("Time converter");
			getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

			MillisecondsAndZonePanel milliAndZonePane = new MillisecondsAndZonePanel(logger);
			add(milliAndZonePane);

			JPanel datePane = new JPanel();
			datePane.setLayout(new BoxLayout(datePane, BoxLayout.X_AXIS));
			timeField = new JLabel();
			timeField.setPreferredSize(new Dimension(600, 30));
			timeField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			datePane.add(new JLabel(" correspond à : "));
			datePane.add(timeField);
			add(datePane);

			DateTimePanel dateTimePanel = new DateTimePanel();
			add(dateTimePanel);

			setFontForAll(this, new Font("Verdana", Font.BOLD, 16));

			milliAndZonePane.addActionListeners(dateTimePanel, timeField);
			dateTimePanel.addActionListeners(milliAndZonePane, timeField);

			pack();

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
