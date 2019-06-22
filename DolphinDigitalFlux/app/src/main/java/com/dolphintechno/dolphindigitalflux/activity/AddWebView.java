
package com.dolphintechno.dolphindigitalflux.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

import com.dolphintechno.dolphindigitalflux.R;

public class AddWebView extends AppCompatActivity {

    WebView webView;

//    private String postUrl = "https://api.androidhive.info/webview/index.html";
    private String postUrl = "http://mygoalway.com/login.php";

//    http://mygoalway.com/user.php

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_web_view);
//        http://mygoalway.com/web_login.php
        webView = (WebView) findViewById(R.id.webView_added);
//        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        imgHeader = (ImageView) findViewById(R.id.backdrop);

        Toast.makeText(getApplicationContext(), "in web view", Toast.LENGTH_LONG).show();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(postUrl);
        webView.setHorizontalScrollBarEnabled(false);


        webView.getSettings().setDisplayZoomControls(false);



    }
}
