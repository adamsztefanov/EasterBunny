package com.sztefanov.simplefit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

import com.sztefanov.ajaxbridge.AjaxBridge;

public class MainActivity extends Activity {

	private WebView mWebView;

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	@SuppressLint("SetJavaScriptEnabled")
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// local connector to backend
		//Thread thread = new Thread(new AjaxBridge());
		//thread.start();

		// cost: 1 beer
		mWebView = findViewById(R.id.activity_main_webview);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);

		if (savedInstanceState != null) {
			mWebView.restoreState(savedInstanceState);
		}

		mWebView.setWebViewClient(new MyWebViewClient());

		// REMOTE RESOURCE
		//mWebView.loadUrl("https://ebooking.com");

		// LOCAL RESOURCE
		mWebView.loadUrl("file:///android_asset/index.html");
	}

	@Override
	public void onBackPressed() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mWebView.saveState(outState);
	}
}
