package com.mattiasbergstrom.reseplanerare.activities;

import java.util.Date;
import java.util.Locale;

import com.mattiasbergstrom.reseplanerare.FavoritesClickedCallback;
import com.mattiasbergstrom.reseplanerare.NearbyStationClickedCallback;
import com.mattiasbergstrom.reseplanerare.R;
import com.mattiasbergstrom.reseplanerare.SearchRouteCallback;
import com.mattiasbergstrom.reseplanerare.fragments.FavoritesFragment;
import com.mattiasbergstrom.reseplanerare.fragments.NearbyStationsFragment;
import com.mattiasbergstrom.reseplanerare.fragments.SearchRouteFragment;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainTabsActivity extends FragmentActivity implements
		ActionBar.TabListener, SearchRouteCallback, FavoritesClickedCallback, NearbyStationClickedCallback {

	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tabs);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_tabs, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.about:
			Intent intent = new Intent(MainTabsActivity.this, AboutActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new SearchRouteFragment();
				break;
			case 1:
				fragment = new NearbyStationsFragment();
				break;
			case 2:
				fragment = new FavoritesFragment();
				break;
			}
			
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	@Override
	public void searchRoute(String from, String to, Date date) {
		// Move to the route results activity and provide the search info
		Intent intent = new Intent(MainTabsActivity.this, RouteResultActivity.class);
		intent.putExtra("date", date.getTime());
		intent.putExtra("from", from);
		intent.putExtra("to", to);
		
		startActivity(intent);
	}

	@Override
	public void favoriteClicked(int fromId, int toId, long time) {
		Intent intent = new Intent(MainTabsActivity.this, RouteResultActivity.class);
        intent.putExtra("fromId", fromId);
        intent.putExtra("toId", toId);
        intent.putExtra("date", time);
        startActivity(intent);
	}

	@Override
	public void nearbyStationClicked(int stationId, String stationName) {
		// Move to the departures activity and provide the id and name of the location to see departures for
        Intent intent = new Intent(MainTabsActivity.this, DeparturesActivity.class);
        intent.putExtra("locationId", stationId);
        intent.putExtra("locationName", stationName);
        startActivity(intent);
	}
}
