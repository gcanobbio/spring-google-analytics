/*******************************************************************************
 *  The MIT License (MIT)
 *  
 *  Copyright (c) 2014
 *  
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *  
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 ******************************************************************************/
package org.gc.googleAnalytics;

/**
 * Google Analytics constant for post request on 
 * collection url.
 * The following parameter are required for Event Tracking request:
 * v=1             // Version.
 * &tid=UA-XXXX-Y  // Tracking ID / Property ID.
 * &cid=555        // Anonymous Client ID.
 * &t=event        // Hit type can be event, exception or other value
 * and other options.
 * 
 * Documentation: 
 * {@linkplain https://developers.google.com/analytics/devguides/collection/protocol/v1/devguide}
 * 
 * @author gcanobbio
 *
 */
public class Constant {
	
	/*
	 * Needed value
	 */
	public static final String PROTOCAL_VERSION = "v";
	
	public static final String TRACKING_ID = "tid";
	
	public static final String CLIENT_ID = "cid";
	
	public static final String APPLICATION_NAME = "an";
	
	public static final String APPLICATION_VERSION = "av";
	
	public static final String DEFAULT_VERSION = "1";
	
	public static final String HIT_TYPE = "t";
	
	/*
	 * Event Tracking
	 * &t=event        // Event hit type
	 * &ec=video       // Event Category. Required.
	 * &ea=play        // Event Action. Required.
	 * &el=holiday     // Event label.
	 * &ev=300         // Event value.
	 */
	
	public static final String EVENT_CATEGORY = "ec";
	
	public static final String EVENT_ACTION= "ea";
	
	public static final String EVENT_LABEL= "el";
	
	public static final String EVENT_VALUE= "ev";
	
	/*
	 * Exception Tracking
	 * &t=exception      // Exception hit type.
	 * &exd=IOException  // Exception description.
	 * &exf=1            // Exception is fatal?
	 */
	public static final String EXP_DESCRIPTION = "exd";
	
	public static final String EXP_ISFATAL = "exf";

}
