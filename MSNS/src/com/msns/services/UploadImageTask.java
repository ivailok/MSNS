package com.msns.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.msns.R;
import com.msns.models.EncodedImage;

public class UploadImageTask extends AsyncTask<Bitmap, Void, Integer> {

	private Context mContext;
	private String mExceptionText = null;
	private Bitmap mImageBitmap;
	
	public UploadImageTask(Context context, Bitmap imageBitmap)
	{
		this.mContext = context;
		this.mImageBitmap = imageBitmap;
	}
	
	@Override
	protected Integer doInBackground(Bitmap... params) {

		Bitmap image = params[0];
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        String url = prefs.getString("EXTERNAL_SERVICES_BASE_URL", "") + "users/uploadimage";
        String userID = prefs.getString("USER_ID", "");
        Header[] headers = {
        	new BasicHeader("Content-Type", "application/json"),
	        new BasicHeader("Accept", "application/json"),
	        new BasicHeader("X-userID", userID)
        };
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		
		for (int i = 0; i < headers.length; i++){
			httpPost.addHeader(headers[i]);
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 75, bos);
        byte[] data = bos.toByteArray();
        
        Gson gsonConverter = new Gson();
		EncodedImage encImg = new EncodedImage();
        encImg.base64String = Base64.encodeToString(data, Base64.DEFAULT);
        String jsonData = gsonConverter.toJson(encImg);
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
			if (statusCode == HttpStatus.SC_CREATED || statusCode == HttpStatus.SC_OK)
			{
				ImageView profileImg = (ImageView) ((Activity)mContext).findViewById(R.id.myProfileImage);
				//profileImg.setImageBitmap(picture);
				BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(), mImageBitmap);
				profileImg.setBackground(drawable);
				
				Toast toast = Toast.makeText(mContext, "Photo uploaded.", Toast.LENGTH_SHORT);
				toast.show();
			}
			else
			{
				Toast toast = Toast.makeText(mContext, "Photo not uploaded.", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		else
		{
			Toast toast = Toast.makeText(mContext, mExceptionText, Toast.LENGTH_SHORT);
			toast.show();
		}
		
		super.onPostExecute(statusCode);
	}
}
