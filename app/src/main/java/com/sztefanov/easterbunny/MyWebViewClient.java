package com.sztefanov.easterbunny;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class MyWebViewClient extends WebViewClient {

  @Override
  public boolean shouldOverrideUrlLoading(WebView view, String url) {
    Uri uri = Uri.parse(url);
    if (url.startsWith("file:") || uri.getHost() != null && uri.getHost().endsWith("example.com")) {
      return false;
    }
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    Context context = view.getContext();
    context.startActivity(intent);
    return true;
  }

}
