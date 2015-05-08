package com.mattiasbergstrom.reseplanerare.adapters;

import java.util.ArrayList;
import java.util.List;

import com.mattiasbergstrom.reseplanerare.R;
import com.mattiasbergstrom.resrobot.Location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StationsAdapter extends ArrayAdapter<Location> {
	private final Context context;
	private final ArrayList<Location> routes;
	
	private android.location.Location userLocation;
	
	public StationsAdapter(Context context,
			int resource, List<Location> objects, android.location.Location userLocation) {
		super(context, resource, objects);
		this.context = context;
		this.routes = (ArrayList<Location>) objects;
		this.userLocation = userLocation;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.fragment_nearby_stations_list_row, parent, false);
		TextView stationName = (TextView) rowView.findViewById(R.id.nearby_station_name);
		TextView stationDistance = (TextView) rowView.findViewById(R.id.nearby_station_distance);
		
		// Set the name of the station
		stationName.setText(routes.get(position).getName());
		
		// Determine the geographical distance between the user locationa and station location 
		float[] distanceResult = new float[1];
		android.location.Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), 
				routes.get(position).getLatitude(), routes.get(position).getLongitude(), distanceResult);
		
		// Set the distance
		stationDistance.setText(String.valueOf(Math.round(distanceResult[0])) + "m");
		
		return rowView;
	}
}
