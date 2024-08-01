/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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

import java.util.logging.Logger;

import org.fl.util.RunningContext;

public class Config {

	public static final String DEFAULT_PROP_FILE = "timeConverter.properties";
	
	private static RunningContext runningContext;
	private static final Logger timeConverterLogger = Logger.getLogger(Config.class.getName());
	private static boolean initialized = false;
	
	private Config() {
	}

	public static void initConfig(String propertyFile) {
			
		runningContext = new RunningContext("org.fl.timeConverter", null, propertyFile);

		initialized = true;
	}
		
	public static RunningContext getRunningContext() {
		if (!initialized) {
			initConfig(DEFAULT_PROP_FILE);
		}
		return runningContext;
	}
	
	public static Logger getLogger() {
		if (!initialized) {
			initConfig(DEFAULT_PROP_FILE);
		}
		return timeConverterLogger;
	}
}
