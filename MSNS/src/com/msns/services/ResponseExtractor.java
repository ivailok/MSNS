package com.msns.services;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpResponse;

import android.content.OperationApplicationException;

import com.msns.models.HttpMethodTypes;
import com.msns.models.HttpRequestObject;

//public class ResponseExtractor {
//	
//	public static String Extract(HttpMethodTypes methodType, HttpRequestObject request, int executingSeconds, int statusCode) throws OperationApplicationException
//	{
//		try
//		{
//			String response = null;
//			switch (methodType)
//			{
//				case GET:
//					break;
//				case POST:
//					response = new SetupTask().execute(request).get(executingSeconds, TimeUnit.SECONDS);
//					break;
//				case PUT:
//					break;
//				default:
//					break;
//			}
//			
//			if (response.getStatusLine().getStatusCode() != statusCode)
//			{
//				throw new OperationApplicationException("The submitted data is not in the proper format.");
//			}
//			
//			return new HttpResponseStringExtractionTask().execute(response.getEntity()).get();
//			
//		} catch (InterruptedException e) {
//			throw new OperationApplicationException("The process of setting your data was interupted. Please try again or check your internet connection.");
//		} catch (ExecutionException e) {
//			throw new OperationApplicationException("The process of setting your data could not be completed. Please try again or check your internet connection.");
//		} catch (TimeoutException e) {
//			throw new OperationApplicationException("Cannot receive response from the server. Please check your connection.");
//		}
//	}
//}
