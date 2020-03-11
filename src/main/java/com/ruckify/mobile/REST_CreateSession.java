package com.ruckify.mobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

public class REST_CreateSession {

	public static Integer getHeadsupHourByAPI(String udid)
			throws URISyntaxException, ClientProtocolException, IOException, ParseException {
		// CONFIG PARAMETERS:
		// BEGIN------------CONFIG PARAMETERS BELOW TO YOUR
		// ENVIRONMENT---------------------------------------
		String baseRestURL = "api1.ruckify.com";
		String username = "wahmed@ruckify.com";
		String password = "10Brewhunters";

		// END------------CONFIG PARAMETERS BELOW TO YOUR
		// ENVIRONMENT---------------------------------------

		// Create HTTPClient - Used to make Request to API
		HttpClient httpClient = null;
		CookieStore httpCookieStore = new BasicCookieStore();
		HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
		httpClient = builder.build();

		// Create Session using /api/auth/login
		String authToken = createAuthToken(baseRestURL, httpClient, username, password);
		System.out.println("Auth Token: " + authToken);

		if (authToken != null) {
			System.out.println("\n ");

			// Get Item /api/items/getItem
			Integer heahdsup_hours = getItemHeadsupTime(baseRestURL, httpClient, username, password, udid);
			System.out.println("Headsup Hour: " + heahdsup_hours);

			System.out.println("\n ");
			// Get Booking Amount /api/bookings/getBooking
			/*
			 * Integer amount = getBooking(baseRestURL, httpClient, username, password,
			 * authToken); System.out.println("Amount: " + amount);
			 */

			return heahdsup_hours;
		} else {
			System.out.println("Error: Unable to generate authToken - check to see if server is running");
			return 0;
		}
	}

	// Creates an AuthToken
	public static String createAuthToken(String baseRestURL, HttpClient httpClient, String username, String password)
			throws URISyntaxException, ClientProtocolException, IOException {
		System.out.println("Login: ");
		String APIPath = "/api/user/login";
		String completeRestURL = "https://" + baseRestURL + APIPath;
		System.out.println("REST API Login URL: " + completeRestURL);

		URIBuilder builder = new URIBuilder();
		builder.setScheme("https").setHost(baseRestURL).setPath(APIPath).setParameter("email", username)
				.setParameter("password", password).setParameter("onesignal_userid=", "");

		URI uri = builder.build();
		HttpPost httppost = new HttpPost(uri);
		System.out.println(httppost.getURI());

		CloseableHttpClient httpclient = HttpClients.createDefault();

		CloseableHttpResponse response = null;

		response = httpclient.execute(httppost);
		String responseString = "";
		int statusCode = response.getStatusLine().getStatusCode();
		String message = response.getStatusLine().getReasonPhrase();
		System.out.println("statusCode: " + statusCode);
		System.out.println("message: " + message);

		HttpEntity responseHttpEntity = response.getEntity();

		InputStream content = responseHttpEntity.getContent();

		BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
		String line;

		while ((line = buffer.readLine()) != null) {
			responseString += line;
		}
		System.out.println(responseString);

		JSONObject jsonObj = new JSONObject(responseString.toString());

		// System.out.println(jsonObj);
		System.out.println(jsonObj.getJSONObject("success").get("token"));
		String token = (String) jsonObj.getJSONObject("success").get("token");
		// System.out.println(responseString.substring(21,344));
		// release all resources held by the responseHttpEntity
		EntityUtils.consume(responseHttpEntity);

		// close the stream
		response.close();

		// return responseString.substring(21,344);
		return token;
	}

