package com.example.prohunt;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GetMapActivity extends ActionBarActivity implements
		OnInfoWindowClickListener {
	double latitude = 0.0;
	double longitude = 0.0;
	Location location;
	TextView txtView;
	int i = 1;
	List<Node> ListNode = new ArrayList<Node>();
	List<MyItem> Item = new ArrayList<MyItem>();
	GoogleMap map;
	boolean secondPage = false;
	private HashMap<Marker, MyItem> visibleMarkers = new HashMap<Marker, MyItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_map);
		txtView = (TextView) findViewById(R.id.txtView);
		latitude = getIntent().getExtras().getDouble("LATITUDE");
		longitude = getIntent().getExtras().getDouble("LONGITUDE");
		String theUrl = getIntent().getExtras().getString("url");
		String theLink = getIntent().getExtras().getString("link1");
		if (theUrl == null) {
			theUrl = theLink;
		}
		new HttpAsyncTask().execute(theUrl);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMyLocationEnabled(true);
		location = map.getMyLocation();

	}

	public void placeMarker(String location) {
		LatLng seattle = new LatLng(latitude, longitude);
		map.addMarker(new MarkerOptions().title("Seattle")
				.snippet("The city of lakes.").position(seattle));
		txtView.setText("returned data " + location);
	}

	/* GET function to get data from a server */
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

	private class HttpAsyncTask extends AsyncTask<String, Void, List<Node>> {
		@Override
		protected List<Node> doInBackground(String... urls) {

			return GET(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		protected void onPostExecute(final List<Node> result) {
			Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG)
					.show();
			String theUrl = "http://api.indeed.com/ads/apisearch?publisher=371468820033530&q=&l="
					+ "Seattle"
					+ "%2C+wa&sort=&radius=25&st=employer&jt=fulltime&start=25&limit=26&fromage=&filter=2&latlong=1&co=us&chnl=&userip=1.2.3.4&useragent=Mozilla/%2F4.0%28Firefox%29&v=2";
			tagLocation();

		}
	}

	/* It tag the location of the company on the map */
	public void tagLocation() {
		int i = 0;
		int j = ListNode.size();
		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		for (i = 0; i < j; i++) {
			List<Address> addresses = null;
			String locate = ListNode.get(i).company + " "
					+ ListNode.get(i).city + " " + ListNode.get(i).state;
			String url = ListNode.get(i).url;
			String jobtitle = ListNode.get(i).jobtitle;
			String company = ListNode.get(i).company;
			try {
				if (Geocoder.isPresent()) {
					// to chect if geocoder is present or not
					// txtView.setText("geocoder is present");
				}
				addresses = geoCoder.getFromLocationName(locate.toString(), 1);
				if (locate.equals("Amazon Corporate LLC Seattle WA")
						|| locate
								.equals("Amazon Web Services, Inc. Seattle WA")) {
					locate = "Amazon Seattle WA";
					addresses = geoCoder.getFromLocationName(locate.toString(),
							1);
				}
				if (!addresses.isEmpty()) {
					Double lat = (double) (addresses.get(0).getLatitude());
					Double lon = (double) (addresses.get(0).getLongitude());
					final LatLng mPosition = new LatLng(lat, lon);
					counter(locate, mPosition, url, jobtitle, company);
				} else {
					double lon = ListNode.get(i).longitude;
					double lat = ListNode.get(i).latitude;
					final LatLng mPosition2 = new LatLng(lat, lon);
					counter("Multiple", mPosition2, url, jobtitle, company);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		showMarker();
	}

	/* Counter function to keep track of campany and number of job offered by it */
	public void counter(String place, LatLng latlng, String url,
			String jobtitle, String company) {
		if (Item.isEmpty()) {
			MyItem subitem = new MyItem(place, latlng, url, jobtitle, company);
			Item.add(subitem);
		} else {
			Iterator<MyItem> it = Item.iterator();
			int count = 1;
			while (it.hasNext()) {
				MyItem cur = it.next();
				String temp = cur.getCompany();
				if (place.equals(temp)) {
					count++;
					cur.incrementNum(url, jobtitle, company);
					break;
				}
				if (count != 1) {
					break;
				}
			}
			if (count == 1) {
				MyItem subitem = new MyItem(place, latlng, url, jobtitle,
						company);
				Item.add(subitem);
			}
		}
	}

	/*
	 * It shows the marker and info window on the map and also take care of info
	 * window click event by forwarding correct info to the next activity
	 */
	public void showMarker() {
		int m = Item.size();
		Marker[] marker = new Marker[m];
		for (int k = 0; k < Item.size(); k++) {
			String company = Item.get(k).companyName;
			int numberOfJobs = Item.get(k).num;
			LatLng mPosition = Item.get(k).mPosition;
			String[] Company = new String[m];
			Company[k] = company;
			BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(mPosition, 15));
			if (numberOfJobs > 1) {
				Marker m1 = map.addMarker(new MarkerOptions().title(company)
						.snippet(numberOfJobs + " Job openings")
						.icon(bitmapDescriptor).position(mPosition));
				visibleMarkers.put(m1, Item.get(k));
				map.setOnInfoWindowClickListener(this);
			} else {
				Marker m2 = map.addMarker(new MarkerOptions().title(company)
						.snippet(numberOfJobs + " Job openings")
						.position(mPosition));
				map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
				map.setOnInfoWindowClickListener(this);
				visibleMarkers.put(m2, Item.get(k));
			}
		}
	}

	private LatLng addOffset(double lat, double lon, int i) {
		// TODO Auto-generated method stub
		double offset = (i * 0.001);
		lat = lat + offset;
		lon = lon + offset;
		final LatLng latLng = new LatLng(lat, lon);
		return latLng;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_map, menu);
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

	@Override
	public void onInfoWindowClick(Marker marker) {
		MyItem subItem = visibleMarkers.get(marker);

		Intent myIntent = new Intent(this, JobListActivity.class);

		myIntent.putExtra("com.example.prohunt.SubItem", subItem);

		startActivity(myIntent);

	}
}
