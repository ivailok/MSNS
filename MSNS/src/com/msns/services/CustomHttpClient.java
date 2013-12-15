package com.msns.services;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

import com.google.gson.Gson;

public class CustomHttpClient {
	private String baseUrl;
	private Context appContext;
	

	public Context getAppContext() {
		return appContext;
	}

	public void setAppContext(Context appContext) {
		this.appContext = appContext;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	
	public CustomHttpClient(Context appContext) {
		this.setAppContext(appContext);
		
		String localUrl = "http://10.0.2.2:8080/api/";
		String remoteUrl = "http://meetsomenearbystranger.apphb.com/api/";
        
        baseUrl = remoteUrl;
	}
	
	
	public HttpResponse Post(String extendedUrl, Object bodyContent , Map<String, String> headers) throws ClientProtocolException, IOException {
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(baseUrl + extendedUrl);
		
		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
		}
		
		Gson serializer = new Gson();
		String jsonString = serializer.toJson(bodyContent);
		StringEntity entity = new StringEntity(jsonString);
		httpPost.setEntity(entity);
		
		HttpResponse response = httpClient.execute(httpPost);
		return response;
	}
	
	public HttpResponse Get(String extendedUrl, Map<String, String> headers) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(baseUrl + extendedUrl);
		
		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue());
			}
		}
		
		HttpResponse response = httpClient.execute(httpGet);
		return response;
	}
	
	public HttpResponse Put(String extendedUrl, Object bodyContent, Map<String, String> headers) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPut httpPut = new HttpPut(baseUrl + extendedUrl);
		
		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				httpPut.addHeader(entry.getKey(), entry.getValue());
			}
		}
		
		if (bodyContent != null) {
			Gson serializer = new Gson();
			String jsonString = serializer.toJson(bodyContent);
			StringEntity entity = new StringEntity(jsonString);
			httpPut.setEntity(entity);
		}
		
		HttpResponse response = httpClient.execute(httpPut);
		return response;
	}
}
