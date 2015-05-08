package com.mattiasbergstrom.reseplanerare.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.mattiasbergstrom.reseplanerare.R;
import com.mattiasbergstrom.reseplanerare.SearchRouteCallback;
import com.mattiasbergstrom.reseplanerare.fragments.DatePickerFragment.DateSetCallback;
import com.mattiasbergstrom.reseplanerare.fragments.TimePickerFragment.TimeSetCallback;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class SearchRouteFragment extends Fragment implements DateSetCallback, TimeSetCallback {
	private Button dateButton;
	private Button timeButton;
	private Button searchButton;
	private EditText fromText;
	private EditText toText;
	private Date selectedDate;
	
	public Button getSearchButton() {
		return searchButton;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_search_route, container, false);
		
		// Get views
		dateButton = (Button) v.findViewById(R.id.search_date_spinner);
		timeButton = (Button) v.findViewById(R.id.search_time_spinner);
		fromText = (EditText) v.findViewById(R.id.search_from_text);
		toText = (EditText) v.findViewById(R.id.search_to_text);
		searchButton = (Button) v.findViewById(R.id.search_button);
		
		final Calendar c = Calendar.getInstance();
		
		// Format time
		SimpleDateFormat dt = new SimpleDateFormat("HH:mm", Locale.getDefault());
		String time = dt.format(c.getTime());
		
		// Format date
		dt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String date = dt.format(c.getTime());
		
		// Set the text in the buttons and the selectedDate to now
		dateButton.setText(date);
		timeButton.setText(time);
		selectedDate = c.getTime();
		
		// When the user clicks the dateButton, present a DatePickerFragment
		dateButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				DialogFragment newFragment = new DatePickerFragment();
		    	((DatePickerFragment)newFragment).setCallback(SearchRouteFragment.this);
		    	newFragment.show(getFragmentManager(), "datePicker");
			}
	    	});
		
		// When the user clicks the timeButton, present a TimePickerFragment
		timeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				DialogFragment newFragment = new TimePickerFragment();
				((TimePickerFragment)newFragment).setCallback(SearchRouteFragment.this);
		    	newFragment.show(getFragmentManager(), "timePicker");
			}
	    	});
		
		// When the user clicks the searchButton, get the activity and tell it what search was done.
		// The activity will then decide what to do with it.
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(validateForm()) {
					if (getActivity() instanceof SearchRouteCallback) {
						SearchRouteCallback callback = (SearchRouteCallback) getActivity();
					    callback.searchRoute(fromText.getText().toString(), toText.getText().toString(), selectedDate);
					}
				}
			}
		});
		
		return v;
	}
	
	/**
	 * Validates the search form
	 * @return True if the form was validated and false if something was wrong.
	 */
	private boolean validateForm() {
		
		// The API requires at least two characters in the search terms
		if(fromText.getText().toString().length() < 2) {
			// Display dialog telling the user to fix the error
			new AlertDialog.Builder(getActivity())
		    .setTitle(R.string.missing_stations_name)
		    .setMessage(R.string.fill_departure_station)
		    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // Do nothing
		        }
		     })
		     .show();
			
			return false;
		}
		
		// The API requires at least two characters in the search terms
		if(toText.getText().toString().length() < 2) {
			// Display dialog telling the user to fix the error
			new AlertDialog.Builder(getActivity())
		    .setTitle(R.string.missing_stations_name)
		    .setMessage(R.string.fill_arrival_station)
		    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // Do nothing
		        }
		     })
		     .show();
			
			return false;
		}
		
		return true;
	}
    
	@Override
	public void SetDate(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		// Update the displayed date and the selected date
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Calendar c = Calendar.getInstance();
		
		// Set the calendar to the current selected time and adjust the variables that this picked can change
		c.setTimeInMillis(selectedDate.getTime());
		c.set(year, monthOfYear, dayOfMonth);
		String date = dt.format(c.getTime());
		dateButton.setText(date);
		selectedDate = c.getTime();
	}

	@Override
	public void SetDate(TimePicker view, int hourOfDay, int minute) {
		// Update the displayed time and the selected date
		SimpleDateFormat dt = new SimpleDateFormat("HH:mm", Locale.getDefault());
		Calendar c = Calendar.getInstance();
		
		// Set the calendar to the current selected time and adjust the variables that this picked can change
		c.setTimeInMillis(selectedDate.getTime());
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		String time = dt.format(c.getTime());
		timeButton.setText(time);
		selectedDate = c.getTime();
	}
	
	
}
