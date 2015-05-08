package com.mattiasbergstrom.reseplanerare.activities;

import com.mattiasbergstrom.reseplanerare.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class DeparturesActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_departures);
		getActionBar().setTitle(R.string.title_departures);
		
		// Display the station from which we are showing departures
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    String name = extras.getString("locationName");
		    getActionBar().setSubtitle(name);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_departures, menu);
		return true;
	}

}
