package com.msns.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msns.activities.MapActivity;
import com.msns.models.Area;
import com.msns.models.UserShortModel;

public class GetNearbyPeopleTask extends AsyncTask<Area, Void, String> {
private Context mContext;
	
	public GetNearbyPeopleTask(Context context)
	{
		this.mContext = context;
	}
	
	@Override
	protected String doInBackground(Area... params) {

		Area area = params[0];
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
    	String baseUrl = prefs.getString("EXTERNAL_SERVICES_BASE_URL", "");
        String getNearbyPeopleUrl = baseUrl + "users/getusersonvisiblemap?bottomLeftX=" + area.bottomLeftCorner.x + "&bottomLeftY=" + area.bottomLeftCorner.x + "&upperRightX=" + area.upperRightCorner.x + "&upperRightY=" + area.upperRightCorner.y + "&timeInSec=" + 120000;
        String userID = prefs.getString("USER_ID", "");
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(getNearbyPeopleUrl);
		
		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-Type", "application/json");
		httpGet.setHeader("X-userID", userID);
		
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			if (entity == null)
			{
				return null;
			}
			
			InputStream is = null;
			try {
				is = entity.getContent();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
			StringBuilder str = new StringBuilder();

			String line = null;
			try {
				while ((line = bufferedReader.readLine()) != null) {
					str.append(line + "\n");
			    }
			} catch (IOException e) {
			    throw new RuntimeException(e);
			} finally {
			    try {
			    	is.close();
			    } catch (IOException e) {
			    	//tough luck...
			    }
			}
			
			return str.toString();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String content) {
		if (content != null) {
			Gson gsonDeserializer = new Gson();
			Type listType = new TypeToken<ArrayList<UserShortModel>>() {}.getType();
        	ArrayList<UserShortModel> userData = gsonDeserializer.fromJson(content, listType);
        	((MapActivity) mContext).setNearbyPeople(userData);
		}
	}
}
