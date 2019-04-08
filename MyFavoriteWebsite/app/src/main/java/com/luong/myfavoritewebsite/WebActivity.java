package com.luong.myfavoritewebsite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        WebView webView = (WebView) findViewById(R.id.webactivity);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        Intent intent = getIntent();
        webView.loadUrl(intent.getStringExtra("url"));

        //webView.loadData("<html><body><h1>Hi there!</h1><p>This is my website.</p></body></html>", "text/html", "UTF-8");


    }
}
