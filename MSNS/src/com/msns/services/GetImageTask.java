package com.msns.services;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.msns.activities.MapActivity;

public class GetImageTask extends AsyncTask<String, Void, Bitmap> {
	private Context mContext;
	private String mExceptionMessage = null;
	private String mNickname = null;
	private double mLatitude;
	private double mLongitude;
	
    public GetImageTask(Context context, String nickname, double latitude, double longitude) {
    	this.mContext = context;
    	this.mNickname = nickname;
    	this.mLatitude = latitude;
    	this.mLongitude = longitude;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
        	mExceptionMessage = "Image can't be loaded!";
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
    	if (mExceptionMessage == null) {
    		((MapActivity) mContext).getmMap().addMarker(new MarkerOptions()
    		.title(mNickname).icon(BitmapDescriptorFactory.fromBitmap(result)).position(new LatLng(mLatitude, mLongitude)));
    	}
    }
}
