package com.mattiasbergstrom.reseplanerare.adapters;

import java.util.LinkedList;

import com.mattiasbergstrom.reseplanerare.Favorite;
import com.mattiasbergstrom.reseplanerare.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FavoritesAdapter extends BaseAdapter {
	private LinkedList<Favorite> favorites;	
	private LayoutInflater inflater;
	
	public FavoritesAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return favorites != null ? favorites.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return favorites != null ? favorites.get(position) : 0;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the views
        View view = inflater.inflate(R.layout.fragment_favorites_list_row, parent, false);
        TextView fromView = (TextView) view.findViewById(R.id.favorites_from);
        TextView toView = (TextView) view.findViewById(R.id.favorites_to);
        
        // Set from and to text
        fromView.setText(favorites.get(position).getFrom());
        toView.setText(favorites.get(position).getTo());
        
		return view;
	}

	/* Getters / setters */
	
	public LinkedList<Favorite> getFavorites() {
		return favorites;
	}

	public void setFavorites(LinkedList<Favorite> favorites) {
		this.favorites = favorites;
		this.notifyDataSetChanged();
	}
}
