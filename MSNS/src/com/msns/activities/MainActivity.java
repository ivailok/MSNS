package com.msns.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;

import com.msns.R;

public class MainActivity extends Activity {
    private static final String BASE_API_URL = "EXTERNAL_SERVICES_BASE_URL";
    
    private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		storeBasicWebApiUrl();
		
		navigateToProperActivity();
		
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void storeBasicWebApiUrl() {
		Editor editablePrefs = prefs.edit();
		//String localUrl = "http://10.0.2.2:8080/api/";
		String remoteUrl = "http://meetsomenearbystranger.apphb.com/api/";
		editablePrefs.putString(BASE_API_URL, remoteUrl);
		editablePrefs.commit();
	}
	
	private boolean isSetupRun() {
		//Editor editablePrefs = prefs.edit();
		//editablePrefs.putBoolean("SETUP_COMPLETED", true);
		//editablePrefs.remove("SETUP_COMPLETED").commit();
				
		boolean isSetupRun = prefs.getBoolean("SETUP_COMPLETED", false);
		return isSetupRun;
	}
	
	private void navigateToProperActivity() {
		boolean isSetupCompleted = isSetupRun();
		Intent intent;
		if (isSetupCompleted == true)
		{
			intent = new Intent(this, ProfileActivity.class);
		}
		else
		{
			intent = new Intent(this, SetupActivity.class);
		}
		startActivity(intent);
	}
}
