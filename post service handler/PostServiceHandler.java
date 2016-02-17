package Extras;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;



import android.util.Log;



public class PostServiceHandler {

	private final String TAG;

	private int maxAttempts;

	private int backOff;

	public PostServiceHandler(String logtag, int maxAttempts, int backOff) {
		
		if(logtag==null){
			throw new IllegalArgumentException("you must provide tag");
		}
		
		if(maxAttempts>5 || maxAttempts<0){
			throw new IllegalArgumentException("max attempt must be between(0-5)");
		}
		
		if(backOff<500 || backOff>10000){
			throw new IllegalArgumentException("back Off must be between(500-10000)");
		}

		TAG=logtag+ " " +PostServiceHandler.class.getSimpleName();
		this.maxAttempts=maxAttempts;
		this.backOff=backOff;		

	}


	public String doPostRequest(String serverUrl, HashMap<String, String> params) {

		for (int i = 1; i <= maxAttempts; i++) {

			Log.d(TAG, "Attempt #" + i);

			try
			{				

				return post(serverUrl, params);

			} catch (IOException e) {

				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				
				Log.e(TAG, "Failed on attempt " + i + ":" + e);

				if (i == maxAttempts) {
					break;
				}
				try {

					Log.d(TAG, "Sleeping for " + backOff + " ms before retry");
					Thread.sleep(backOff);
				} catch (InterruptedException e1) {

					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return "";
				}

				// increase backoff exponentially
				backOff *= 2;
			}
		}
		return "";
	} 


	private String constructBody(Map<String, String> params)
	{
		StringBuilder bodyBuilder = new StringBuilder();

		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();

		// constructs the POST body using the parameters
		while (iterator.hasNext()) {

			Entry<String, String> param = iterator.next();

			bodyBuilder.append(param.getKey()).append('=').append(param.getValue());

			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}

		}

		return bodyBuilder.toString();
	}


	//Issue a POST request to the server.

	private String post(String endpoint, Map<String, String> params)
			throws IOException {    

		URL url;

		String body="";

		String response = "";

		byte[] bytes;

		Log.i(TAG, "URL: " + endpoint);

		try {

			url = new URL(endpoint);

		} catch (MalformedURLException e) {

			throw new IllegalArgumentException("invalid url: " + endpoint);

		}

		StringBuilder bodyBuilder = new StringBuilder();

		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();

		// constructs the POST body using the parameters
		while (iterator.hasNext()) {

			Entry<String, String> param = iterator.next();

			bodyBuilder.append(param.getKey()).append('=').append(param.getValue());

			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}

		}

		body=bodyBuilder.toString();

		Log.v(TAG, "Posting: " + body + " to " + url);

		bytes = body.getBytes();

		HttpURLConnection conn = null;

		try 
		{

			Log.i(TAG, "URL->" + url);

			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			conn.setFixedLengthStreamingMode(bytes.length);

			conn.setRequestMethod("POST");
			//conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");

			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();

			// handle the response
			int status = conn.getResponseCode();

			Log.i(TAG, "RESPONSE from server: "+status);

			Log.i(TAG, "JSON Response Message: "+conn.getResponseMessage());

			if (status == HttpURLConnection.HTTP_OK) {
				String line;
				BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line=br.readLine()) != null) {
					response+=line;
				}
				Log.i(TAG, "RESPONSE STRING: "+response);
				
				
				
			}
			else
			{
				throw new IOException("Post failed with error code " + status);
			}


		} finally {

			if (conn != null) {

				conn.disconnect();

			}

		}
		return response;
	}


}

