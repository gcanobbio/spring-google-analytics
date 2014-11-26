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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.gc.googleAnalytics.Constant.*;

/**
 * Collect data of api access and write them in
 * google analytics account of api owner if 
 * he/she gave us tracking id.
 *
 * The request for Event Tracking is the following:
 * POST /collect HTTP/1.1
 * Host: www.google-analytics.com
 * payload_data
 * 
 * NOTE: for payload_data see class {@link Constant}
 * 
 * @author gcanobbio
 *
 */
public class PostData {

	/**
	 * Instance of {@link Logger}
	 */
	private static final Logger logger = LoggerFactory.getLogger(PostData.class);
	
	/*
	 * Global variables
	 */
	private static final String ENDPOINT = "http://www.google-analytics.com/collect";
	
	private final String clientID = "555";//Anonymous client id
	
	private static String trackingID;
	private String appVersion;
	private String appName;
	
	/**
	 * Constructor with parameters.
	 * 
	 * @param trackingID : String
	 * @param appName : String
	 * @param appVersion : String
	 */
	public PostData(String trackingID, String appName, String appVersion){
		PostData.trackingID = trackingID;
		this.appName = appName;
		this.appVersion = appVersion;
	}
	
	/**
	 * 
	 * @param gaTrackingID : static string
	 */
	public static void setGaTrackingID(String gaTrackingID) {
		PostData.trackingID = gaTrackingID;
	}
	
	/**
	 * 
	 * @param appName : String
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	/**
	 * 
	 * @param appVersion : String
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	/**
	 * Executes post request to collect data on Event Tracking
	 * in owner google analytics account.
	 * 
	 * @param category : String
	 * @param action : String
	 * @param name : String, suppose to be id or name of api
	 * @param value : String, number of access
	 * @return boolean value, if request is ok then true else false.
	 */
	public boolean eventTracking(String category, String action, String name, String value) {
		//check category and action
		if(category==null || action==null){
			logger.error("In Event Tracking, Category and Action are required.");
			return false;
		}
		//set request
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(ENDPOINT);
		method.addParameter(PROTOCAL_VERSION, "1");
		method.addParameter(TRACKING_ID, trackingID);
		method.addParameter(CLIENT_ID, clientID);
		method.addParameter(HIT_TYPE, "event");
		method.addParameter(APPLICATION_NAME, appName);
		method.addParameter(APPLICATION_VERSION,
				(!(this.appVersion == null || this.appVersion.length() == 0)) ? this.appVersion
						: DEFAULT_VERSION);
		method.addParameter(EVENT_CATEGORY, category);
		method.addParameter(EVENT_ACTION, action);
		method.addParameter(EVENT_LABEL, name);//name/id of api
		method.addParameter(EVENT_VALUE, value);//1
		try {
			// Checks that value is a valid number.
			Integer.parseInt(value);
			//execute request
			int returnCode = client.executeMethod(method);
			return (returnCode == HttpStatus.SC_OK);
			
		} catch (NumberFormatException e) {
			throw new RuntimeException("Only valid number is allowed", e);
			
		} catch (Exception e) {
			logger.error("ERROR: {}", e.getMessage());
			return false;
			
		}
	}
	
	/**
	 * Executes post request to collect data on Exception Tracking
	 * in owner google analytics account.
	 * 
	 * @param description : String
	 * @param fatal : String
	 * @return boolean value, if request is ok then true else false.
	 */
	public boolean exceptionTracking(String description, boolean fatal) {
		//check input parameter
		if(description==null){
			logger.error("In Exception Tracking, Description is required.");
			return false;
		}
		// set request
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(ENDPOINT);
		method.addParameter(PROTOCAL_VERSION, "1");
		method.addParameter(TRACKING_ID, trackingID);
		method.addParameter(CLIENT_ID, clientID);
		method.addParameter(HIT_TYPE, "exception");
		method.addParameter(APPLICATION_NAME, appName);
		method.addParameter(
				APPLICATION_VERSION,
				(!(this.appVersion == null || this.appVersion.length() == 0)) ? this.appVersion
						: DEFAULT_VERSION);
		method.addParameter(EXP_DESCRIPTION, description);
		method.addParameter(EXP_ISFATAL, ""+fatal);
		try {
			// execute request
			int returnCode = client.executeMethod(method);
			return (returnCode == HttpStatus.SC_OK);

		} catch (Exception e) {
			logger.error("ERROR: {}", e.getMessage());
			return false;

		}
	}
}
