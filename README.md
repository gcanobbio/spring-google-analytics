spring-google-analytics
=======================

This an example that integrates spring 4.0.2.RELEASE and Google Analytics.

It allows to post data (event / exception tracking) to users' google analytics account with tracking id and 
to login in their google analytics account to retrieve data.

In package org.gc.googleAnalytics there are classes that post data to Google Analytics account and create google authentication flow and after login it creates Analytics object and retrieve data:

1- PostData: post event and exception tracking to google analytics data. 
   In src/test/java PostDataTest collects test data.

2- AuthHelper: start authentication flow with google analytics (scope) and there are functions that retrieve event      and exception by filters.

In package org.gc there are two controllers: home (retrieve jsp) and google controller.
The latter is for google oauth authentication and for filters GaData.

Reference
=======================
The library and some sample on how to connect with Google Products such as Google Analytics can be found at https://code.google.com/p/google-api-java-client/wiki/Setup

Measurement Protocol is need to POST data in users' google analytics account, documentation can be found at https://developers.google.com/analytics/devguides/collection/protocol/v1/devguide

The latest sample about how to connect to users' google analytics accounts and retreive google analytics data can be found at https://code.google.com/p/google-api-java-client/source/browse/analytics-cmdline-sample/src/main/java/com/google/api/services/samples/analytics/cmdline/HelloAnalyticsApiSample.java?repo=samples

Google Analytics Dimensions & Metrics for retrieving data about event and exception tracking with filters can be found at https://developers.google.com/analytics/devguides/reporting/core/dimsmets

