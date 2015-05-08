package com.mattiasbergstrom.reseplanerare.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import com.mattiasbergstrom.reseplanerare.R;
import com.mattiasbergstrom.resrobot.Route;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RoutesAdapter extends ArrayAdapter<Route> implements StickyListHeadersAdapter {

	private final Context context;
	private final ArrayList<Route> routes;
	private LayoutInflater inflater;
		
	public RoutesAdapter(Context context, int resource, List<Route> objects) {
		super(context, resource, objects);
		this.inflater = LayoutInflater.from(context);
		this.routes = (ArrayList<Route>) objects;
		this.context = context;
		
		Activity a = (Activity)context;
		
		// Set subtitle in actionbar if there exists a route in the list
		if(routes != null && routes.size() > 0) {
			a.getActionBar().setSubtitle(routes.get(0).getSegments().getFirst().getDeparture().getLocation().getName() + " - " + routes.get(0).getSegments().getLast().getArrival().getLocation().getName());
		}
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.fragment_route_result_list_row, parent, false);
		TextView textView = (TextView)rowView.findViewById(R.id.route_result_from_to_time);
		TextView swaps = (TextView) rowView.findViewById(R.id.route_result_swaps);
		
		// Determine travel time
		String startStopTime = "";
		
		SimpleDateFormat dt = new SimpleDateFormat("HH:mm", Locale.getDefault());
		Date fromDate = routes.get(position).getSegments().getFirst().getDeparture().getDateTime();
		Date toDate = routes.get(position).getSegments().getLast().getArrival().getDateTime();
		startStopTime = dt.format(fromDate) + " - " + dt.format(toDate);
		
		textView.setText(startStopTime);
		
		textView = (TextView)rowView.findViewById(R.id.route_result_duration);
		
		String duration = " (";
		
		long dif = (long) Math.abs((toDate.getTime() - fromDate.getTime()));
		
		if(TimeUnit.MILLISECONDS.toDays(dif) > 0)
			duration += TimeUnit.MILLISECONDS.toDays(dif) + " dagar ";
		long millisecondsPerDay = 86400000;
		dif = dif - (millisecondsPerDay*TimeUnit.MILLISECONDS.toDays(dif));
		
		if(TimeUnit.MILLISECONDS.toHours(dif) > 0)
			duration += TimeUnit.MILLISECONDS.toHours(dif) + "h ";
		
		long milliSecondsPerHour = 3600000;
		dif = dif - (milliSecondsPerHour*TimeUnit.MILLISECONDS.toHours(dif));
		
		if(TimeUnit.MILLISECONDS.toMinutes(dif) > 0)
			duration += TimeUnit.MILLISECONDS.toMinutes(dif) + " min";
		
		if(duration.endsWith(" "))
			duration = duration.substring(0, duration.length()-1);
		
		duration += ")";
		
		// Set travel start time, end time and duration
		textView.setText(duration);
		
		// Set travel swaps
		swaps.setText(routes.get(position).getSegments().size() - 1 + " byten");
		
		return rowView;
	}
	
	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.fragment_route_result_list_header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.route_result_header_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set the date as text in the header
        SimpleDateFormat dt = new SimpleDateFormat("EEEE dd MMMM", Locale.getDefault());
		String date = dt.format(routes.get(position).getSegments().getFirst().getDeparture().getDateTime());
        holder.text.setText(date);

        return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		long millisecsondsPerDay = 86400000;
		long time = routes.get(position).getSegments().getFirst().getDeparture().getDateTime().getTime() / millisecsondsPerDay;
		return time;
	}
	
	class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
    }
}
