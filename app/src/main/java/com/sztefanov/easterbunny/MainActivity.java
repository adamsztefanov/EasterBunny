package com.sztefanov.easterbunny;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.sztefanov.ajaxbridge.AjaxBridge;
import com.sztefanov.ajaxbridge.CommandAbstract;
import com.sztefanov.ajaxbridge.datastructure.Data;

import org.slf4j.simple.SimpleLogger;

public class MainActivity extends Activity {

  private WebView mWebView;

  @Override
  @SuppressLint("SetJavaScriptEnabled")
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mWebView = findViewById(R.id.activity_main_webview);
    Context context = mWebView.getContext();
    ActivityManager mActivityManager = (ActivityManager) context
      .getSystemService(Context.ACTIVITY_SERVICE);
    //mActivityManager.killBackgroundProcesses("com.sztefanov.ajaxbridge");

    WebSettings webSettings = mWebView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);

    if (savedInstanceState != null) {
      mWebView.restoreState(savedInstanceState);
    }

    mWebView.setWebChromeClient(new WebChromeClient() {
      @Override
      public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.d("WebView", consoleMessage.message());
        return true;
      }
    });

    //mWebView.setWebViewClient(new MyWebViewClient());
    // REMOTE RESOURCE
    //mWebView.loadUrl("https://ebooking.com");
    // LOCAL RESOURCE
    //
    // local connector to backend
    // this way we can store on the device locally in backend accessing java


    System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "info");
    System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "true");
    System.setProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, "yyyy-MM-dd HH:mm:ss");
    System.setProperty(SimpleLogger.SHOW_THREAD_NAME_KEY, "true");

    AjaxBridge ajaxBridge = new AjaxBridge() {
      @Override
      public CommandAbstract createCommand(Data request) {
        return new Command(request);
      }
    };
    Thread t = new Thread(ajaxBridge);
    t.start();

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
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    mWebView.saveState(outState);
  }
}
