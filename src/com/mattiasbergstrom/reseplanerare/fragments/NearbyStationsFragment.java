package com.mattiasbergstrom.reseplanerare.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.mattiasbergstrom.reseplanerare.NearbyStationClickedCallback;
import com.mattiasbergstrom.reseplanerare.R;
import com.mattiasbergstrom.reseplanerare.adapters.StationsAdapter;
import com.mattiasbergstrom.resrobot.ResrobotClient;
import com.mattiasbergstrom.resrobot.ResrobotClient.StationsInZoneCallback;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class NearbyStationsFragment extends Fragment {
	
	private LocationManager locationManager;
	private View view;
	private ListView list;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_nearby_stations, container, false);
		
		list = (ListView) view.findViewById(R.id.nearby_stations_list_view);
		list.setEmptyView(view.findViewById(R.id.nearby_stations_list_empty));
		list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	if(getActivity() instanceof NearbyStationClickedCallback) {
            		NearbyStationClickedCallback callback = (NearbyStationClickedCallback) getActivity();
            		com.mattiasbergstrom.resrobot.Location location = (com.mattiasbergstrom.resrobot.Location)list.getAdapter().getItem(position);
                    
            		callback.nearbyStationClicked(location.getId(), location.getName());
            	}
            }
        });
		
		return this.view;
	}
	
	@Override
	public void onStart() {
		super.onStart();

		String locCtx = Context.LOCATION_SERVICE; 
		locationManager = (LocationManager) getActivity().getSystemService(locCtx);

	    Criteria criteria  = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    criteria.setAltitudeRequired(false);
	    criteria.setBearingRequired(false);
	    criteria.setPowerRequirement(Criteria.POWER_LOW);
	    String provider = locationManager.getBestProvider(criteria, true);
	    
	    if (locationManager.isProviderEnabled(provider)) {
            final Location location = locationManager.getLastKnownLocation(provider);
            
            if(location == null) {
            	list.getEmptyView().setVisibility(View.GONE);
            	list.setEmptyView(view.findViewById(R.id.nearby_stations_list_no_position));
            	
            	return;
            }
            
            final ListView list = (ListView) view.findViewById(R.id.nearby_stations_list_view);
    		
    		new ResrobotClient("319657daa0887321fb8544161b452ef9").stationsInZone(20.315609, 63.800571, 5000, new StationsInZoneCallback() {
    			
    			@Override
    			public void stationsInZoneComplete(
    					ArrayList<com.mattiasbergstrom.resrobot.Location> result) {
    				if(getActivity() != null) {
	    				class SortByDistance implements Comparator<com.mattiasbergstrom.resrobot.Location>{
	    					/**
	    					 * Compares distance from user location in meters between two locations
	    					 */
	    				    public int compare(com.mattiasbergstrom.resrobot.Location location1, com.mattiasbergstrom.resrobot.Location location2) {
	    				    	
	    				    	float[] results = new float[1];
	    				    	Location.distanceBetween(location.getLatitude(), location.getLongitude(), location1.getLatitude(), location1.getLongitude(), results);
	    				    	float distance1 = results[0];
	    				    	
	    				    	Location.distanceBetween(location.getLatitude(), location.getLongitude(), location2.getLatitude(), location2.getLongitude(), results);
	    				    	float distance2 = results[0];
	    				    	
	    				        return Math.round(distance1 - distance2);
	    				    }
	    				}
	    				
	    				@SuppressWarnings("unchecked")
						ArrayList<com.mattiasbergstrom.resrobot.Location> nearbyStations = (ArrayList<com.mattiasbergstrom.resrobot.Location>) result.clone();
	    				
	    				Collections.sort(nearbyStations, new SortByDistance());
	    				
	    				StationsAdapter adapter = new StationsAdapter(getActivity(), R.layout.fragment_nearby_stations_list_row, nearbyStations, location);
	    				
	    				list.setAdapter(adapter);
    				}
    			}
    		});
        }
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
	}
}
