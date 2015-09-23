package com.example.prohunt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

//import com.example.prohunt.GetMapActivity.HttpAsyncTask;

public class HTMLParsar {
	String result = "";
	GetMapActivity Delegate;
	public HTMLParsar(GetMapActivity theMap)
	{
		Delegate = theMap;
	}

	public void parse(String url) {
		// TODO Auto-generated method stub
		//new HttpAsyncTask().execute(url);
		//String Data = "waiting for data";
		new HttpAsyncTask().execute(url);
		//return Data;
	}
	/*public void getLocation(String url_1)
	{
		new HttpAsyncTask().execute(url_1);
	}*/

	/*public void sleep() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				// my_button.setBackgroundResource(R.drawable.defaultcard);
			}
		}, 20000);
	}*/

	public static String GET(String url) {
		InputStream inputStream = null;
		String result1= "";

		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();
			result1 = convertInputStreamToString(inputStream);
		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result1;
	}

	/* This function is used to convert the data from the server into String */
	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}
	public String retrivedata(String Data)
	{
		String HTML = Data;
		String link="";
		for(int i=0; i<=HTML.length();i++)
		{
			if(HTML.charAt(i)=='r' && HTML.charAt(i+1)=='a' && HTML.charAt(i+2)=='q' && HTML.charAt(i+3)=='u' && HTML.charAt(i+4)=='o')
			{
				for(int j =i+5; j<=HTML.length();j++)
				{
					if(HTML.charAt(j)=='\"')
					{
						for(int k=j+1; k<=HTML.length(); k++)
						{
							link = link + HTML.substring(k, k+1);
							//k++;
							if(HTML.charAt(k+1)=='\"')
							{
								break;
							}
						}
						break;
			         }
		         }
				break;
			 }
		 }
		//String url_1 = "www.indeed.com"+link;
		//getLocation(url_1);
		return link;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			return GET(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String results) {
			result = results;
			String link = retrivedata(result);
			//String location = retriveAddress(link);
			Delegate.placeMarker(link);
			

		}

		private String retriveAddress(String link) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
	


