package com.mattiasbergstrom.reseplanerare.fragments;

import java.util.Calendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;


public class DatePickerFragment extends DialogFragment 
								implements OnDateSetListener {
	public interface DateSetCallback {
		public void SetDate(DatePicker view, int year, int monthOfYear, int dayOfMonth);
	}
	
	private DateSetCallback callback;
	
	public void setCallback(DateSetCallback callback) {
		this.callback = callback;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        
        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		callback.SetDate(view, year, monthOfYear, dayOfMonth);
	}
}
