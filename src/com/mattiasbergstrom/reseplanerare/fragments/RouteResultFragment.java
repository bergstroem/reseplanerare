package com.mattiasbergstrom.reseplanerare.fragments;

import java.util.ArrayList;
import java.util.Date;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import com.mattiasbergstrom.reseplanerare.Favorite;
import com.mattiasbergstrom.reseplanerare.FavoritesCheckCallback;
import com.mattiasbergstrom.reseplanerare.R;
import com.mattiasbergstrom.reseplanerare.RouteResultClickedCallback;
import com.mattiasbergstrom.reseplanerare.adapters.RoutesAdapter;
import com.mattiasbergstrom.resrobot.Location;
import com.mattiasbergstrom.resrobot.ResrobotClient.ErrorCallback;
import com.mattiasbergstrom.resrobot.ResrobotClient.FindLocationCallback;
import com.mattiasbergstrom.resrobot.ResrobotClient.SearchCallback;
import com.mattiasbergstrom.resrobot.ResrobotClient;
import com.mattiasbergstrom.resrobot.Route;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class RouteResultFragment extends Fragment {
	View view;
	ResrobotClient client;
	StickyListHeadersListView list;
	ArrayList<Route> routes;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_route_result, container, false);
		
		list = (StickyListHeadersListView) view.findViewById(R.id.route_result_list_view);
		list.setEmptyView(view.findViewById(R.id.route_result_list_empty_fetching));
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// When an item is clicked, call a callback on the activity telling it which route was
				// clicked. The activity then decides what to do.
				if (getActivity() instanceof RouteResultClickedCallback) {
					RouteResultClickedCallback callback = (RouteResultClickedCallback) getActivity();
				    callback.routeResultClicked((Route) list.getAdapter().getItem(position));
				}
			}
		});
		
		if(savedInstanceState != null) {
			// If we have a saved instance state get the saved routes and set the list adapter
			routes = savedInstanceState.getParcelableArrayList("routes");
			if(routes != null) {
				RoutesAdapter adapter = new RoutesAdapter(getActivity(), R.layout.fragment_route_result_list_row, routes);
				list.setAdapter(adapter);
			} else {
				startLookup();
			}
		} else {
			// If no savedInstanceState exists, start a lookup for the routes
			startLookup();
		} 
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Save the routes
		outState.putParcelableArrayList("routes", routes);
		super.onSaveInstanceState(outState);
	}
	
	public void startLookup() {
		Bundle extras = getActivity().getIntent().getExtras();
		if (extras != null) {
			client = new ResrobotClient("319657daa0887321fb8544161b452ef9");
			
			// Get extras from the bundle
			long timeInMillis = extras.getLong("date");
		    final Date date = new Date(timeInMillis);
		    final String from = extras.getString("from");
		    final String to = extras.getString("to");
		    int fromId = extras.getInt("fromId");
		    int toId = extras.getInt("toId");
		    
		    // If IDs where provided, dont try to find location, just do a search
		    if(fromId != 0 && toId != 0) {
		    	
		    	Favorite fav = new Favorite();
				fav.setFrom("");
				fav.setTo("");
				fav.setFromId(fromId);
				fav.setToId(toId);
				
				if(getActivity() != null) {
					// Tell the activity to check the favorites against this search
					if (getActivity() instanceof FavoritesCheckCallback) {
						((FavoritesCheckCallback)getActivity()).checkFavorite(fav);
						// Search
						search(fromId, toId, date);
					}
				}
		    } else {
		    	// Find matches for the user entered from and to locations.
		    	client.findLocation(from, to, new FindLocationCallback() {
					
					@Override
					public void findLocationComplete(ArrayList<Location> fromResult,
							ArrayList<Location> toResult) {
						
						// Default to two valid stations
						int fromId = -1;
						int toId = -1;
						String realFrom = "";
						String realTo = "";
						
						// Find best station matches
						for(int i = 0; i < fromResult.size(); i++) {
							if(fromResult.get(i).isBestMatch()) {
								fromId = fromResult.get(i).getId();
								realFrom = fromResult.get(i).getName();
							}
						}
						for(int i = 0; i < toResult.size(); i++) {
							if(toResult.get(i).isBestMatch()) {
								toId = toResult.get(i).getId();
								realTo = toResult.get(i).getName();
							}
						}
						
						// If no matches where found, present a text explaining it to the user
						if(toId == -1 || fromId == -1) {
							list.getEmptyView().setVisibility(View.GONE);
							list.setEmptyView(view.findViewById(R.id.route_result_list_empty_no_results));
							return;
						}
						
						Favorite fav = new Favorite();
						fav.setFrom(realFrom);
						fav.setTo(realTo);
						fav.setFromId(fromId);
						fav.setToId(toId);
						
						if(getActivity() != null) {
							// Tell the activity to check the favorites against this search
							if (getActivity() instanceof FavoritesCheckCallback) {
								((FavoritesCheckCallback)getActivity()).checkFavorite(fav);
								// Search
								search(fromId, toId, date);
							}
		
						}
					}
				});
		    }
		}
	}
	
	public void search(int fromId, int toId, Date date) {
		// Do the search
		
		client.search(String.valueOf(fromId), String.valueOf(toId), date, false, new SearchCallback() {
			
			@Override
			public void searchComplete(ArrayList<Route> result) {
				if(result.size() == 0) {
					// Tell the user no results where found
					list.getEmptyView().setVisibility(View.GONE);
					list.setEmptyView(view.findViewById(R.id.route_result_list_empty_no_results));
					return;
				}
				
				// If activity == null, then the user has most likely left the page and we cant present the data
				if(getActivity() != null) {
					routes = result;
					RoutesAdapter adapter = new RoutesAdapter(getActivity(), R.layout.fragment_route_result_list_row, result);
					list.setAdapter(adapter);
				}
			}
		}, new ErrorCallback() {
			
			@Override
			public void errorOccurred() {
				// Show error dialog telling the user something wen't wrong when fetching the routes
				new AlertDialog.Builder(getActivity())
			    .setTitle(R.string.error)
			    .setMessage(R.string.something_wrong_download)
			    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // Do nothing
			        }
			     })
			     .show();
			}
		});
	}
}
