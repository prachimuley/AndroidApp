package com.example.prohunt;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class JobListActivity extends Activity {

	MyItem mSubItem;
	TextView txtView;
	ListView list;
	String company = "";
	String numOfJobs = "";
	Context thisContext = this;
	List<MyItemList> mItemList = new ArrayList<MyItemList>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_list);
		txtView = (TextView) findViewById(R.id.txtView);
		list = (ListView) findViewById(R.id.list);
		Bundle bundle = getIntent().getExtras();
		mSubItem = bundle.getParcelable("com.example.prohunt.SubItem");
		mItemList = mSubItem.jobList();
		adapterMethod();
	}

	/* Function for showing list after clicking info window of the map */
	public void adapterMethod() {
		MyMapAdapter adapter = new MyMapAdapter(this, mItemList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getBaseContext(), "New page opened!!",
						Toast.LENGTH_LONG).show();
				Intent myIntent = new Intent(thisContext,
						DescriptionActivity.class);
				String url = mItemList.get(position).url;
				myIntent.putExtra("url", url);
				startActivity(myIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.job_list, menu);
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
