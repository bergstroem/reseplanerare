package com.mattiasbergstrom.reseplanerare.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mattiasbergstrom.reseplanerare.R;
import com.mattiasbergstrom.reseplanerare.adapters.DeparturesAdapter;
import com.mattiasbergstrom.resrobot.ResrobotClient;
import com.mattiasbergstrom.resrobot.ResrobotClient.DeparturesCallback;
import com.mattiasbergstrom.resrobot.ResrobotClient.ErrorCallback;
import com.mattiasbergstrom.resrobot.RouteSegment;

public class DeparturesFragment extends Fragment {
	private ListView list;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_departures, container, false);
		
		list = (ListView) v.findViewById(R.id.departures_list_view);
		list.setEmptyView(v.findViewById(R.id.departures_list_empty));
		
		Bundle extras = getActivity().getIntent().getExtras();
		if (extras != null) {
		    int id = extras.getInt("locationId");
		    
		    new ResrobotClient("", "174d0e15e29947d371c671ada7dfa254").departures(id, 120, new DeparturesCallback() {
				
				@Override
				public void departuresComplete(ArrayList<RouteSegment> result) {
					// Check that the activity actually still exists.
					// It will be null if the user has left the page and then we don't want to do anything
					if(getActivity() != null) {
						
						if(result.size() > 0) {
							DeparturesAdapter adapter = new DeparturesAdapter(getActivity(), R.layout.fragment_departures_list_row, result);
							list.setAdapter(adapter);
						} else {
							list.getEmptyView().setVisibility(View.GONE);
							list.setEmptyView(v.findViewById(R.id.departures_list_no_result));
						}
						
					}
				}
			}, new ErrorCallback() {
				
				@Override
				public void errorOccurred() {
					list.getEmptyView().setVisibility(View.GONE);
					list.setEmptyView(v.findViewById(R.id.departures_list_no_result));
				}
			});
		}
		
		return v;
	}
}
