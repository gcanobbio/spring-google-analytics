spring-google-analytics
=======================

This an example that integrates spring 4.0.2.RELEASE and Google Analytics.

It allows to post data (event / exception tracking) to users' google analytics account with tracking id and 
to login in their google analytics account to retrieve data.

In package org.gc.googleAnalytics there are classes that post data to Google Analytics account and create google authentication flow and after login it creates Analytics object and retrieve data:

1- PostData: post event and exception tracking to google analytics data. 
   In src/test/java PostDataTest collects test data, before starting this test, you must insert a valid tracking id.

2- AuthHelper: start authentication flow with google analytics (scope) and there are functions that retrieve event      and exception by filters (see documentation on dimensions & metrics to change filter).

In package org.gc there are two controllers: home (retrieve jsp) and google controller.
The latter is for google oauth authentication and for filters GaData.

Libraries
=======================

```
<dependency>
	<groupId>com.google.apis</groupId>
	<artifactId>google-api-services-analytics</artifactId>
	<version>v3-rev104-1.18.0-rc</version>
</dependency>
		
<dependency>
	<groupId>com.google.http-client</groupId>
	<artifactId>google-http-client</artifactId>
	<version>1.18.0-rc</version>
</dependency>

<dependency>
	<groupId>com.google.http-client</groupId>
	<artifactId>google-http-client-jackson</artifactId>
	<version>1.17.0-rc</version>
</dependency>
		
<dependency>
	<groupId>com.google.apis</groupId>
	<artifactId>google-api-services-oauth2</artifactId>
	<version>v2-rev70-1.17.0-rc</version>
</dependency>
```

Reference
=======================
The library and some sample on how to connect with Google Products such as Google Analytics can be found at https://code.google.com/p/google-api-java-client/wiki/Setup

Measurement Protocol is needed to POST data in users' google analytics account, documentation can be found at https://developers.google.com/analytics/devguides/collection/protocol/v1/devguide

The latest sample about how to connect to users' google analytics accounts and retreive google analytics data can be found at https://code.google.com/p/google-api-java-client/source/browse/analytics-cmdline-sample/src/main/java/com/google/api/services/samples/analytics/cmdline/HelloAnalyticsApiSample.java?repo=samples

Google Analytics Dimensions & Metrics for retrieving data about event and exception tracking with filters can be found at https://developers.google.com/analytics/devguides/reporting/core/dimsmets


