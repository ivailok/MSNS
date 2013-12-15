package com.msns.services;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;


public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	private ImageView bmImage;
	private Context mContext;
	private String mExceptionMessage = null;
	
    public DownloadImageTask(Context context, ImageView bmImage) {
        this.bmImage = bmImage;
        this.mContext = context;
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
    		BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(), result);
    		bmImage.setBackground(drawable);
    	}
    	else {
    		Toast.makeText(mContext, mExceptionMessage, Toast.LENGTH_SHORT).show();
    	}
    }
}
