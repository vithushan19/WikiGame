package com.example.wikigame;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import android.R.string;
import android.content.Context;
import android.util.Log;



public class RemoteWiki {
	private final String BASE_URL = "en.wikipedia.org";
	
	private static RemoteWiki sRemoteWiki;
	
	private AsyncHttpClient mClient;
	
	public interface RemoteListener {
		public void onEndPointsReceived(String response);

		public void onSinglePageReceived(String response);
	}
	private RemoteListener mListener;
	
	/*
	 *  Private constructor since class is singleton. Must pass in an object that implements the RemoteListener
	 *  interface above.
	 */
	private RemoteWiki(RemoteListener listener) {
		mListener = listener;
		mClient = new AsyncHttpClient();
	}
	
	/*
	 *  Getter for singleton.
	 */
	public static RemoteWiki getInstance(RemoteListener listener) {
		if (sRemoteWiki == null) {
			sRemoteWiki = new RemoteWiki(listener);
		}
		return sRemoteWiki;
	}
	
	/*
	 * Gets two random pages. (MediaWiki API states that only the first page is random, and
	 * subsequent pages are derived from the first randomized page.)
	 * 
	 * Eg. If the call returns the Main Wikipedia page randomly, it will always return the
	 * List of fictional monkeys page second. 
	 * 
	 * Only returns pageID, title, and namespace
	 * 
	 * We only want pages from namespace 0 which are the main article pages.
	 */
	public void getRandomPage() {
		
		// Params
		ArrayList<BasicNameValuePair> qparams = new ArrayList<BasicNameValuePair>();
	    qparams.add(new BasicNameValuePair("action", "query"));
	    qparams.add(new BasicNameValuePair("list", "random"));
	    qparams.add(new BasicNameValuePair("rnlimit", "2"));
	    qparams.add(new BasicNameValuePair("format", "json"));
	    qparams.add(new BasicNameValuePair("rnnamespace", "0"));

	    getPage(qparams, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("VITHUSHAN", "ON END PAGES RECEIVED: " + response);
		        // Delegate onSuccess action to the listener
		        mListener.onEndPointsReceived(response);
		    }
			
		});
	}
	
	public void getSinglePage(String title) {
		
		ArrayList<BasicNameValuePair> qparams = new ArrayList<BasicNameValuePair>();
	    qparams.add(new BasicNameValuePair("action", "query"));
	    qparams.add(new BasicNameValuePair("titles", title));
	    qparams.add(new BasicNameValuePair("prop", "info|links"));
	    qparams.add(new BasicNameValuePair("format", "json"));
	    qparams.add(new BasicNameValuePair("inprop", "url"));

	    getPage(qparams, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.i("VITHUSHAN", "SUCCESS: " + response);
		        mListener.onSinglePageReceived(response);
		    }
			
		});
	}
	
	/**
	 * This function builds a URI with the passed in parameters and makes a call to the MediaWiki API.
	 * Other functions define what the behavior should be when the response is returned.
	 * 
	 * @param params: An arrayList of http parameters that will be used to build the final URI
	 * @param responseHandler: Defines the behavior for when the response is received. 
	 */
	public void getPage(ArrayList<BasicNameValuePair> params, AsyncHttpResponseHandler responseHandler) {

	    URI uri = null;
		try {
			uri = URIUtils.createURI("http", BASE_URL, -1, "/w/api.php",
			                             URLEncodedUtils.format(params, "UTF-8"), null);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    HttpGet httpget = new HttpGet(uri);
	    		
		mClient.get(httpget.getURI().toString(), responseHandler);
	}


}

   

