package com.mattiasbergstrom.reseplanerare.activities;

import com.mattiasbergstrom.reseplanerare.Favorite;
import com.mattiasbergstrom.reseplanerare.Favorites;
import com.mattiasbergstrom.reseplanerare.FavoritesCheckCallback;
import com.mattiasbergstrom.reseplanerare.R;
import com.mattiasbergstrom.reseplanerare.RouteResultClickedCallback;
import com.mattiasbergstrom.resrobot.Route;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class RouteResultActivity extends FragmentActivity implements RouteResultClickedCallback, FavoritesCheckCallback {
	
	private Favorites favorites;
	private Favorite favorite;
	private boolean isFavorited;
	private boolean shouldFavoriteButtonBeVisible;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_result);
		getActionBar().setTitle(R.string.search_result_title);	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.favorite_button:
			toggleFavorite();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Toggles between removing and adding a favorite
	 */
	public void toggleFavorite() {
		if(favorites != null && favorite != null) {
			if(favorites.getFavorites().contains(favorite)) {
				// Remove favorite
				favorites.getFavorites().remove(favorite);
				isFavorited = false;
			} else {
				// Add favorite
				favorites.getFavorites().add(favorite);
				isFavorited = true;
			}
			// Update menu and save favorites
			invalidateOptionsMenu();
			favorites.save(this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_route_result, menu);
		MenuItem favoriteItem = menu.findItem(R.id.favorite_button);
		
		// Set icon differently depending on if the search is favorited
		if(isFavorited) {
			favoriteItem.setIcon(R.drawable.star_filled);
		} else {
			favoriteItem.setIcon(R.drawable.star_border);
		}
		
		if(shouldFavoriteButtonBeVisible) {
			favoriteItem.setVisible(true);
		}
		
		return true;
	}

	@Override
	public void routeResultClicked(Route route) {
		Intent intent = new Intent(RouteResultActivity.this, RouteDetailsActivity.class);
		intent.putExtra("route", route);
		startActivity(intent);
	}
	
	@Override
	public void checkFavorite(Favorite favorite) {
		this.favorite = favorite;
		this.shouldFavoriteButtonBeVisible = true;
		
		// Check if the favorite exists, if it does set isFavorited to true
		favorites = Favorites.load(this);
		if(favorites.getFavorites().contains(favorite)) {
			isFavorited = true;
		} else {
			isFavorited = false;
		}
		
		// Update menu
		invalidateOptionsMenu();
	}
}
