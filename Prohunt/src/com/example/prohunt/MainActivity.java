package com.example.prohunt;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
//import android.app.ListActivity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	Button list, map, search;
	TextView txtView;
	EditText query, city, state, radius;
	private LocationManager mLocMgr;
	double latitude = 0.0;
	double longitude = 0.0;
	String cityName = "";
	String data = "";
	String link = "";
	String link1 = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list = (Button) findViewById(R.id.button_send1);
		if (isConnected()) {
		} else {
		}
		link = "http://api.indeed.com/ads/apisearch?publisher=371468820033530&q=&l="
				+ "lynnwood"
				+ "%2C+wa&sort=&radius=15&st=employer&jt=fulltime&start=&limit=26&fromage=&filter=2&latlong=1&co=us&chnl=&userip=1.2.3.4&useragent=Mozilla/%2F4.0%28Firefox%29&v=2";
		list.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
				Intent myIntent = new Intent(MainActivity.this,
						ListActivity.class);
				Toast.makeText(MainActivity.this,
						"The List button was clicked.", Toast.LENGTH_LONG)
						.show();
				if (cityName == "") {
					// Ask for a city
				}
				myIntent.putExtra("cityname", cityName);
				myIntent.putExtra("url", link);
				MainActivity.this.startActivity(myIntent);
			}
		}); 

		map = (Button) findViewById(R.id.button_send2);
		map.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
				Intent myIntent = new Intent(MainActivity.this,
						GetMapActivity.class);
				Toast.makeText(MainActivity.this,
						"The Map button was clicked.", Toast.LENGTH_LONG)
						.show();
				myIntent.putExtra("url", link);
				myIntent.putExtra("LATITUDE", latitude);
				myIntent.putExtra("LONGITUDE", longitude);
				MainActivity.this.startActivity(myIntent);
			}
		});

		query = (EditText) findViewById(R.id.edittext1);
		city = (EditText) findViewById(R.id.edittext2);
		state = (EditText) findViewById(R.id.edittext3);
		radius = (EditText) findViewById(R.id.edittext4);
		search = (Button) findViewById(R.id.button_send3);
		search.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
				final String query_data = query.getText().toString()
						.toLowerCase().replaceAll("\\s+", "%20");
				final String city_data = city.getText().toString()
						.toLowerCase().replaceAll("\\s+", "%20");
				final String state_data = state.getText().toString()
						.toLowerCase().replaceAll("\\s+", "");
				final String radius_data = radius.getText().toString()
						.toLowerCase().replaceAll("\\s+", "");
				link1 = "http://api.indeed.com/ads/apisearch?publisher=371468820033530&q="
						+ query_data
						+ "&l="
						+ city_data
						+ "%2C+"
						+ state_data
						+ "&sort=&radius="
						+ radius_data
						+ "&st=employer&jt=fulltime&start=&limit=26&fromage=&filter=2&latlong=1&co=us&chnl=&userip=1.2.3.4&useragent=Mozilla/%2F4.0%28Firefox%29&v=2";
				Intent myIntent = new Intent(MainActivity.this,
						GetMapActivity.class);
				myIntent.putExtra("link1", link1);
				myIntent.putExtra("LATITUDE", latitude);
				myIntent.putExtra("LONGITUDE", longitude);
				MainActivity.this.startActivity(myIntent);
			}
		});
        /*For make done on keyboard after filling radius to work as same as clicking search button*/
		radius.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				boolean handled = false;
				if (arg1 == EditorInfo.IME_ACTION_DONE) {
					// TODO do something
					search.performClick();
					return true;
				}
				return false;
			}
		});

		String city1 = getLocation();
	}

	/* This function is used to check network connection */
	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	/* This function is used to get the location */
	public String getLocation() {
		mLocMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
		LocationListener mLocationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onProviderDisabled(String provider) {
			}

			@Override
			public void onLocationChanged(Location location) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				//String location1 = getLocationName(latitude, longitude);
			}
		};
		mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				mLocationListener);
		return cityName;
	}

	/* This function is used to filter the cityName from the address */
	/*
	 * public String getLocationName(double lattitude, double longitude) {
	 * 
	 * String cityName = "Not Found"; Geocoder gcd = new
	 * Geocoder(getBaseContext(), Locale.getDefault()); try {
	 * 
	 * List<Address> addresses = gcd.getFromLocation(lattitude, longitude, 1);
	 * 
	 * for (Address adrs : addresses) { if (adrs != null) {
	 * 
	 * String city = adrs.getLocality(); if (city != null && !city.equals("")) {
	 * cityName = city; System.out.println("city ::  " + cityName); } else {
	 * 
	 * } // // you should also try with addresses.get(0).toSring();
	 * 
	 * }
	 * 
	 * } } catch (IOException e) { e.printStackTrace(); } return cityName;
	 * 
	 * }
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
}
