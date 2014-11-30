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
package org.gc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gc.googleAnalytics.AuthHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.api.services.analytics.model.GaData;

/**
 * Google controller connects to google and retrieve user data.
 * 
 * @author gcanobbio
 *
 */
@Controller
@RequestMapping(value = "/api/oauth/google")
public class GoogleController {
	
	/**
	 * Instance of {@link Logger}.
	 */
	private static final Logger logger = LoggerFactory.getLogger(GoogleController.class);
	/**
	 * Instance of {@link AuthHelper}.
	 */
	@Autowired
	private AuthHelper auth;
	
	/**
	 * Service that start oauth authentication flow with Google,
	 * building a state token and login url.
	 * 
	 * @param response : instance of {@link HttpServletResponse}
	 * @param request : instance of {@link HttpServletRequest}
	 * @return string : Google login url
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String socialGooglePlus(HttpServletResponse response, HttpServletRequest request) {
		logger.info("****** Google auth ******");
		
		String token = auth.getStateToken();
		String loginURL = auth.buildLoginUrl();
		
		//save in session
		request.getSession().setAttribute("state",token);
		response.setStatus(HttpServletResponse.SC_OK);
		
		if(token!=null && loginURL!=null){
			return loginURL;
		}else{
			return null;
		}
	}
	
	/**
	 * This rest web service is the one that google called after login (callback url).
	 * First it retrieve code and token that google sends back. 
	 * It checks if code and token are not null, then if token is the same as the one saved in session.
	 * If it is not then response status is UNAUTHORIZED, otherwise it retrieves user data.
	 * 
	 * Then redirects authenticated user to home page where user can access protected resources.
	 * 
	 * @param request : instance of {@link HttpServletRequest}
	 * @param response : instance of {@link HttpServletResponse}
	 * @return redirect to home page
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.GET, produces = "application/json")
	public String confirmStateToken(HttpServletRequest request, HttpServletResponse response){
		
		logger.info("****** Google callback ******");
		String code = request.getParameter("code");
		String token = request.getParameter("state");
		String session_token = "";
		if(request.getSession().getAttribute("state")!=null){
			session_token = request.getSession().getAttribute("state").toString();
		}
		
		logger.info("request code: "+code);
		logger.info("request token: "+token);
		logger.info("request session token: "+session_token);
		
		//compare state token in session and state token in response of google
		//if equals return to home
		//if not error page
		if( (code==null || token==null) && (!token.equals(session_token))){
			logger.info("Error: You have to sign in!");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}else{
			try {
				//init analytics
				auth.getUserAnalytics(code);
				
				response.setStatus(HttpServletResponse.SC_OK);
					
				
			} catch (IOException e) {
				logger.info("IOException .. Problem in reading user data.");
				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		}
		
		return "redirect:/";
	}
	
	/**
	 * Retrieves google analytics event data from user's account and searching
	 * by label.
	 * 
	 * @param label : String
	 * @param trackingID : String
	 * @param request : instance of {@link HttpServletRequest}
	 * @return instance of {@link GaData}
	 */
	@RequestMapping(value = "/event/{label}/{trackingID}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public GaData googleEvent(@PathVariable String label, @PathVariable String trackingID,
			HttpServletRequest request){
		logger.info("Retrieve event from ga account.");
		
		if (request.getSession().getAttribute("state") != null) {
			try {
				String profileID = auth.getProfileId(trackingID);
				GaData event = auth.executeDataQueryEvent(profileID, label);
				return event;

			} catch (IOException e) {
				return null;

			}
		} else
			return null;
	}
	
	/**
	 * Retrieves google analytics exception data from user's account and searching
	 * by description.
	 * 
	 * @param description : String
	 * @param trackingID : String
	 * @param request : instance of {@link HttpServletRequest}
	 * @return instance of {@link GaData}
	 */
	@RequestMapping(value = "/exception/{description}/{trackingID}", method = RequestMethod.GET, 
			produces = "application/json")
	@ResponseBody
	public GaData googleException(@PathVariable String description, @PathVariable String trackingID,
			HttpServletRequest request){
		logger.info("Retrieve exception from ga account.");
		if (request.getSession().getAttribute("state") != null) {
			try {
				String profileID = auth.getProfileId(trackingID);
				GaData event = auth.executeDataQueryException(profileID,
						description);
				return event;

			} catch (IOException e) {
				return null;

			}
		} else
			return null;
	}
	
}
