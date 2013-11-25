package com.msns.models;

import java.util.Map;

public class HttpRequestObject {
	private int executingSeconds;

	private Map<String, String> headers;
	
	private Object data;
	
	private String url;

	public int getExecutingSeconds() {
		return executingSeconds;
	}

	public void setExecutingSeconds(int executingSeconds) {
		this.executingSeconds = executingSeconds;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
