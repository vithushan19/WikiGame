package com.example.wikigame;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class MainActivity extends SingleFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected Fragment createFragment() {
		return new WikiFragment();
	}

}
