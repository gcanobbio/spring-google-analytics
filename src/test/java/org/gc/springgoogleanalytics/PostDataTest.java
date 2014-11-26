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
package org.gc.springgoogleanalytics;

import static org.junit.Assert.assertTrue;

import org.gc.googleAnalytics.PostData;
import org.gc.googleAnalytics.TrackingIDValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value= {"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
/**
 * Test to POST data in user's google analytics account.
 * Tracking ID is needed.
 * Data to post are:
 * 1- event tracking
 * 2- exception tracking
 * 
 * @author gcanobbio
 *
 */
public class PostDataTest {
	
	/**
	 * Instance of {@link Logger}.
	 */
	private Logger log = LoggerFactory.getLogger(PostDataTest.class);
	/**
	 * Instance of {@link PostData}
	 */
	private PostData gatemplate;
	
	/**
	 * First insert tracking ID.
	 * Then a validator checks that tracking ID is valid.
	 * If it is valid then class that post data is initialized else
	 * an IllegalArgument exception is threw.
	 */
	@Before
	public void setUp() {
		String trackingID= "<insert here TRACKING ID>";
		
		TrackingIDValidator validator = new TrackingIDValidator();
		if(!validator.validate(trackingID)){
			throw new IllegalArgumentException("Tracking ID is not valid.");
		}
		
		gatemplate = new PostData(trackingID, "Test Google Analytics POST data", "1");
	}
	
	/**
	 * It posts test event data.
	 * You can change category, action, label before running this test.
	 */
	@Test
	public void testEventTracking(){
		log.info("Event tracking test..");
		
		String category = "Test";
		String action = "First data saved";
		String label = "Test 1";
		
		boolean request = gatemplate.eventTracking(category, action, label, "1");
		
		assertTrue("POST problem", request);
	}
	
	/**
	 * It posts test exception data, an illegal argument exception that is fatal.
	 */
	@Test
	public void testExceptionTracking(){
		log.info("Exception tracking test..");
		boolean request = gatemplate.exceptionTracking("Illegal Argument Exception", true);
		
		assertTrue("POST problem", request);
	}

}
