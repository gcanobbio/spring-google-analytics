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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Google Analytics Tracking ID must be in format:
 * UA-XXXXX-YY
 * 
 * @author Giulia Canobbio
 *
 */
public class TrackingIDValidator {
	
	private Pattern pattern;
	private Matcher matcher;
	
	private static final String TID_PATTERN = "([U][A])[-]([0-9]{5,7})[-]([0-9]{1,2})/?";
	
	/**
	 * New instance of {@link UriValidation}.
	 */
	public TrackingIDValidator(){
		pattern = Pattern.compile(TID_PATTERN);
	}
	
	/**
	 * Validate a user's tracking id, checking if it matches pattern.
	 * 
	 * @param hex : String to validate
	 * @return boolean value true if it matches otherwise false
	 */
	public boolean validate(final String hex){
		matcher = pattern.matcher(hex);
		return matcher.matches();
	}

}
