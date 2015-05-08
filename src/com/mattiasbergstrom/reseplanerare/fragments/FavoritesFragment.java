package com.mattiasbergstrom.reseplanerare.fragments;

import java.util.Date;

import com.mattiasbergstrom.reseplanerare.Favorite;
import com.mattiasbergstrom.reseplanerare.Favorites;
import com.mattiasbergstrom.reseplanerare.FavoritesClickedCallback;
import com.mattiasbergstrom.reseplanerare.R;
import com.mattiasbergstrom.reseplanerare.adapters.FavoritesAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FavoritesFragment extends Fragment {
	View view;
	Favorites favorites;
	FavoritesAdapter adapter;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_favorites, container, false);
		adapter = new FavoritesAdapter(getActivity());
		
		ListView list = (ListView) view.findViewById(R.id.favorites_list_view);
		list.setEmptyView(view.findViewById(R.id.favorites_list_empty));
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
				if (getActivity() instanceof FavoritesClickedCallback) {
					FavoritesClickedCallback callback = (FavoritesClickedCallback) getActivity();
					callback.favoriteClicked(((Favorite)adapter.getItem(position)).getFromId(), 
											 ((Favorite)adapter.getItem(position)).getToId(), 
											 (new Date()).getTime());
				}
			}
			
		});
		
		return view;
	}
	
	@Override
	public void onResume() {
		// Update favorites
		Favorites favorites = Favorites.load(getActivity());
		adapter.setFavorites(favorites.getFavorites());
		super.onResume();
	}
	
	@Override
	public void onPause() {
		// Save favorites
		if(favorites != null) {
			favorites.save(getActivity());
		}
		
		super.onPause();
	}
}
