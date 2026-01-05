/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

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

package org.fl.timeConverter;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.fl.timeConverter.gui.TimeConverterGui;
import org.fl.util.RunningContext;

public class Config {
	
	private static final String DATE_PATTERN = "EEEE dd MMMM uuuu  HH:mm:ss.SSS";
	private DateTimeFormatter dateTimeFormatter; 
	
	private static Config instance;
	
	private static Config getInstance() {
		if (instance == null) {
			instance = new Config(TimeConverterGui.getRunningContext());
		}
		return instance;
	}
	
	private Config() {
	}

	private Config(RunningContext runningContext) {
		dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN).localizedBy(Locale.FRENCH);
	}

	public static DateTimeFormatter getDateTimeFormatter() {
		return getInstance().dateTimeFormatter;
	}
}
