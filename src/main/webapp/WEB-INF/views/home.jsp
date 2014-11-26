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
		GET /api/oauth/google/auth
		This return url for Google login.
	</li>
	
</ul>

</body>
</html>
