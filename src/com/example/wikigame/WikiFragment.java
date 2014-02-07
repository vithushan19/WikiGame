package com.example.wikigame;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.wikigame.RemoteWiki.RemoteListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;



public class WikiFragment extends Fragment implements RemoteListener {
	private final String TAG = "com.example.wikigame";
	private RemoteWiki mRemoteWiki;
	private GameModel mGameModel;
	
	// Views
	TextView mStartTitle;
	TextView mTargetTitle;
	TextView mClickCounter;
	WebView mWebView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	

	private void init(View v) {

		mStartTitle = (TextView) v.findViewById(R.id.startPage);
		mTargetTitle = (TextView) v.findViewById(R.id.targetPage);
		mClickCounter = (TextView) v.findViewById(R.id.score);
		mWebView = (WebView) v.findViewById(R.id.webview);
		
		
		mGameModel = GameModel.getInstance(getActivity());
		mRemoteWiki = RemoteWiki.getInstance(this);
		mRemoteWiki.getRandomPage();
	}


	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_game, parent, false);
		init(v);
		return v;
		
	}


	@Override
	public void onEndPointsReceived(String response) {
		
		ArrayList<BasicNameValuePair> result = new ArrayList<BasicNameValuePair>();
		String startPage = null;
		String targetPage = null;
		
		try {
			JSONObject obj = new JSONObject(response);
			JSONArray pages = obj.getJSONObject("query").getJSONArray("random");
			
			startPage = pages.getJSONObject(0).getString("title");
			targetPage = pages.getJSONObject(1).getString("title");
			Log.i("VITHUSHAN", "START PAGE: " + startPage);
			Log.i("VITHUSHAN", "END PAGE: " + targetPage);
			
		} catch (JSONException e) {
			Log.e("VITHUSHAN", "Invalid JSON format", e);
		}
		
		// Update the model
		mGameModel.setStartPageTitle(startPage);
		mGameModel.setTargetPageTitle(targetPage);
		
		// Update the views
		mStartTitle.setText(mGameModel.getStartPageTitle());
		mTargetTitle.setText(mGameModel.getTargetPageTitle());
		
		mRemoteWiki.getSinglePage(startPage);
	}


	@Override
	public void onSinglePageReceived(String response) {
		String url = null;
		try {
			JSONObject obj = new JSONObject(response);
			JSONObject pages = obj.getJSONObject("query").getJSONObject("pages");
			Iterator<?> iterator = pages.keys();
			String first = (String) iterator.next();
		    url = pages.getJSONObject(first).getString("fullurl");
		} catch (JSONException e) {
			Log.e("VITHUSHAN", "Invalid JSON format", e);
		}
		
		Log.i("VITHUSHAN", "URL: " + url);
		// TODO: Save the current page to the model
		
		mWebView.loadUrl(url);
		
		
		mWebView.setWebViewClient(new WebViewClient() {
		    public boolean shouldOverrideUrlLoading(WebView view, String url){
		        // do your handling codes here, which url is the requested url
		        // probably you need to open that url rather than redirect:
		    	Log.i("VITHUSHAN", "URL CLICKED: " + url);
		        view.loadUrl(url);
		        mGameModel.incrementClickCounter();
		        
		        String counter = mGameModel.getClickCounter() + "";
		        mClickCounter.setText(counter);
		        
		        return false; // then it is not handled by default action
		   }
		});
		
	}
	
}
