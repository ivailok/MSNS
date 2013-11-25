package com.msns.services;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.msns.models.Location;

public class SendLocationTask extends AsyncTask<Location, Void, Void> {
	
	private Context mContext;
	
	public SendLocationTask(Context context)
	{
		this.mContext = context;
	}
	
	@Override
	protected Void doInBackground(Location... params) {

		Location loc = params[0];
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
    	String baseUrl = prefs.getString("EXTERNAL_SERVICES_BASE_URL", "");
        String sendLocationUrl = baseUrl + "users/sendlocation?latitude=" + loc.latitude + "&longitude=" + loc.longitude;
        String userID = prefs.getString("USER_ID", "");
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPut httpPut = new HttpPut(sendLocationUrl);
		
		httpPut.setHeader("Accept", "application/json");
		httpPut.setHeader("Content-Type", "application/json");
		httpPut.setHeader("X-userID", userID);
		
		try {
			httpClient.execute(httpPut);
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		
		return null;
	}
}
