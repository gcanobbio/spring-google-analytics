<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>

If you want to try Google Analytics, use the following rest services:

<ul type="disc">
	<li>
		GET <mark>/api/oauth/google/auth</mark> <br/>
		This return url for Google login.
	</li>
	<li>
		GET <mark>/api/oauth/google/event/{label}/{trackingID}</mark> <br/>
		Retrieves event data saved in your google analytics account, searching by label.
	</li>
	<li>
		GET <mark>/api/oauth/google/exception/{description}/{trackingID}</mark> <br/>
		Retrieves exception data saved in your google analytics account, searching by description.
	</li>
	
</ul>

<hr/>

<div>
	<h3>NOTE</h3>
	<div>
		You must register this application in google console, adding callback url 
		(yourdomain//api/oauth/google/callback). <br/>
		After retrieving client id and client secret (
		<a href="https://developers.google.com/console/help/new/#generatingoauth2">
			see documentation</a>),
		you can complete value.properties in src/main/resources.
	</div>
	
	<div>
		The main part of this project is in org.gc.googleAnalytics package.<br/>
		The main classes are:
			<ul type="disc">
				<li>
					<b>AuthHelper</b> <br/>
					a service, added in servlet-context spring configuration file.
					<br/>
					This class starts oauth 2.0 flow to connect with google and retrieves permissions
					to access google analytics user's account.
				</li>
				
				<li>
					<b>PostData</b> <br/>
					a simple class that writes event and exception tracking data to user google
					analytics account. <br/>
					Test is in src/test/java and it needs a tracking ID.
				</li>
	
			</ul>
	</div>
	
</div>

</body>
</html>
