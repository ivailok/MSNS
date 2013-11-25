package com.msns.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;

import android.os.AsyncTask;

public class HttpResponseStringExtractionTask extends
		AsyncTask<HttpEntity, Void, String> {

	@Override
	protected String doInBackground(HttpEntity... params) {
		if (params[0] == null)
		{
			return "";
		}
		
		InputStream is = null;
		try {
			is = params[0].getContent();
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

}
