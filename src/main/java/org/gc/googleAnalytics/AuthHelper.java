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

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.GaData.ColumnHeaders;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * A helper class for Google's OAuth2 authentication API.
 * 
 * @version 20130224
 * @author Matyas Danter
 * 
 * Modified by gcanobbio.
 */
@Service("googleHelper")
public final class AuthHelper {

	private final String CLIENT_ID;
	private final String CLIENT_SECRET;
	private final String CALLBACK_URI;

	/*
	 *  google authentication constants
	 */
	
	//scope
	//First: write access to the Analytics API
	//Second: view and manage user permissions for Analytics accounts
	private static final Iterable<String> SCOPE = Arrays
			.asList("https://www.googleapis.com/auth/analytics;https://www.googleapis.com/auth/analytics.manage.users"
					.split(";"));
	
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	private String stateToken;
	private final GoogleAuthorizationCodeFlow flow;
	
	private String APPLICATION_NAME;
	/**
	 * Instance of {@link Analytics}
	 */
	private static Analytics analytics;

	/**
	 * Constructor initializes the Google Authorization Code Flow with CLIENT
	 * ID, SECRET, and SCOPE.
	 * 
	 * @param client_id : String
	 * @param client_secret : String
	 * @param callback_uri : String
	 */
	public AuthHelper(String client_id, String client_secret, String callback_uri) {
		
		CLIENT_ID=client_id;
		CLIENT_SECRET=client_secret;
		CALLBACK_URI=callback_uri;
		
		System.out.println("CLIENT ID: "+CLIENT_ID);
		System.out.println("CLIENT SECRET: "+CLIENT_SECRET);
		System.out.println("CALLBACK: "+CALLBACK_URI);
		
		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
				JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, (Collection<String>) SCOPE).build();

		generateStateToken();

	}

	/**
	 * Builds a login URL based on client ID, secret, callback URI, and scope.
	 * 
	 * @return String, login URL
	 */
	public String buildLoginUrl() {

		final GoogleAuthorizationCodeRequestUrl url = flow
				.newAuthorizationUrl();

		return url.setRedirectUri(CALLBACK_URI).setState(stateToken).build();
	}

	/**
	 * Generates a secure state token.
	 */
	private void generateStateToken() {

		SecureRandom sr1 = new SecureRandom();

		stateToken = "google;" + sr1.nextInt();

	}

	/**
	 * 
	 * @return state token : String
	 */
	public String getStateToken() {
		return stateToken;
	}

	/**
	 * Expects an Authentication Code, and makes an authenticated request for
	 * the user's profile information.
	 * 
	 * @param authCode : String, authentication code provided by google
	 * @throws IOException
	 */
	public void getUserAnalytics(final String authCode) throws IOException{

		final GoogleTokenResponse response = flow.newTokenRequest(authCode)
				.setRedirectUri(CALLBACK_URI).execute();
		final Credential credential = flow.createAndStoreCredential(response,
				null);
		
		//init analytics
		analytics = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
		        APPLICATION_NAME).build();

	}

	/**
	 * Retrieves event of an api from user's google Analytics account.
	 * 
	 * @param trackingID : String
	 * @param apiName : String
	 * @return instance of {@link GaData}
	 * @throws IOException
	 */
	public GaData executeDataQueryEvent(String trackingID, String apiName)
			throws IOException {
		
		//profile id from tracking ID
		String[] s = trackingID.split("-");//tracking ID: UA-XXXXXX-YY where XXXXXX is profile ID
		String profileId = s[1];
		
		// Today date
		Date today = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dToday = format.format(today);

		// Today -1 month
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Date beforeToday = cal.getTime();
		String before = format.format(beforeToday);
		
		//TODO
		if (analytics != null) {

			GaData dataEvent = analytics.data()
					.ga()
					.get("ga:" + profileId, // Table Id. ga: + profile id.
							before, // Start date.
							dToday, // End date.
							"ga:totalEvents")
					// Metrics.
					.setDimensions("ga:eventLabel,ga:eventAction")
					.setSort("-ga:eventLabel")
					.setFilters("ga:eventLabel contains '" + apiName + "'")
					.setMaxResults(150).execute();
			
			printGaData(dataEvent);

			return dataEvent;
		} else
			return null;
	}
	
	/**
	 * Retrieves exception of an api from user's google Analytics account.
	 * 
	 * @param trackingID : String
	 * @param apiName : String
	 * @return instance of {@link GaData}
	 * @throws IOException
	 */
	public GaData executeDataQueryException(String trackingID, String apiName)
			throws IOException {
		
		//profile id from tracking ID
		String[] s = trackingID.split("-");//tracking ID: UA-XXXXXX-YY where XXXXXX is profile ID
		String profileId = s[1];
				
		//Today date
		Date today = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dToday = format.format(today);
		
		//Today -1 month
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Date beforeToday = cal.getTime();
		String before = format.format(beforeToday);
		
		// TODO
		if (analytics != null) {
			GaData dataExc = analytics.data()
					.ga()
					.get("ga:" + profileId, // Table Id. ga: + profile id.
							before, // Start date.
							dToday, // End date.
							"ga:exceptions")
					// Metrics.
					.setDimensions("ga:exceptionDescription")
					.setSort("-ga:exceptionDescription")
					.setFilters("ga:exceptionDescription contains '" + apiName + "'")
					.setMaxResults(150).execute();
			printGaData(dataExc);
			
			return dataExc;
		} else
			return null;
	}

	/**
	 * Function that prints Google Analytics data.
	 * 
	 * @param results : instance of {@link GaData}
	 */
	private static void printGaData(GaData results) {
		System.out.println("printing results for profile: "
				+ results.getProfileInfo().getProfileName());

		if (results.getRows() == null || results.getRows().isEmpty()) {
			System.out.println("No results Found.");
		} else {

			// Print column headers.
			for (ColumnHeaders header : results.getColumnHeaders()) {
				System.out.printf("%30s", header.getName());
			}
			System.out.println();

			// Print actual data.
			for (List<String> row : results.getRows()) {
				for (String column : row) {
					System.out.printf("%30s", column);
				}
				System.out.println();
			}

			System.out.println();
		}
	}
}