	// GetItem headsup hour
	public static Integer getItemHeadsupTime(String baseRestURL, HttpClient httpClient, String username,
			String password, String udid) throws URISyntaxException, ClientProtocolException, IOException, ParseException {
		System.out.println("Get Item: ");
		String APIPath = "/api/items/getItem";
		String completeRestURL = "https://" + baseRestURL + APIPath;
		System.out.println("REST API Get Item URL: " + completeRestURL);

		URIBuilder builder = new URIBuilder();
		builder.setScheme("https").setHost(baseRestURL).setPath(APIPath).setParameter("response_uuid=", "")
				.setParameter("uuid", udid)
				.setParameter("start_date", "2019-12-09 09:11").setParameter("end_date", "2020-12-10 09:01")
				.setParameter("quantity", "1").setParameter("version=", "");
		URI uri = builder.build();
		HttpPost httppost = new HttpPost(uri);
		System.out.println(httppost.getURI());

		CloseableHttpClient httpclient = HttpClients.createDefault();

		CloseableHttpResponse response = null;

		response = httpclient.execute(httppost);
		String responseString = "";
		int statusCode = response.getStatusLine().getStatusCode();
		String message = response.getStatusLine().getReasonPhrase();
		System.out.println("statusCode: " + statusCode);
		System.out.println("message: " + message);

		HttpEntity responseHttpEntity = response.getEntity();

		InputStream content = responseHttpEntity.getContent();

		BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
		String line;

		while ((line = buffer.readLine()) != null) {
			responseString += line;
		}
		System.out.println(responseString);

		JSONObject jsonObj = new JSONObject(responseString.toString());
		Integer headsuphours = 0;
		
		//System.out.println(jsonObj);
		if(! jsonObj.getJSONObject("success").get("headsup_hours").toString().equals("null"))
			headsuphours = (Integer) jsonObj.getJSONObject("success").get("headsup_hours");

		// release all resources held by the responseHttpEntity
		EntityUtils.consume(responseHttpEntity);

		// close the stream
		response.close();

		return headsuphours;
	}

	// GetBooking Amount
	public static Integer getBooking(String udid)
			throws URISyntaxException, ClientProtocolException, IOException, ParseException {
		
		String baseRestURL = "api1.ruckify.com";
		String username = "wahmed@ruckify.com";
		String password = "10Brewhunters";
		
		HttpClient httpClient = null;
		CookieStore httpCookieStore = new BasicCookieStore();
		HttpClientBuilder hbuilder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
		httpClient = hbuilder.build();

		// Create Session using /api/auth/login
		String authToken = createAuthToken(baseRestURL, httpClient, username, password);
		System.out.println("Auth Token: " + authToken);
		
		System.out.println("Get Booking: ");
		String APIPath = "/api/bookings/getBooking";
		String completeRestURL = "https://" + baseRestURL + APIPath;
		System.out.println("REST API Get Item URL: " + completeRestURL);

		URIBuilder builder = new URIBuilder();
		builder.setScheme("https").setHost(baseRestURL).setPath(APIPath).setParameter("uuid", udid);

		URI uri = builder.build();

		HttpPost httppost = new HttpPost(uri);
		httppost.addHeader("Authorization", "Bearer" + authToken);
		System.out.println(httppost.getURI());

		CloseableHttpClient httpclient = HttpClients.createDefault();

		CloseableHttpResponse response = null;

		response = httpclient.execute(httppost);
		String responseString = "";
		int statusCode = response.getStatusLine().getStatusCode();
		String message = response.getStatusLine().getReasonPhrase();
		System.out.println("statusCode: " + statusCode);
		System.out.println("message: " + message);

		HttpEntity responseHttpEntity = response.getEntity();

		InputStream content = responseHttpEntity.getContent();

		BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
		String line;

		while ((line = buffer.readLine()) != null) {
			responseString += line;
		}
		System.out.println(responseString);

		JSONObject jsonObj = new JSONObject(responseString.toString());

		// System.out.println(jsonObj);
		System.out.println(jsonObj.getJSONObject("success").get("amount"));
		String amount = (String) jsonObj.getJSONObject("success").get("amount");
		double amt = Double.parseDouble(amount);

		DecimalFormat df = new DecimalFormat("#.00");
		Integer newamt = Integer.parseInt(df.format(amt));
		System.out.println(newamt);

		// release all resources held by the responseHttpEntity
		EntityUtils.consume(responseHttpEntity);

		// close the stream
		response.close();

		return newamt;
	}
}
