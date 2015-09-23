package com.example.prohunt;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyMapAdapter extends ArrayAdapter<MyItemList>{

	public MyMapAdapter(Context context, List<MyItemList> result) {
		//super(context, result);
		super(context, R.layout.row_result, result);
		// TODO Auto-generated constructor stub
	}
	  @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	       // Get the data item for this positio
	       MyItemList mItemList = getItem(position);
	       // Check if an existing view is being reused, otherwise inflate the view
	       if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_result, parent, false);
	       }
	       // Lookup view for data population
	       TextView tvCompany = (TextView) convertView.findViewById(R.id.tvCompany);
	       TextView tvJobTitle = (TextView) convertView.findViewById(R.id.tvJobTitle);
	       
	       // Populate the data into the template view using the data object
	       tvCompany.setText(mItemList.company);
	       tvJobTitle.setText(mItemList.jobtitle);
	       // Return the completed view to render on screen
	       return convertView;
	   }
	
	

}
