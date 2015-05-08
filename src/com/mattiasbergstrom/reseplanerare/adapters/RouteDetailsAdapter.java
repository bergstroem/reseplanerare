package com.mattiasbergstrom.reseplanerare.adapters;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mattiasbergstrom.reseplanerare.R;
import com.mattiasbergstrom.resrobot.RouteSegment;

public class RouteDetailsAdapter extends ArrayAdapter<RouteSegment> {
	private final Context context;
	private LinkedList<RouteSegment> routeSegments;
	
	public RouteDetailsAdapter(Context context, int resource, List<RouteSegment> objects) {
		super(context, resource, objects);
		this.routeSegments = (LinkedList<RouteSegment>) objects;
		this.context = context;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		SimpleDateFormat dt = new SimpleDateFormat("HH:mm", Locale.getDefault());
		View rowView = null;
		
		// If last item use different layout
		if(position == routeSegments.size() - 1) {
			rowView = inflater.inflate(R.layout.fragment_route_details_list_row_last, parent, false);
			TextView goalArrivalName = (TextView) rowView.findViewById(R.id.route_details_goal_name);
			TextView goalArrivalTime = (TextView) rowView.findViewById(R.id.route_details_goal_arrival);
			
			String arrivalTime = dt.format(routeSegments.get(position).getArrival().getDateTime());
			
			// Set the name and time of the goal arrival
			goalArrivalName.setText(routeSegments.get(position).getArrival().getLocation().getName());
			goalArrivalTime.setText(arrivalTime);
			
		} else { // Use regular layout
			rowView = inflater.inflate(R.layout.fragment_route_details_list_row, parent, false);
		}
		
		// Make the timeline "line" shorter on the first item. This way the line seems to start inside
		// the timeline dot
		if(position == 0) {
			View timeLine = rowView.findViewById(R.id.timeline);
			MarginLayoutParams marginLayout = (MarginLayoutParams) timeLine.getLayoutParams();
			marginLayout.topMargin = (int) (22 * context.getResources().getDisplayMetrics().density);
			timeLine.setLayoutParams(marginLayout);
		} 
		
		// Get views
		TextView segmentName = (TextView) rowView.findViewById(R.id.route_details_segment_name);
		TextView segmentType = (TextView) rowView.findViewById(R.id.route_details_segment_type);
		TextView segmentDepartureTime = (TextView) rowView.findViewById(R.id.route_details_segment_departure_time);
		TextView segmentDuration = (TextView) rowView.findViewById(R.id.route_details_segment_duration);
		
		// Set name of station
		segmentName.setText(routeSegments.get(position).getDeparture().getLocation().getName());
		
		// Set name of travel type, and which line (bus line for example) of applicable
		String segmentTypeString = routeSegments.get(position).getSegmentId().getTransportType().getName();
		
		if(routeSegments.get(position).getSegmentId().getCarrier() != null) {
			segmentTypeString += " " + routeSegments.get(position).getSegmentId().getCarrier().getNumber();
		}
		segmentType.setText(segmentTypeString);
		
		// Set departure time (and arrival if applicable) 
		String departureTime = dt.format(routeSegments.get(position).getDeparture().getDateTime());
		
		// Stop index out of bounds. No need to check upper limit since we are only looking back
		if(position > 0) {
			String arrivalTime = dt.format(routeSegments.get(position-1).getArrival().getDateTime());
			
			// If arrival time does not match departure time we need to put the arrival time int there to
			if(!arrivalTime.equals(departureTime)) {
				TextView segmentArrivalTime = (TextView) rowView.findViewById(R.id.route_details_segment_arrival_time);
				segmentArrivalTime.setVisibility(View.VISIBLE);
				segmentArrivalTime.setText(arrivalTime);
			}
		}
		segmentDepartureTime.setText(segmentDepartureTime.getText() + departureTime);
		
		
		// Set duration of travel (only present hours if applicable)
		long durationMilliseconds = routeSegments.get(position).getArrival().getDateTime().getTime() - routeSegments.get(position).getDeparture().getDateTime().getTime();
		String durationString = "";
		long millisecondsPerHour = 1000 * 60 * 60;
		long hours = TimeUnit.MILLISECONDS.toHours(durationMilliseconds);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMilliseconds - (millisecondsPerHour * TimeUnit.MILLISECONDS.toHours(durationMilliseconds)));
		
		if(hours > 0) {
			durationString += "" + hours + ((hours == 1) ? " timme " : " timmar ");
		}
		if(minutes > 0) {
			durationString += "" + minutes + ((minutes == 1) ? " minut" : " minuter");
		}
		
		segmentDuration.setText(durationString);
		
		if(position == routeSegments.size() - 1) {
			
		}
		
		return rowView;
	}
}
