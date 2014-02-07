package com.example.wikigame;

import com.loopj.android.http.AsyncHttpClient;

import android.content.Context;

public class GameModel {
	private static GameModel sGameModel;
	
	private String mStartPageTitle;
	private String mTargetPageTitle;
	private int mClickCounter;
	
	private Context mAppContext;
	
	private GameModel(Context appContext) {
		mAppContext = appContext;
		mClickCounter = 0;
	}
	
	public static GameModel getInstance(Context c) {
		if (sGameModel == null) {
			sGameModel = new GameModel(c.getApplicationContext());
		}
		return sGameModel;
	}
	
	
	public String getStartPageTitle() {
		return mStartPageTitle;
	}
	public void setStartPageTitle(String startPageTitle) {
		this.mStartPageTitle = startPageTitle;
	}
	
	public String getTargetPageTitle() {
		return mTargetPageTitle;
	}
	public void setTargetPageTitle(String targetPageTitle) {
		this.mTargetPageTitle = targetPageTitle;
	}

	public int getClickCounter() {
		return mClickCounter;
	}

	public void incrementClickCounter() {
		mClickCounter++;
	}
}
