package com.example.prohunt;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends ActionBarActivity {
	TextView txtView;
	ListView list;
	Context thisContext = this;
	String value = "";
	String data = "";
	List<Node> ListNode = new ArrayList<Node>();
	int i = ListNode.size();
	boolean secondPage = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		txtView = (TextView) findViewById(R.id.txtView);
		list = (ListView) findViewById(R.id.list);
		String theUrl = getIntent().getExtras().getString("url");
		new HttpAsyncTask().execute(theUrl);
	}

	/* This GET function will get the data back from the server. */
	public List<Node> GET(String url) {

		InputStream inputStream = null;
		XMLParsar Parser = new XMLParsar();
		List<Result> results = null;
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
				results = Parser.parse(inputStream);
			// result = convertInputStreamToString(inputStream);
			else
				results = null;

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}
		i = results.size();
		for (int i = 0; i < results.size(); i++) {
			String company = results.get(i).company;
			String jobtitle = results.get(i).jobtitle;
			String url1 = results.get(i).url;
			double longitude = results.get(i).longitude;
			double latitude = results.get(i).latitude;
			String city = results.get(i).city;
			String state = results.get(i).state;
			Node node = new Node(company, jobtitle, url1, longitude, latitude,
					city, state);
			ListNode.add(i, node);
		}

		return ListNode;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * This sub class is used to send an HTTP request to the server. This class
	 * is using a separate thread to send the request and getting the result
	 * back from the same thread
	 */

	private class HttpAsyncTask extends AsyncTask<String, Void, List<Node>> {
		@Override
		protected List<Node> doInBackground(String... urls) {

			return GET(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(final List<Node> result) {
			Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG)
					.show();
			String theUrl = "http://api.indeed.com/ads/apisearch?publisher=371468820033530&q=&l="
					+ "Seattle"
					+ "%2C+wa&sort=&radius=25&st=employer&jt=fulltime&start=25&limit=26&fromage=&filter=2&latlong=1&co=us&chnl=&userip=1.2.3.4&useragent=Mozilla/%2F4.0%28Firefox%29&v=2";
			if (!secondPage) {
				secondPage = true;
				new HttpAsyncTask().execute(theUrl);
			}
			MyAdapter adapter = new MyAdapter(thisContext, result);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Toast.makeText(getBaseContext(), "New page opened!!",
							Toast.LENGTH_LONG).show();
					Intent myIntent = new Intent(thisContext,
							DescriptionActivity.class);
					String url = result.get(position).url;
					myIntent.putExtra("url", url);
					startActivity(myIntent);

				}
			});
		}
	}

}
