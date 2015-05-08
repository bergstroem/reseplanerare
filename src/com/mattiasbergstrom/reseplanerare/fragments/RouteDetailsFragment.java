package com.mattiasbergstrom.reseplanerare.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mattiasbergstrom.reseplanerare.R;
import com.mattiasbergstrom.reseplanerare.adapters.RouteDetailsAdapter;
import com.mattiasbergstrom.resrobot.Route;

public class RouteDetailsFragment extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_route_details, container, false);
		ListView list = (ListView) v.findViewById(R.id.route_details_list_view);
		
		Bundle extras = getActivity().getIntent().getExtras();
		if (extras != null) {
			// Get the route provided and set the adapter for the list.
			Route route = extras.getParcelable("route");
			list.setAdapter(new RouteDetailsAdapter(getActivity(), R.layout.fragment_route_details_list_row, route.getSegments()));
		}
		
		return v;
	}
}