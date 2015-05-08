package com.mattiasbergstrom.reseplanerare.adapters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mattiasbergstrom.reseplanerare.R;
import com.mattiasbergstrom.resrobot.RouteSegment;

public class DeparturesAdapter extends ArrayAdapter<RouteSegment> {
	private final Context context;
	private final ArrayList<RouteSegment> routes;
	
	public DeparturesAdapter(Context context,
			int resource, List<RouteSegment> objects) {
		super(context, resource, objects);
		this.context = context;
		this.routes = (ArrayList<RouteSegment>) objects;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		String type = routes.get(position).getSegmentId().getTransportType().getName();
		String number = "" + routes.get(position).getSegmentId().getCarrier().getNumber();
		String direction = routes.get(position).getDirection();
		
		// Determine hours and minutes until departure
		Calendar c = Calendar.getInstance();
		long timeDifferenceInMilliseconds = Math.abs(routes.get(position).getDeparture().getDateTime().getTime() - c.getTimeInMillis());
		long hours = TimeUnit.MILLISECONDS.toHours(timeDifferenceInMilliseconds);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifferenceInMilliseconds - (1000*60*60*TimeUnit.MILLISECONDS.toHours(timeDifferenceInMilliseconds)));
		
		// Inflate view and get the views in it
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.fragment_departures_list_row, parent, false);
		TextView stationName = (TextView) rowView.findViewById(R.id.departures_station_route);
		TextView departureTime = (TextView) rowView.findViewById(R.id.departures_station_time);
		
		// TODO: This should not be hard coded strings. But it can produce grammar
		// problems when translated directly. So currently it stays as hard coded.
		String timeDifferenceString = "AvgŒr ";
		
		// Determine how to present the time (hours and minute(s), just minute(s) or "now")
		if(hours == 0 && minutes == 0) {
			timeDifferenceString = "nu";
		} else {
			timeDifferenceString += "om ";
			
			if(hours > 0) {
				timeDifferenceString += hours + "h ";
			}
			if(minutes > 0) {
				timeDifferenceString += minutes + ((minutes == 1) ? " minut" : " minuter");
			}
		}
		
		stationName.setText(type + " " + number + " mot " + direction);
		departureTime.setText(timeDifferenceString);
		
		return rowView;
	}
}
