package com.example.prohunt;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class MyAdapter extends ArrayAdapter<Node> {
		public MyAdapter(Context context, List<Node> result) {
		       super(context, R.layout.row_result, result);
		    }

		    @Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		       // Get the data item for this position
		       Node node = getItem(position);    
		       // Check if an existing view is being reused, otherwise inflate the view
		       if (convertView == null) {
		          convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_result, parent, false);
		       }
		       // Lookup view for data population
		       TextView tvCompany = (TextView) convertView.findViewById(R.id.tvCompany);
		       TextView tvJobTitle = (TextView) convertView.findViewById(R.id.tvJobTitle);
		       // Populate the data into the template view using the data object
		       tvCompany.setText(node.company);
		       tvJobTitle.setText(node.jobtitle);
		       // Return the completed view to render on screen
		       return convertView;
		   }


}
