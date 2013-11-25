package com.msns.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.msns.activities.ProfileActivity;
import com.msns.activities.SetupActivity;
import com.msns.models.HttpRequestObject;

public class SetupTask extends AsyncTask<HttpRequestObject, Void, Integer> {

	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "REGISTRATION_ID";
    private static final String PROPERTY_APP_VERSION = "APP_VERSION";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String SENDER_ID = "190481575602";
    private static final String TAG = "MSNS";
    private static final String BASE_API_URL = "EXTERNAL_SERVICES_BASE_URL";

    private GoogleCloudMessaging gcm;
    private SharedPreferences prefs;
    private Context appContext;
    private String regId;
	
	private Context activityContext;
	private String mExceptionText = null;
	
	public SetupTask(Context context)
	{
		this.activityContext = context;
		this.appContext = activityContext.getApplicationContext();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(appContext);
	}
	
	@Override
	protected Integer doInBackground(HttpRequestObject... params) {

		HttpRequestObject requestData = params[0];
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(requestData.getUrl());
		
		if (requestData.getHeaders() != null)
		{
			for (Map.Entry<String, String> header : requestData.getHeaders().entrySet()){
	            httpPost.addHeader(header.getKey(), header.getValue());
	        }
		}
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-Type", "application/json");
		
		Gson gsonConverter = new Gson();
		String jsonData = gsonConverter.toJson(requestData.getData());
		StringEntity entity = null;
		try {
			entity = new StringEntity(jsonData);
		} catch (UnsupportedEncodingException e) {
			mExceptionText = "The provided data is not in the correct format.";
		}
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			mExceptionText = "The process of setting your data could not be completed. Please try again or check your internet connection.";
		} catch (IOException e) {
			mExceptionText = "The process of setting your data was interupted. Please try again or check your internet connection.";
		}
		
		int statusCode = response.getStatusLine().getStatusCode();
		return statusCode;
	}
	
	@Override
	protected void onPostExecute(Integer statusCode) {
		// TODO Auto-generated method stub
		
		if (mExceptionText == null)
		{
			if (statusCode == HttpStatus.SC_NO_CONTENT || statusCode == HttpStatus.SC_OK)
			{
				// mark setup as completed
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activityContext.getApplicationContext());
				Editor editablePrefs = preferences.edit();
				editablePrefs.putBoolean("SETUP_COMPLETED", true);
				editablePrefs.commit();
			
				// going to Base view of the app after setup
				Intent intent = new Intent(activityContext, ProfileActivity.class);
				activityContext.startActivity(intent);
				
				// Check device for Play Services APK. If check succeeds, proceed with
		        //  GCM registration.
		        if (checkPlayServices()) {
		            gcm = GoogleCloudMessaging.getInstance(activityContext);
		            regId = getRegistrationId(appContext);

		            if (regId.isEmpty()) {
		                registerInBackground();
		            }
		        } else {
		            Log.i(TAG, "No valid Google Play Services APK found.");
		        }
				
				((SetupActivity)activityContext).finish();
			}
			else
			{
				((SetupActivity) activityContext).showProgress(false);
				Toast toast = Toast.makeText(activityContext, "The submitted data is not in the proper format.", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		else
		{
			((SetupActivity) activityContext).showProgress(false);
			Toast toast = Toast.makeText(activityContext, mExceptionText, Toast.LENGTH_SHORT);
			toast.show();
		}
		
		super.onPostExecute(statusCode);
	}
	
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activityContext);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) activityContext, PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(TAG, "This device is not supported.");
	            ((Activity) activityContext).finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	private String getRegistrationId(Context context) {
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	        Log.i(TAG, "Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i(TAG, "App version changed.");
	        return "";
	    }
	    return registrationId;
	}
	
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
	    new AsyncTask<Void, Void, String>() {
	        @Override
	        protected String doInBackground(Void... params) {
	            String msg = "";
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(appContext);
	                }
	                regId = gcm.register(SENDER_ID);
	                msg = "Device registered, registration ID=" + regId;

	                // You should send the registration ID to your server over HTTP,
	                // so it can use GCM/HTTP or CCS to send messages to your app.
	                // The request to your server should be authenticated if your app
	                // is using accounts.
	                sendRegistrationIdToBackend();

	                // For this demo: we don't need to send it because the device
	                // will send upstream messages to a server that echo back the
	                // message using the 'from' address in the message.

	                // Persist the regID - no need to register again.
	                storeRegistrationId(regId);
	            } catch (IOException ex) {
	                msg = "Error :" + ex.getMessage();
	                // If there is an error, don't just keep trying to register.
	                // Require the user to click a button again, or perform
	                // exponential back-off.
	            }
	            return msg;
	        }

	        @Override
	        protected void onPostExecute(String msg) {
	            Log.e(TAG, msg);
	        }
	    }.execute(null, null, null);
	}
	
	/**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
	private void sendRegistrationIdToBackend() {
		HttpClient httpClient = new DefaultHttpClient();
		String url = prefs.getString(BASE_API_URL, "") + "users/setgcmregid?regId=" + regId;
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("X-userID", prefs.getString("USER_ID", ""));
		
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			sendRegistrationIdToBackend();
		} catch (IOException e) {
			sendRegistrationIdToBackend();
		}
		
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			sendRegistrationIdToBackend();
		}
	}
	
	private void storeRegistrationId(String regId) {
	    int appVersion = getAppVersion(appContext);
	    Log.i(TAG, "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
}
